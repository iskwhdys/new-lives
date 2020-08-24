package com.iskwhdys.newlives.pres;

import javax.annotation.PostConstruct;

import com.iskwhdys.newlives.infra.youtube.YoutubeService;

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
    YoutubeService youtubeService;

    @PostConstruct
    public void run() {
        log.info("run");
        youtubeService.feed();

        // youtubeService.getVideo();
        // youtubeService.searchLive();

        // youtubeService.getLiveId();
    }

    @Scheduled(cron = "0 * * * * *", zone = "Asia/Tokyo")
    public void cronPerMinute() {

        // youtubeService.getLiveId();
    }

}
