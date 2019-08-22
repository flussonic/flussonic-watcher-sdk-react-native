//
//  RCTFlussonicThumbnailViewManager.swift
//  RCTFlussonicThumbnailView
//
//  Created by MacPro9_2 on 21/01/2019.
//  Copyright © 2019 Facebook. All rights reserved.
//

import Foundation

@objc(RCTFlussonicThumbnailViewManager)
class RCTFlussonicThumbnailViewManager: RCTViewManager {
    // needed for hide RN Main queue setup warning
    override static func requiresMainQueueSetup() -> Bool {
        return true
    }
    
    @objc override func view() -> UIView! {
        let thumbView = RCTFlussonicThumbnailView()
        thumbView.bridge = self.bridge
        return thumbView
    }
    
    @objc
    func componentDidMount(_ viewTag: NSNumber) -> Void {
        DispatchQueue.main.async {
            let thumbnailView = self.bridge.uiManager.view(
                forReactTag: viewTag
                ) as! RCTFlussonicThumbnailView
            thumbnailView.componentDidMount(viewTag: viewTag)
        }
    }
}
