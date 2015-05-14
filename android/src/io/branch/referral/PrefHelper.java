package io.branch.referral;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import org.json.JSONException;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * <p>A class that uses the helper pattern to provide regularly referenced static values and 
 * logging capabilities used in various other parts of the SDK, and that are related to globally set  
 * preference values.</p>
 */
public class PrefHelper {

	/**
	 * {@link Boolean} value that enables/disables Branch developer external debug mode.
	 */
	private static boolean BNC_Dev_Debug = false;
	
	/**
	 * {@link Boolean} value that enables/disables Branch general debug mode.
	 */
	private static boolean BNC_Debug = false;
	
	/**
	 * {@link Boolean} value that indicates whether debugger is in transitional connecting state.
	 */
	private static boolean BNC_Debug_Connecting = false;
	
	/**
	 * {@link Boolean} value that enables/disables remote debugging via the server.
	 */
	private static boolean BNC_Remote_Debug = false;
	
	/**
	 * {@link Boolean} value that determines whether external App Listing is enabled or not.
	 * 
	 * @see {@link Branch#scheduleListOfApps()}
	 * @see {@link SystemObserver#getListOfApps()}
	 */
	private static boolean BNC_App_Listing = true;

	private static boolean BNC_Smart_Session = true;

	/**
	 * A {@link String} value used where no string value is available.
	 */
	public static final String NO_STRING_VALUE = "bnc_no_value";

	private static final int INTERVAL_RETRY = 3000;
	
	/**
	 * Number of times to reattempt connection to the Branch server before giving up and throwing an 
	 * exception.
	 */
	private static final int MAX_RETRIES = 5;
	
	private static final int TIMEOUT = 3000;

	private static final String SHARED_PREF_FILE = "branch_referral_shared_pref";

	private static final String KEY_APP_KEY = "bnc_app_key";
	private static final String KEY_APP_VERSION = "bnc_app_version";
	private static final String KEY_DEVICE_FINGERPRINT_ID = "bnc_device_fingerprint_id";
	private static final String KEY_SESSION_ID = "bnc_session_id";
	private static final String KEY_IDENTITY_ID = "bnc_identity_id";
	private static final String KEY_IDENTITY = "bnc_identity";
	private static final String KEY_LINK_CLICK_ID = "bnc_link_click_id";
	private static final String KEY_LINK_CLICK_IDENTIFIER = "bnc_link_click_identifier";
	private static final String KEY_SESSION_PARAMS = "bnc_session_params";
	private static final String KEY_INSTALL_PARAMS = "bnc_install_params";
	private static final String KEY_USER_URL = "bnc_user_url";
	private static final String KEY_IS_REFERRABLE = "bnc_is_referrable";

	private static final String KEY_BUCKETS = "bnc_buckets";
	private static final String KEY_CREDIT_BASE = "bnc_credit_base_";

	private static final String KEY_ACTIONS = "bnc_actions";
	private static final String KEY_TOTAL_BASE = "bnc_total_base_";
	private static final String KEY_UNIQUE_BASE = "bnc_balance_base_";

	private static final String KEY_RETRY_COUNT = "bnc_retry_count";
	private static final String KEY_RETRY_INTERVAL = "bnc_retry_interval";
	private static final String KEY_TIMEOUT = "bnc_timeout";

	private static final String KEY_LAST_READ_SYSTEM = "bnc_system_read_date";

	/**
	 * {@link String} value used by {@link BranchRemoteInterface#connectToDebug()}.
	 */
	public static final String REQ_TAG_DEBUG_CONNECT = "t_debug_connect";
	
	/**
	 * {@link String} value used by {@link BranchRemoteInterface#sendLog(String log)}.
	 */
	public static final String REQ_TAG_DEBUG_LOG = "t_debug_log";
	
	/**
	 * {@link String} value used by {@link BranchRemoteInterface}.
	 * 
	 * @see BranchRemoteInterface
	 */
	public static final String REQ_TAG_DEBUG_SCREEN = "t_debug_screen";
	
	/**
	 * {@link String} value used by {@link BranchRemoteInterface#disconnectFromDebug()}.
	 * 
	 * @see BranchRemoteInterface
	 * @see BranchRemoteInterface#disconnectFromDebug()
	 */
	public static final String REQ_TAG_DEBUG_DISCONNECT = "t_debug_disconnect";
	
	/**
	 * The debug action is triggered by holding a multi-touch gesture. This {@link Integer} value 
	 * defines how many multi-touch points need to be held on the screen in order for the debug 
	 * action to be triggered.
	 */
	public static final int DEBUG_TRIGGER_NUM_FINGERS = 4;
	
	/**
	 * The debug action is triggered by holding a multi-touch gesture. This {@link Integer} value 
	 * defines how many milliseconds the gesture must be held in order for the debug action to be 
	 * triggered.
	 */
	public static final int DEBUG_TRIGGER_PRESS_TIME = 3000;

