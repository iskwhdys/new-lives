package com.iskwhdys.newlives.app.image;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;

import com.iskwhdys.newlives.app.Constants;
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
        channelRepository.findByEnabledTrueAndEndDateIsNullOrEndDateAfter(LocalDate.now()).parallelStream()
                .forEach(this::download);
    }

    public Path getOriginIconPath() {
        return Paths.get(appConfig.getImage().getYoutube().getChannelPath());
    }

    public Path getIconPath() {
        return Paths.get(appConfig.getImage().getYoutube().getChannelPath(), Constants.SIZE_CHANNEL_ICON);
    }

    public void download(YoutubeChannelEntity c) {
        try {
            Path origin = getOriginIconPath().resolve(c.getId() + Constants.IMAGE_EXT);
            Files.createDirectories(origin.getParent());
            byte[] bytes = restTemplate.getForObject(c.getThumbnailUrl(), byte[].class);
            Files.write(origin, bytes, StandardOpenOption.CREATE);

            Path resize = getIconPath().resolve(c.getId() + Constants.IMAGE_EXT);
            bytes = ImageEditor.resize(bytes, 30, 30, 1.0f);
            Files.createDirectories(resize.getParent());
            Files.write(resize, bytes, StandardOpenOption.CREATE);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
