package io.branch.nativeExtensions.branch.functions;

import io.branch.nativeExtensions.branch.BranchActivity;
import io.branch.nativeExtensions.branch.BranchExtension;
import io.branch.referral.Branch.BranchLinkCreateListener;
import io.branch.referral.BranchError;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.adobe.fre.FREArray;
import com.adobe.fre.FREContext;
import com.adobe.fre.FREObject;

public class GetShortUrlFunction extends BaseFunction {
	
	@Override
	public FREObject call(FREContext context, FREObject[] args) {
		super.call(context, args);
		
		List<String> tags = getListOfStringFromFREArray((FREArray)args[0]);
		String channel = getStringFromFREObject(args[1]);
		String feature = getStringFromFREObject(args[2]);
		String stage = getStringFromFREObject(args[3]);
		String json = getStringFromFREObject(args[4]);
		
		try {
			
			JSONObject obj = new JSONObject(json);
			
			BranchActivity.branch.getShortUrl(tags, channel, feature, stage, obj, new BranchLinkCreateListener() {
				
				@Override
				public void onLinkCreate(String url, BranchError error) {
					
					if (error == null) {
						
						BranchExtension.context.dispatchStatusEventAsync("GET_SHORT_URL_SUCCESSED", url);
						
					} else {
						
						BranchExtension.context.dispatchStatusEventAsync("GET_SHORT_URL_FAILED", error.getMessage());
					}
					
				}
			});
			
		} catch (JSONException t) {
			
			BranchExtension.context.dispatchStatusEventAsync("GET_SHORT_URL_FAILED", "Could not parse malformed JSON");
		}
		
		return null;
	}
}
