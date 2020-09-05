package com.iskwhdys.newlives.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.iskwhdys.newlives.domain.youtube.YoutubeChannelRepository;
import com.iskwhdys.newlives.infra.youtube.YoutubeFeedApi;
import com.iskwhdys.newlives.infra.youtube.YoutubeFeedEntity;

import org.jdom2.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class YoutubeFeedService {

    @Autowired
    YoutubeChannelRepository channelRepository;

    List<YoutubeFeedEntity> feedcache = new ArrayList<>();

    public void feed() {
        var channles = channelRepository.findAll();

        try {
            var feed = YoutubeFeedApi.download("UC0g1AE0DOjBYnLhkgoRWN1w");
            log.info(feed.toString());
        } catch (JDOMException | IOException e) {
            e.printStackTrace();
        }
    }
}
