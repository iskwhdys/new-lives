package com.iskwhdys.newlives.pres;

import java.time.LocalDateTime;

import javax.annotation.PostConstruct;

import com.iskwhdys.newlives.app.youtube.YoutubeChannelImageService;
import com.iskwhdys.newlives.app.youtube.YoutubeChannelService;
import com.iskwhdys.newlives.app.youtube.YoutubeFeedService;
import com.iskwhdys.newlives.app.youtube.YoutubeVideoImageService;
import com.iskwhdys.newlives.app.youtube.YoutubeVideoService;
import com.iskwhdys.newlives.infra.google.SitemapService;

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
    @Autowired
    SitemapService sitemapService;
    @Autowired
    YoutubeVideoImageService youtubeVideoImageService;
    @Autowired
    YoutubeChannelImageService youtubeChannelImageService;

    @PostConstruct
    public void init() {
        cronPerMinute();
    }

    @Scheduled(cron = "0 * * * * *", zone = "Asia/Tokyo")
    public void cronPerMinute() {
        log.info("cronPerMinute Start:" + LocalDateTime.now());

        int hour = LocalDateTime.now().getHour();
        int min = LocalDateTime.now().getMinute();

        if (hour == 16 && min == 45) {
            // 日次(API回復直前に実行)
            youtubeChannelService.updateAllChannelInfo();
            youtubeChannelImageService.downloadAll();
            youtubeVideoService.updateReserveVideo();
        }

        // TODO サムネ画像の更新タイミングをどうするか
        // ライブは開始30分は5分おき？予約は1日未満なら1時間、6時間なら20分、1時間なら5分置き？
        // 過去分は？
        // 動画のEnabledにもつながる

        youtubeFeedService.update();
        if (min == 0) {
            // 60分間隔
            youtubeVideoService.updateStreamVideo();
            youtubeVideoService.updateReserveVideo(60 * 24, 60 * 24);
            sitemapService.update();
        } else if (min % 20 == 0) {
            // 20分間隔
            youtubeVideoService.updateStreamVideo();
            youtubeVideoService.updateReserveVideo(60 * 2, 60 * 2);
        } else if (min % 5 == 0) {
            // 5分間隔
            youtubeVideoService.updateStreamVideo();
            youtubeVideoService.updateReserveVideo(60, 60);
        } else {
            // 1分間隔
            youtubeVideoService.updateReserveVideo(15, 15);
        }
        youtubeVideoService.updateNewVideo();

        log.info("cronPerMinute end:" + LocalDateTime.now());
    }
}