    private static String Branch_Key = null;
	/**
	 * Internal static variable of own type {@link PrefHelper}. This variable holds the single 
	 * instance used when the class is instantiated via the Singleton pattern.
	 */
	private static PrefHelper prefHelper_;
	
	/**
	 * A single variable that holds a reference to the application's {@link SharedPreferences} 
	 * object for use whenever {@link SharedPreferences} values are read or written via this helper 
	 * class.
	 */
	private SharedPreferences appSharedPrefs_;
	
	/**
	 * A single variable that holds a reference to an {@link Editor} object that is used by the 
	 * helper class whenever the preferences for the application are changed.
	 */
	private Editor prefsEditor_;

	/**
	 * Instance of {@link BranchRemoteInterface} enabling remote interaction via the server interface.
	 */
	private BranchRemoteInterface remoteInterface_;
	
	/**
	 * Reference of application {@link Context}, normally the base context of the application.
	 */
	private Context context_;

	/**
	 * <p>Empty, but required constructor for the {@link PrefHelper} {@link SharedPreferences} 
	 * helper class.</p>
	 */
	public PrefHelper() {
	}

	/**
	 * <p>Constructor with context passed from calling {@link Activity}.</p>
	 * 
	 * @param context 		A reference to the {@link Context} that the application is operating 
	 * 						within. This is normally the base context of the application.
	 */
	private PrefHelper(Context context) {
		this.appSharedPrefs_ = context.getSharedPreferences(SHARED_PREF_FILE,
				Context.MODE_PRIVATE);
		this.prefsEditor_ = this.appSharedPrefs_.edit();
		this.context_ = context;
	}

	/**
	 * <p>Singleton method to return the pre-initialised, or newly initialise and return, a singleton 
	 * object of the type {@link PrefHelper}.</p>
	 * 
	 * @param context		The {@link Context} within which the object should be instantiated; this 
	 * 						parameter is passed to the private {@link #PrefHelper(Context)} 
	 * 						constructor method.
	 * 
	 * @return				A {@link PrefHelper} object instance.
	 */
	public static PrefHelper getInstance(Context context) {
		if (prefHelper_ == null) {
			prefHelper_ = new PrefHelper(context);
		}
		return prefHelper_;
	}

	/**
	 * <p>Returns the base URL to use for all calls to the Branch API as a {@link String}.</p>
	 * 
	 * @return 			A {@link String} variable containing the hard-coded base URL that the Branch 
	 * 					API uses.
	 */
	public String getAPIBaseUrl() {
		return "https://api.branch.io/";
	}

	/**
	 * <p>Sets the duration in milliseconds to override the timeout value for calls to the Branch API.</p>
	 * 
	 * @param timeout	The {@link Integer} value of the timeout setting in milliseconds.
	 */
	public void setTimeout(int timeout) {
		setInteger(KEY_TIMEOUT, timeout);
	}

	/**
	 * <p>Returns the currently set timeout value for calls to the Branch API. This will be the default 
	 * SDK setting unless it has been overridden manually between Branch object instantiation and 
	 * this call.</p>
	 * 
	 * @return 		An {@link Integer} value containing the currently set timeout value in 
	 * 				milliseconds.
	 */
	public int getTimeout() {
		return getInteger(KEY_TIMEOUT, TIMEOUT);
	}

	/**
	 * <p>Sets the value specifying the number of times that a Branch API call has been re-attempted.</p>
	 * 
	 * <p>This overrides the default retry value.</p>
	 * 
	 * @param retry		An {@link Integer} value specifying the value to be specified in preferences 
	 * 					that determines the number of times that a Branch API call has been re-
	 * 					attempted. 
	 */
	public void setRetryCount(int retry) {
		setInteger(KEY_RETRY_COUNT, retry);
	}

	/**
	 * <p>Gets the current count of the number of times that a Branch API call has been re-attempted.</p>
	 * 
	 * @return			An {@link Integer} value containing the current count of the number of times 
	 * 					that a Branch API call has been attempted.
	 */
	public int getRetryCount() {
		return getInteger(KEY_RETRY_COUNT, MAX_RETRIES);
	}

	/**
	 * <p>Sets the amount of time in milliseconds to wait before re-attempting a timed-out request 
     * to the Branch API.</p>
	 * 
	 * @param retryInt		An {@link Integer} value specifying the number of milliseconds to wait 
	 * 						before re-attempting a timed-out request.
	 */
	public void setRetryInterval(int retryInt) {
		setInteger(KEY_RETRY_INTERVAL, retryInt);
	}

	/**
	 * <p>Gets the amount of time in milliseconds to wait before re-attempting a timed-out request 
     * to the Branch API.</p>
	 * 
	 * @return		An {@link Integer} value containing the currently set retry interval in 
	 * 				milliseconds.
	 */
	public int getRetryInterval() {
		return getInteger(KEY_RETRY_INTERVAL, INTERVAL_RETRY);
	}
	
	/**
	 * <p>Sets the value of {@link #KEY_APP_VERSION} in preferences.</p>
	 * 
	 * @param version		A {@link String} value containing the current app version.
	 */
	public void setAppVersion(String version) {
		setString(KEY_APP_VERSION, version);
	}
	
