package io.branch.nativeExtensions.branch {

	import flash.events.Event;

	public class BranchEvent extends Event {

		/**
		* Dispatched when the key has been succesfully set up. See event's <code>informations</code> for referringParams.
		*/
		static public const INIT_SUCCESSED:String = "INIT_SUCCESSED";

		/**
		* Dispatched when the key init has failed. See event's <code>informations</code> for details.
		*/
		static public const INIT_FAILED:String = "INIT_FAILED";

		/**
		* Dispatched when the identity has been succesfully set up.
		*/
		static public const SET_IDENTITY_SUCCESSED:String = "SET_IDENTITY_SUCCESSED";

		/**
		* Dispatched when the identity set up has failed. See event's <code>informations</code> for details.
		*/
		static public const SET_IDENTITY_FAILED:String = "SET_IDENTITY_FAILED";

		/**
		* Dispatched when the short url has been succesfully created. See event's <code>informations</code> for the result.
		*/
		static public const GET_SHORT_URL_SUCCESSED:String = "GET_SHORT_URL_SUCCESSED";

		/**
		* Dispatched when the <code>getShortUrl</code> has failed. See event's <code>informations</code> for details.
		*/
		static public const GET_SHORT_URL_FAILED:String = "GET_SHORT_URL_FAILED";

		/**
		* Dispatched when the <code>getCredits</code> method is called and successed. See event's <code>informations</code> for details.
		*/
		static public const GET_CREDITS_SUCCESSED:String = "GET_CREDITS_SUCCESSED";

		/**
		* Dispatched when the <code>getCredits</code> has failed. See event's <code>informations</code> for details.
		*/
		static public const GET_CREDITS_FAILED:String = "GET_CREDITS_FAILED";

		/**
		* Dispatched when the <code>redeemRewards</code> has successed.
		*/
		static public const REDEEM_REWARDS_SUCCESSED:String = "REDEEM_REWARDS_SUCCESSED";

		/**
		* Dispatched when the <code>redeemRewards</code> has failed. See event's <code>informations</code> for details.
		*/
		static public const REDEEM_REWARDS_FAILED:String = "REDEEM_REWARDS_FAILED";

		private var _informations:String;

		public function BranchEvent(type:String, informations:String, bubbles:Boolean = false, cancelable:Boolean = false) {
			super(type, bubbles, cancelable);

			_informations = informations;
		}


		/**
		* <code>informations</code> contains the error from the base SDK or the the result expected like for <code>getShortUrl</code>.
		* It can also contains credits, think to turn them into <code>int</code>!
		*/
		public function get informations():String {

			return _informations;
		}
	}
}
