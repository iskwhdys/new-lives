package com.iskwhdys.newlives.app;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.iskwhdys.newlives.domain.youtube.YoutubeChannelEntity;
import com.iskwhdys.newlives.domain.youtube.YoutubeChannelRepository;
import com.iskwhdys.newlives.domain.youtube.YoutubeVideoEntity;
import com.iskwhdys.newlives.domain.youtube.YoutubeVideoRepository;
import com.iskwhdys.newlives.infra.youtube.YoutubeFeedApi;
import com.iskwhdys.newlives.infra.youtube.YoutubeFeedEntity;
import com.iskwhdys.newlives.infra.youtube.YoutubeFeedLogic;
import com.iskwhdys.newlives.infra.youtube.YoutubeFeedEntity.Video;

import org.jdom2.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class YoutubeFeedService {

    @Autowired
    YoutubeChannelRepository channelRepository;
    @Autowired
    YoutubeVideoRepository videoRepository;

    Map<String, YoutubeFeedEntity> feedCache = new HashMap<>();

    public List<String> getFeedVideoIdList(String channelId) {
        try {
            return getFeed(channelId).getVideos().stream().map(Video::getId).collect(Collectors.toList());
        } catch (JDOMException | IOException e) {
            log.error(e.getMessage(), e);
            return List.of();
        }
    }

    public void update() {
        for (YoutubeChannelEntity channel : channelRepository.findAll()) {
            try {
                update(channel);
            } catch (JDOMException | IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private YoutubeFeedEntity getFeed(String channelId) throws JDOMException, IOException {
        YoutubeFeedEntity feed = feedCache.get(channelId);
        if (feed != null && System.currentTimeMillis() < feed.getExpires()) {
            return feed;
        }
        return YoutubeFeedApi.download(channelId);
    }

    private void update(YoutubeChannelEntity channel) throws JDOMException, IOException {
        YoutubeFeedEntity feed = getFeed(channel.getId());
        log.info(channel.getId() + ":expire:" + feed.getLocalDateTimeExpires());

        for (Video feedVideo : feed.getVideos()) {
            try {
                updateVideo(channel, feedVideo);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        feedCache.put(channel.getId(), feed);
    }

    private void updateVideo(YoutubeChannelEntity channel, Video feedVideo) {
        log.info(feedVideo.getId());
        YoutubeVideoEntity video = videoRepository.findById(feedVideo.getId()).orElse(null);
        if (video == null) {
            video = new YoutubeVideoEntity();
        }
        YoutubeFeedLogic.setElementData(channel, video, feedVideo);
        videoRepository.save(video);
    }
}
