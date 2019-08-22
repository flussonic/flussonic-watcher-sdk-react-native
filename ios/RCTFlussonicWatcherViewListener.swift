//
//  NativeListeners.swift
//  RNFlussonicWatcherReactSdk
//
//  Created by MacPro9_2 on 29/11/2018.
//  Copyright © 2018 Facebook. All rights reserved.
//

import UIKit
import FlussonicSDK

@objc(RCTFlussonicWatcherViewListener)
class RCTFlussonicWatcherViewListener: NSObject, FlussonicBufferingListener, FlussonicDownloadRequestListener, FlussonicUpdateProgressEventListener, FlussonicPlayerErrorListener, FlussonicWatcherDelegateProtocol {

    private var myVideoView: RCTFlussonicWatcherView?
    init(rctview: RCTFlussonicWatcherView) {
        self.myVideoView = rctview
    }
    
    deinit {
        myVideoView = nil
    }

    public func onBufferingStart() {
        if self.myVideoView?.onBufferingStart != nil {
            self.myVideoView?.onBufferingStart!([:])
        }
    }

    public func onBufferingStop() {
        if self.myVideoView?.onBufferingStop != nil {
            self.myVideoView?.onBufferingStop!([:])
        }
    }

    public func onDownloadRequest(from: Int64, to: Int64) {
        if self.myVideoView?.onDownloadRequest != nil {
            let event = ["from": from, "to": to]
            self.myVideoView?.onDownloadRequest!(event)
        }
    }
    
    public func onPlayerError(code: String, message: String, url: String) {
        if self.myVideoView?.onPlayerError != nil {
            self.myVideoView?.onPlayerError!(["code": code, "message": message, "url": url])
        }
    }

    public func onUpdateProgress(event: ProgressEvent) {
//        print("onUpdateProgress: \(event.playbackStatus), \(event.playbackStatusString)")
        if self.myVideoView?.onUpdateProgress != nil {
            self.myVideoView?.onUpdateProgress!([
                "currentUtcInSeconds": event.currentUtcInSeconds,
                "playbackStatus": event.playbackStatusString,
                "speed": event.speed
            ]);
        }
    }

    public func expandToolbar() {
        if self.myVideoView?.onExpandToolbar != nil {
            let event = ["animationDuration": 150]
            self.myVideoView?.onExpandToolbar!(event)
        }
    }

    public func collapseToolbar() {
        if self.myVideoView?.onCollapseToolbar != nil {
            let event = ["animationDuration": 150]
            self.myVideoView?.onCollapseToolbar!(event)
        }
    }

    public func showToolbar() {
        if self.myVideoView?.onExpandToolbar != nil {
            let event = ["animationDuration": 0]
            self.myVideoView?.onExpandToolbar!(event)
        }
    }

    public func hideToolbar() {
        if self.myVideoView?.onCollapseToolbar != nil {
            let event = ["animationDuration": 0]
            self.myVideoView?.onCollapseToolbar!(event)
        }
    }
}
