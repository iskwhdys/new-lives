package com.iskwhdys.newlives.app;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.iskwhdys.newlives.domain.youtube.YoutubeChannelEntity;
import com.iskwhdys.newlives.domain.youtube.YoutubeChannelRepository;
import com.iskwhdys.newlives.infra.config.AppConfig;
import com.iskwhdys.newlives.infra.youtube.YoutubeDataChannelsLogic;
import com.iskwhdys.newlives.infra.youtube.YoutubeDataVideosApi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class YoutubeVideoService {

    @Autowired
    AppConfig appConfig;
    @Autowired
    YoutubeChannelRepository channelRepository;
    YoutubeDataVideosApi dataApi;

    @PostConstruct
    public void init() {
        dataApi = new YoutubeDataVideosApi(appConfig.getYoutube().getApikey().getVideo());
        log.info("API Channel:" + dataApi.getApiKey());
    }

    public void update(List<String> ids) {

    }

}