	/**
	 * <p>Returns the current value of {@link #KEY_APP_VERSION} as stored in preferences.</p>
	 * 
	 * @return		A {@link String} value containing the current app version.
	 */
	public String getAppVersion() {
		return getString(KEY_APP_VERSION);
	}

	/**
	 * <p>Sets the Branch App Key in preferences programmatically.</p>
	 * 
	 * <p><b>Note: </b> This is a deprecated method, you should configure your <i>App Key</i> as an XML 
	 * String value instead.</p>
	 * 
	 * @see <a href="https://github.com/BranchMetrics/Branch-Integration-Guides/blob/master/android-quick-start.md">
	 * Branch Quick-Start Guide for Android</a>
	 * @see <a href="https://github.com/BranchMetrics/Branch-Android-SDK/blob/2cb4f05fd8f67bce1019456f26b7f384a39abb2c/README.md#add-your-app-key-to-your-project">
	 * Adding your app key to your project</a>
	 * 
	 * @param key		A {@link String} value containing the App Key for the current App.
	 */
    public void setAppKey(String key) {
        setString(KEY_APP_KEY, key);
    }
    /**
	 * <p>Gets the Branch App Key in preferences programmatically.</p>
	 * 
	 * @return		A {@link String} value containing the current App Key as configured.
	 */
    public String getAppKey() {
        String appKey = null;
        try {
            final ApplicationInfo ai = context_.getPackageManager().getApplicationInfo(context_.getPackageName(), PackageManager.GET_META_DATA);
            if (ai.metaData != null) {
                appKey = ai.metaData.getString("io.branch.sdk.ApplicationId");
            }
        } catch (final PackageManager.NameNotFoundException e) {
        }

        if (appKey == null) {
            appKey = getString(KEY_APP_KEY);
        }

        return appKey;
    }

	public void setBranchKey(String key) {
	    Branch_Key = key;
    }

    public String getBranchKey(boolean isLive) {
        String branchKey = null;
        String metaDataKey = isLive ? "io.branch.sdk.BranchKey" : "io.branch.sdk.BranchKey.test";
        try {
            final ApplicationInfo ai = context_.getPackageManager().getApplicationInfo(context_.getPackageName(), PackageManager.GET_META_DATA);
            if (ai.metaData != null) {
                branchKey = ai.metaData.getString(metaDataKey);
            }
        } catch (final PackageManager.NameNotFoundException e) {
        }

        if (branchKey == null) {
            branchKey = NO_STRING_VALUE;
        }

        setBranchKey(branchKey);

        return branchKey;
    }

	public String getBranchKey() {
        if (Branch_Key == null) {
            Branch_Key = getBranchKey(true);
        }
		return Branch_Key;
	}

	/**
	 * <p>Sets the {@link android.os.Build#FINGERPRINT} value of the current OS build, on the current device,
	 * as a {@link String} in preferences.</p>
	 * 
	 * @param device_fingerprint_id 		A {@link String} that uniquely identifies this build.
	 */
	public void setDeviceFingerPrintID(String device_fingerprint_id) {
		setString(KEY_DEVICE_FINGERPRINT_ID, device_fingerprint_id);
	}

	/**
	 * <p>Gets the {@link android.os.Build#FINGERPRINT} value of the current OS build, on the current device,
	 * as a {@link String} from preferences.</p>
	 * 
	 * @return		A {@link String} that uniquely identifies this build.
	 */
	public String getDeviceFingerPrintID() {
		return getString(KEY_DEVICE_FINGERPRINT_ID);
	}

	/**
	 * <p>Sets the ID of the {@link KEY_SESSION_ID} {@link String} value in preferences.</p>
	 * 
	 * @param session_id		A {@link String} value containing the session ID as returned by the 
	 * 							Branch API upon successful initialisation.
	 */
	public void setSessionID(String session_id) {
		setString(KEY_SESSION_ID, session_id);
	}

	/**
	 * <p>Gets the ID of the {@link KEY_SESSION_ID} {@link String} value from preferences.</p>
	 * 
	 * @return			A {@link String} value containing the session ID as returned by the Branch 
	 * 					API upon successful initialisation.
	 */
	public String getSessionID() {
		return getString(KEY_SESSION_ID);
	}

	/**
	 * <p>Sets the {@link KEY_IDENTITY_ID} {@link String} value that has been set via the Branch API.</p>
	 * 
	 * <p>This is used to identify a specific <b>user ID</b> and link that to a current session. Useful both 
	 * for analytics and debugging purposes.</p>
	 * 
	 * <p><b>Note: </b> Not to be confused with {@link #setIdentity(String)} - the name of the user</p>
	 * 
	 * @param identity_id		A {@link String} value containing the currently configured identity 
	 * 							within preferences.
	 */
	public void setIdentityID(String identity_id) {
		setString(KEY_IDENTITY_ID, identity_id);
	}

