//
//  Branch_SDK.h
//  Branch-SDK
//
//  Created by Alex Austin on 6/5/14.
//  Copyright (c) 2014 Branch Metrics. All rights reserved.
//
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "BranchActivityItemProvider.h"

typedef void (^callbackWithParams) (NSDictionary *params, NSError *error);
typedef void (^callbackWithUrl) (NSString *url, NSError *error);
typedef void (^callbackWithStatus) (BOOL changed, NSError *error);
typedef void (^callbackWithList) (NSArray *list, NSError *error);

static NSString *BRANCH_FEATURE_TAG_SHARE = @"share";
static NSString *BRANCH_FEATURE_TAG_REFERRAL = @"referral";
static NSString *BRANCH_FEATURE_TAG_INVITE = @"invite";
static NSString *BRANCH_FEATURE_TAG_DEAL = @"deal";
static NSString *BRANCH_FEATURE_TAG_GIFT = @"gift";

static NSString *TAGS = @"tags";
static NSString *LINK_TYPE = @"type";
static NSString *ALIAS = @"alias";
static NSString *CHANNEL = @"channel";
static NSString *FEATURE = @"feature";
static NSString *STAGE = @"stage";
static NSString *DURATION = @"duration";
static NSString *DATA = @"data";
static NSString *IGNORE_UA_STRING = @"ignore_ua_string";

typedef NS_ENUM(NSUInteger, BranchCreditHistoryOrder) {
    BranchMostRecentFirst,
    BranchLeastRecentFirst
};


typedef NS_ENUM(NSUInteger, BranchReferralCodeLocation) {
    BranchReferreeUser = 0,
    BranchReferringUser = 2,
    BranchBothUsers = 3
};

typedef NS_ENUM(NSUInteger, BranchReferralCodeCalculation) {
    BranchUniqueRewards = 1,
    BranchUnlimitedRewards = 0
};

typedef NS_ENUM(NSUInteger, BranchLinkType) {
    BranchLinkTypeUnlimitedUse = 0,
    BranchLinkTypeOneTimeUse = 1
};

// Backwards compat
typedef BranchCreditHistoryOrder CreditHistoryOrder;
typedef BranchReferralCodeLocation ReferralCodeLocation;
typedef BranchReferralCodeCalculation ReferralCodeCalculation;

@interface Branch : NSObject

+ (Branch *)getInstance;
+ (Branch *)getInstance:(NSString *)branchKey;
+ (Branch *)getTestInstance;

// Branch Activity item providers for UIActivityViewController
+ (BranchActivityItemProvider *)getBranchActivityItemWithParams:(NSDictionary *)params
                                                     andTags:(NSArray *)tags
                                                  andFeature:(NSString *)feature
                                                    andStage:(NSString *)stage
                                                    andAlias:(NSString *)alias;

+ (BranchActivityItemProvider *)getBranchActivityItemWithParams:(NSDictionary *)params;

+ (BranchActivityItemProvider *)getBranchActivityItemWithParams:(NSDictionary *)params
                                                  andFeature:(NSString *)feature;

+ (BranchActivityItemProvider *)getBranchActivityItemWithParams:(NSDictionary *)params
                                                  andFeature:(NSString *)feature
                                                    andStage:(NSString *)stage;

+ (BranchActivityItemProvider *)getBranchActivityItemWithParams:(NSDictionary *)params
                                                         andFeature:(NSString *)feature
                                                           andStage:(NSString *)stage
                                                           andTags:(NSArray *)tags;

+ (BranchActivityItemProvider *)getBranchActivityItemWithParams:(NSDictionary *)params
                                                         andFeature:(NSString *)feature
                                                           andStage:(NSString *)stage
                                                           andAlias:(NSString *)alias;

+ (void)setDebug;

- (void)initSession;
- (void)initSessionWithLaunchOptions:(NSDictionary *)options;
- (void)initSession:(BOOL)isReferrable;
- (void)initSessionWithLaunchOptions:(NSDictionary *)options isReferrable:(BOOL)isReferrable;
- (void)initSessionAndRegisterDeepLinkHandler:(callbackWithParams)callback;
- (void)initSessionWithLaunchOptions:(NSDictionary *)options andRegisterDeepLinkHandler:(callbackWithParams)callback;
- (void)initSession:(BOOL)isReferrable andRegisterDeepLinkHandler:(callbackWithParams)callback;
- (void)initSessionWithLaunchOptions:(NSDictionary *)options isReferrable:(BOOL)isReferrable andRegisterDeepLinkHandler:(callbackWithParams)callback;

- (NSDictionary *)getFirstReferringParams;
- (NSDictionary *)getLatestReferringParams;
- (void)resetUserSession;
- (void)setRetryInterval:(NSInteger)retryInterval;
- (void)setMaxRetries:(NSInteger)maxRetries;
- (void)setNetworkTimeout:(NSInteger)timeout;

