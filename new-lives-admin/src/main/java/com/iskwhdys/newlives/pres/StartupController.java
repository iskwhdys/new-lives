package com.iskwhdys.newlives.pres;

import javax.annotation.PostConstruct;

import com.iskwhdys.newlives.app.image.LiverImageService;
import com.iskwhdys.newlives.app.image.YoutubeChannelImageService;
import com.iskwhdys.newlives.app.image.YoutubeVideoImageService;
import com.iskwhdys.newlives.app.youtube.YoutubeChannelService;
import com.iskwhdys.newlives.app.youtube.YoutubeFeedService;
import com.iskwhdys.newlives.app.youtube.YoutubeVideoService;
import com.iskwhdys.newlives.infra.config.AppConfig;

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
    @Autowired
    AppConfig appConfig;

    @Autowired
    YoutubeVideoImageService youtubeVideoImageService;
    @Autowired
    YoutubeChannelImageService youtubeChannelImageService;

    @Autowired
    LiverImageService liverImageService;

    @PostConstruct
    public void run() {

        // youtubeChannelService.updateAllChannelInfo();
        // youtubeChannelImageService.downloadAll();
        // youtubeVideoImageService.downloadUploadThumbnail();
        // liverImageService.downloadAll();

    }
}
