package io.branch.nativeExtensions.branch.functions;

import io.branch.nativeExtensions.branch.BranchExtension;
import io.branch.referral.Branch;

import org.json.JSONObject;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREObject;
import com.adobe.fre.FREWrongThreadException;

public class GetLatestReferringParamsFunction extends BaseFunction {
	
	@Override
	public FREObject call(FREContext context, FREObject[] args) {
		super.call(context, args);
		
		JSONObject sessionsParams = Branch.getInstance(BranchExtension.context.getActivity().getApplicationContext()).getLatestReferringParams();
		
		try {
			
			String result = sessionsParams.toString().replace("\\", "");
			
			return FREObject.newObject(result);
			
		} catch (FREWrongThreadException e) {
			e.printStackTrace();
        	return null;
		}
	}
}
