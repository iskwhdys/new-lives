package com.iskwhdys.newlives.pres;

import javax.annotation.PostConstruct;

import com.iskwhdys.newlives.app.YoutubeChannelService;
import com.iskwhdys.newlives.app.YoutubeFeedService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import lombok.extern.slf4j.Slf4j;

@Controller
@EnableScheduling
@Slf4j
public class StartupController {
    @Autowired
    YoutubeChannelService youtubeChannelService;
    @Autowired
    YoutubeFeedService youtubeFeedService;

    @PostConstruct
    public void run() {
        log.info("run");
        // youtubeChannelService.updateAllChannelInfo();
        // youtubeFeedService.update();

    }

    @Scheduled(cron = "0 * * * * *", zone = "Asia/Tokyo")
    public void cronPerMinute() {
        // youtubeFeedService.update();
    }

}
