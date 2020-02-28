package flussonic.watcher.sdk.react;

import android.app.Activity;
import android.content.res.Resources;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.RCTEventEmitter;

import java.util.Map;

import flussonic.watcher.sdk.domain.pojo.PlaybackStatus;
import flussonic.watcher.sdk.domain.pojo.UpdateProgressEvent;
import flussonic.watcher.sdk.presentation.core.listeners.FlussonicBufferingListener;
import flussonic.watcher.sdk.presentation.core.listeners.FlussonicCollapseExpandTimelineListener;
import flussonic.watcher.sdk.presentation.core.listeners.FlussonicDownloadRequestListener;
import flussonic.watcher.sdk.presentation.core.listeners.FlussonicUpdateProgressEventListener;
import flussonic.watcher.sdk.presentation.watcher.FlussonicWatcherView;
import timber.log.Timber;

@SuppressWarnings("unused")
public class RNFlussonicWatcherViewManager extends SimpleViewManager<FlussonicWatcherView> {

    private static final String REACT_CLASS = "RCTFlussonicWatcherView";

    private static final String PHASED_REGISTRATION_NAMES = "phasedRegistrationNames";
    private static final String BUBBLED = "bubbled";

    private static final String NATIVE_EVENT_BUFFERING_START = "NATIVE_EVENT_BUFFERING_START";
    private static final String NATIVE_EVENT_BUFFERING_STOP = "NATIVE_EVENT_BUFFERING_STOP";
    private static final String NATIVE_EVENT_DOWNLOAD_REQUEST = "NATIVE_EVENT_DOWNLOAD_REQUEST";
    private static final String NATIVE_EVENT_UPDATE_PROGRESS = "NATIVE_EVENT_UPDATE_PROGRESS";
    private static final String NATIVE_EVENT_COLLAPSE_TOOLBAR = "NATIVE_EVENT_COLLAPSE_TOOLBAR";
    private static final String NATIVE_EVENT_EXPAND_TOOLBAR = "NATIVE_EVENT_EXPAND_TOOLBAR";
    private static final String NATIVE_EVENT_SHOW_TOOLBAR = "NATIVE_EVENT_SHOW_TOOLBAR";
    private static final String NATIVE_EVENT_HIDE_TOOLBAR = "NATIVE_EVENT_HIDE_TOOLBAR";
    private static final String NATIVE_EVENT_PLAYER_ERROR = "NATIVE_EVENT_PLAYER_ERROR";

    @Nullable
    private static Integer extractIntegerFromReadableMapOrNull(@NonNull ReadableMap source,
                                                               @NonNull String key) {
        return source.hasKey(key) ? source.getInt(key) : null;
    }

    private static FragmentActivity getFragmentActivity(Activity activity) {
        if (activity == null) {
            throw new IllegalStateException("Activity not provided");
        }

        if (!(activity instanceof FragmentActivity)) {
            throw new IllegalStateException("Please use FragmentActivity instead of " + activity.getClass().getSimpleName());
        }

        return (FragmentActivity) activity;
    }

    private static void clearNativeEvents(FlussonicWatcherView flussonicWatcherView) {
        flussonicWatcherView.setDownloadRequestListener(null);
        flussonicWatcherView.setBufferingListener(null);
        flussonicWatcherView.setUpdateProgressEventListener(null);
        flussonicWatcherView.setCollapseExpandTimelineListener(null);
    }

    static void setupNativeEvents(ReactContext reactContext, FlussonicWatcherView flussonicWatcherView) {
        //noinspection Convert2Lambda
        flussonicWatcherView.setExoPlayerErrorListener(new FlussonicWatcherView.FlussonicExoPlayerErrorListener() {

        @Override
        public void onExoPlayerError(String code, String message, String url) {
            WritableMap event = Arguments.createMap();
            event.putString("code", code);
            event.putString("message", message);
            event.putString("url", url);
            reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                    flussonicWatcherView.getId(),
                    NATIVE_EVENT_PLAYER_ERROR,
                    event);
        }
    });

