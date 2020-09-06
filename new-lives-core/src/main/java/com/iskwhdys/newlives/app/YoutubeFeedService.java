package com.iskwhdys.newlives.app;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import com.iskwhdys.newlives.domain.youtube.YoutubeChannelEntity;
import com.iskwhdys.newlives.domain.youtube.YoutubeChannelRepository;
import com.iskwhdys.newlives.domain.youtube.YoutubeVideoEntity;
import com.iskwhdys.newlives.domain.youtube.YoutubeVideoRepository;
import com.iskwhdys.newlives.infra.youtube.YoutubeCommon;
import com.iskwhdys.newlives.infra.youtube.YoutubeFeedApi;
import com.iskwhdys.newlives.infra.youtube.YoutubeFeedEntity;
import com.iskwhdys.newlives.infra.youtube.YoutubeFeedEntity.Video;

import org.jdom2.Element;
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

    public void update() {
        for (YoutubeChannelEntity channel : channelRepository.findAll()) {
            try {
                update(channel);
            } catch (JDOMException | IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private void update(YoutubeChannelEntity channel) throws JDOMException, IOException {
        YoutubeFeedEntity feed = feedCache.get(channel.getId());
        if (feed != null && System.currentTimeMillis() < feed.getExpires()) {
            return;
        }
        feed = YoutubeFeedApi.download(channel.getId());

        var time = LocalDateTime.ofInstant(Instant.ofEpochMilli(feed.getExpires()), ZoneId.systemDefault());
        log.info(channel.getId() + ":expire:" + time);

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
        setElementData(channel, video, feedVideo);
        videoRepository.save(video);
    }

    private YoutubeVideoEntity setElementData(YoutubeChannelEntity channel, YoutubeVideoEntity video, Video feedVideo) {
        video.setId(feedVideo.getId());
        video.setYoutubeChannelEntity(channel);
        video.setEnabled(true);
        video.setUpdateDate(LocalDateTime.now());
        for (Element element : feedVideo.getElement().getChildren()) {
            switch (element.getName()) {
                case "title":
                    video.setTitle(element.getValue());
                    break;
                case "published":
                    video.setUploadDate(LocalDateTime.parse(element.getValue(), DateTimeFormatter.ISO_DATE_TIME));
                    break;
                case "group":
                    setElementGroupData(video, element);
                    break;
                default:
                    break;
            }
        }
        return video;
    }

    private YoutubeVideoEntity setElementGroupData(YoutubeVideoEntity video, Element group) {
        for (Element element : group.getChildren()) {
            switch (element.getName()) {
                case "description":
                    video.setDescription(element.getValue());
                    break;
                case "thumbnail":
                    video.setThumbnailUrl(element.getAttributeValue("url"));
                    break;
                case "community":
                    setElementGroupCommunityData(video, element);
                    break;
                default:
                    break;
            }
        }
        return video;
    }

    private YoutubeVideoEntity setElementGroupCommunityData(YoutubeVideoEntity video, Element community) {
        for (Element element : community.getChildren()) {
            switch (element.getName()) {
                case "starRating":
                    int count = Integer.parseInt(element.getAttributeValue("count"));
                    String ave = element.getAttributeValue("average");
                    int like = YoutubeCommon.getLikeCount(count, ave);
                    int dislike = count - like;
                    video.setLikes(like);
                    video.setDislikes(dislike);
                    break;
                case "statistics":
                    video.setViews(Integer.parseInt(element.getAttributeValue("views")));
                    break;
                default:
                    break;
            }
        }
        return video;
    }
}
