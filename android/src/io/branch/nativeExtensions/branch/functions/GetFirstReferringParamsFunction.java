package io.branch.nativeExtensions.branch.functions;

import io.branch.nativeExtensions.branch.BranchExtension;
import io.branch.referral.Branch;

import org.json.JSONObject;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREObject;
import com.adobe.fre.FREWrongThreadException;

public class GetFirstReferringParamsFunction extends BaseFunction {
	
	@Override
	public FREObject call(FREContext context, FREObject[] args) {
		super.call(context, args);
		
		JSONObject installParams = Branch.getInstance(BranchExtension.context.getActivity().getApplicationContext()).getFirstReferringParams();
		
		try {
			
			String result = installParams.toString().replace("\\", "");
			
			return FREObject.newObject(result);
			
		} catch (FREWrongThreadException e) {
			e.printStackTrace();
        	return null;
		}
	}
}
