//
//  Helpers.swift
//  RNFlussonicWatcherReactSdk
//
//  Created by MacPro9_2 on 22/01/2019.
//  Copyright © 2019 Facebook. All rights reserved.
//

import Foundation
import FlussonicSDK

let PreviewMp4Status = ["noUrl", "loading", "loaded", "loadedFromCache", "error"]

func ResizeModeMapper(resizeMode: NSString) -> UIView.ContentMode {
    switch resizeMode {
    case "cover":
        return UIView.ContentMode.scaleAspectFill
    case "stretch":
        return UIView.ContentMode.scaleToFill
    default:
        return UIView.ContentMode.scaleAspectFit
    }
}

func convertToDict(item:StreamItem) -> NSDictionary {
  return [
    "bitrate": item.bitrate,
    "codec": item.codec,
    "content": item.content.rawValue,
    "height": item.height as Any,
    "lang": item.lang as Any,
    "lengthSize": item.lengthSize as Any,
    "level": item.level as Any,
    "pixelHeight": item.pixelHeight as Any,
    "pixelWidth": item.pixelWidth as Any,
    "profile": item.profile as Any,
    "sarHeight": item.sarHeight as Any,
    "sarWidth": item.sarWidth as Any,
    "size": item.size as Any,
    "trackId": item.trackId,
    "width": item.width as Any
  ]
}