- (BOOL)handleDeepLink:(NSURL *)url;

- (void)setIdentity:(NSString *)userId;
- (void)setIdentity:(NSString *)userId withCallback:(callbackWithParams)callback;
- (void)logout;

- (void)loadRewardsWithCallback:(callbackWithStatus)callback;
- (void)loadActionCountsWithCallback:(callbackWithStatus)callback;
- (NSInteger)getCredits;
- (void)redeemRewards:(NSInteger)count;
- (NSInteger)getCreditsForBucket:(NSString *)bucket;
- (void)redeemRewards:(NSInteger)count forBucket:(NSString *)bucket;
- (void)userCompletedAction:(NSString *)action;
- (void)userCompletedAction:(NSString *)action withState:(NSDictionary *)state;
- (NSInteger)getTotalCountsForAction:(NSString *)action;
- (NSInteger)getUniqueCountsForAction:(NSString *)action;

- (void)getCreditHistoryWithCallback:(callbackWithList)callback;
- (void)getCreditHistoryForBucket:(NSString *)bucket andCallback:(callbackWithList)callback;
- (void)getCreditHistoryAfter:(NSString *)creditTransactionId number:(NSInteger)length order:(BranchCreditHistoryOrder)order andCallback:(callbackWithList)callback;
- (void)getCreditHistoryForBucket:(NSString *)bucket after:(NSString *)creditTransactionId number:(NSInteger)length order:(BranchCreditHistoryOrder)order andCallback:(callbackWithList)callback;

- (NSString *)getShortURL;
- (NSString *)getShortURLWithParams:(NSDictionary *)params;
- (NSString *)getContentUrlWithParams:(NSDictionary *)params andChannel:(NSString *)channel;
- (NSString *)getContentUrlWithParams:(NSDictionary *)params andTags:(NSArray *)tags andChannel:(NSString *)channel;
- (NSString *)getReferralUrlWithParams:(NSDictionary *)params andTags:(NSArray *)tags andChannel:(NSString *)channel;
- (NSString *)getReferralUrlWithParams:(NSDictionary *)params andChannel:(NSString *)channel;
- (NSString *)getShortURLWithParams:(NSDictionary *)params andTags:(NSArray *)tags andChannel:(NSString *)channel andFeature:(NSString *)feature andStage:(NSString *)stage;
- (NSString *)getShortURLWithParams:(NSDictionary *)params andTags:(NSArray *)tags andChannel:(NSString *)channel andFeature:(NSString *)feature andStage:(NSString *)stage andAlias:(NSString *)alias;
- (NSString *)getShortURLWithParams:(NSDictionary *)params andTags:(NSArray *)tags andChannel:(NSString *)channel andFeature:(NSString *)feature andStage:(NSString *)stage andAlias:(NSString *)alias ignoreUAString:(NSString *)ignoreUAString;
- (NSString *)getShortURLWithParams:(NSDictionary *)params andTags:(NSArray *)tags andChannel:(NSString *)channel andFeature:(NSString *)feature andStage:(NSString *)stage andType:(BranchLinkType)type;
- (NSString *)getShortURLWithParams:(NSDictionary *)params andTags:(NSArray *)tags andChannel:(NSString *)channel andFeature:(NSString *)feature andStage:(NSString *)stage andMatchDuration:(NSUInteger)duration;
- (NSString *)getShortURLWithParams:(NSDictionary *)params andChannel:(NSString *)channel andFeature:(NSString *)feature andStage:(NSString *)stage;
- (NSString *)getShortURLWithParams:(NSDictionary *)params andChannel:(NSString *)channel andFeature:(NSString *)feature andStage:(NSString *)stage andAlias:(NSString *)alias;
- (NSString *)getShortURLWithParams:(NSDictionary *)params andChannel:(NSString *)channel andFeature:(NSString *)feature andStage:(NSString *)stage andType:(BranchLinkType)type;
- (NSString *)getShortURLWithParams:(NSDictionary *)params andChannel:(NSString *)channel andFeature:(NSString *)feature andStage:(NSString *)stage andMatchDuration:(NSUInteger)duration;
- (NSString *)getShortURLWithParams:(NSDictionary *)params andChannel:(NSString *)channel andFeature:(NSString *)feature;

- (NSString *)getLongURLWithParams:(NSDictionary *)params
                                                andChannel:(NSString *)channel
                                                    andTags:(NSArray *)tags
                                                 andFeature:(NSString *)feature
                                                   andStage:(NSString *)stage
                                                   andAlias:(NSString *)alias;

- (NSString *)getLongURLWithParams:(NSDictionary *)params;

