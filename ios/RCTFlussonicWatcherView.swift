//
//  RCTFlussonicWatcherView.swift
//  RCTFlussonicWatcherView
//
//  Created by MacPro9_2 on 27/11/2018.
//  Copyright © 2018 Facebook. All rights reserved.
//

import Foundation
import UIKit
import FlussonicSDK

@objc(RCTFlussonicWatcherView)
class RCTFlussonicWatcherView: RCTView {
    @objc var onDownloadRequest: RCTBubblingEventBlock?
    @objc var onUpdateProgress: RCTBubblingEventBlock?
    @objc var onCollapseToolbar: RCTBubblingEventBlock?
    @objc var onExpandToolbar: RCTBubblingEventBlock?
    @objc var onBufferingStart: RCTBubblingEventBlock?
    @objc var onBufferingStop: RCTBubblingEventBlock?
    @objc var onPlayerError: RCTBubblingEventBlock?

    lazy var videoViewListener: RCTFlussonicWatcherViewListener! = nil
    lazy var flussonicView: FlussonicWatcherView! = nil
    private let vlcAdapter = FlussonicVlcAdapter()

    var bridge: RCTBridge!

    private var notification: NSObjectProtocol?
    private var paused: Bool = true
    private var myCompletionTarget:MyRCTCompletionTarget?
    var currentUrlStr: String?

    let LOG_KEY: String = "RCTFlussonicWatcher"

    override init(frame: CGRect) {
        super.init(frame: frame)
        flussonicView = FlussonicWatcherView()
        flussonicView?.frame = self.bounds
        flussonicView?.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        autoresizesSubviews = true
        setListeners()
        flussonicView?.setNetworkQualityThresholdCount(count: 3)
        flussonicView?.setShowDebugInfo(newValue: false)
        self.addSubview(self.flussonicView!)
        
        // плеер останавливает проигрыш потока в фоне, нужно снова его запускать при просыпании
        #if swift(>=4.2)
            notification = NotificationCenter.default.addObserver(forName: UIApplication.willEnterForegroundNotification, object: nil, queue: .main) { [weak self] notification in
                guard let `self` = self else { return }
                self.setListeners()
                self.resume()
            }
        #else
            notification = NotificationCenter.default.addObserver(forName: NSNotification.Name.UIApplicationWillEnterForeground, object: nil, queue: .main) { [weak self] notification in
                guard let `self` = self else { return }
                self.setListeners()
                self.resume()
            }
        #endif
    }

    deinit {
        if let notification = notification {
            NotificationCenter.default.removeObserver(notification)
        }
        print(">>>>>>>>>>> \(LOG_KEY) deinit <<<<<<<<<<<<<<<")
    }

    func setListeners() {
        if videoViewListener == nil {
            videoViewListener = RCTFlussonicWatcherViewListener(rctview: self)
        }
        flussonicView?.setBufferingListener(bufferingListener: videoViewListener)
        flussonicView?.setDownloadRequestListener(downloadRequestListener: videoViewListener)
        flussonicView?.setUpdateProgressEventListener(updateProgressEventListener: videoViewListener)
        flussonicView?.setPlayerErrorListener(playerErrorListener: videoViewListener)
        flussonicView?.delegate = videoViewListener
        flussonicView?.alertDelegate = RCTPresentedViewController()
    }
    
    func releaseResources() {
        vlcAdapter.stop()
//        flussonicView?.setBufferingListener(bufferingListener: nil)
//        flussonicView?.setDownloadRequestListener(downloadRequestListener: nil)
//        flussonicView?.setUpdateProgressEventListener(updateProgressEventListener: nil)
//        flussonicView?.delegate = nil
//        flussonicView?.alertDelegate = nil
//        flussonicView?.removeFromSuperview()
//        flussonicView = nil
        videoViewListener = nil
    }
    
    override func willMove(toSuperview newSuperview: UIView?) {
        super.willMove(toSuperview: newSuperview)
        if newSuperview == nil {
            print(">>>>>>>>>>> \(LOG_KEY) willMove to nil <<<<<<<<<<<<<<<")
            if flussonicView != nil {
                pause()
                releaseResources()
            }
        }
    }

    required init?(coder aDecoder: NSCoder) {
       fatalError("init(coder:) has not been implemented")
    }

