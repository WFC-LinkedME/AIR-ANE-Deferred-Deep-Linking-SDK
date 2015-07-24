//
//  BranchHelpers.h
//  BranchIosExtension
//
//  Created by Aymeric Lamboley on 13/04/2015.
//  Copyright (c) 2015 Pawprint Labs. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "FlashRuntimeExtensions.h"
#import <Branch/Branch.h>

@interface BranchHelpers : NSObject {
    
    FREContext ctx;
    Branch *branch;
}

- (id) initWithContext:(FREContext) context;

- (void) initBranch:(BOOL) useTestKey;
- (void) setIdentity:(NSString *) userId;
- (void) getShortURL:(NSString *) json andTags:(NSArray *) tags andChannel:(NSString *) channel andFeature:(NSString *) feature andStage:(NSString *) stage andAlias:(NSString *) alias andType:(int) type;
- (void) logout;

- (NSDictionary *) getLatestReferringParams;
- (NSDictionary *) getFirstReferringParams;
- (void) getCredits:(NSString *) bucket;
- (void) redeemRewards:(NSInteger) credits andBucket:(NSString *) bucket;
- (void) getCreditsHistory:(NSString *) bucket;
- (void) getReferralCode;
- (void) applyReferralCode:(NSString *) code;

- (void) dispatchEvent:(NSString *) event withParams:(NSString * ) params;

@end
