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
Change `./android/build.gradle` file:

```groovy
repositories {
    maven { url 'https://raw.githubusercontent.com/flussonic/flussonic-watcher-sdk-android/master/' }
}
```
Change `./android/app/build.gradle` file:

```groovy
// ...

defaultConfig {
  // ...
  minSdkVersion 19
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

#### Install Git LFS  
```
  brew install git-lfs && git lfs install
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

* Add to your Podfile
  - At the top of file
  ```ruby
  platform :ios, '9.3'
  use_frameworks! # needed for support embed swift frameworks
  ```
  - You need to specify React dependency in Podfile at the top of your project's target.
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

  - Add Flussonic Watcher SDK dependency to your project target
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
  
####  Ensure that there were no warnings in `pod install` process
  Modify `ios/<YourProjectName>.xcworkspace` using XCode:
  
  - Select you project target > Build Phases > Link Binary With Libraries
  - Remove react-native libraries (they should be specified in Podfile) except JavaScriptCore.framework and except Pods_<YourProjectName>.framework
  

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
