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

const COMPONENT_NAME = 'RCTFlussonicThumbnailView';
const FlussonicThumbnailViewModule = Platform.OS === 'ios' ? NativeModules.FlussonicThumbnailViewManager : NativeModules.RCTFlussonicThumbnailView || NativeModules.RNFlussonicThumbnailReactSdkModule;

class FlussonicThumbnailView extends Component {
    static propTypes = {
        url: PropTypes.string,
        cacheKey: PropTypes.string,
        resizeMode: PropTypes.string,
        ...ViewPropTypes, // include the default view properties
    }

    static defaultProps = {
      resizeMode: 'contain'
    }
    constructor(props) {
        super(props);
    }

    setRef = (ref) => {
        this.nativeRef = ref;
        this.nativeHandle = findNodeHandle(ref);
    }

    componentDidMount() {
        FlussonicThumbnailViewModule.componentDidMount(this.nativeHandle);
    }

    _onStatus = (event) => {
        if (this.isUnmounted) return;
        if (this.props.onStatus) {
            this.props.onStatus(event.nativeEvent)
        }
    }

    _onClick = (event) => {
        if (this.isUnmounted) return;
        if (this.props.onClick) {
            this.props.onClick()
        }
    }

    render() {
        return (<RCTFlussonicThumbnailView
            ref={this.setRef}
            {...this.props}
            onStatus={this._onStatus}
            onClick={this._onClick}
        />);
    }
}

const RCTFlussonicThumbnailView = requireNativeComponent(COMPONENT_NAME, FlussonicThumbnailView, {
  nativeOnly: {onStatus: true, onClick: true},
})
export default FlussonicThumbnailView;
