package flussonic.watcher.sdk.react;

import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.uimanager.NativeViewHierarchyManager;
import com.facebook.react.uimanager.UIManagerModule;

import java.io.File;
import java.io.IOException;
import java.util.List;

import flussonic.watcher.sdk.domain.pojo.PlaybackStatus;
import flussonic.watcher.sdk.domain.pojo.Track;
import flussonic.watcher.sdk.presentation.watcher.FlussonicWatcherView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

@SuppressWarnings("unused")
@ReactModule(name = RNFlussonicWatcherReactSdkModule.MODULE_NAME)
public class RNFlussonicWatcherReactSdkModule extends ReactContextBaseJavaModule
        implements LifecycleEventListener {

    static final String MODULE_NAME = "RNFlussonicWatcherReactSdkModule";

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @SuppressWarnings("WeakerAccess")
    public RNFlussonicWatcherReactSdkModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return MODULE_NAME;
    }

    @Override
    public void initialize() {
        getReactApplicationContext().addLifecycleEventListener(this);
    }

    @Override
    public void onHostResume() {
        Timber.d("onHostResume");
    }

    @Override
    public void onHostPause() {
        Timber.d("onHostPause");
    }

    @Override
    public void onHostDestroy() {
        Timber.d("onHostDestroy");
        compositeDisposable.clear();
    }

    @ReactMethod
    public void pause(final int viewTag) {
        final ReactApplicationContext context = getReactApplicationContext();
        UIManagerModule uiManager = context.getNativeModule(UIManagerModule.class);
        uiManager.addUIBlock((NativeViewHierarchyManager nativeViewHierarchyManager) -> {
            try {
                FlussonicWatcherView flussonicWatcherView = (FlussonicWatcherView) nativeViewHierarchyManager.resolveView(viewTag);
                flussonicWatcherView.pause();
            } catch (Exception e) {
                Timber.e(e, "failed to call native pause");
            }
        });
    }

    @ReactMethod
    public void resume(final int viewTag) {
        final ReactApplicationContext context = getReactApplicationContext();
        UIManagerModule uiManager = context.getNativeModule(UIManagerModule.class);
        uiManager.addUIBlock((NativeViewHierarchyManager nativeViewHierarchyManager) -> {
            try {
                FlussonicWatcherView flussonicWatcherView = (FlussonicWatcherView) nativeViewHierarchyManager.resolveView(viewTag);
                flussonicWatcherView.resume();
            } catch (Exception e) {
                Timber.e(e, "failed to call native resume");
            }
        });
    }

    @ReactMethod
    public void seek(final int viewTag, double seconds) {
        final ReactApplicationContext context = getReactApplicationContext();
        UIManagerModule uiManager = context.getNativeModule(UIManagerModule.class);
        uiManager.addUIBlock((NativeViewHierarchyManager nativeViewHierarchyManager) -> {
            try {
                FlussonicWatcherView flussonicWatcherView = (FlussonicWatcherView) nativeViewHierarchyManager.resolveView(viewTag);
                flussonicWatcherView.seek((long) seconds);
            } catch (Exception e) {
                Timber.e(e, "failed to call native seek");
            }
        });
    }

    @ReactMethod
    public void captureScreenshot(final int viewTag,
                                  String fileName,
                                  @Nullable String picturesSubdirectoryName,
                                  Promise promise) {
        final ReactApplicationContext context = getReactApplicationContext();
        UIManagerModule uiManager = context.getNativeModule(UIManagerModule.class);
        uiManager.addUIBlock((NativeViewHierarchyManager nativeViewHierarchyManager) -> {
            try {
                FlussonicWatcherView flussonicWatcherView = (FlussonicWatcherView) nativeViewHierarchyManager.resolveView(viewTag);
                captureScreenshot(flussonicWatcherView, fileName, picturesSubdirectoryName, promise);
            } catch (Exception e) {
                Timber.e(e, "failed to call native captureScreenshot");
                promise.reject(e);
            }
        });
    }

    @ReactMethod
    public void getAvailableTracks(final int viewTag, Promise promise) {
        final ReactApplicationContext context = getReactApplicationContext();
        UIManagerModule uiManager = context.getNativeModule(UIManagerModule.class);
        uiManager.addUIBlock((NativeViewHierarchyManager nativeViewHierarchyManager) -> {
            try {
                FlussonicWatcherView flussonicWatcherView = (FlussonicWatcherView) nativeViewHierarchyManager.resolveView(viewTag);
                List<Track> tracks = flussonicWatcherView.getAvailableTracks();
                promise.resolve(MappingUtils.map(tracks));
            } catch (Exception e) {
                Timber.e(e, "failed to call native getAvailableTracks");
                promise.reject(e);
            }
        });
    }

    @ReactMethod
    public void getCurrentTrack(final int viewTag, Promise promise) {
        final ReactApplicationContext context = getReactApplicationContext();
        UIManagerModule uiManager = context.getNativeModule(UIManagerModule.class);
        uiManager.addUIBlock((NativeViewHierarchyManager nativeViewHierarchyManager) -> {
            try {
                FlussonicWatcherView flussonicWatcherView = (FlussonicWatcherView) nativeViewHierarchyManager.resolveView(viewTag);
                Track track = flussonicWatcherView.getCurrentTrack();
                promise.resolve(track == null ? null : MappingUtils.map(track));
            } catch (Exception e) {
                Timber.e(e, "failed to call native getCurrentTrack");
                promise.reject(e);
            }
        });
    }

    @ReactMethod
    public void componentDidMount(final int viewTag) {
        final ReactApplicationContext context = getReactApplicationContext();
        UIManagerModule uiManager = context.getNativeModule(UIManagerModule.class);
        uiManager.addUIBlock((NativeViewHierarchyManager nativeViewHierarchyManager) -> {
            try {
                FlussonicWatcherView flussonicWatcherView = (FlussonicWatcherView) nativeViewHierarchyManager.resolveView(viewTag);
                RNFlussonicWatcherViewManager.setupNativeEvents(context, flussonicWatcherView);
            } catch (Exception e) {
                Timber.e(e, "failed to setup native events");
            }
        });
    }

    private void captureScreenshot(FlussonicWatcherView flussonicWatcherView,
                                   String fileName,
                                   @Nullable String picturesSubdirectoryName,
                                   Promise promise) {
        String state = Environment.getExternalStorageState();
        boolean isExternalStorageWritable = Environment.MEDIA_MOUNTED.equals(state);
        if (!isExternalStorageWritable) {
            promise.reject(new IOException("External storage isn't mounted"));
            return;
        }

        PlaybackStatus playbackStatus = flussonicWatcherView.getPlaybackStatus();
        if (playbackStatus == PlaybackStatus.PREPARING
                || playbackStatus == PlaybackStatus.IDLE) {
            promise.reject(new IllegalStateException("Playing not started"));
            return;
        }

        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!TextUtils.isEmpty(picturesSubdirectoryName)) {
            dir = new File(dir, picturesSubdirectoryName);
            //noinspection ResultOfMethodCallIgnored
            dir.mkdirs();
        }
        File file = new File(dir, fileName);
        String path = file.getAbsolutePath();
        Uri uri = Uri.parse(path);

        compositeDisposable.add(
                flussonicWatcherView.captureScreenshot(uri)
                        .doOnComplete(() -> Timber.d("Screenshot saved as %s", path))
                        .doOnError(throwable -> Timber.e(throwable, "Failed to save screenshot"))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> promise.resolve(path), promise::reject));
    }
}
