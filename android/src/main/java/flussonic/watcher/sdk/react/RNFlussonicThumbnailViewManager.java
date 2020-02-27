package flussonic.watcher.sdk.react;

import androidx.annotation.NonNull;
import android.view.View;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.RCTEventEmitter;

import java.util.Map;

import javax.annotation.Nullable;

import flussonic.watcher.sdk.presentation.thumbnail.FlussonicThumbnailView;
import timber.log.Timber;

@SuppressWarnings("unused")
public class RNFlussonicThumbnailViewManager extends SimpleViewManager<FlussonicThumbnailView> {
    private static final String REACT_CLASS = "RCTFlussonicThumbnailView";
    private static final String BUBBLED = "bubbled";
    private static final String NATIVE_EVENT_ON_STATUS = "NATIVE_EVENT_ON_STATUS";
    private static final String NATIVE_EVENT_ON_CLICK = "NATIVE_EVENT_ON_CLICK";
    private static final String PHASED_REGISTRATION_NAMES = "phasedRegistrationNames";
    private static final String WIDTH = "width";
    private static final String HEIGHT = "height";

    static void setupNativeEvents(ReactApplicationContext reactContext, FlussonicThumbnailView view) {
        //noinspection Convert2Lambda
        view.setStatusListener(new FlussonicThumbnailView.StatusListener() {
            @Override
            public void onStatus(@NonNull FlussonicThumbnailView.Status status, String code, String message) {
                WritableMap event = Arguments.createMap();
                event.putString("status", status.toString());
                event.putString("code", code);
                event.putString("message", message);
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                        view.getId(),
                        NATIVE_EVENT_ON_STATUS,
                        event);
            }
        });
        //noinspection Convert2Lambda
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WritableMap event = Arguments.createMap();
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                        view.getId(),
                        NATIVE_EVENT_ON_CLICK,
                        event);
            }
        });
    }

    @Nullable
    @Override
    public Map<String, Object> getExportedCustomBubblingEventTypeConstants() {
        return MapBuilder.<String, Object>builder()
                .put(
                        NATIVE_EVENT_ON_STATUS,
                        MapBuilder.of(
                                PHASED_REGISTRATION_NAMES,
                                MapBuilder.of(BUBBLED, "onStatus")))
                .put(
                        NATIVE_EVENT_ON_CLICK,
                        MapBuilder.of(
                                PHASED_REGISTRATION_NAMES,
                                MapBuilder.of(BUBBLED, "onClick")))
                .build();
    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @ReactProp(name = "url")
    public void setUrl(final FlussonicThumbnailView view, String url) {
        if (url != null) {
            UiThreadUtil.runOnUiThread(() -> view.setUrl(url));
        } else {
            Timber.e("setUrl expected url value String, but got null");
        }
    }

    @ReactProp(name = "cacheKey")
    public void setCacheKey(final FlussonicThumbnailView view, String cacheKey) {
        UiThreadUtil.runOnUiThread(() -> view.setCacheKey(cacheKey));
    }

    @ReactProp(name = "size")
    public void setSize(final FlussonicThumbnailView view, @Nullable ReadableMap size) {
        if (size != null) {
            final int width, height;
            width = size.hasKey(WIDTH) ? size.getInt(WIDTH) : -1;
            height = size.hasKey(HEIGHT) ? size.getInt(HEIGHT) : -1;
            if (width > 0 && height > 0) {
                UiThreadUtil.runOnUiThread(() -> view.setSize(width, height));
            }
        }
    }

    @Override
    public void onDropViewInstance(FlussonicThumbnailView view) {
        view.cancelRequest();
        view.setStatusListener(null);
        super.onDropViewInstance(view);
    }

    @Override
    protected FlussonicThumbnailView createViewInstance(ThemedReactContext reactContext) {
        return new FlussonicThumbnailView(reactContext);
    }
}
