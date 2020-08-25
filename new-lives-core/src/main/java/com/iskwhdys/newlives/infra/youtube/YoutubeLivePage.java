package com.iskwhdys.newlives.infra.youtube;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class YoutubeLivePage {

    // private static final String BASE_URL =
    // "https://www.youtube.com/channel/%s/live";
    private static final String BASE_URL = "https://www.youtube.com/channel/%s/videos?view=2&live_view=501";

    // private static final String REGEX_LIVE_STARTED = "<strong
    // class=\"watch-time-text metadata-updateable-date-text\">.*ライブ配信開始</strong>";
    // static final String REGEX_LIVE_STARTED = "<strong class=\"watch-time-text
    // metadata-updateable-date-text\">.*</strong>";
    private static final String LIVE_STARTED = "viewCountText\":{\"runs\"";
    private static final String REGEX_VIDEO_ID = "\"videoId\":\"[a-z,A-Z,0-9,_]*\"";

    private static final String REGEX_NO_CONTENT_TITLE = "<title>YouTube</title>";

    private RestTemplate restTemplate = new RestTemplate();

    public String getId(String channelId) {

        ResponseEntity<String> obj;
        Pattern titlePattern = Pattern.compile(REGEX_NO_CONTENT_TITLE);
        while (true) {

            obj = restTemplate.getForEntity(String.format(BASE_URL, channelId), String.class);

            if (!titlePattern.matcher(obj.getBody()).find()) {
                break;
            }
        }

        if (obj.getBody().contains(LIVE_STARTED)) {

            Matcher videoId = Pattern.compile(REGEX_VIDEO_ID).matcher(obj.getBody());

            if (videoId.find()) {
                return videoId.group().split(":")[1].replace("\"", "").trim();
            } else {
                System.out.println("node videoid ");
            }
        }
        return null;
    }

}