	/**
	 * <p>Gets the {@link KEY_IDENTITY_ID} {@link String} value that has been set via the Branch API.</p>
	 * 
	 * @return		A {@link String} value containing the currently configured user id within 
	 * 				preferences.
	 */
	public String getIdentityID() {
		return getString(KEY_IDENTITY_ID);
	}

	/**
	 * <p>Sets the {@link KEY_IDENTITY} {@link String} value that has been set via the Branch API.</p>
	 * 
	 * <p>This is used to identify a specific <b>user identity</b> and link that to a current session. Useful both 
	 * for analytics and debugging purposes.</p>
	 * 
	 * <p><b>Note: </b> Not to be confused with {@link #setIdentityID(String)} - the UID reference of the user</p>
	 * 
	 * @param identity		A {@link String} value containing the currently configured identity 
	 * 						within preferences.
	 */
	public void setIdentity(String identity) {
		setString(KEY_IDENTITY, identity);
	}

	/**
	 * <p>Gets the {@link #KEY_IDENTITY} {@link String} value that has been set via the Branch API.</p>
	 * 
	 * <p>This is used to identify a specific <b>user identity</b> and link that to a current session. Useful both 
	 * for analytics and debugging purposes.</p>
	 * 
	 * @return		A {@link String} value containing the username assigned to the currentuser ID.
	 */
	public String getIdentity() {
		return getString(KEY_IDENTITY);
	}

	/**
	 * <p>Sets the {@link #KEY_LINK_CLICK_ID} {@link String} value that has been set via the Branch API.</p>
	 * 
	 * @param link_click_id			A {@link String} value containing the identifier of the 
	 * 								associated link.
	 */
	public void setLinkClickID(String link_click_id) {
		setString(KEY_LINK_CLICK_ID, link_click_id);
	}

	/**
	 * <p>Gets the {@link #KEY_LINK_CLICK_ID} {@link String} value that has been set via the Branch API.</p>
	 * 
	 * @return		A {@link String} value containing the identifier of the associated link.
	 */
	public String getLinkClickID() {
		return getString(KEY_LINK_CLICK_ID);
	}

	/**
	 * <p>Sets the KEY_LINK_CLICK_IDENTIFIER {@link String} value that has been set via the Branch API.</p>
	 * 
	 * @param identifer			A {@link String} value containing the identifier of the associated 
	 * 							link.
	 */
	public void setLinkClickIdentifier(String identifer) {
		setString(KEY_LINK_CLICK_IDENTIFIER, identifer);
	}

	/**
	 * <p>Gets the KEY_LINK_CLICK_IDENTIFER {@link String} value that has been set via the Branch API.</p>
	 * 
	 * @return		A {@link String} value containing the identifier of the associated link.
	 */
	public String getLinkClickIdentifier() {
		return getString(KEY_LINK_CLICK_IDENTIFIER);
	}

	/**
	 * <p>Gets the session parameters as currently set in preferences.</p>
	 * 
	 * <p>Parameters are stored in JSON format, and must be parsed prior to access.</p>
	 * 
	 * @return		A {@link String} value containing the JSON-encoded structure of parameters for 
	 * 				the current session.
	 */
	public String getSessionParams() {
		return getString(KEY_SESSION_PARAMS);
	}

	/**
	 * <p>Sets the session parameters as currently set in preferences.</p>
	 * 
	 * @param params		A {@link String} value containing the JSON-encoded structure of 
	 * 						parameters for the current session.
	 */
	public void setSessionParams(String params) {
		setString(KEY_SESSION_PARAMS, params);
	}

	/**
	 * <p>Gets the session parameters as originally set at time of app installation, in preferences.</p>
	 * 
	 * @return		A {@link String} value containing the JSON-encoded structure of parameters as 
	 * 				they were at the time of installation.
	 */
	public String getInstallParams() {
		return getString(KEY_INSTALL_PARAMS);
	}

	/**
	 * <p>Sets the session parameters as originally set at time of app installation, in preferences.</p>
	 * 
	 * @param params		A {@link String} value containing the JSON-encoded structure of 
	 * 						parameters as they should be at the time of installation.
	 */
	public void setInstallParams(String params) {
		setString(KEY_INSTALL_PARAMS, params);
	}

	/**
	 * <p>Sets the user URL from preferences.</p>
	 * 
	 * @param user_url		A {@link String} value containing the current user URL.
	 */
	public void setUserURL(String user_url) {
		setString(KEY_USER_URL, user_url);
	}

	/**
	 * <p>Sets the user URL from preferences.</p>
	 * 
	 * @return		A {@link String} value containing the current user URL. 
	 */
	public String getUserURL() {
		return getString(KEY_USER_URL);
	}

	/**
	 * <p>Gets the {@link Integer} value of the preference setting {@link #KEY_IS_REFERRABLE}, which 
	 * indicates whether or not the current session should be considered referrable.</p>
	 * 
	 * @return		A {@link Integer} value indicating whether or not the session should be 
	 * 				considered referrable.
	 */
	public int getIsReferrable() {
		return getInteger(KEY_IS_REFERRABLE);
	}

