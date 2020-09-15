package com.iskwhdys.newlives.app.youtube;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

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
        log.info("API Video:" + dataApi.getApiKey());
    }

    public void updateNewVideo() {
        updateVideo(videoRepository.findByEnabledTrueAndTypeEquals(YoutubeVideoLogic.TYPE_NEW), dataApi::getAll);
    }

    public void updateStramVideo() {
        updateVideo(videoRepository.findByEnabledTrueAndStatusEquals(YoutubeVideoLogic.STATUS_STREAM),
                dataApi::getLiveStreamingDetails);
    }

    public void updateReserveVideo() {
        updateVideo(videoRepository.findByEnabledTrueAndStatusEquals(YoutubeVideoLogic.STATUS_RESERVE),
                dataApi::getLiveStreamingDetails);
    }

    private void updateVideo(List<YoutubeVideoEntity> list, Function<String, Map<String, Object>> dataApiGetFunction) {
        for (YoutubeVideoEntity v : list) {
            try {
                Map<String, Object> map = dataApiGetFunction.apply(v.getId());
                YoutubeDataVideosLogic.updateData(v, map);
                YoutubeVideoLogic.updateTypeAndStatus(v);
                videoRepository.save(v);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                try {
                    v.setType(YoutubeVideoLogic.TYPE_ERROR);
                    videoRepository.save(v);
                } catch (Exception e2) {
                    log.error(e2.getMessage(), e2);
                }
            }
        }
    }

}
