package com.iskwhdys.newlives.infra.youtube;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.client.RestTemplate;
import lombok.Getter;

public class YoutubeDataApi {

    private static final String URL_BASE = "https://www.googleapis.com/youtube/v3";
    private static final String URL_VIDEO = URL_BASE + "/videos?key=%s&id=%s&&part=%s";
    private static final String URL_CHANNEL = URL_BASE + "/channels?key=%s&id=%s&part=%s";
    private static final String URL_SEARCH = URL_BASE + "/search?key=%s&channelId=%s&part=%s";

    private RestTemplate restTemplate = new RestTemplate();
    @Getter
    private String apiKey;

    public YoutubeDataApi(String apiKey) {
        this.apiKey = apiKey;
    }

    public boolean enabled() {
        return apiKey != null;
    }

    public Map<String, Object> videos(String videoId, String... parts) {
        return getItem(String.format(URL_VIDEO, apiKey, videoId, String.join(",", parts)));
    }

    public Map<String, Object> channels(String channelId, String... parts) {
        return getItem(String.format(URL_CHANNEL, apiKey, channelId, String.join(",", parts)));
    }

    public Map<String, Object> search(String channelId, String... parts) {
        return getItem(String.format(URL_SEARCH, apiKey, channelId, String.join(",", parts)));
    }

    private Map<String, Object> getItem(String url) {
        var map = restTemplate.getForObject(url, Map.class);

        if (map == null || map.isEmpty() || !map.containsKey("items")) {
            return new HashMap<>();
        }
        List<?> items = (List<?>) map.get("items");
        return castMap(items.get(0));
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> castMap(Object item) {
        return (Map<String, Object>) item;
    }

}