	/**
	 * <p>Sets the {@link #KEY_IS_REFERRABLE} value in preferences to 1, or <i>true</i> if parsed as a {@link Boolean}. 
	 * This value is used by the {@link Branch} object.</p>
	 * 
	 * <ul>
	 * 		<li>Sets {@link #KEY_IS_REFERRABLE} to 1 - <i>true</i> - This session <b><u>is</u></b> referrable.</li>
	 * </ul>
	 */
	public void setIsReferrable() {
		setInteger(KEY_IS_REFERRABLE, 1);
	}

	/**
	 * <p>Sets the {@link #KEY_IS_REFERRABLE} value in preferences to 0, or <i>false</i> if parsed as a {@link Boolean}. 
	 * This value is used by the {@link Branch} object.</p>
	 * 
	 * <ul>
	 * 		<li>Sets {@link #KEY_IS_REFERRABLE} to 0 - <i>false</i> - This session <b><u>is not</u></b> referrable.</li>
	 * </ul>
	 * 
	 */
	public void clearIsReferrable() {
		setInteger(KEY_IS_REFERRABLE, 0);
	}

	/**
	 * <p>Resets the time that the system was last read. This is used to calculate how "stale" the 
	 * values are that are in use in preferences.</p>
	 */
	public void clearSystemReadStatus() {
		Calendar c = Calendar.getInstance();
		setLong(KEY_LAST_READ_SYSTEM, c.getTimeInMillis() / 1000);
	}

	/**
	 * <p>Resets the user-related values that have been stored in preferences. This will cause a 
	 * sync to occur whenever a method reads any of the values and finds the value to be 0 or unset.</p>
	 */
	public void clearUserValues() {
		ArrayList<String> buckets = getBuckets();
		for (String bucket : buckets) {
			setCreditCount(bucket, 0);
		}
		setBuckets(new ArrayList<String>());

		ArrayList<String> actions = getActions();
		for (String action : actions) {
			setActionTotalCount(action, 0);
			setActionUniqueCount(action, 0);
		}
		setActions(new ArrayList<String>());
	}

	// REWARD TRACKING CALLS

	private ArrayList<String> getBuckets() {
		String bucketList = getString(KEY_BUCKETS);
		if (bucketList.equals(NO_STRING_VALUE)) {
			return new ArrayList<String>();
		} else {
			return deserializeString(bucketList);
		}
	}

	private void setBuckets(ArrayList<String> buckets) {
		if (buckets.size() == 0) {
			setString(KEY_BUCKETS, NO_STRING_VALUE);
		} else {
			setString(KEY_BUCKETS, serializeArrayList(buckets));
		}
	}

	/**
	 * <p>Sets the credit count for the default bucket to the specified {@link Integer}, in preferences.</p>
	 * 
	 * <p><b>Note:</b> This does not set the actual value of the bucket itself on the Branch server, 
	 * but only the cached value as stored in preferences for the current app. The age of that value 
	 * should be checked before being considered accurate; read {@link #KEY_LAST_READ_SYSTEM} to see 
	 * when the last system sync occurred.
	 * </p>
	 * 
	 * @param count		A {@link Integer} value that the default bucket credit count will be set to.
	 */
	public void setCreditCount(int count) {
		setCreditCount("default", count);
	}

	/**
	 * <p>Sets the credit count for the default bucket to the specified {@link Integer}, in preferences.</p>
	 * 
	 * <p><b>Note:</b> This does not set the actual value of the bucket itself on the Branch server, 
	 * but only the cached value as stored in preferences for the current app. The age of that value 
	 * should be checked before being considered accurate; read {@link #KEY_LAST_READ_SYSTEM} to see 
	 * when the last system sync occurred.
	 * </p>
	 * 
	 * @param bucket	A {@link String} value containing the value of the bucket being referenced.
	 * @param count		A {@link Integer} value that the default bucket credit count will be set to.
	 */
	public void setCreditCount(String bucket, int count) {
		ArrayList<String> buckets = getBuckets();
		if (!buckets.contains(bucket)) {
			buckets.add(bucket);
			setBuckets(buckets);
		}
		setInteger(KEY_CREDIT_BASE + bucket, count);
	}

	/**
	 * <p>Get the current cached credit count for the default bucket, as currently stored in 
	 * preferences for the current app.</p>
	 * 
	 * @return	A {@link Integer} value specifying the current number of credits in the bucket, as 
	 * 			currently stored in preferences.
	 */
	public int getCreditCount() {
		return getCreditCount("default");
	}

	/**
	 * <p>Get the current cached credit count for the default bucket, as currently stored in 
	 * preferences for the current app.</p>
	 * 
	 * @param bucket	- A {@link String} value containing the value of the bucket being referenced.
	 * 
	 * @return	A {@link Integer} value specifying the current number of credits in the bucket, as 
	 * 			currently stored in preferences.
	 */
	public int getCreditCount(String bucket) {
		return getInteger(KEY_CREDIT_BASE + bucket);
	}

