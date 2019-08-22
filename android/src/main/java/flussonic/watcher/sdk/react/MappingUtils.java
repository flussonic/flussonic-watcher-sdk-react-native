package flussonic.watcher.sdk.react;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;

import java.util.List;

import flussonic.watcher.sdk.domain.pojo.Track;

class MappingUtils {

    static WritableMap map(Track track) {
        WritableMap map = Arguments.createMap();
        map.putString("level", track.level());
        map.putString("profile", track.profile());
        map.putInt("bitrate", track.bitrate());
        map.putString("content", track.content());
        map.putString("codec", track.codec());
        map.putString("size", track.size());
        map.putString("trackId", track.trackId());
        map.putInt("sarHeight", track.sarHeight());
        map.putInt("width", track.width());
        map.putInt("pixelHeight", track.pixelHeight());
        map.putInt("sarWidth", track.sarWidth());
        map.putInt("pixelWidth", track.pixelWidth());
        map.putInt("height", track.height());
        return map;
    }

    static WritableArray map(List<Track> tracks) {
        WritableArray array = Arguments.createArray();
        for (Track track : tracks) {
            array.pushMap(map(track));
        }
        return array;
    }
}
