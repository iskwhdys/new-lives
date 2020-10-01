package com.iskwhdys.newlives.app.youtube;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;

import com.iskwhdys.newlives.domain.youtube.YoutubeVideoEntity;
import com.iskwhdys.newlives.domain.youtube.YoutubeVideoRepository;
import com.iskwhdys.newlives.infra.config.AppConfig;
import com.iskwhdys.newlives.infra.image.ImageEditor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class YoutubeImageService {

    @Autowired
    private AppConfig appConfig;
    @Autowired
    private YoutubeVideoRepository videoRepository;

    private static RestTemplate restTemplate = new RestTemplate();

    public void downloadReceiveThumbnail(int before, int after) {
        LocalDateTime since = LocalDateTime.now().minusMinutes(before);
        LocalDateTime until = LocalDateTime.now().plusMinutes(after);
        videoRepository
                .findByEnabledTrueAndStatusEqualsAndLiveScheduleBetween(YoutubeVideoLogic.STATUS_RESERVE, since, until)
                .forEach(this::downloadThumbnail);
    }

    public void downloadStreamThumbnail(int before, int after) {
        LocalDateTime since = LocalDateTime.now().minusMinutes(before);
        LocalDateTime until = LocalDateTime.now().plusMinutes(after);
        videoRepository
                .findByEnabledTrueAndStatusEqualsAndLiveScheduleBetween(YoutubeVideoLogic.STATUS_STREAM, since, until)
                .forEach(this::downloadThumbnail);
    }

    public void downloadReceiveThumbnail() {
        videoRepository.findByEnabledTrueAndStatusEquals(YoutubeVideoLogic.STATUS_RESERVE)
                .forEach(this::downloadThumbnail);
    }

    public void downloadStreamThumbnail() {
        videoRepository.findByEnabledTrueAndStatusEquals(YoutubeVideoLogic.STATUS_STREAM)
                .forEach(this::downloadThumbnail);
    }

    public void downloadThumbnail(YoutubeVideoEntity v) {
        try {
            String dir = appConfig.getImage().getYoutube().getThumbnailPath();

            Path origin = Paths.get(dir, v.getId(), ".jpg");
            Files.createDirectory(origin.getParent());
            byte[] bytes = restTemplate.getForObject(v.getThumbnailUrl(), byte[].class);
            Files.write(origin, bytes, StandardOpenOption.CREATE);

            Path resize = Paths.get(dir, "176x98", v.getId(), ".jpg");
            bytes = ImageEditor.resize(bytes, 176, 132, 1.0f);
            bytes = ImageEditor.trim(bytes, 176, 98, 1.0f);
            Files.createDirectory(resize.getParent());
            Files.write(resize, bytes, StandardOpenOption.CREATE);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
