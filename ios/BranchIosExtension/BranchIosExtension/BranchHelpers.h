//
//  BranchHelpers.h
//  BranchIosExtension
//
//  Created by Aymeric Lamboley on 13/04/2015.
//  Copyright (c) 2015 Pawprint Labs. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "FlashRuntimeExtensions.h"
#import "Branch-SDK/Branch.h"

@interface BranchHelpers : NSObject {
    
    FREContext ctx;
}

- (id) initWithContext:(FREContext) context;

- (void) initBranch;
- (void) setIdentity:(NSString *) userId;
- (void) getShortURL:(NSString *) json andTags:(NSArray *) tags andChannel:(NSString *) channel andFeature:(NSString *) feature andStage:(NSString *) stage;
- (void) logout;

- (void) dispatchEvent:(NSString *) event withParams:(NSString * ) params;

@end
