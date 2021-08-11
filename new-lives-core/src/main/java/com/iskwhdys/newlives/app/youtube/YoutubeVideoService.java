package com.iskwhdys.newlives.app.youtube;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import com.iskwhdys.newlives.app.twitter.TweetService;
import com.iskwhdys.newlives.domain.youtube.YoutubeChannelEntity;
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
    private AppConfig appConfig;
    @Autowired
    private YoutubeVideoRepository videoRepository;
    @Autowired
    private TweetService tweetService;

    @Autowired
    private YoutubeChannelRepository channelRepository;

    private YoutubeDataVideosApi dataApi;

    @PostConstruct
    public void init() {
        dataApi = new YoutubeDataVideosApi(appConfig.getYoutube().getApikey().getVideo());
        log.info("API Youtube Video:" + dataApi.getApiKey());
    }

    public void updateNewVideo() {
        updateVideos(videoRepository.findByEnabledTrueAndTypeEquals(YoutubeVideoLogic.TYPE_NEW), dataApi::getAll);
    }

    public void updateStreamVideo() {
        updateVideos(videoRepository.findByEnabledTrueAndStatusEquals(YoutubeVideoLogic.STATUS_STREAM),
                dataApi::getAll);
    }

    public void updateReserveVideo() {
        updateVideos(videoRepository.findByEnabledTrueAndStatusEquals(YoutubeVideoLogic.STATUS_RESERVE),
                dataApi::getAll);
    }

    public void updateReserveVideo(int before, int after) {
        LocalDateTime since = LocalDateTime.now().minusMinutes(before);
        LocalDateTime until = LocalDateTime.now().plusMinutes(after);
        updateVideos(videoRepository.findByEnabledTrueAndStatusEqualsAndLiveScheduleBetween(
                YoutubeVideoLogic.STATUS_RESERVE, since, until), dataApi::getAll);
    }

    public void updateUploadedVideo() {
        updateVideos(videoRepository.findByEnabledTrueAndStatusEquals(YoutubeVideoLogic.STATUS_RESERVE),
                dataApi::getAll);
    }

    private void updateVideos(List<YoutubeVideoEntity> list, Function<String, Map<String, Object>> dataApiGetFunction) {
        var bannedId = channelRepository.findByEnabledFalseAndEndDateIsNullOrEndDateAfter(LocalDate.now()).stream()
                .map(YoutubeChannelEntity::getId).collect(Collectors.toList());

        list.stream().forEach(v -> {
            if (!bannedId.contains(v.getChannel())) {
                updateVideo(v, dataApiGetFunction);
            }
        });
    }

    private void updateVideo(YoutubeVideoEntity v, Function<String, Map<String, Object>> dataApiGetFunction) {
        try {
            Map<String, Object> map = dataApiGetFunction.apply(v.getId());
            if (map.isEmpty()) {
                v.setEnabled(false);
            } else {
                YoutubeDataVideosLogic.updateData(v, map);
                updateStatus(v);
            }
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

    private void updateStatus(YoutubeVideoEntity v) {
        String lastType = v.getType();
        String lastStatus = v.getStatus();
        v.setType(YoutubeVideoLogic.updateType(v));
        v.setStatus(YoutubeVideoLogic.updateStatus(v));

        if (!lastStatus.equals(v.getStatus()) || !lastType.equals(v.getType())) {
            log.info(v.getId() + ":" + lastType + "->" + v.getType() + ":" + lastStatus + "->" + v.getStatus() + ":"
                    + v.getTitle());
            if (YoutubeVideoLogic.isTypeUpload(v) || YoutubeVideoLogic.isStatusStream(v)) {
                tweetService.tweet(v);
            }
        }
    }
}
