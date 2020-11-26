package com.iskwhdys.newlives.pres;

import com.iskwhdys.newlives.app.Constants;
import com.iskwhdys.newlives.app.delivery.ImageDeliveryService;
import com.iskwhdys.newlives.app.delivery.ImageDeliveryService.ImageTimestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequestMapping("/api/image")
public class ImageController {

    @Autowired
    ImageDeliveryService imageDeliveryService;

    @GetMapping("/video/{id}/" + Constants.SIZE_THUMBNAIL)
    public ResponseEntity<byte[]> getVideoThumbnail(WebRequest swr, @PathVariable String id) {
        return getResponse(swr, imageDeliveryService.getYoutubeThumbnail(id));
    }

    @GetMapping("/channel/{id}")
    public ResponseEntity<byte[]> getChannelIconOrigin(WebRequest swr, @PathVariable String id) {
        return getResponse(swr, imageDeliveryService.getYoutubeChannelIcon(id, ""));
    }

    @GetMapping("/channel/{id}/" + Constants.SIZE_CHANNEL_ICON)
    public ResponseEntity<byte[]> getChannelIcon(WebRequest swr, @PathVariable String id) {
        return getResponse(swr, imageDeliveryService.getYoutubeChannelIcon(id, Constants.SIZE_CHANNEL_ICON));
    }

    @GetMapping("/liver/{id}/" + Constants.SIZE_LIVER_BLACK_ICON)
    public ResponseEntity<byte[]> getLiverBlackIcon(WebRequest swr, @PathVariable String id) {
        return getResponse(swr, imageDeliveryService.getLiverIcon(id, Constants.SIZE_LIVER_BLACK_ICON));
    }

    @GetMapping("/liver/{id}/" + Constants.SIZE_LIVER_WHITE_ICON)
    public ResponseEntity<byte[]> getLiverWhiteIcon(WebRequest swr, @PathVariable String id) {
        return getResponse(swr, imageDeliveryService.getLiverIcon(id, Constants.SIZE_LIVER_WHITE_ICON));
    }

    private ResponseEntity<byte[]> getResponse(WebRequest swr, ImageTimestamp imageTimestamp) {
        HttpHeaders headers = new HttpHeaders();
        long time = imageTimestamp.getTime().getTime();
        if (swr.checkNotModified(time)) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        } else {
            headers.setContentType(MediaType.IMAGE_JPEG);
            headers.setLastModified(time);
            return new ResponseEntity<>(imageTimestamp.getImage(), headers, HttpStatus.OK);
        }
    }

}
