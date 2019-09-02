## Android quickstart

### Initialize a new react-native application
```
react-native init watcher_example_app --version 0.59.10
cd watcher_example_app
```


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

### Use components FlussonicWatcherView and FlussonicThumbnailView in your application
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
