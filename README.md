# Flussonic Watcher SDK Quickstart


## Initialize a new react-native application
```
  
react-native init watcher_example_app --version 0.61.4
cd watcher_example_app
```

## Add Flussonic Watcher SDK dependency
```	
yarn add https://github.com/flussonic/flussonic-watcher-sdk-react-native	
```	

### Update application's dependencies	
```	
react-native link react-native-flussonic-watcher-react-sdk	
```	

## Android

### Add native dependency:	
Change `./android/build.gradle` file:

```groovy
repositories {
    maven { url 'https://flussonic-watcher-mobile-sdk.s3.eu-central-1.amazonaws.com/android/watcher-sdk/release' }
}
```
Change `./android/app/build.gradle` file:

```groovy
// ...

defaultConfig {
  // ...
  minSdkVersion 16
}
// ...

compileOptions {
    sourceCompatibility JavaVersion.VERSION_1_8
    targetCompatibility JavaVersion.VERSION_1_8
}
```

You need to change your MainActivity to ReactFragmentActivity (`./android/app/src/main/java/your/bundle/MainActivity.java`)
```
import com.facebook.react.ReactFragmentActivity;

public class MainActivity extends ReactFragmentActivity {
```

## IOS

### Before Linking

#### Create Swift Brigging-Header file (if not exist)
Using [react-native-swift](https://github.com/rhdeck/react-native-swift) library:
  ```
  yarn global add react-native-swift-cli
  yarn add react-native-swift
  react-native link
  ```

Or create it manually with Xcode. Add a new Swift file to your Xcode project. You should get an alert box asking if you would like to create a bridging header. Confirm it

### Linking with Cocoapods

Add to your Podfile at the top of file
  ```ruby
  platform :ios, '9.3'
  use_frameworks!

  dynamic_frameworks = ['DynamicMobileVLCKit', 'flussonic-watcher-sdk-ios', 'Alamofire', 'Async', 'Moya', 'Result', 'RxCocoa', 'RxSwift', 'SwiftyXMLParser', 'TrueTime']

  # make all the other dependencies into static libraries by defining pod.build_type as static_library
  pre_install do |installer|
      installer.pod_targets.each do |pod|
          if !dynamic_frameworks.include?(pod.name)
              puts "Overriding the static_library build type for #{pod.name}"
              def pod.build_type;
                Pod::Target::BuildType.static_library
              end
          end
      end
  end
  ```

Add react-native dependency at the top of your project's target
  ```ruby
  require_relative '../node_modules/@react-native-community/cli-platform-ios/native_modules'

  # react-native
  # All these libraries below have been removed from the Xcode project file and now live in the Podfile.
  # Cocoapods handles the linking now. Here you can add more libraries with native modules.
  pod 'FBLazyVector', :path => "../node_modules/react-native/Libraries/FBLazyVector"
  pod 'FBReactNativeSpec', :path => "../node_modules/react-native/Libraries/FBReactNativeSpec"
  pod 'RCTRequired', :path => "../node_modules/react-native/Libraries/RCTRequired"
  pod 'RCTTypeSafety', :path => "../node_modules/react-native/Libraries/TypeSafety"
  pod 'React', :path => '../node_modules/react-native/'
  pod 'React-Core', :path => '../node_modules/react-native/'
  pod 'React-CoreModules', :path => '../node_modules/react-native/React/CoreModules'
  pod 'React-Core/DevSupport', :path => '../node_modules/react-native/'
  pod 'React-RCTActionSheet', :path => '../node_modules/react-native/Libraries/ActionSheetIOS'
  pod 'React-RCTAnimation', :path => '../node_modules/react-native/Libraries/NativeAnimation'
  pod 'React-RCTBlob', :path => '../node_modules/react-native/Libraries/Blob'
  pod 'React-RCTImage', :path => '../node_modules/react-native/Libraries/Image'
  pod 'React-RCTLinking', :path => '../node_modules/react-native/Libraries/LinkingIOS'
  pod 'React-RCTNetwork', :path => '../node_modules/react-native/Libraries/Network'
  pod 'React-RCTSettings', :path => '../node_modules/react-native/Libraries/Settings'
  pod 'React-RCTText', :path => '../node_modules/react-native/Libraries/Text'
  pod 'React-RCTVibration', :path => '../node_modules/react-native/Libraries/Vibration'
  pod 'React-Core/RCTWebSocket', :path => '../node_modules/react-native/'
  pod 'React-cxxreact', :path => '../node_modules/react-native/ReactCommon/cxxreact'
  pod 'React-jsi', :path => '../node_modules/react-native/ReactCommon/jsi'
  pod 'React-jsiexecutor', :path => '../node_modules/react-native/ReactCommon/jsiexecutor'
  pod 'React-jsinspector', :path => '../node_modules/react-native/ReactCommon/jsinspector'
  pod 'ReactCommon/jscallinvoker', :path => "../node_modules/react-native/ReactCommon"
  pod 'ReactCommon/turbomodule/core', :path => "../node_modules/react-native/ReactCommon"
  pod 'Yoga', :path => '../node_modules/react-native/ReactCommon/yoga'
  pod 'DoubleConversion', :podspec => '../node_modules/react-native/third-party-podspecs/DoubleConversion.podspec'
  pod 'glog', :podspec => '../node_modules/react-native/third-party-podspecs/glog.podspec'
  pod 'Folly', :podspec => '../node_modules/react-native/third-party-podspecs/Folly.podspec'
# react-native
  ```

Add Flussonic Watcher SDK dependency
  ```ruby
  pod 'react-native-flussonic-watcher-react-sdk', :path => '../node_modules/react-native-flussonic-watcher-react-sdk'
  # Pods for react-native-flussonic-watcher-react-sdk
  pod 'DynamicMobileVLCKit', :http => 'https://flussonic-watcher-mobile-sdk.s3.eu-central-1.amazonaws.com/ios/DynamicMobileVLCKit/release/3.3.0/DynamicMobileVLCKit.zip'
  pod 'flussonic-watcher-sdk-ios', :http => 'https://flussonic-watcher-mobile-sdk.s3.eu-central-1.amazonaws.com/ios/watcher-sdk/release/2.0.0/FlussonicSDK.zip'
  ```

Add this hook for RxSwift at the bottom of your Podfile 
  ```ruby
  post_install do |installer|
    installer.pods_project.targets.each do |target|
      if target.name == "RxSwift"
        target.build_configurations.each do |config|
          if config.name == "Debug"
            config.build_settings['SWIFT_ACTIVE_COMPILATION_CONDITIONS'] = []
          end
        end
      end
    end
  end
  ```

Install pods
  ```ruby
  pod cache clean --all
  pod install
  ```  

Ensure there were no warnings in `pod install` process

Check `ios/<YourProjectName>.xcworkspace` using XCode:
- Select `Project Target` > `Build Phases` > `Link Binary With Libraries`
- You should see `Pods_<YourProjectName>.framework`
- Select `Project Target` > `Build Phases` > `[CP] Embed Pods Frameworks`
- You should see a lot of embed frameworks and `${PODS_ROOT}/flussonic-watcher-sdk-ios/FlussonicSDK.framework`
  
  

## Usage:

```js
import { FlussonicThumbnailView, FlussonicWatcherView } from 'react-native-flussonic-watcher-react-sdk';

const styles = StyleSheet.create({
  thumbnail: {
    flexGrow: 0,
    zIndex: 0,
    margin: 10,
    height: 150,
    width: 150 * 16 / 9,
  },
  info: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
})
// ...
const watcherAddress = ''; 

// see https://flussonic.github.io/watcher-docs/api.html#post--vsaas-api-v2-auth-login	    // ...
const userSession = '';	 
// see https://flussonic.github.io/watcher-docs/api.html#get--vsaas-api-v2-cameras	
const streamName = '';	
  
const streamUrl = 'http://' + userSession + '@' + watcherAddress + '/' + streamName;	

// mp4 preview
const renderThumbnail = () => (
  <TouchableOpacity onPress={this.onMP4PreviewPress}
    style={styles.thumbnail}>
    <FlussonicThumbnailView
      style={styles.info}
      onStatus={this.onStatus}
      onClick={this.onMP4PreviewPress}
      resizeMode="stretch"
      url={streamUrl}
    />
  </TouchableOpacity>
);

// video player
const renderPlayer = () => (
  <FlussonicWatcherView
    ref={this.setWatcherRef}
    style={isFullscreen ? styles.watcherFullscreen : styles.watcher}
    onBufferingStart={this.onBufferingStart.bind(this)}
    onBufferingStop={this.onBufferingStop.bind(this)}
    onDownloadRequest={this.onDownloadRequest.bind(this)}
    onUpdateProgress={this.onUpdateProgress.bind(this)}
    onCollapseToolbar={this.collapseToolbar.bind(this)}
    onExpandToolbar={this.expandToolbar.bind(this)}
    toolbarHeight={56}
    url={streamUrl}
    allowDownload={this.state.allowDownload}
    startPosition={startPosition}
  />
);
```

## License

 - See [LICENSE](/LICENSE)
