**React Native Flussonic Watcher React SDK**

## Installation

  `yarn add https://github.com/flussonic/react-native-flussonic-watcher-react-sdk.git`

  After installing the npm package you need to link the native modules.

## IOS

## Before Linking

*  Run in project directory:
  ```
  brew install git-lfs && git lfs install
  ```

* You should create in your project Brigging-Header file (if not exist) because of this library has written in swift.
 - Automatic creating Bridging-Header with [react-native-swift](https://github.com/rhdeck/react-native-swift) library:

  ```
  yarn global add react-native-swift-cli
  yarn add react-native-swift
  react-native link
  ```


### Automatic Linking with Cocoapods

* Add to your Podfile
  - At the top of file
  ```ruby
  platform :ios, '9.3'
  use_frameworks! # needed for support embed swift frameworks
  ```
  - You need to specify React dependency in Podfile at the top of your project's target. For Example
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

  - To your project target
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
* Open in Xcode `ios/<YourProjectName>.xcworkspace` file
  - Select you project target > Build Phases > Link Binary With Libraries
  - Remove react-native libraries (they should be specified in Podfile) except JavaScriptCore.framework and except Pods_<YourProjectName>.framework
#### More info about Cocoapods
 - [Cocoapods oficial site](https://cocoapods.org/)

## Android

### Manual linking

Add `react-native-flussonic-watcher-react-sdk` to your `./android/settings.gradle` file as follows:

```gradle
include ':app', ':react-native-flussonic-watcher-react-sdk'
project(':react-native-flussonic-watcher-react-sdk').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-flussonic-watcher-react-sdk/android')
```

Include it as dependency in `./android/app/build.gradle` file:

```gradle
// should be at the top of file after "import com.android.build.OutputFile" line
repositories {
    maven { url 'https://raw.github.com/flussonic/flussonic-watcher-sdk-android/master/' }
}
// ...

compileOptions {
    sourceCompatibility JavaVersion.VERSION_1_8
    targetCompatibility JavaVersion.VERSION_1_8
}
// ...

dependencies {
    // ...
    implementation project(':react-native-flussonic-watcher-react-sdk')
}
```

You need to change your MainActivity to ReactFragmentActivity (`./android/app/src/main/java/your/bundle/MainActivity.java`)
```
import com.facebook.react.ReactFragmentActivity;

public class MainActivity extends ReactFragmentActivity {
```

Finally, you need to add the package to your MainApplication (`./android/app/src/main/java/your/bundle/MainApplication.java`):

```java
import flussonic.watcher.sdk.react.RNFlussonicWatcherReactSdkPackage; // <-- Add RNFlussonicWatcherReactSdkPackage to the imports

// ...

@Override
protected List<ReactPackage> getPackages() {
    return Arrays.<ReactPackage>asList(
        new MainReactPackage(),
        // ...
        new RNFlussonicWatcherReactSdkPackage(), // <-- Add it to the packages list
    );
}

// ...
```

After that, you will need to recompile your project with `react-native run-android`.

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

// mp4 preview
<TouchableOpacity onPress={this.onMP4PreviewPress}
  style={styles.thumbnail}>
  <FlussonicThumbnailView
    style={styles.info}
    onStatus={this.onStatus}
    onClick={this.onMP4PreviewPress}
    resizeMode="stretch"
    url={url}
  />
</TouchableOpacity>
// ...

// video player
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
  url={url}
  allowDownload={this.state.allowDownload}
  startPosition={startPosition}
/>
```

## License

 - See [LICENSE](/LICENSE)
