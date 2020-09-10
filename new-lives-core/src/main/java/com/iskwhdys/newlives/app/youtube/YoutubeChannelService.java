package com.iskwhdys.newlives.app.youtube;

import java.util.Map;

import javax.annotation.PostConstruct;

import com.iskwhdys.newlives.domain.youtube.YoutubeChannelEntity;
import com.iskwhdys.newlives.domain.youtube.YoutubeChannelRepository;
import com.iskwhdys.newlives.infra.config.AppConfig;
import com.iskwhdys.newlives.infra.youtube.YoutubeDataApi;
import com.iskwhdys.newlives.infra.youtube.YoutubeDataChannelsLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class YoutubeChannelService {

    @Autowired
    AppConfig appConfig;
    @Autowired
    YoutubeChannelRepository channelRepository;
    YoutubeDataApi dataApi;

    @PostConstruct
    public void init() {
        dataApi = new YoutubeDataApi(appConfig.getYoutube().getApikey().getChannel());
        log.info("API Channel:" + dataApi.getApiKey());
    }

    public void updateAllChannelInfo() {
        for (YoutubeChannelEntity channel : channelRepository.findAll()) {
            try {
                log.info(channel.getId() + ":" + channel.getTitle());
                Map<String, Object> items = dataApi.channels(channel.getId(), "snippet", "statistics");
                YoutubeDataChannelsLogic.update(channel, items);
                channelRepository.save(channel);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
