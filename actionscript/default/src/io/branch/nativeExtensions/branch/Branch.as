package io.branch.nativeExtensions.branch {

	import flash.events.EventDispatcher;
	import flash.external.ExtensionContext;

	public class Branch extends EventDispatcher {

		private static var _instance:Branch;

		/**
		* Get the <code>Branch</code> instance. If none has been created, it creates one.
		*/
		public static function getInstance():Branch {

			if (!_instance)
				_instance = new Branch();

			return _instance;
		}

		/**
		* <code>Branch</code> is a Singleton, it can only be initialized one time.
		*/
		public function Branch() {

			if (_instance)
				throw new Error("Branch is already initialized.");

			_instance = this;
		}

		/**
		* Init the Branch SDK. For iOS and Android, the key must be set in the *-app.xml Please refer to the README.md and the example.
		* You'll get the deep linked params associated with the link that the user clicked before showing up via the <code>BranchEvent.INIT_SUCCESSED</code> event.
		* @param useTestKey Set it to true to use the key test.
		*/
		public function init(useTestKey:Boolean = false):void {

		}

		/**
		* Often, you might have your own user IDs, or want referral and event data to persist across platforms or uninstall/reinstall.
		* It's helpful if you know your users access your service from different devices. This where we introduce the concept of an 'identity'.
		* @param userId Identify a user, with his user id.
		*/
		public function setIdentity(userId:String):void {

		}

		/**
		* With each Branch link, we pack in as much functionality and measurement as possible.
		* You get the powerful deep linking functionality in addition to the all the install and reengagement attribution, all in one link.
		* For more details on how to create links, see the <a href="https://github.com/BranchMetrics/Branch-Integration-Guides/blob/master/url-creation-guide.md">Branch link creation guide</a>.
		* @param tags examples: set of tags could be "version1", "trial6", etc; each tag should not exceed 64 characters
		* @param channel examples: "facebook", "twitter", "text_message", etc; should not exceed 128 characters
		* @param feature examples: Branch.FEATURE_TAG_SHARE, Branch.FEATURE_TAG_REFERRAL, "unlock", etc; should not exceed 128 characters
		* @param stage examples: "past_customer", "logged_in", "level_6"; should not exceed 128 characters
		* @param json a stringify JSON, you can access this data from any instance that installs or opens the app from this link, customize the display of the Branch link and customize the desktop redirect location
		* @param alias the alias for a link.
		* @param type can be used for scenarios where you want the link to only deep link the first time.
		*/
		public function getShortUrl(tags:Array = null, channel:String = "", feature:String = "", stage:String = "", json:String = "{}", alias:String = "", type:int = -1):void {

		}

		/**
		* If you provide a logout function in your app, be sure to clear the user when the logout completes.
		* This will ensure that all the stored parameters get cleared and all events are properly attributed to the right identity.
		* <b>Warning</b> this call will clear the referral credits and attribution on the device.
		*/
		public function logout():void {
			
		}

		/**
		* These session parameters will be available at any point later on with this command. If no params, the dictionary will be empty.
		* This refreshes with every new session (app installs AND app opens).
		* @return a String with sessionParams, turn it into a JSON.
		*/
		public function getLatestReferringParams():String {

			return "";
		}

		/**
		* If you ever want to access the original session params (the parameters passed in for the first install event only), you can use this line.
		* This is useful if you only want to reward users who newly installed the app from a referral link or something.
		* @return a String with installParams, turn it into a JSON.
		*/
		public function getFirstReferringParams():String {

			return "";
		}

		/**
		* Reward balances change randomly on the backend when certain actions are taken (defined by your rules), so it'll make an asynchronous call to retrieve the balance.
		* Be sure to listen <code>GET_CREDITS</code> event.
		* @param bucket The bucket to get credits balance from.
		*/
		public function getCredits(bucket:String = ""):void {
			
		}

		/**
		* We will store how many of the rewards have been deployed so that you don't have to track it on your end. In order to save that you gave the credits to the user, you can call redeem.
		* Redemptions will reduce the balance of outstanding credits permanently.
		* @param credits credits given to the user.
		* @param bucket The bucket to get credits balance from.
		*/
		public function redeemRewards(credits:int, bucket:String = ""):void {

		}
	}
}