package com.iskwhdys.newlives.pres;

import javax.annotation.PostConstruct;

import com.iskwhdys.newlives.app.youtube.YoutubeChannelService;
import com.iskwhdys.newlives.app.youtube.YoutubeFeedService;
import com.iskwhdys.newlives.app.youtube.YoutubeVideoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;

@Controller
@EnableScheduling
public class StartupController {
    @Autowired
    YoutubeChannelService youtubeChannelService;
    @Autowired
    YoutubeFeedService youtubeFeedService;
    @Autowired
    YoutubeVideoService youtubeVideoService;

    @PostConstruct
    public void run() {
        //
    }
}