	// EVENT REFERRAL INSTALL CALLS

	private ArrayList<String> getActions() {
		String actionList = getString(KEY_ACTIONS);
		if (actionList.equals(NO_STRING_VALUE)) {
			return new ArrayList<String>();
		} else {
			return deserializeString(actionList);
		}
	}

	private void setActions(ArrayList<String> actions) {
		if (actions.size() == 0) {
			setString(KEY_ACTIONS, NO_STRING_VALUE);
		} else {
			setString(KEY_ACTIONS, serializeArrayList(actions));
		}
	}

	/**
	 * <p>Sets the count of total number of times that the specified action has been carried out 
	 * during the current session, as defined in preferences.</p>
	 * 
	 * @param action	- A {@link String} value containing the name of the action to return the 
	 * 					count for.
	 * @param count		- An {@link Integer} value containing the total number of times that the 
	 * 					specified action has been carried out during the current session. 
	 */
	public void setActionTotalCount(String action, int count) {
		ArrayList<String> actions = getActions();
		if (!actions.contains(action)) {
			actions.add(action);
			setActions(actions);
		}
		setInteger(KEY_TOTAL_BASE + action, count);
	}

	/**
	 * <p>Sets the count of the unique number of times that the specified action has been carried 
	 * out during the current session, as defined in preferences.</p>
	 * 
	 * @param action	- A {@link String} value containing the name of the action to return the 
	 * 					count for.
	 * @param count		- An {@link Integer} value containing the total number of times that the 
	 * 					specified action has been carried out during the current session. 
	 */
	public void setActionUniqueCount(String action, int count) {
		setInteger(KEY_UNIQUE_BASE + action, count);
	}

	/**
	 * <p>Gets the count of total number of times that the specified action has been carried 
	 * out during the current session, as defined in preferences.</p>
	 * 
	 * @param action	A {@link String} value containing the name of the action to return the 
	 * 					count for.
	 * 
	 * @return 			An {@link Integer} value containing the total number of times that the 
	 * 					specified action has been carried out during the current session.
	 */
	public int getActionTotalCount(String action) {
		return getInteger(KEY_TOTAL_BASE + action);
	}

	/**
	 * <p>Gets the count of the unique number of times that the specified action has been carried 
	 * out during the current session, as defined in preferences.</p>
	 * 
	 * @param action	A {@link String} value containing the name of the action to return the 
	 * 					count for.
	 * 
	 * @return 			An {@link Integer} value containing the total number of times that the 
	 * 					specified action has been carried out during the current session.
	 */
	public int getActionUniqueCount(String action) {
		return getInteger(KEY_UNIQUE_BASE + action);
	}

	// ALL GENERIC CALLS

	private String serializeArrayList(ArrayList<String> strings) {
		String retString = "";
		for (String value : strings) {
			retString = retString + value + ",";
		}
		retString = retString.substring(0, retString.length() - 1);
		return retString;
	}

	private ArrayList<String> deserializeString(String list) {
		ArrayList<String> strings = new ArrayList<String>();
		String[] stringArr = list.split(",");
		Collections.addAll(strings, stringArr);
		return strings;
	}

	/**
	 * <p>A basic method that returns an integer value from a specified preferences Key.</p>
	 * 
	 * @param key	- A {@link String} value containing the key to reference.
	 * 
	 * @return	An {@link Integer} value of the specified key as stored in preferences.
	 */
	public int getInteger(String key) {
		return getInteger(key, 0);
	}

	/**
	 * <p>A basic method that returns an {@link Integer} value from a specified preferences Key, with a 
	 * default value supplied in case the value is null.</p>
	 * 
	 * @param key	- A {@link String} value containing the key to reference.
	 * 
	 * @param defaultValue	- An {@link Integer} specifying the value to use if the preferences value 
	 * 						is null.
	 * 
	 * @return	An {@link Integer} value containing the value of the specified key, or the supplied 
	 * 			default value if null.
	 */
	public int getInteger(String key, int defaultValue) {
		return prefHelper_.appSharedPrefs_.getInt(key, defaultValue);
	}

	/**
	 * <p>A basic method that returns a {@link Long} value from a specified preferences Key.</p>
	 * 
	 * @param key	- A {@link String} value containing the key to reference.
	 * 
	 * @return	A {@link Long} value of the specified key as stored in preferences.
	 */
	public long getLong(String key) {
		return prefHelper_.appSharedPrefs_.getLong(key, 0);
	}

	/**
	 * <p>A basic method that returns a {@link Float} value from a specified preferences Key.</p>
	 * 
	 * @param key	- A {@link String} value containing the key to reference.
	 * 
	 * @return	A {@link Float} value of the specified key as stored in preferences.
	 */
	public float getFloat(String key) {
		return prefHelper_.appSharedPrefs_.getFloat(key, 0);
	}

