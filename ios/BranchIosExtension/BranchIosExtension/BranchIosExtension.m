//
//  BranchIosExtension.m
//  BranchIosExtension
//
//  Created by Aymeric Lamboley on 08/04/2015.
//  Copyright (c) 2015 Pawprint Labs. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <objc/runtime.h>

#import "FlashRuntimeExtensions.h"
#import "Branch-SDK/Branch.h"

#import "TypeConversion.h"
#import "BranchHelpers.h"

#define DEFINE_ANE_FUNCTION(fn) FREObject (fn)(FREContext context, void* functionData, uint32_t argc, FREObject argv[])
#define MAP_FUNCTION(fn, data) { (const uint8_t*)(#fn), (data), &(fn) }


TypeConversion* typeConverter;
BranchHelpers* branchHelpers;

DEFINE_ANE_FUNCTION(init) {
    
    [branchHelpers initBranch];
    
    return NULL;
}

DEFINE_ANE_FUNCTION(setIdentity) {
    
    NSString* userId;
    [typeConverter FREGetObject:argv[0] asString:&userId];
    
    [branchHelpers setIdentity:userId];
    
    return NULL;
}

DEFINE_ANE_FUNCTION(getShortUrl) {
    
    NSMutableSet* tags;
    [typeConverter FREGetObject:argv[0] asSetOfStrings:&tags];
    
    NSString* channel;
    [typeConverter FREGetObject:argv[1] asString:&channel];
    
    NSString* feature;
    [typeConverter FREGetObject:argv[2] asString:&feature];
    
    NSString* stage;
    [typeConverter FREGetObject:argv[3] asString:&stage];
    
    NSString* json;
    [typeConverter FREGetObject:argv[4] asString:&json];
    
    [branchHelpers getShortURL:json andTags:[tags allObjects] andChannel:channel andFeature:feature andStage:stage];
    
    return NULL;
}

DEFINE_ANE_FUNCTION(logout) {
    
    [branchHelpers logout];
    
    return NULL;
}


bool applicationDidFinishLaunchingWithOptions(id self, SEL _cmd, UIApplication* application, NSDictionary* launchOptions) {
    //NSLog(@"applicationDidFinishLaunchingWithOptions");
    
    Branch *branch = [Branch getInstance];
    [branch initSessionWithLaunchOptions:launchOptions andRegisterDeepLinkHandler:^(NSDictionary *params, NSError *error) {
        
        if (!error) {
            
            [branchHelpers dispatchEvent:@"INIT_SUCCESSED" withParams:params.description];
            
        } else {
            
            [branchHelpers dispatchEvent:@"INIT_FAILED" withParams:error.description];
        }
    }];
    
    return YES;
}

bool applicationOpenURLSourceApplication(id self, SEL _cmd, UIApplication* application, NSURL* url, NSString* sourceApplication, id annotation) {
    //NSLog(@"applicationOpenURLSourceApplication");
    
    // if handleDeepLink returns YES, and you registered a callback in initSessionAndRegisterDeepLinkHandler, the callback will be called with the data associated with the deep link
    if (![[Branch getInstance] handleDeepLink:url]) {
        // do other deep link routing for the Facebook SDK, Pinterest SDK, etc
    }
    
    return YES;
}

void BranchContextInitializer(void* extData, const uint8_t* ctxType, FREContext ctx, uint32_t* numFunctionsToSet, const FRENamedFunction** functionsToSet) {
    
    id delegate = [[UIApplication sharedApplication] delegate];
    
    Class objectClass = object_getClass(delegate);
    
    NSString *newClassName = [NSString stringWithFormat:@"Custom_%@", NSStringFromClass(objectClass)];
    Class  modDelegate = NSClassFromString(newClassName);
    
    if (modDelegate == nil) {
        
        modDelegate = objc_allocateClassPair(objectClass, [newClassName UTF8String], 0);
        
        SEL selectorToOverride1 = @selector(application:openURL:sourceApplication:annotation:);
        SEL selectorToOverride2 = @selector(application:didFinishLaunchingWithOptions:);
        
        Method m1 = class_getInstanceMethod(objectClass, selectorToOverride1);
        Method m2 = class_getInstanceMethod(objectClass, selectorToOverride2);
        
        class_addMethod(modDelegate, selectorToOverride1, (IMP)applicationOpenURLSourceApplication, method_getTypeEncoding(m1));
        class_addMethod(modDelegate, selectorToOverride1, (IMP)applicationDidFinishLaunchingWithOptions, method_getTypeEncoding(m2));
        
        objc_registerClassPair(modDelegate);
    }
    
    object_setClass(delegate, modDelegate);
    
    static FRENamedFunction functionMap[] = {
        MAP_FUNCTION(init, NULL),
        MAP_FUNCTION(setIdentity, NULL),
        MAP_FUNCTION(getShortUrl, NULL),
        MAP_FUNCTION(logout, NULL)
    };
    
    *numFunctionsToSet = sizeof( functionMap ) / sizeof( FRENamedFunction );
    *functionsToSet = functionMap;
    
    typeConverter = [[TypeConversion alloc] init];
    branchHelpers = [[BranchHelpers alloc] initWithContext:ctx];
}

void BranchContextFinalizer(FREContext ctx) {
    return;
}

void BranchExtensionInitializer( void** extDataToSet, FREContextInitializer* ctxInitializerToSet, FREContextFinalizer* ctxFinalizerToSet ) {
    
    extDataToSet = NULL;
    *ctxInitializerToSet = &BranchContextInitializer;
    *ctxFinalizerToSet = &BranchContextFinalizer;
}

void BranchExtensionFinalizer() {
    return;
}