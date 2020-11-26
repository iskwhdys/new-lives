package com.iskwhdys.newlives.app.delivery;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.annotation.PostConstruct;

import com.iskwhdys.newlives.app.Constants;
import com.iskwhdys.newlives.app.image.LiverImageService;
import com.iskwhdys.newlives.app.image.YoutubeChannelImageService;
import com.iskwhdys.newlives.app.image.YoutubeVideoImageService;
import com.iskwhdys.newlives.domain.liver.LiverRepository;
import com.iskwhdys.newlives.domain.youtube.YoutubeChannelRepository;
import com.iskwhdys.newlives.domain.youtube.YoutubeVideoRepository;
import com.iskwhdys.newlives.infra.config.AppConfig;
import com.iskwhdys.newlives.infra.util.HashCache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ImageDeliveryService {

    private HashCache<String, byte[]> thumbnailCache;
    private HashCache<String, byte[]> channelIconCache;
    private HashCache<String, byte[]> liverIconCache;
    @Autowired
    private AppConfig appConfig;
    @Autowired
    private YoutubeVideoRepository videoRepository;
    @Autowired
    private YoutubeChannelRepository channelRepository;
    @Autowired
    private LiverRepository liverRepository;
    @Autowired
    private YoutubeVideoImageService youtubeVideoImageService;
    @Autowired
    private YoutubeChannelImageService youtubeChannelImageService;
    @Autowired
    private LiverImageService liverImageService;

    @PostConstruct
    private void init() {
        thumbnailCache = new HashCache<>(appConfig.getImage().getThumbnailCacheCount());
        channelIconCache = new HashCache<>(appConfig.getImage().getIconCacheCount());
        liverIconCache = new HashCache<>(appConfig.getImage().getIconCacheCount());
    }

    public ImageTimestamp getLiverIcon(String id, String size) {
        String key = id + "." + size;
        if (liverIconCache.containsKey(key)) {
            return new ImageTimestamp(liverIconCache.get(key), liverIconCache.getLastWriteTime(key));
        }
        Path path;
        if (size.contains(Constants.SIZE_LIVER_WHITE_ICON)) {
            path = liverImageService.getWhiteIconPath().resolve(id + Constants.IMAGE_EXT);
        } else {
            path = liverImageService.getBlackIconPath().resolve(id + Constants.IMAGE_EXT);
        }
        if (!Files.exists(path)) {
            var l = liverRepository.findById(id);
            if (l.isEmpty()) {
                return new ImageTimestamp();
            }
            liverImageService.download(l.get());
        }
        try {
            liverIconCache.put(key, Files.readAllBytes(path));
            return new ImageTimestamp(liverIconCache.get(key), liverIconCache.getLastWriteTime(key));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return new ImageTimestamp();
    }

    public ImageTimestamp getYoutubeChannelIcon(String id, String size) {
        String key = id + "." + size;
        if (channelIconCache.containsKey(key)) {
            return new ImageTimestamp(channelIconCache.get(key), channelIconCache.getLastWriteTime(key));
        }
        Path path;
        if (size.contains(Constants.SIZE_CHANNEL_ICON)) {
            path = youtubeChannelImageService.getIconPath().resolve(id + Constants.IMAGE_EXT);
        } else {
            path = youtubeChannelImageService.getOriginIconPath().resolve(id + Constants.IMAGE_EXT);
        }
        if (!Files.exists(path)) {
            var c = channelRepository.findById(id);
            if (c.isEmpty()) {
                return new ImageTimestamp();
            }
            youtubeChannelImageService.download(c.get());
        }
        try {
            channelIconCache.put(key, Files.readAllBytes(path));
            return new ImageTimestamp(channelIconCache.get(key), channelIconCache.getLastWriteTime(key));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return new ImageTimestamp();
    }

    public ImageTimestamp getYoutubeThumbnail(String id) {
        if (thumbnailCache.containsKey(id)) {
            return new ImageTimestamp(thumbnailCache.get(id), thumbnailCache.getLastWriteTime(id));
        }
        Path path = youtubeVideoImageService.getThumnailPath().resolve(id + Constants.IMAGE_EXT);
        if (!Files.exists(path)) {
            var v = videoRepository.findById(id);
            if (v.isEmpty()) {
                return new ImageTimestamp();
            }
            youtubeVideoImageService.downloadThumbnail(v.get());
        }
        try {
            thumbnailCache.put(id, Files.readAllBytes(path));
            return new ImageTimestamp(thumbnailCache.get(id), thumbnailCache.getLastWriteTime(id));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return new ImageTimestamp();
    }

    public class ImageTimestamp {
        @Getter
        private byte[] image;
        @Getter
        private Timestamp time;

        public ImageTimestamp() {
            this.image = new byte[0];
            this.time = Timestamp.valueOf(LocalDateTime.now());
        }

        public ImageTimestamp(byte[] image, LocalDateTime time) {
            this.image = image;
            this.time = Timestamp.valueOf(time);
        }
    }
}
