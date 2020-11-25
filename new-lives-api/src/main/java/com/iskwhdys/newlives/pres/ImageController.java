package com.iskwhdys.newlives.pres;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.iskwhdys.newlives.app.Constants;
import com.iskwhdys.newlives.app.delivery.ImageDeliveryService;

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

    @GetMapping("/video/" + Constants.SIZE_THUMBNAIL + "/{id}")
    public ResponseEntity<byte[]> getVideoThumbnail(WebRequest swr, @PathVariable String id) {
        byte[] data = imageDeliveryService.getYoutubeThumbnail(id);
        LocalDateTime time = imageDeliveryService.getLastUpateTime(id);
        return getResponse(swr, time, data);
    }

    private ResponseEntity<byte[]> getResponse(WebRequest swr, LocalDateTime lastUpdateTime, byte[] obj) {
        HttpHeaders headers = new HttpHeaders();
        long time = Timestamp.valueOf(lastUpdateTime).getTime();
        if (swr.checkNotModified(time)) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        } else {
            headers.setContentType(MediaType.IMAGE_JPEG);
            headers.setLastModified(time);
            return new ResponseEntity<>(obj, headers, HttpStatus.OK);
        }
    }

}
