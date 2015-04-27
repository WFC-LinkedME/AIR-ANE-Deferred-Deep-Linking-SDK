package {

	import io.branch.nativeExtensions.branch.BranchConst;
	import io.branch.nativeExtensions.branch.Branch;
	import io.branch.nativeExtensions.branch.BranchEvent;

	import flash.display.Sprite;

	/**
	 * @author Aymeric
	 */
	public class BranchTest extends Sprite {
		
		private var _branch:Branch;

		public function BranchTest() {
			
			_branch = new Branch();

			_branch.addEventListener(BranchEvent.INIT_FAILED, _initFailed);
			_branch.addEventListener(BranchEvent.INIT_SUCCESSED, _initSuccessed);

			_branch.addEventListener(BranchEvent.SET_IDENTITY_FAILED, _setIdentityFailed);
			_branch.addEventListener(BranchEvent.SET_IDENTITY_SUCCESSED, _setIdentitySuccessed);

			_branch.addEventListener(BranchEvent.GET_SHORT_URL_FAILED, _getShortUrlFailed);
			_branch.addEventListener(BranchEvent.GET_SHORT_URL_SUCCESSED, _getShortUrlSuccessed);

			_branch.init();
		}

		private function _initSuccessed(bEvt:BranchEvent):void {
			trace("BranchEvent.INIT_SUCCESSED", bEvt.informations);
			
			_branch.setIdentity("Bob");
		}
		
		private function _setIdentitySuccessed(bEvt:BranchEvent):void {
			trace("BranchEvent.SET_IDENTITY_SUCCESSED", bEvt.informations);
			
			var dataToInclude:Object = {
				user:"Joe",
				profile_pic:"https://avatars3.githubusercontent.com/u/7772941?v=3&s=200",
				description:"Joe likes long walks on the beach...",
				
				// customize the display of the Branch link
				"$og_title":"Joe's My App Referral",
				"$og_image_url":"https://branch.io/img/logo_white.png",
				"$og_description":"Join Joe in My App - it's awesome"
			};
			
			var tags:Array = ["version1", "trial6"];
			
			_branch.getShortUrl(tags, "text_message", BranchConst.FEATURE_TAG_SHARE, "level_3", JSON.stringify(dataToInclude));
		}
		
		private function _getShortUrlSuccessed(bEvt:BranchEvent):void {
			trace("BranchEvent.GET_SHORT_URL_SUCCESSED", bEvt.informations);
		}
		
		private function _initFailed(bEvt:BranchEvent):void {
			trace("BranchEvent.INIT_FAILED", bEvt.informations);
		}
		
		private function _setIdentityFailed(bEvt:BranchEvent):void {
			trace("BranchEvent.SET_IDENTITY_FAILED", bEvt.informations);
		}
		
		private function _getShortUrlFailed(bEvt:BranchEvent):void {
			trace("BranchEvent.GET_SHORT_URL_FAILED", bEvt.informations);
		}
	}
}