	/**
	 * <p>A basic method that returns a {@link String} value from a specified preferences Key.</p>
	 * 
	 * @param key	- A {@link String} value containing the key to reference.
	 * 
	 * @return	A {@link String} value of the specified key as stored in preferences.
	 */
	public String getString(String key) {
		return prefHelper_.appSharedPrefs_.getString(key, NO_STRING_VALUE);
	}

	/**
	 * <p>A basic method that returns a {@link Boolean} value from a specified preferences Key.</p>
	 * 
	 * @param key	- A {@link String} value containing the key to reference.
	 * 
	 * @return	An {@link Boolean} value of the specified key as stored in preferences.
	 */
	public boolean getBool(String key) {
		return prefHelper_.appSharedPrefs_.getBoolean(key, false);
	}

	/**
	 * <p>Sets the value of the {@link String} key value supplied in preferences.</p>
	 * 
	 * @param key		- A {@link String} value containing the key to reference.
	 * @param value		- An {@link Integer} value to set the preference record to.
	 */
	public void setInteger(String key, int value) {
		prefHelper_.prefsEditor_.putInt(key, value);
		prefHelper_.prefsEditor_.commit();
	}

	/**
	 * <p>Sets the value of the {@link String} key value supplied in preferences.</p>
	 * 
	 * @param key		- A {@link String} value containing the key to reference.
	 * @param value		- A {@link Long} value to set the preference record to.
	 */
	public void setLong(String key, long value) {
		prefHelper_.prefsEditor_.putLong(key, value);
		prefHelper_.prefsEditor_.commit();
	}

	/**
	 * <p>Sets the value of the {@link String} key value supplied in preferences.</p>
	 * 
	 * @param key		- A {@link String} value containing the key to reference.
	 * @param value		- A {@link Float} value to set the preference record to.
	 */
	public void setFloat(String key, float value) {
		prefHelper_.prefsEditor_.putFloat(key, value);
		prefHelper_.prefsEditor_.commit();
	}

	/**
	 * <p>Sets the value of the {@link String} key value supplied in preferences.</p>
	 * 
	 * @param key		- A {@link String} value containing the key to reference.
	 * @param value		- A {@link String} value to set the preference record to.
	 */
	public void setString(String key, String value) {
		prefHelper_.prefsEditor_.putString(key, value);
		prefHelper_.prefsEditor_.commit();
	}

	/**
	 * <p>Sets the value of the {@link String} key value supplied in preferences.</p>
	 * 
	 * @param key		- A {@link String} value containing the key to reference.
	 * @param value		- A {@link Boolean} value to set the preference record to.
	 */
	public void setBool(String key, Boolean value) {
		prefHelper_.prefsEditor_.putBoolean(key, value);
		prefHelper_.prefsEditor_.commit();
	}

	/**
	 * <p>Switches external debugging on.</p>
	 */
	public void setExternDebug() {
		BNC_Dev_Debug = true;
	}

	/**
	 * <p>Gets the value of the debug status {@link Boolean} value.</p>
	 * 
	 * @return	A {@link Boolean} value indicating the current state of external debugging.
	 */
	public boolean getExternDebug() {
		return BNC_Dev_Debug;
	}

	/**
	 * <p>Sets the {@link Boolean} value that is checked prior to the listing of external apps to 
	 * <i>false</i>.</p>
	 * 
	 */
	public void disableExternAppListing() {
		BNC_App_Listing = false;
	}
	
	/**
	 * <p>Sets the {@link Boolean} value that is checked prior to the listing of external apps.</p>
	 * 
	 * @return	A {@link Boolean} value containing the current value of the 
	 * {@link #BNC_App_Listing} boolean.
	 */
	public boolean getExternAppListing() {
		return BNC_App_Listing;
	}

	/**
	 * {@link Branch#disableSmartSession()}
	 */
	public void disableSmartSession() {
		BNC_Smart_Session = false;
	}

	/**
	 * <p>Gets the state of the {@link Boolean} value indicating whether or not the <i>Smart Session</i>
	 * feature is enabled or not.</p>
	 */
	public boolean getSmartSession() {
		return BNC_Smart_Session;
	}