- (NSString *)getLongURLWithParams:(NSDictionary *)params
                                                 andFeature:(NSString *)feature;

- (NSString *)getLongURLWithParams:(NSDictionary *)params
                                                 andFeature:(NSString *)feature
                                                   andStage:(NSString *)stage;

- (NSString *)getLongURLWithParams:(NSDictionary *)params
                                                 andFeature:(NSString *)feature
                                                   andStage:(NSString *)stage
                                                    andTags:(NSArray *)tags;

- (NSString *)getLongURLWithParams:(NSDictionary *)params
                                                 andFeature:(NSString *)feature
                                                   andStage:(NSString *)stage
                                                   andAlias:(NSString *)alias;

- (void)getShortURLWithCallback:(callbackWithUrl)callback;
- (void)getShortURLWithParams:(NSDictionary *)params andCallback:(callbackWithUrl)callback;
- (void)getContentUrlWithParams:(NSDictionary *)params andChannel:(NSString *)channel andCallback:(callbackWithUrl)callback;
- (void)getContentUrlWithParams:(NSDictionary *)params andTags:(NSArray *)tags andChannel:(NSString *)channel andCallback:(callbackWithUrl)callback;
- (void)getReferralUrlWithParams:(NSDictionary *)params andTags:(NSArray *)tags andChannel:(NSString *)channel andCallback:(callbackWithUrl)callback;
- (void)getReferralUrlWithParams:(NSDictionary *)params andChannel:(NSString *)channel andCallback:(callbackWithUrl)callback;
- (void)getShortURLWithParams:(NSDictionary *)params andTags:(NSArray *)tags andChannel:(NSString *)channel andFeature:(NSString *)feature andStage:(NSString *)stage andCallback:(callbackWithUrl)callback;
- (void)getShortURLWithParams:(NSDictionary *)params andTags:(NSArray *)tags andChannel:(NSString *)channel andFeature:(NSString *)feature andStage:(NSString *)stage andAlias:(NSString *)alias andCallback:(callbackWithUrl)callback;
- (void)getShortURLWithParams:(NSDictionary *)params andTags:(NSArray *)tags andChannel:(NSString *)channel andFeature:(NSString *)feature andStage:(NSString *)stage andType:(BranchLinkType)type andCallback:(callbackWithUrl)callback;
- (void)getShortURLWithParams:(NSDictionary *)params andTags:(NSArray *)tags andChannel:(NSString *)channel andFeature:(NSString *)feature andStage:(NSString *)stage andMatchDuration:(NSUInteger)duration andCallback:(callbackWithUrl)callback;
- (void)getShortURLWithParams:(NSDictionary *)params andChannel:(NSString *)channel andFeature:(NSString *)feature andStage:(NSString *)stage andCallback:(callbackWithUrl)callback;
- (void)getShortURLWithParams:(NSDictionary *)params andChannel:(NSString *)channel andFeature:(NSString *)feature andStage:(NSString *)stage andAlias:(NSString *)alias andCallback:(callbackWithUrl)callback;
- (void)getShortURLWithParams:(NSDictionary *)params andChannel:(NSString *)channel andFeature:(NSString *)feature andStage:(NSString *)stage andType:(BranchLinkType)type andCallback:(callbackWithUrl)callback;
- (void)getShortURLWithParams:(NSDictionary *)params andChannel:(NSString *)channel andFeature:(NSString *)feature andStage:(NSString *)stage andMatchDuration:(NSUInteger)duration andCallback:(callbackWithUrl)callback;
- (void)getShortURLWithParams:(NSDictionary *)params andChannel:(NSString *)channel andFeature:(NSString *)feature andCallback:(callbackWithUrl)callback;

- (void)getReferralCodeWithCallback:(callbackWithParams)callback;
- (void)getReferralCodeWithAmount:(NSInteger)amount andCallback:(callbackWithParams)callback;
- (void)getReferralCodeWithPrefix:(NSString *)prefix amount:(NSInteger)amount andCallback:(callbackWithParams)callback;
- (void)getReferralCodeWithAmount:(NSInteger)amount expiration:(NSDate *)expiration andCallback:(callbackWithParams)callback;
- (void)getReferralCodeWithPrefix:(NSString *)prefix amount:(NSInteger)amount expiration:(NSDate *)expiration andCallback:(callbackWithParams)callback;
- (void)getReferralCodeWithPrefix:(NSString *)prefix amount:(NSInteger)amount expiration:(NSDate *)expiration bucket:(NSString *)bucket calculationType:(BranchReferralCodeCalculation)calcType location:(BranchReferralCodeLocation)location andCallback:(callbackWithParams)callback;
- (void)validateReferralCode:(NSString *)code andCallback:(callbackWithParams)callback;
- (void)applyReferralCode:(NSString *)code andCallback:(callbackWithParams)callback;

@end
