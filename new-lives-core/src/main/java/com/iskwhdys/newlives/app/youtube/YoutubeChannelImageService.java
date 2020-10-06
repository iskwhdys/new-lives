package com.iskwhdys.newlives.app.youtube;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.iskwhdys.newlives.domain.youtube.YoutubeChannelEntity;
import com.iskwhdys.newlives.domain.youtube.YoutubeChannelRepository;
import com.iskwhdys.newlives.infra.config.AppConfig;
import com.iskwhdys.newlives.infra.image.ImageEditor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class YoutubeChannelImageService {

    @Autowired
    private AppConfig appConfig;
    @Autowired
    private YoutubeChannelRepository channelRepository;

    private static RestTemplate restTemplate = new RestTemplate();

    public void downloadAll() {
        channelRepository.findAll().forEach(this::download);
    }

    public void download(YoutubeChannelEntity c) {
        try {
            String dir = appConfig.getImage().getYoutube().getChannelPath();

            Path origin = Paths.get(dir, c.getId() + ".jpg");
            Files.createDirectories(origin.getParent());
            byte[] bytes = restTemplate.getForObject(c.getThumbnailUrl(), byte[].class);
            Files.write(origin, bytes, StandardOpenOption.CREATE);

            Path resize = Paths.get(dir, "30x30", c.getId() + ".jpg");
            bytes = ImageEditor.resize(bytes, 30, 30, 1.0f);
            Files.createDirectories(resize.getParent());
            Files.write(resize, bytes, StandardOpenOption.CREATE);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
