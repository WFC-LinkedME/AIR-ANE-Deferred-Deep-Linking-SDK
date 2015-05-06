package io.branch.nativeExtensions.branch;

import io.branch.nativeExtensions.branch.functions.GetFirstReferringParamsFunction;
import io.branch.nativeExtensions.branch.functions.GetLatestReferringParamsFunction;
import io.branch.nativeExtensions.branch.functions.GetShortUrlFunction;
import io.branch.nativeExtensions.branch.functions.InitFunction;
import io.branch.nativeExtensions.branch.functions.LogoutFunction;
import io.branch.nativeExtensions.branch.functions.SetIdentityFunction;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;

public class BranchExtensionContext extends FREContext {

	@Override
	public void dispose() {
	}

	@Override
	public Map<String, FREFunction> getFunctions() {
		
		Map<String, FREFunction> functionMap = new HashMap<String, FREFunction>();
		
		functionMap.put("init", new InitFunction());
		functionMap.put("setIdentity", new SetIdentityFunction());
		functionMap.put("getShortUrl", new GetShortUrlFunction());
		functionMap.put("logout", new LogoutFunction());
		functionMap.put("getLatestReferringParams", new GetLatestReferringParamsFunction());
		functionMap.put("getFirstReferringParams", new GetFirstReferringParamsFunction());
		
		return functionMap;
	}
	
	public void initActivity() {
		
		Intent i = new Intent(getActivity().getApplicationContext(), BranchActivity.class);
		
		getActivity().startActivity(i);
	}

}
