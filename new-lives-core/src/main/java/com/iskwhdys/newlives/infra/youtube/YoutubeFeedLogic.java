package com.iskwhdys.newlives.infra.youtube;

import java.util.Map;
import java.util.Map.Entry;

import com.iskwhdys.newlives.domain.youtube.YoutubeChannelEntity;

public class YoutubeFeedLogic {

    private YoutubeFeedLogic() {
    }

    public static YoutubeChannelEntity update(YoutubeChannelEntity channel, Map<String, Object> items) {
        for (Entry<String, ?> entry : items.entrySet()) {
            String key = entry.getKey();

            if (key.equals("snippet"))
                setSnippet(channel, castMap(entry.getValue()));
            else if (key.equals("statistics"))
                setStatistics(channel, castMap(entry.getValue()));
        }
        return channel;
    }

    private static YoutubeChannelEntity setSnippet(YoutubeChannelEntity channel, Map<String, ?> map) {
        if (map == null)
            return channel;
        if (map.containsKey("title"))
            channel.setTitle(map.get("title").toString());
        if (map.containsKey("description"))
            channel.setDescription(map.get("description").toString());
        if (map.containsKey("thumbnails")) {
            var thumbnails = castMap(map.get("thumbnails"));
            for (var key : new String[] { "default", "medium", "high" }) {
                if (thumbnails.containsKey(key)) {
                    var th = castMap(thumbnails.get(key));
                    channel.setThumbnailUrl(th.get("url").toString());
                    break;
                }
            }
        }
        return channel;
    }

    private static YoutubeChannelEntity setStatistics(YoutubeChannelEntity channel, Map<String, ?> map) {
        if (map == null)
            return channel;
        if (map.containsKey("subscriberCount"))
            channel.setSubscriberCount(toInteger(map, "subscriberCount"));
        return channel;
    }

    private static Integer toInteger(Map<String, ?> map, String key) {
        return Integer.parseInt(map.get(key).toString());
    }

    @SuppressWarnings("unchecked")
    private static Map<String, ?> castMap(Object item) {
        return (Map<String, ?>) item;
    }
}
