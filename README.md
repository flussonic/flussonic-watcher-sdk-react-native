# Flussonic Watcher SDK Quickstart

## Initialize a new react-native application
```
react-native init watcher_example_app --version 0.59.10
cd watcher_example_app
```

## Android

### Add Flussonic Watcher SDK dependency
```
yarn add https://github.com/flussonic/flussonic-watcher-sdk-react-native
```

### Update application's dependencies
```
react-native link react-native-flussonic-watcher-react-sdk
```

### Add native dependency:
Change `android/build.gradle`:
```
minSdkVersion 19
repositories {
    maven { url 'https://raw.githubusercontent.com/flussonic/flussonic-watcher-sdk-android/master/' }
}
```

## IOS

### Before Linking

#### Install Git LFS
  ```
  brew install git-lfs
  git lfs install
  ```

#### Create Swift Brigging-Header file (if not exist)
Using [react-native-swift](https://github.com/rhdeck/react-native-swift) library:
  ```
  yarn global add react-native-swift-cli
  yarn add react-native-swift
  react-native link
  ```

Or create it manually Xcode. Add a new Swift file to your Xcode project. You should get an alert box asking if you would like to create a bridging header. Confirm it

### Linking with Cocoapods

At the top of your Podfile
  ```ruby
  platform :ios, '9.3'
  use_frameworks! # needed for support embed swift frameworks
  ```

Add react-native dependency at the top of your project's target
  ```ruby
  react_native = "../node_modules/react-native"
  third_party = "#{react_native}/third-party-podspecs"
  pod 'React', path: react_native, :subspecs => [
    'Core',
    'CxxBridge', # required for RN >= 0.47
    'DevSupport', # enable In-App Devmenu if RN >= 0.43
    'RCTAnimation',
    'RCTActionSheet',
    'RCTBlob',
    'RCTImage',
    'RCTLinkingIOS',
    'RCTNetwork',
    'RCTSettings',
    'RCTText',
    'RCTVibration',
    'RCTWebSocket' # required for app debugging
    # Any React-Native subspec you need
  ]
  pod 'yoga', :path => "#{react_native}/ReactCommon/yoga"
  pod 'DoubleConversion', :podspec => "#{third_party}/DoubleConversion.podspec"
  pod 'glog', :podspec => "#{third_party}/GLog.podspec"
  pod 'Folly', :podspec => "#{third_party}/Folly.podspec"
  ```

Add Flussonic Watcher SDK dependency
  ```ruby
    pod 'react-native-flussonic-watcher-react-sdk', :path => '../node_modules/react-native-flussonic-watcher-react-sdk'
    # Pod Dependency for react-native-flussonic-watcher-react-sdk
    pod 'DynamicMobileVLCKit', :http => 'https://flussonic-watcher-mobile-sdk.s3.eu-central-1.amazonaws.com/ios/DynamicMobileVLCKit/release/3.3.0/DynamicMobileVLCKit.zip'
    pod 'flussonic-watcher-sdk-ios', :http => 'https://flussonic-watcher-mobile-sdk.s3.eu-central-1.amazonaws.com/ios/watcher-sdk/release/1.5.6/FlussonicSDK.zip'
  ```

Install pods
  ```ruby
  pod cache clean --all
  pod install
  ```  

Ensure that there were no warnings in `pod install` process

Modify `ios/<YourProjectName>.xcworkspace` using XCode:
- Select `Project Target` > `Build Phases` > `Link Binary With Libraries`
- Remove react-native libraries (they are managed by Cocoapods) except `JavaScriptCore.framework` and  `Pods_<YourProjectName>.framework`


## Use components FlussonicWatcherView and FlussonicThumbnailView in your application
Change `App.js`:
```jsx
import {FlussonicWatcherView} from 'react-native-flussonic-watcher-react-sdk';


render = () => {
  const watcherAddress = '';
  
  // see https://flussonic.github.io/watcher-docs/api.html#post--vsaas-api-v2-auth-login
  const userSession = '';
  
  // see https://flussonic.github.io/watcher-docs/api.html#get--vsaas-api-v2-cameras
  const streamName = '';
  
  const streamUrl = 'http://' + userSession + '@' + watcherAddress + '/' + streamName;

  return (
    ...
    <FlussonicWatcherView
      style={{width: 400, height:400}}
      url={streamUrl} />
    ...
  );
}
```
