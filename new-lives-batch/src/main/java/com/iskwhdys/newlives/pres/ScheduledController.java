package com.iskwhdys.newlives.pres;

import java.time.LocalDateTime;
import java.util.Arrays;

import javax.annotation.PostConstruct;

import com.iskwhdys.newlives.app.delivery.TopVideoUpdateService;
import com.iskwhdys.newlives.app.image.YoutubeChannelImageService;
import com.iskwhdys.newlives.app.image.YoutubeVideoImageService;
import com.iskwhdys.newlives.app.youtube.YoutubeChannelService;
import com.iskwhdys.newlives.app.youtube.YoutubeFeedService;
import com.iskwhdys.newlives.app.youtube.YoutubeVideoService;
import com.iskwhdys.newlives.infra.google.SitemapService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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

    @Autowired
    TopVideoUpdateService topVideoUpdateService;

    @Autowired
    private Environment environment;

    boolean lock = false;

    @Scheduled(cron = "0 * * * * *", zone = "Asia/Tokyo")
    public void cronPerMinute() {
        if (lock)
            return;
        lock = true;
        log.info("cronPerMinute Start:" + LocalDateTime.now());
        try {
            updateJob(LocalDateTime.now().getHour(), LocalDateTime.now().getMinute());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        log.info("cronPerMinute end:" + LocalDateTime.now());
        lock = false;
    }

    @PostConstruct
    private void startup() {
        lock = true;
        log.info("startup Start:" + LocalDateTime.now());
        updateJob(16, 45);
        log.info("startup end:" + LocalDateTime.now());

        if (!Arrays.stream(environment.getActiveProfiles()).anyMatch(e -> e.equals("local"))) {
            lock = false;
        }
    }

    private void updateJob(int hour, int min) {
        if (hour == 16 && min == 45) {
            // 日次(API回復直前に実行)
            youtubeChannelService.updateAllChannelInfo();
            youtubeChannelImageService.downloadAll();
            youtubeVideoService.updateReserveVideo();
        }

        // TODO Feed外の動画の更新タイミング
        // TODO 画像の再キャッシュ（都度ファイルのタイムスタンプ見るか

        youtubeFeedService.updateAllChannelVideo();
        if (min == 0) {
            // 60分間隔
            youtubeVideoService.updateStreamVideo();
            youtubeVideoService.updateReserveVideo(60 * 24, 60 * 24);
            youtubeVideoImageService.downloadFeedThumbnail();
            sitemapService.update();
        } else if (min % 20 == 0) {
            // 20分間隔
            youtubeVideoService.updateStreamVideo();
            youtubeVideoService.updateReserveVideo(60 * 2, 60 * 2);
            youtubeVideoImageService.downloadStreamThumbnail(0, 60);
        } else if (min % 5 == 0) {
            // 5分間隔
            youtubeVideoService.updateStreamVideo();
            youtubeVideoService.updateReserveVideo(60, 60);
            youtubeVideoImageService.downloadStreamThumbnail(0, 60);
        } else {
            // 1分間隔
            youtubeVideoService.updateReserveVideo(15, 20);
        }
        youtubeVideoService.updateNewVideo();

        topVideoUpdateService.updateLive();
        topVideoUpdateService.updateUpload();
        topVideoUpdateService.updateArchive();
        topVideoUpdateService.updatePremier();
        topVideoUpdateService.updateSchedule();
    }

}
