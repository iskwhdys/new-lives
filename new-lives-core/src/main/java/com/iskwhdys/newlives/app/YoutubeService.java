package com.iskwhdys.newlives.app;

import javax.annotation.PostConstruct;

import com.iskwhdys.newlives.domain.youtube.YoutubeChannelEntity;
import com.iskwhdys.newlives.infra.config.AppConfig;
import com.iskwhdys.newlives.infra.youtube.YoutubeDataApi;
import com.iskwhdys.newlives.infra.youtube.YoutubeLivePage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class YoutubeService {

    @Autowired
    AppConfig appConfig;

    YoutubeDataApi channelApi;
    YoutubeDataApi videoApi;

    YoutubeLivePage youtubeLivePage = new YoutubeLivePage();

    @PostConstruct
    private void init() {
        channelApi = new YoutubeDataApi(appConfig.getYoutube().getApikey().getChannel());
        videoApi = new YoutubeDataApi(appConfig.getYoutube().getApikey().getVideo());
        log.info("API Channel:" + channelApi.getApiKey());
        log.info("API Video:" + videoApi.getApiKey());
    }

    public void getVideo() {
        if (!channelApi.enabled())
            return;
        var video = videoApi.videos("KBqtdtOzmcc",
                "id,snippet,contentDetails,liveStreamingDetails,player,recordingDetails,statistics,status,topicDetails");
    }

    public void downloadFeed() {
        YoutubeChannelEntity yc = new YoutubeChannelEntity();

    }

    public void searchLive() {
        var map = videoApi.search("UCIG9rDtgR45VCZmYnd-4DUw", "id");
        log.info(map.toString());
    }

    public void getLiveId() {
        log.info(youtubeLivePage.getId("UCspv01oxUFf_MTSipURRhkA"));
    }
}