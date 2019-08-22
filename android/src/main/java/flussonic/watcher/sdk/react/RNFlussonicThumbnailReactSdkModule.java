package flussonic.watcher.sdk.react;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.uimanager.NativeViewHierarchyManager;
import com.facebook.react.uimanager.UIManagerModule;

import flussonic.watcher.sdk.presentation.thumbnail.FlussonicThumbnailView;
import timber.log.Timber;

@SuppressWarnings("unused")
@ReactModule(name = RNFlussonicThumbnailReactSdkModule.MODULE_NAME)
public class RNFlussonicThumbnailReactSdkModule extends ReactContextBaseJavaModule {

    static final String MODULE_NAME = "RNFlussonicThumbnailReactSdkModule";

    @SuppressWarnings("WeakerAccess")
    public RNFlussonicThumbnailReactSdkModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return MODULE_NAME;
    }

    @ReactMethod
    public void componentDidMount(final int viewTag) {
        final ReactApplicationContext context = getReactApplicationContext();
        UIManagerModule uiManager = context.getNativeModule(UIManagerModule.class);
        uiManager.addUIBlock((NativeViewHierarchyManager nativeViewHierarchyManager) -> {
            try {
                FlussonicThumbnailView flussonicThumbnailView = (FlussonicThumbnailView) nativeViewHierarchyManager.resolveView(viewTag);
                RNFlussonicThumbnailViewManager.setupNativeEvents(context, flussonicThumbnailView);
            } catch (Exception e) {
                Timber.e(e, "failed to setup native events");
            }
        });
    }
}
