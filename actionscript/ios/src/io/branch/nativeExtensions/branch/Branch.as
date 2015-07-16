package io.branch.nativeExtensions.branch {

	import flash.events.EventDispatcher;
	import flash.events.StatusEvent;
	import flash.external.ExtensionContext;

	public class Branch extends EventDispatcher {

		private static var _instance:Branch;

		public var extensionContext:ExtensionContext;

		public static function getInstance():Branch {

			if (!_instance)
				_instance = new Branch();

			return _instance;
		}

		public function Branch() {

			if (_instance)
				throw new Error("Branch is already initialized.");

			_instance = this;

			extensionContext = ExtensionContext.createExtensionContext("io.branch.nativeExtensions.Branch", null);

			if (!extensionContext)
				throw new Error("Branch native extension is not supported on this platform.");

			extensionContext.addEventListener(StatusEvent.STATUS, _onStatus);
		}

		private function _onStatus(sEvt:StatusEvent):void {

			dispatchEvent(new BranchEvent(sEvt.code, sEvt.level));
		}

		public function init(useTestKey:Boolean = false):void {

			extensionContext.call("init", useTestKey);
		}

		public function setIdentity(userId:String):void {
			
			extensionContext.call("setIdentity", userId);
		}

		public function getShortUrl(tags:Array = null, channel:String = "", feature:String = "", stage:String = "", json:String = "{}"):void {

			extensionContext.call("getShortUrl", tags, channel, feature, stage, json);
		}

		public function logout():void {
			
			extensionContext.call("logout");
		}

		public function getLatestReferringParams():String {

			return extensionContext.call("getLatestReferringParams") as String;
		}

		public function getFirstReferringParams():String {

			return extensionContext.call("getFirstReferringParams") as String;
		}
	}
}