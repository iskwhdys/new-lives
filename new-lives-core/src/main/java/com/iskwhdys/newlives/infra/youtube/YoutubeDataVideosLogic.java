package com.iskwhdys.newlives.infra.youtube;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import com.iskwhdys.newlives.domain.youtube.YoutubeVideoEntity;

public class YoutubeDataVideosLogic {
    private YoutubeDataVideosLogic() {
    }

    public static YoutubeVideoEntity updateData(YoutubeVideoEntity video, Map<String, Object> item) {
        setSnippet(video, toMap(item, "snippet"));
        setStatistics(video, toMap(item, "statistics"));
        setContentDetails(video, toMap(item, "contentDetails"));
        setLiveStreamingDetails(video, toMap(item, "liveStreamingDetails"));
        setStatus(video, toMap(item, "status"));
        return video;
    }

    private static YoutubeVideoEntity setSnippet(YoutubeVideoEntity video, Map<String, ?> map) {
        if (map == null)
            return video;
        if (map.containsKey("title"))
            video.setTitle(map.get("title").toString());
        if (map.containsKey("description"))
            video.setDescription(map.get("description").toString());
        return video;
    }

    private static YoutubeVideoEntity setStatistics(YoutubeVideoEntity video, Map<String, ?> map) {
        if (map == null)
            return video;
        if (map.containsKey("viewCount"))
            video.setViews(toInteger(map, "viewCount"));
        if (map.containsKey("likeCount"))
            video.setLikes(toInteger(map, "likeCount"));
        if (map.containsKey("dislikeCount"))
            video.setDislikes(toInteger(map, "dislikeCount"));
        if (map.containsKey("favoriteCount"))
            video.setFavorites(toInteger(map, "favoriteCount"));
        if (map.containsKey("commentCount"))
            video.setComments(toInteger(map, "commentCount"));
        return video;
    }

    private static YoutubeVideoEntity setContentDetails(YoutubeVideoEntity video, Map<String, ?> map) {
        if (map == null)
            return video;
        String duration = map.get("duration").toString();
        video.setDuration((int) Duration.parse(duration).toSeconds());
        return video;
    }

    private static YoutubeVideoEntity setLiveStreamingDetails(YoutubeVideoEntity video, Map<String, ?> map) {
        if (map == null)
            return video;
        if (map.containsKey("actualStartTime"))
            video.setLiveStart(toDate(map, "actualStartTime"));
        if (map.containsKey("actualEndTime"))
            video.setLiveEnd(toDate(map, "actualEndTime"));
        if (map.containsKey("scheduledStartTime"))
            video.setLiveSchedule(toDate(map, "scheduledStartTime"));
        if (map.containsKey("concurrentViewers"))
            video.setLiveViews(toInteger(map, "concurrentViewers"));
        return video;
    }

    private static YoutubeVideoEntity setStatus(YoutubeVideoEntity video, Map<String, ?> map) {
        if (map == null)
            return video;
        if (map.containsKey("uploadStatus"))
            video.setUploadStatus(map.get("uploadStatus").toString());
        return video;
    }

    private static LocalDateTime toDate(Map<String, ?> map, String key) {
        return LocalDateTime.parse(map.get(key).toString(), DateTimeFormatter.ISO_DATE_TIME);
    }

    private static Integer toInteger(Map<String, ?> map, String key) {
        return Integer.parseInt(map.get(key).toString());
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> toMap(Map<String, Object> map, String key) {
        return (Map<String, Object>) map.get(key);
    }
}
