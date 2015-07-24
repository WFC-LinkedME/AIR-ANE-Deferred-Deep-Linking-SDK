//
//  BranchHelpers.m
//  BranchIosExtension
//
//  Created by Aymeric Lamboley on 13/04/2015.
//  Copyright (c) 2015 Pawprint Labs. All rights reserved.
//

#import "BranchHelpers.h"
#import "TypeConversion.h"

@implementation BranchHelpers

- (id) initWithContext:(FREContext) context {
    
    if (self = [super init])
        ctx = context;
    
    return self;
}

- (void) initBranch:(BOOL) useTestKey {
    
    branch = useTestKey ? [Branch getTestInstance] : [Branch getInstance];
    
    [branch initSessionAndRegisterDeepLinkHandler:^(NSDictionary *params, NSError *error) {
        
        if (!error) {
            
            NSString *JSONString = [TypeConversion ConvertNSDictionaryToJSONString:params];
            
            [self dispatchEvent:@"INIT_SUCCESSED" withParams:JSONString];
            
        } else {
            
            [self dispatchEvent:@"INIT_FAILED" withParams:error.description];
        }
    }];
}

- (void) setIdentity:(NSString *) userId {
    
    [branch setIdentity:userId withCallback:^(NSDictionary *params, NSError *error) {
        
        if (!error) {
            
            [self dispatchEvent:@"SET_IDENTITY_SUCCESSED" withParams:@""];
            
        } else {
            
            [self dispatchEvent:@"SET_IDENTITY_FAILED" withParams:error.description];
        }
    }];
}

- (void) getShortURL:(NSString *) json andTags:(NSArray *) tags andChannel:(NSString *) channel andFeature:(NSString *) feature andStage:(NSString *) stage andAlias:(NSString *) alias andType:(int) type {
    
    NSData *data = [json dataUsingEncoding:NSUTF8StringEncoding];
    NSError *jsonError;
    
    NSDictionary* params = [NSJSONSerialization JSONObjectWithData:data options:kNilOptions error:&jsonError];
    
    if (jsonError)
        [self dispatchEvent:@"GET_SHORT_URL_FAILED" withParams:jsonError.description];
    
    //find a way to have the same callback for everyone...
    
    if (alias.length != 0)
        [branch getShortURLWithParams:params andTags:tags andChannel:channel andFeature:feature andStage:stage andAlias:alias andCallback:^(NSString *url, NSError *error) {
            
            if (!error)
                [self dispatchEvent:@"GET_SHORT_URL_SUCCESSED" withParams:url];
                
            else
                [self dispatchEvent:@"GET_SHORT_URL_FAILED" withParams:error.description];
        }];
    
    else if (type != -1)
        [branch getShortURLWithParams:params andTags:tags andChannel:channel andFeature:feature andStage:stage andType:type andCallback:^(NSString *url, NSError *error) {
            
            if (!error)
                [self dispatchEvent:@"GET_SHORT_URL_SUCCESSED" withParams:url];
            
            else
                [self dispatchEvent:@"GET_SHORT_URL_FAILED" withParams:error.description];
        }];
    else
        [branch getShortURLWithParams:params andTags:tags andChannel:channel andFeature:feature andStage:stage andCallback:^(NSString *url, NSError *error) {
        
            if (!error)
                [self dispatchEvent:@"GET_SHORT_URL_SUCCESSED" withParams:url];
            
            else
                [self dispatchEvent:@"GET_SHORT_URL_FAILED" withParams:error.description];
        }];
}

- (void) logout {
    
    [branch logout];
}

- (NSDictionary *) getLatestReferringParams {
    
    return [branch getLatestReferringParams];
}

- (NSDictionary *) getFirstReferringParams {
    
    return [branch getFirstReferringParams];
}

- (void) getCredits {
    
    [branch loadRewardsWithCallback:^(BOOL changed, NSError *error) {
        
        if (!error)
            [self dispatchEvent:@"GET_CREDITS_SUCCESSED" withParams:[NSString stringWithFormat: @"%ld", (long) [branch getCredits]]];
        
        else
            [self dispatchEvent:@"GET_CREDITS_FAILED" withParams:error.description];
    }];
}

- (void) redeemRewards:(NSInteger) credits {
    
    [branch redeemRewards:credits];
}

- (void) dispatchEvent:(NSString *) event withParams:(NSString * ) params {
    
    const uint8_t* par = (const uint8_t*) [params UTF8String];
    const uint8_t* evt = (const uint8_t*) [event UTF8String];
    
    FREDispatchStatusEventAsync(ctx, evt, par);
}

@end
