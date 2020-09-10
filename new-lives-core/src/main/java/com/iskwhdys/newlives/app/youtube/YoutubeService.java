package com.iskwhdys.newlives.app.youtube;

import javax.annotation.PostConstruct;

import com.iskwhdys.newlives.infra.config.AppConfig;
import com.iskwhdys.newlives.infra.youtube.YoutubeDataApi;

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

    @PostConstruct
    private void init() {
        channelApi = new YoutubeDataApi(appConfig.getYoutube().getApikey().getChannel());
        videoApi = new YoutubeDataApi(appConfig.getYoutube().getApikey().getVideo());
        log.info("API Channel:" + channelApi.getApiKey());
        log.info("API Video:" + videoApi.getApiKey());
    }

}