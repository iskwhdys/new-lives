package com.iskwhdys.newlives.infra.youtube;

import java.util.Map;

public class YoutubeDataVideosApi extends YoutubeDataApi {

    private static final String SNIPPET = "snippet";
    private static final String STATISTICS = "statistics";
    private static final String CONTENT_DETAILS = "contentDetails";
    private static final String LIVE_STREAMING_DETAILS = "liveStreamingDetails";
    private static final String STATUS = "status";

    public YoutubeDataVideosApi(String apiKey) {
        super(apiKey);
    }

    public Map<String, Object> getAll(String id) {
        return videos(id, SNIPPET, STATISTICS, CONTENT_DETAILS, LIVE_STREAMING_DETAILS, STATUS);
    }

    public Map<String, Object> getLiveStreamingDetails(String id) {
        return videos(id, LIVE_STREAMING_DETAILS);
    }

    public Map<String, Object> getContentDetails(String id) {
        return videos(id, CONTENT_DETAILS);
    }
}
