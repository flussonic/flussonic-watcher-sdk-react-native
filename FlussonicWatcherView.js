import React, { Component } from 'react';
import {
    requireNativeComponent,
    findNodeHandle,
    ViewPropTypes,
    UIManager,
    Platform,
    NativeModules
} from 'react-native';
import PropTypes from 'prop-types';

const FlussonicManager = Platform.OS === 'ios' ? NativeModules.FlussonicWatcherViewManager : (NativeModules.RCTFlussonicWatcherView || NativeModules.RNFlussonicWatcherReactSdkModule);
class FlussonicWatcherView extends Component {
  static propTypes = {
        url: PropTypes.string,
        allowDownload: PropTypes.bool,
        startPosition: PropTypes.number,
        toolbarHeight: PropTypes.number,
        onBufferingStart: PropTypes.func,
        onBufferingStop: PropTypes.func,
        onDownloadRequest: PropTypes.func,
        onUpdateProgress: PropTypes.func,
        onCollapseToolbar: PropTypes.func,
        onExpandToolbar: PropTypes.func,
        onPlayerError: PropTypes.func,
        ...ViewPropTypes, // include the default view properties
    }

    constructor(props) {
        super(props);
    }

    isUnmounted = false;

    setRef = (ref) => {
        this.nativeRef = ref;
        this.nativeHandle = findNodeHandle(ref);
    }

    componentDidMount() {
        FlussonicManager.componentDidMount(this.nativeHandle);
    }

    componentWillUnmount() {
        this.isUnmounted = true;
    }

    pause() {
        FlussonicManager.pause(this.nativeHandle);
    }

    resume() {
        FlussonicManager.resume(this.nativeHandle);
    }

    seek(seconds) {
        FlussonicManager.seek(this.nativeHandle, seconds);
    }

    captureScreenshot(fileName, picturesSubdirectoryName) {
        return FlussonicManager.captureScreenshot(this.nativeHandle, fileName, picturesSubdirectoryName);
    }

    getAvailableTracks() {
        return FlussonicManager.getAvailableTracks(this.nativeHandle);
    }

    getCurrentTrack() {
        return FlussonicManager.getCurrentTrack(this.nativeHandle);
    }

    _onDownloadRequest = (event) => {
        if (this.isUnmounted) return;
        if (this.props.onDownloadRequest) {
            this.props.onDownloadRequest(event.nativeEvent.from, event.nativeEvent.to)
        }
    }

    _onUpdateProgress = (event) => {
        if (this.isUnmounted) return;
        Object.assign(this, event.nativeEvent);
        if (this.props.onUpdateProgress) {
            this.props.onUpdateProgress(
                event.nativeEvent.currentUtcInSeconds,
                event.nativeEvent.playbackStatus,
                event.nativeEvent.speed)
        }
    }

    _onCollapseToolbar = (event) => {
        if (this.isUnmounted) return;
        if (this.props.onCollapseToolbar) {
            this.props.onCollapseToolbar(
                event.nativeEvent.animationDuration
            )
        }
    }

    _onExpandToolbar = (event) => {
        if (this.isUnmounted) return;
        if (this.props.onExpandToolbar) {
            this.props.onExpandToolbar(
                event.nativeEvent.animationDuration
            )
        }
    }

    _onPlayerError = (event) => {
        if (this.isUnmounted) return;
         if (this.props.onPlayerError) {
             this.props.onPlayerError(
                event.nativeEvent.code,
                event.nativeEvent.message,
                event.nativeEvent.url
        )
      }
    }

    render() {
        return (<RCTFlussonicWatcherView
            ref={this.setRef}
            {...this.props}
            onDownloadRequest={this._onDownloadRequest}
            onUpdateProgress={this._onUpdateProgress}
            onPlayerError={this._onPlayerError}
            onCollapseToolbar={this._onCollapseToolbar}
            onExpandToolbar={this._onExpandToolbar}
        />);
    }
}

const RCTFlussonicWatcherView = requireNativeComponent(`RCTFlussonicWatcherView`, FlussonicWatcherView,
  {
  nativeOnly: {
    onDownloadRequest: true,
    onUpdateProgress: true,
    onCollapseToolbar: true,
    onExpandToolbar: true,
    onPlayerError: true,
  },
}
);

export default FlussonicWatcherView;
