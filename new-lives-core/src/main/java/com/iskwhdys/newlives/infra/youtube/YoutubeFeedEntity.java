package com.iskwhdys.newlives.infra.youtube;

import java.util.List;

import org.jdom2.Element;
import org.springframework.http.ResponseEntity;

import lombok.Data;

@Data
public class YoutubeFeedEntity {
    private String channelId;
    private long expires;
    private ResponseEntity<byte[]> response;
    private List<Video> videos;

    @Data
    public class Video {
        private String id;
        private Element element;
    }
}