	/**
	 * <p>Enable debugging, by setting the {@link Boolean} debug flags {@link #BNC_Debug} and 
	 * {@link #BNC_Debug_Connecting} to true.</p>
	 */
	public void setDebug() {
		BNC_Debug = true;
		BNC_Debug_Connecting = true;

		if (!BNC_Remote_Debug) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					if (remoteInterface_ == null) {
						remoteInterface_ = new BranchRemoteInterface(context_);
						remoteInterface_
								.setNetworkCallbackListener(new DebugNetworkCallback());
					}
					remoteInterface_.connectToDebug();
				}
			}).start();
		}
	}

	/**
	 * <p>Disable debugging, by setting the {@link Boolean} debug flags {@link #BNC_Debug} and 
	 * {@link #BNC_Debug_Connecting} to false.</p>
	 */
	public void clearDebug() {
		BNC_Debug = false;
		BNC_Debug_Connecting = false;

		if (BNC_Remote_Debug) {
			BNC_Remote_Debug = false;

			if (remoteInterface_ != null) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						remoteInterface_.disconnectFromDebug();
					}
				}).start();
			}
		}
	}

	/**
	 * <p>Gets the {@link Boolean} value of {@link #BNC_Debug}, which indicates whether or not 
	 * debugging is enabled.</p>
	 * 
	 * @return	A {@link Boolean} value indicating current debug state:
	 * 
	 * <ul>
	 * 	<li><i>true</i> - Debugging is enabled.</li>
	 *  <li><i>false</i> - Debugging is disabled.</li>
	 * </ul>
	 */
	public boolean isDebug() {
		return BNC_Debug;
	}

	/**
	 * <p>Creates a <b>Log</b> message in the debugger. If debugging is disabled, this will fail silently.</p>
	 * 
	 * @param tag		- A {@link String} value specifying the logging tag to use for the message.
	 * 
	 * @param message	- A {@link String} value containing the logging message to record.
	 */
	public void log(final String tag, final String message) {
		if (BNC_Debug || BNC_Dev_Debug) {
			Log.i(tag, message);

			if (BNC_Remote_Debug && remoteInterface_ != null) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						remoteInterface_.sendLog(tag + "\t" + message);
					}
				}).start();
			}
		}
	}

	/**
	 * <p>Creates a <b>Debug</b> message in the debugger. If debugging is disabled, this will fail silently.</p>
	 * 
	 * @param tag		A {@link String} value specifying the logging tag to use for the message.
	 * 
	 * @param message	A {@link String} value containing the debug message to record.
	 */
	public static void Debug(String tag, String message) {
		if (prefHelper_ != null) {
			prefHelper_.log(tag, message);
		} else {
			if (BNC_Debug || BNC_Dev_Debug) {
				Log.i(tag, message);
			}
		}
	}

	/**
	 * <p>Sends an empty logging message to keep the debugger connection alive.</p>
	 * 
	 * @return		A {@link Boolean} value indicating the debug connection state:
	 * 
	 * <ul>
	 * 		<li><i>true</i> - If the debug connection has been kept alive.</li>
	 * 		<li><i>false</i> - If the debug connection has not been kept alive, if 
	 * {@link #BNC_Remote_Debug} is false, or if there is no current remote connection.</li>
	 * </ul>
	 */
	public boolean keepDebugConnection() {
		if (BNC_Remote_Debug && remoteInterface_ != null) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					remoteInterface_.sendLog("");
				}
			}).start();
			return true;
		}

		return BNC_Debug_Connecting;
	}

	/**
	 * <p>Debug connection callback that implements {@link NetworkCallback} to react to server calls 
	 * to debug API end-points.</p>
	 */
	public static class DebugNetworkCallback implements NetworkCallback {
		private int connectionStatus;

		/**
		 * 
		 * @return {@link Integer} value containing the HTTP Status code of the current connection.
		 * 
		 * <ul>
		 *     <li>200 - The request has succeeded.</li>
		 *     <li>400 - Request cannot be fulfilled due to bad syntax</li>
		 *     <li>465 - Server is not listening.</li>
		 *     <li>500 - The server encountered an unexpected condition which prevented it from fulfilling the request.</li>
		 * </ul>
		 * 
		 * @see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html">HTTP/1.1 - Status Code Definitions</a>
		 */
		public int getConnectionStatus() {
			return connectionStatus;
		}

		/**
		 * Called when the server response is returned following a request to the debug API.
		 * 
		 * @param serverResponse	A {@link ServerResponse} object containing the result of the 
		 * 							{@link DebugNetworkCallback} action.
		 */
		@Override
		public void finished(ServerResponse serverResponse) {
			if (serverResponse != null) {
				try {
					connectionStatus = serverResponse.getStatusCode();
					String requestTag = serverResponse.getTag();

					if (connectionStatus == 465) {
						BNC_Remote_Debug = false;
						Log.i("Branch Debug",
								"======= Server is not listening =======");
					} else if (connectionStatus >= 400
							&& connectionStatus < 500) {
						if (serverResponse.getObject() != null
								&& serverResponse.getObject().has("error")
								&& serverResponse.getObject()
										.getJSONObject("error").has("message")) {
							Log.i("BranchSDK",
									"Branch API Error: "
											+ serverResponse.getObject()
													.getJSONObject("error")
													.getString("message"));
						}
					} else if (connectionStatus != 200) {
						if (connectionStatus == RemoteInterface.NO_CONNECTIVITY_STATUS) {
							Log.i("BranchSDK",
									"Branch API Error: poor network connectivity. Please try again later.");
						} else {
							Log.i("BranchSDK",
									"Trouble reaching server. Please try again in a few minutes.");
						}
					} else if (requestTag.equals(REQ_TAG_DEBUG_CONNECT)) {
						BNC_Remote_Debug = true;
						Log.i("Branch Debug",
								"======= Connected to Branch Remote Debugger =======");
					}

					BNC_Debug_Connecting = false;
				} catch (JSONException ex) {
					ex.printStackTrace();
				}
			}
		}
	}

}
