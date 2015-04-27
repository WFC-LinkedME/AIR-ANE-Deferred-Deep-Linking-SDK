//
//  BNCLinkData.h
//  Branch-SDK
//
//  Created by Qinwei Gong on 1/22/15.
//  Copyright (c) 2015 Branch Metrics. All rights reserved.
//

#import "Branch.h"

@interface BNCLinkData : NSObject <NSCopying>

@property (nonatomic, strong) NSMutableDictionary *data;
@property (readonly, copy) NSArray *allKeys;

@property (nonatomic, strong) NSArray *tags;
@property (nonatomic, strong) NSString *alias;
@property (nonatomic, assign) BranchLinkType type;
@property (nonatomic, strong) NSString *channel;
@property (nonatomic, strong) NSString *feature;
@property (nonatomic, strong) NSString *stage;
@property (nonatomic, strong) NSString *params;
@property (nonatomic, assign) NSUInteger duration;
@property (nonatomic, strong) NSString *ignoreUAString;

- (void)setupTags:(NSArray *)tags;
- (void)setupAlias:(NSString *)alias;
- (void)setupType:(BranchLinkType)type;
- (void)setupChannel:(NSString *)channel;
- (void)setupFeature:(NSString *)feature;
- (void)setupStage:(NSString *)stage;
- (void)setupParams:(NSString *)params;
- (void)setupMatchDuration:(NSUInteger)duration;
- (void)setupIgnoreUAString:(NSString *)ignoreUAString;

- (void)setObject:(id)anObject forKey:(id <NSCopying>)aKey;
- (void)setObject:(id)obj forKeyedSubscript:(id <NSCopying>)key NS_AVAILABLE(10_8, 6_0);
- (id)objectForKey:(id)aKey;

@end