    // props
    @objc
    var url: NSString? {
        set(val) {
            if val != nil {
                let urlStr : String = (val?.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed))!
                if currentUrlStr != urlStr {
                    let convertedURL : URL = URL(string: urlStr)!
                    print("configure with url: \(convertedURL.absoluteString)")
                    self.flussonicView?.configure(withUrl:convertedURL, playerAdapter:vlcAdapter)
                    resume()
                }
            }
        }
        get {
            return nil
        }
    }

    // props
    @objc
    var allowDownload: ObjCBool {
        set(val) {
            self.flussonicView?.allowDownload = val.boolValue
        }
        get {
            return false
        }
    }

    // props
    @objc
    var startPosition: NSNumber? {
        set(val) {
            if val != nil && val?.intValue ?? 0 > 0 {
                print("startPosition: \(String(describing: val))")
                let fromDouble = TimeInterval(truncating: val!)
                self.flussonicView?.startPositionDate = Date(timeIntervalSince1970: fromDouble)
                if vlcAdapter.mediaState == FlussonicPlayerAdapterMediaState.playing ||
                    vlcAdapter.mediaState == FlussonicPlayerAdapterMediaState.buffering {
                    self.flussonicView.seek(seconds: Date(timeIntervalSince1970: fromDouble).timeIntervalSince1970)
                }
            }
        }
        get {
            return nil
        }
    }
    // props
    @objc
    var toolbarHeight: NSNumber? {
        set(val) {
            if val != nil {
                print("toolbarHeight has not been implemented in FlussonicWatcherProtocol setToolbarHeight method")
            }
        }
        get {
            return nil
        }
    }

    @objc
    func componentDidMount(viewTag: NSNumber) {
        print("stub for componentDidMount viewTag: \(viewTag)")
        resume()
    }

    @objc
    func pause() {
        if(!paused) {
            paused = true
            self.flussonicView?.pause()
        }
    }

    @objc
    func resume() {
        paused = false
        self.flussonicView?.resume()
    }

    @objc
    func seek(_ seconds: NSNumber) {
        if seconds.intValue > 0 {
            let fromDouble = TimeInterval(truncating: seconds)
            self.flussonicView?.seek(seconds: fromDouble)
        }
    }

    @objc
    func captureScreenshot(
       _ fileName: NSString,
       withDirName picturesSubdirectoryName: NSString,
       resolver resolve: @escaping RCTPromiseResolveBlock,
       rejecter reject: @escaping RCTPromiseRejectBlock) {
        let documentsPath = NSSearchPathForDirectoriesInDomains(.documentDirectory, .userDomainMask, true)[0]
        let dataPath = URL(fileURLWithPath: documentsPath).appendingPathComponent(picturesSubdirectoryName as String).path //Set folder name
        //Check is folder available or not, if not create
        if !FileManager.default.fileExists(atPath: dataPath) {
            print("screenshot dataPath: \(dataPath)")
            do{
              try FileManager.default.createDirectory(atPath: dataPath, withIntermediateDirectories: true, attributes: nil) //Create folder if not
            }catch let error {
                reject("screenshot_error", "RCTFlussonicWatcherView: failed to save screenshot", error)
            }
        }
        // create the destination file url to save your image
        let fileURL = URL(fileURLWithPath:dataPath).appendingPathComponent(fileName as String)// image name
        myCompletionTarget = MyRCTCompletionTarget(resolve: resolve, reject: reject, convertedURL: fileURL)
        let screenshotCaptured: (UIImage) -> () = { [weak myCompletionTarget] image in
            guard let myCompletionTarget = myCompletionTarget else { return }
            UIImageWriteToSavedPhotosAlbum(image, myCompletionTarget, #selector(myCompletionTarget.image(_:didFinishSavingWithError:contextInfo:)), nil)
        }
        self.flussonicView?.screenshotCaptured = screenshotCaptured
        self.flussonicView?.captureScreenshot(destUrl: fileURL)
        print("screenshot path: \(fileURL.path)")
    }

    @objc
    func getAvailableTracks(
        _ resolve: RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock) {
        let tracks: Array<StreamItem> = self.flussonicView!.getAvailableStreams()
        let mappedTracks = tracks.map(convertToDict)
        resolve(mappedTracks)
    }

    @objc
    func getCurrentTrack(
        _ resolve: RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock) {
        let track = self.flussonicView?.getCurrentStream()
        let mappedTrack = track != nil ? convertToDict(item: track!) : [:]
        resolve(mappedTrack)
    }
}
