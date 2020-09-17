package com.iskwhdys.newlives.pres;

import java.time.LocalDateTime;

import javax.annotation.PostConstruct;

import com.iskwhdys.newlives.app.youtube.YoutubeChannelService;
import com.iskwhdys.newlives.app.youtube.YoutubeFeedService;
import com.iskwhdys.newlives.app.youtube.YoutubeVideoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import lombok.extern.slf4j.Slf4j;

@Controller
@EnableScheduling
@Slf4j
public class ScheduledController {
    @Autowired
    YoutubeChannelService youtubeChannelService;
    @Autowired
    YoutubeFeedService youtubeFeedService;
    @Autowired
    YoutubeVideoService youtubeVideoService;

    @PostConstruct
    public void run() {
        log.info("run");
    }

    @Scheduled(cron = "0 * * * * *", zone = "Asia/Tokyo")
    public void cronPerMinute() {
        int min = LocalDateTime.now().getMinute();

        if (min == 0) {
            youtubeChannelService.updateAllChannelInfo();
            youtubeFeedService.update();
            youtubeVideoService.updateStreamVideo();
            youtubeVideoService.updateReserveVideo(60 * 24, 60 * 24);
            youtubeVideoService.updateNewVideo();
        } else if (min % 20 == 0) {
            youtubeFeedService.update();
            youtubeVideoService.updateStreamVideo();
            youtubeVideoService.updateReserveVideo(60 * 2, 60 * 2);
            youtubeVideoService.updateNewVideo();
        } else if (min % 5 == 0) {
            youtubeFeedService.update();
            youtubeVideoService.updateStreamVideo();
            youtubeVideoService.updateReserveVideo(60, 60);
            youtubeVideoService.updateNewVideo();
        } else {
            youtubeFeedService.update();
            youtubeVideoService.updateReserveVideo(15, 15);
            youtubeVideoService.updateNewVideo();
        }
    }
}
