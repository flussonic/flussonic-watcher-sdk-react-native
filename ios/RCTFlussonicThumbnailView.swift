//
//  RCTFlussonicThumbnailView.swift
//  RCTFlussonicThumbnailView
//
//  Created by MacPro9_2 on 27/11/2018.
//  Copyright © 2018 Facebook. All rights reserved.
//

import Foundation
import FlussonicSDK

@objc(RCTFlussonicThumbnailView)
class RCTFlussonicThumbnailView: RCTView, PreviewMp4ViewStatusListener {
    var previewView: PreviewMp4View?
    @objc var onStatus: RCTBubblingEventBlock?
    @objc var onClick: RCTBubblingEventBlock?
    var convertedURL: URL?
    var urlKey: String? = nil
    var oldKey: String? = nil
    var previewStatus: String?
    // need for calling RCT events
    var bridge: RCTBridge!
    open var _resizeMode: UIView.ContentMode?

    let LOG_KEY: String = "RCTFlussonicThumbnail"

    override init(frame: CGRect) {
        super.init(frame: frame)
        self.frame = frame
        previewView = PreviewMp4View()
        previewView!.frame = self.bounds
        previewView!.autoresizingMask = [.flexibleHeight, .flexibleWidth]
        previewView!.statusListener = self
        autoresizesSubviews = true
        addSubview(previewView!)
    }

    override func layoutSubviews() {
        super.layoutSubviews()
        self.previewView!.frame = self.bounds
        self.previewView!.contentMode = self.contentMode
    }

    required init?(coder aDecoder: NSCoder) {
      fatalError("init(coder:) has not been implemented")
    }

    deinit {
        releaseResources()
    }
    
//    override func willMove(toSuperview newSuperview: UIView?) {
//        super.willMove(toSuperview: newSuperview)
//        if newSuperview == nil {
//            releaseResources()
//        }
//    }

    func releaseResources() {
        if self.previewView != nil {
            print(">>>>>>>>>>> \(LOG_KEY):\(self.previewView) release resources url: \(String(describing: self.convertedURL?.path))<<<<<<<<<<<<<<<")
            if self.urlKey != nil {
                self.previewView!.cleanCache(for: self.urlKey!)
                self.previewView!.reset()
            }
//            self.previewView!.removeFromSuperview()
//            self.previewView!.statusListener = nil
//            self.previewView = nil
        }
    }

    public func onStatusChanged(_ status: Int8, _ code: String, _ message: String) {
        let previewStatus = PreviewMp4Status[Int.init(status)]
        let event = ["status": previewStatus as Any, "code": code, "message": message]
//        print("\(LOG_KEY): status \(previewStatus), url: \(self.convertedURL?.path)")
        self.previewStatus = previewStatus
        if self.onStatus != nil {
            self.onStatus?(event)
        }
        if self.oldKey != nil {
            self.previewView?.cleanCache(for: self.oldKey!)
        }
    }
    
    // props
    @objc
    var cacheKey: NSString? {
        set(val) {
            let newKey = val as String?
            if self.urlKey != nil && newKey != self.urlKey {
                self.oldKey = self.urlKey
            }
            if convertedURL != nil {
                self.previewView?.configure(withUrl: self.convertedURL!, cacheKey: newKey)
            }
            self.urlKey = newKey
        }
        get {
            return nil
        }
    }

    // props
    @objc
    var resizeMode: NSString? {
        set(val) {
            let resMode: UIView.ContentMode = ResizeModeMapper(resizeMode: val!)
            if val != nil && self._resizeMode != resMode {
                self._resizeMode = resMode
                self.contentMode = resMode
                self.previewView!.contentMode = resMode
                self.previewView!.setNeedsLayout()
            }
        }
        get {
            return nil
        }
    }

    // props
    @objc
    var url: NSString? {
        set(val) {
            if val != nil {
                let urlStr : String = (val?.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed))!
                self.convertedURL = URL(string: urlStr)!
                self.previewView!.configure(withUrl: self.convertedURL!, cacheKey: self.urlKey)
            }
        }
        get {
            return nil
        }
    }

    @objc
    func componentDidMount(viewTag: NSNumber) {
//        print("\(LOG_KEY): stub for componentDidMount viewTag: \(viewTag)")
    }

    // unused
    func applyClick() {
        if self.onClick != nil {
            self.onClick!([:])
        }
    }

}
