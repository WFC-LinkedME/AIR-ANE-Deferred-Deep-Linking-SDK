package io.branch.nativeExtensions.branch.functions;

import io.branch.nativeExtensions.branch.BranchActivity;
import io.branch.nativeExtensions.branch.BranchExtension;
import io.branch.referral.Branch.BranchReferralStateChangedListener;
import io.branch.referral.BranchError;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;

public class GetCreditsFunction extends BaseFunction implements FREFunction {
	
	@Override
	public FREObject call(FREContext context, FREObject[] args) {
		super.call(context, args);
		
		BranchActivity.branch.loadRewards(new BranchReferralStateChangedListener() {
			
			@Override
			public void onStateChanged(boolean changed, BranchError error) {
				
				if (error == null)
					BranchExtension.context.dispatchStatusEventAsync("GET_SHORT_URL_SUCCESSED", String.valueOf(BranchActivity.branch.getCredits()));
					
				else
					BranchExtension.context.dispatchStatusEventAsync("GET_SHORT_URL_FAILED", error.getMessage());
			}
		});
		
		return null;
	}
}
