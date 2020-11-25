package com.iskwhdys.newlives.app.delivery;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import javax.annotation.PostConstruct;

import com.iskwhdys.newlives.app.Constants;
import com.iskwhdys.newlives.app.image.YoutubeVideoImageService;
import com.iskwhdys.newlives.domain.youtube.YoutubeVideoRepository;
import com.iskwhdys.newlives.infra.config.AppConfig;
import com.iskwhdys.newlives.infra.util.HashCache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ImageDeliveryService {

    private HashCache<String, byte[]> thumbnailCache;
    @Autowired
    private AppConfig appConfig;
    @Autowired
    private YoutubeVideoRepository videoRepository;
    @Autowired
    private YoutubeVideoImageService youtubeVideoImageService;

    @PostConstruct
    private void init() {
        thumbnailCache = new HashCache<>(appConfig.getImage().getThumbnailCacheCount());
    }

    public byte[] getYoutubeThumbnail(String id) {
        if (thumbnailCache.containsKey(id)) {
            return thumbnailCache.get(id);
        }

        Path path = Paths.get(appConfig.getImage().getYoutube().getThumbnailPath(), Constants.SIZE_THUMBNAIL,
                id + Constants.THUMBNAIL_EXT);

        if (!Files.exists(path)) {
            var v = videoRepository.findById(id);
            if (v.isEmpty()) {
                return new byte[0];
            }
            youtubeVideoImageService.downloadThumbnail(v.get());
        }
        try {
            return thumbnailCache.put(id, Files.readAllBytes(path));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return new byte[0];
    }

    public LocalDateTime getLastUpateTime(String id) {
        if (thumbnailCache.containsKey(id)) {
            return thumbnailCache.getLastWriteTime(id);
        }
        return LocalDateTime.now();
    }

}
