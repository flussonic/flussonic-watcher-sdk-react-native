//
//  RCTFlussonicWatcherViewBridge.m
//  RCTFlussonicWatcherView
//
//  Created by MacPro9_2 on 28/11/2018.
//  Copyright © 2018 Facebook. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <React/RCTBridgeModule.h>
#import <React/RCTViewManager.h>

@interface RCT_EXTERN_MODULE(RCTFlussonicWatcherViewManager, RCTViewManager)

RCT_EXPORT_VIEW_PROPERTY(url, NSString)
RCT_EXPORT_VIEW_PROPERTY(startPosition, NSNumber)
RCT_EXPORT_VIEW_PROPERTY(toolbarHeight, NSNumber)
RCT_EXPORT_VIEW_PROPERTY(allowDownload, BOOL)

RCT_EXTERN_METHOD(componentDidMount:(nonnull NSNumber *)viewTag)
RCT_EXTERN_METHOD(pause:(nonnull NSNumber *)viewTag)
RCT_EXTERN_METHOD(resume:(nonnull NSNumber *)viewTag)
RCT_EXTERN_METHOD(seek:(nonnull NSNumber *)viewTag
                  to:(nonnull NSNumber *)seconds
                  )
RCT_EXTERN_METHOD(captureScreenshot:(nonnull NSNumber *)viewTag
                  withFilename:(NSString *)fileName
                  withDirName:(NSString *)picturesSubdirectoryName
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject
                  )
RCT_EXTERN_METHOD(getAvailableTracks:(nonnull NSNumber *)viewTag
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject
                  )
RCT_EXTERN_METHOD(getCurrentTrack:(nonnull NSNumber *)viewTag
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject
                  )

RCT_EXPORT_VIEW_PROPERTY(onDownloadRequest, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onPlayerError, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onCollapseToolbar, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onExpandToolbar, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onBufferingStart, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onBufferingStop, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onUpdateProgress, RCTBubblingEventBlock)

@end
