package com.iskwhdys.newlives.infra.youtube;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.xml.XMLConstants;

import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.springframework.web.client.RestTemplate;

public class YoutubeFeedApi {
    private static final String URL_FEED = "https://www.youtube.com/feeds/videos.xml?channel_id=";

    private RestTemplate restTemplate = new RestTemplate();
    private SAXBuilder builder = new SAXBuilder();

    public YoutubeFeedApi() {
        builder.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        builder.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
    }

    public YoutubeFeedEntity download(String channelId) throws JDOMException, IOException {
        var response = restTemplate.getForEntity(URL_FEED + channelId, byte[].class);
        var bytes = response.getBody();

        var is = new ByteArrayInputStream(bytes);
        var root = builder.build(is).getRootElement();
        var entries = root.getChildren().stream().filter(p -> p.getName().contains("entry"))
                .collect(Collectors.toList());

        var entity = new YoutubeFeedEntity();
        entity.setChannelId(channelId);
        entity.setResponse(response);
        entity.setExpires(response.getHeaders().getExpires());
        entity.setVideos(new ArrayList<>());

        for (var entry : entries) {
            var videoElement = entry.getChildren().stream().filter(e -> e.getName().equals("videoId")).findFirst();
            if (videoElement.isEmpty()) {
                continue;
            }
            var video = entity.new Video();
            video.setId(videoElement.get().getValue());
            video.setElement(entry);
            entity.getVideos().add(video);
        }

        return entity;
    }

}