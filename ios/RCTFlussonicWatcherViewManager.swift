//
//  RCTFlussonicWatcherViewManager.swift
//  RCTFlussonicWatcherView
//
//  Created by MacPro9_2 on 28/11/2018.
//  Copyright © 2018 Facebook. All rights reserved.
//

import Foundation

@objc(RCTFlussonicWatcherViewManager)
class RCTFlussonicWatcherViewManager: RCTViewManager {
    // needed for hide RN Main queue setup warning
    override static func requiresMainQueueSetup() -> Bool {
        return true
    }

    @objc override func view() -> UIView! {
        let flussonicVideoView = RCTFlussonicWatcherView()
        flussonicVideoView.bridge = self.bridge
        return flussonicVideoView
    }

    @objc
    func componentDidMount(_ viewTag: NSNumber) -> Void {
        DispatchQueue.main.async {
            let watcherView = self.bridge.uiManager.view(
                forReactTag: viewTag
                ) as! RCTFlussonicWatcherView
            watcherView.componentDidMount(viewTag: viewTag)
        }
    }

    @objc
    func pause(_ viewTag: NSNumber) -> Void {
        DispatchQueue.main.async {
            let watcherView = self.bridge.uiManager.view(
                forReactTag: viewTag
                ) as! RCTFlussonicWatcherView
            watcherView.pause()
        }
    }

    @objc
    func resume(_ viewTag: NSNumber) -> Void {
        DispatchQueue.main.async {
            let watcherView = self.bridge.uiManager.view(
                forReactTag: viewTag
                ) as! RCTFlussonicWatcherView
            watcherView.resume()
        }
    }

    @objc
    func seek(_ viewTag: NSNumber, to seconds: NSNumber) -> Void {
        DispatchQueue.main.async {
            let watcherView = self.bridge.uiManager.view(
                forReactTag: viewTag
                ) as! RCTFlussonicWatcherView
            watcherView.seek(seconds)
        }
    }

    @objc
    func captureScreenshot(
      _ viewTag: NSNumber,
      withFilename fileName: NSString,
      withDirName picturesSubdirectoryName: NSString,
      resolver resolve: @escaping RCTPromiseResolveBlock,
      rejecter reject: @escaping RCTPromiseRejectBlock) -> Void {
        DispatchQueue.main.async {
            let watcherView = self.bridge.uiManager.view(
                forReactTag: viewTag
                ) as! RCTFlussonicWatcherView
            watcherView.captureScreenshot(
                fileName,
                withDirName: picturesSubdirectoryName,
                resolver: resolve,
                rejecter: reject)
        }
    }

    @objc
    func getAvailableTracks(
      _ viewTag: NSNumber,
      resolver resolve: @escaping RCTPromiseResolveBlock,
      rejecter reject: @escaping RCTPromiseRejectBlock) -> Void {
        DispatchQueue.main.async {
            let watcherView = self.bridge.uiManager.view(
                forReactTag: viewTag
                ) as! RCTFlussonicWatcherView
            watcherView.getAvailableTracks(resolve,
                                           rejecter: reject)
        }
    }

    @objc
    func getCurrentTrack(
      _ viewTag: NSNumber,
      resolver resolve: @escaping RCTPromiseResolveBlock,
      rejecter reject: @escaping RCTPromiseRejectBlock) -> Void {
        DispatchQueue.main.async {
            let watcherView = self.bridge.uiManager.view(
                forReactTag: viewTag
                ) as! RCTFlussonicWatcherView
            watcherView.getCurrentTrack(resolve,
                                        rejecter:reject)
        }
    }

}
