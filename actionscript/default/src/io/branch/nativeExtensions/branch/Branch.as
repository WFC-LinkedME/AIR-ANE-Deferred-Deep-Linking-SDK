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
		*/
		public function init():void {

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
		*/
		public function getShortUrl(tags:Array = null, channel:String = "", feature:String = "", stage:String = "", json:String = "{}"):void {

		}

		/**
		* If you provide a logout function in your app, be sure to clear the user when the logout completes.
		* This will ensure that all the stored parameters get cleared and all events are properly attributed to the right identity.
		* <b>Warning</b> this call will clear the referral credits and attribution on the device.
		*/
		public function logout():void {
			
		}
	}
}