        flussonicWatcherView.setDownloadRequestListener(new FlussonicDownloadRequestListener() {

            @Override
            public void onDownloadRequest(long from, long to) {
                WritableMap event = Arguments.createMap();
                event.putString("from", String.valueOf(from));
                event.putString("to", String.valueOf(to));
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                        flussonicWatcherView.getId(),
                        NATIVE_EVENT_DOWNLOAD_REQUEST,
                        event);
            }
        });

        flussonicWatcherView.setBufferingListener(new FlussonicBufferingListener() {

            @Override
            public void onBufferingStart() {
                WritableMap event = Arguments.createMap();
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                        flussonicWatcherView.getId(),
                        NATIVE_EVENT_BUFFERING_START,
                        event);
            }

            @Override
            public void onBufferingStop() {
                WritableMap event = Arguments.createMap();
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                        flussonicWatcherView.getId(),
                        NATIVE_EVENT_BUFFERING_STOP,
                        event);
            }
        });

        //noinspection Convert2Lambda
        flussonicWatcherView.setUpdateProgressEventListener(new FlussonicUpdateProgressEventListener() {

            @Override
            public void onUpdateProgress(@NonNull UpdateProgressEvent nativeEvent) {
                WritableMap event = Arguments.createMap();
                event.putDouble("currentUtcInSeconds", nativeEvent.currentUtcInSeconds());
                event.putString("playbackStatus", nativeEvent.playbackStatus().toString());
                event.putDouble("speed", nativeEvent.speed());
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                        flussonicWatcherView.getId(),
                        NATIVE_EVENT_UPDATE_PROGRESS,
                        event);
            }
        });

        flussonicWatcherView.setCollapseExpandTimelineListener(new FlussonicCollapseExpandTimelineListener() {

            @Override
            public void collapseToolbar(int animationDuration) {
                WritableMap event = Arguments.createMap();
                event.putInt("animationDuration", animationDuration);
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                        flussonicWatcherView.getId(),
                        NATIVE_EVENT_COLLAPSE_TOOLBAR,
                        event);
            }

            @Override
            public void expandToolbar(int animationDuration) {
                WritableMap event = Arguments.createMap();
                event.putInt("animationDuration", animationDuration);
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                        flussonicWatcherView.getId(),
                        NATIVE_EVENT_EXPAND_TOOLBAR,
                        event);
            }

            @Override
            public void showToolbar(int animationDuration) {
                WritableMap event = Arguments.createMap();
                event.putInt("animationDuration", animationDuration);
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                        flussonicWatcherView.getId(),
                        NATIVE_EVENT_EXPAND_TOOLBAR,
                        event);
            }

            @Override
            public void hideToolbar(int animationDuration) {
                WritableMap event = Arguments.createMap();
                event.putInt("animationDuration", animationDuration);
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                        flussonicWatcherView.getId(),
                        NATIVE_EVENT_COLLAPSE_TOOLBAR,
                        event);
            }
        });
    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    protected FlussonicWatcherView createViewInstance(ThemedReactContext reactContext) {
        FlussonicWatcherView flussonicWatcherView = new FlussonicWatcherView(reactContext);
        // called from componentDidMount
        // setupNativeEvents(reactContext, flussonicWatcherView);
        FragmentActivity activity = getFragmentActivity(reactContext.getCurrentActivity());
        flussonicWatcherView.initialize(activity, true);
        return flussonicWatcherView;
    }

    @Override
    public void onDropViewInstance(FlussonicWatcherView view) {
        super.onDropViewInstance(view);
        clearNativeEvents(view);
        view.release();
    }

    @Override
    public Map<String, Object> getExportedCustomBubblingEventTypeConstants() {
        return MapBuilder.<String, Object>builder()
                .put(
                        NATIVE_EVENT_BUFFERING_START,
                        MapBuilder.of(
                                PHASED_REGISTRATION_NAMES,
                                MapBuilder.of(BUBBLED, "onBufferingStart")))
                .put(
                        NATIVE_EVENT_BUFFERING_STOP,
                        MapBuilder.of(
                                PHASED_REGISTRATION_NAMES,
                                MapBuilder.of(BUBBLED, "onBufferingStop")))
                .put(
                        NATIVE_EVENT_DOWNLOAD_REQUEST,
                        MapBuilder.of(
                                PHASED_REGISTRATION_NAMES,
                                MapBuilder.of(BUBBLED, "onDownloadRequest")))
                .put(
                        NATIVE_EVENT_UPDATE_PROGRESS,
                        MapBuilder.of(
                                PHASED_REGISTRATION_NAMES,
                                MapBuilder.of(BUBBLED, "onUpdateProgress")
                        )
                )
                .put(
                        NATIVE_EVENT_COLLAPSE_TOOLBAR,
                        MapBuilder.of(
                                PHASED_REGISTRATION_NAMES,
                                MapBuilder.of(BUBBLED, "onCollapseToolbar")
                        )
                )
                .put(
                        NATIVE_EVENT_EXPAND_TOOLBAR,
                        MapBuilder.of(
                                PHASED_REGISTRATION_NAMES,
                                MapBuilder.of(BUBBLED, "onExpandToolbar")
                        )
                )
                .put(
                        NATIVE_EVENT_PLAYER_ERROR,
                        MapBuilder.of(
                                PHASED_REGISTRATION_NAMES,
                                MapBuilder.of(BUBBLED, "onPlayerError")
                        )
                )
                .build();
    }

    @ReactProp(name = "url")
    public void setUrl(final FlussonicWatcherView view, String url) {
        UiThreadUtil.runOnUiThread(() -> view.setUrl(url));
    }

    @ReactProp(name = "allowDownload")
    public void setAllowDownload(final FlussonicWatcherView view, boolean allowDownload) {
        UiThreadUtil.runOnUiThread(() -> view.setAllowDownload(allowDownload));
    }

    @ReactProp(name = "startPosition")
    public void setStartPosition(final FlussonicWatcherView view, double startPosition) {
        UiThreadUtil.runOnUiThread(() -> {
            view.setStartPosition((long) startPosition);
            try {
                PlaybackStatus status = view.getPlaybackStatus();
                if (status == PlaybackStatus.PLAYING ||
                        status == PlaybackStatus.PREPARING ||
                        status == PlaybackStatus.PAUSED ) {
                    view.seek((long) startPosition);
                }
            } catch (IllegalStateException ex) {
                Timber.d(ex,"We should call view.getPlaybackStatus after view.setUrl\n");
            }
        });
    }

    @ReactProp(name = "toolbarHeight")
    public void setToolbarHeight(final FlussonicWatcherView view, int toolbarHeight) {
        int px = Math.round(toolbarHeight * Resources.getSystem().getDisplayMetrics().density);
        UiThreadUtil.runOnUiThread(() -> view.setToolbarHeight(px));
    }
}
