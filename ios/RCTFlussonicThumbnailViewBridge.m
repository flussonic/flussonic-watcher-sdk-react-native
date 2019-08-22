//
//  RCTFlussonicThumbnailViewBridge.m
//  RCTFlussonicThumbnailView
//
//  Created by MacPro9_2 on 21/01/2019.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <React/RCTBridgeModule.h>
#import <React/RCTViewManager.h>

@interface RCT_EXTERN_MODULE(RCTFlussonicThumbnailViewManager, RCTViewManager)

RCT_EXPORT_VIEW_PROPERTY(url, NSString)
RCT_EXPORT_VIEW_PROPERTY(cacheKey, NSString)
RCT_EXPORT_VIEW_PROPERTY(resizeMode, NSString)

RCT_EXPORT_VIEW_PROPERTY(onStatus, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onClick, RCTBubblingEventBlock)

RCT_EXTERN_METHOD(componentDidMount:(nonnull NSNumber *)viewTag)

@end
