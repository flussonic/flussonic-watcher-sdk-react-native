//
//  MyRCTCompletionTarget.swift
//  RNFlussonicWatcherReactSdk
//
//  Created by MacPro9_2 on 23/01/2019.
//  Copyright © 2019 Facebook. All rights reserved.
//

import Foundation


@objc(MyRCTCompletionTarget)
class MyRCTCompletionTarget: NSObject {
    private var resolve: RCTPromiseResolveBlock?
    private var reject: RCTPromiseRejectBlock?
    private var convertedURL: URL?
    private var alreadyCalled = false

    init(resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock, convertedURL: URL) {
        super.init()
        self.resolve = resolve
        self.reject = reject
        self.convertedURL = convertedURL
    }
    
    @objc
    func image(_ image: UIImage, didFinishSavingWithError error: Error?, contextInfo: UnsafeRawPointer) {
        if alreadyCalled { return }
        alreadyCalled = true
        if error != nil {
            print("screenshot_error RCTFlussonicWatcherView error: ", error.debugDescription)
            reject!("screenshot_error", "RCTFlussonicWatcherView: failed to save screenshot", error)
        } else {
            resolve!(convertedURL!.absoluteString);
        }
    }
}
