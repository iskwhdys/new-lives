package com.iskwhdys.newlives.app.youtube;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import com.iskwhdys.newlives.domain.youtube.YoutubeChannelRepository;
import com.iskwhdys.newlives.domain.youtube.YoutubeVideoEntity;
import com.iskwhdys.newlives.domain.youtube.YoutubeVideoRepository;
import com.iskwhdys.newlives.infra.config.AppConfig;
import com.iskwhdys.newlives.infra.youtube.YoutubeDataVideosApi;
import com.iskwhdys.newlives.infra.youtube.YoutubeDataVideosLogic;

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
    @Autowired
    YoutubeVideoRepository videoRepository;

    YoutubeDataVideosApi dataApi;

    @PostConstruct
    public void init() {
        dataApi = new YoutubeDataVideosApi(appConfig.getYoutube().getApikey().getVideo());
        log.info("API Channel:" + dataApi.getApiKey());
    }

    public void update() {
        getNewVideos().stream().forEach(this::updateVideo);

    }

    private List<YoutubeVideoEntity> getNewVideos() {
        return videoRepository.findByEnabledTrueAndTypeEquals(YoutubeVideoLogic.TYPE_NEW);
    }

    private YoutubeVideoEntity updateVideo(YoutubeVideoEntity video) {
        Map<String, Object> data = null;
        if (YoutubeVideoLogic.isNew(video)) {
            data = dataApi.getAll(video.getId());
        }

        YoutubeDataVideosLogic.updateData(video, data);
        YoutubeVideoLogic.updateTypeAndStatus(video);

        return null;
    }

}
