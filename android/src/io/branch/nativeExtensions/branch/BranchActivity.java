package io.branch.nativeExtensions.branch;

import io.branch.referral.Branch;
import io.branch.referral.Branch.BranchReferralInitListener;
import io.branch.referral.BranchError;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;

public class BranchActivity extends Activity {
	
	Branch branch;
	
	@Override
	protected void onStart() {
		super.onStart();
		
		branch = Branch.getInstance(getApplicationContext());
		
		branch.initSession(new BranchReferralInitListener() {
			
			@Override
			public void onInitFinished(JSONObject referringParams, BranchError error) {
				
				if (error == null) {
					
					BranchExtension.context.dispatchStatusEventAsync("INIT_SUCCESSED", referringParams.toString());
					
				} else {
					
					BranchExtension.context.dispatchStatusEventAsync("INIT_FAILED", error.getMessage());
				}
				
			}
		}, getIntent().getData(), this);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		
		branch.closeSession();
	}
	
	@Override
	public void onNewIntent(Intent intent) {
		this.setIntent(intent);
	}

}
