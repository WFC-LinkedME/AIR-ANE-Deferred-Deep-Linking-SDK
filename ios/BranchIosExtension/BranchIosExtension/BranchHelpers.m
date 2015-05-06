//
//  BranchHelpers.m
//  BranchIosExtension
//
//  Created by Aymeric Lamboley on 13/04/2015.
//  Copyright (c) 2015 Pawprint Labs. All rights reserved.
//

#import "BranchHelpers.h"

@implementation BranchHelpers

- (id) initWithContext:(FREContext) context {
    
    if (self = [super init])
        ctx = context;
    
    return self;
}

- (void) initBranch {
    
    Branch *branch = [Branch getInstance];
    
    [branch initSessionAndRegisterDeepLinkHandler:^(NSDictionary *params, NSError *error) {
        
        if (!error) {
            
            NSData *jsonData = [NSJSONSerialization dataWithJSONObject:params options:0 error:&error];
            
            NSString *JSONString;
            
            if (!jsonData)
                JSONString = [[NSString alloc] init];
            else {
                JSONString = [[NSString alloc] initWithBytes:[jsonData bytes] length:[jsonData length] encoding:NSUTF8StringEncoding];
                JSONString = [JSONString stringByReplacingOccurrencesOfString:@"\\/" withString:@"/"];
            }
            
            [self dispatchEvent:@"INIT_SUCCESSED" withParams:JSONString];
            
        } else {
            
            [self dispatchEvent:@"INIT_FAILED" withParams:error.description];
        }
    }];
}

- (void) setIdentity:(NSString *) userId {
    
    [[Branch getInstance] setIdentity:userId withCallback:^(NSDictionary *params, NSError *error) {
        
        if (!error) {
            
            [self dispatchEvent:@"SET_IDENTITY_SUCCESSED" withParams:@""];
            
        } else {
            
            [self dispatchEvent:@"SET_IDENTITY_FAILED" withParams:error.description];
        }
    }];
}

- (void) getShortURL:(NSString *) json andTags:(NSArray *) tags andChannel:(NSString *) channel andFeature:(NSString *) feature andStage:(NSString *) stage {
    
    NSData *data = [json dataUsingEncoding:NSUTF8StringEncoding];
    NSError *jsonError;
    
    NSDictionary* params = [NSJSONSerialization JSONObjectWithData:data options:kNilOptions error:&jsonError];
    
    if (jsonError)
        [self dispatchEvent:@"GET_SHORT_URL_FAILED" withParams:jsonError.description];
    
    [[Branch getInstance] getShortURLWithParams:params andTags:tags andChannel:channel andFeature:feature andStage:stage andCallback:^(NSString *url, NSError *error) {
        
        if (!error) {
            
            [self dispatchEvent:@"GET_SHORT_URL_SUCCESSED" withParams:url];
            
        } else {
            
            [self dispatchEvent:@"GET_SHORT_URL_FAILED" withParams:error.description];
        }
    }];
}

- (void) logout {
    
    [[Branch getInstance] logout];
}

- (NSDictionary *) getLatestReferringParams {
    
    return [[Branch getInstance] getLatestReferringParams];
}

- (NSDictionary *) getFirstReferringParams {
    
    return [[Branch getInstance] getFirstReferringParams];
}

- (void) dispatchEvent:(NSString *) event withParams:(NSString * ) params {
    
    const uint8_t* par = (const uint8_t*) [params UTF8String];
    const uint8_t* evt = (const uint8_t*) [event UTF8String];
    
    FREDispatchStatusEventAsync(ctx, evt, par);
}

@end
