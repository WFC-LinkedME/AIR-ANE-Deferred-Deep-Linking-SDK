package io.branch.nativeExtensions.branch.functions;

import io.branch.nativeExtensions.branch.BranchExtension;
import io.branch.referral.Branch;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREObject;

public class LogoutFunction extends BaseFunction {
	
	@Override
	public FREObject call(FREContext context, FREObject[] args) {
		super.call(context, args);
		
		Branch.getInstance(BranchExtension.context.getActivity().getApplicationContext()).logout();
		
		return null;
	}
}
