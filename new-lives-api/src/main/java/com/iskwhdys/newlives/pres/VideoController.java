package com.iskwhdys.newlives.pres;

import java.util.ArrayList;
import java.util.List;

import com.iskwhdys.newlives.app.delivery.VideoDeliveryService;
import com.iskwhdys.newlives.domain.delivery.TopArchiveEntity;
import com.iskwhdys.newlives.domain.delivery.TopLiveEntity;
import com.iskwhdys.newlives.domain.delivery.TopPremierEntity;
import com.iskwhdys.newlives.domain.delivery.TopScheduleEntity;
import com.iskwhdys.newlives.domain.delivery.TopUploadEntity;
import com.iskwhdys.newlives.domain.youtube.YoutubeVideoEntity;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/video")
public class VideoController {
  @Autowired
  VideoDeliveryService videoDeloveryService;

  @GetMapping("/live")
  public List<TopLiveEntity> getLive() {
    return videoDeloveryService.getLive();
  }

  @GetMapping("/upload")
  public List<TopUploadEntity> getUpload(@RequestParam(required = false) String from,
      @RequestParam(required = false) Integer count) {
    if (StringUtils.isEmpty(from)) {
      return videoDeloveryService.getUpdate();
    }
    if (StringUtils.isNotEmpty(from) && count != null) {
      return videoDeloveryService.getUpdate(from, count);
    }
    return new ArrayList<>();
  }

  @GetMapping("/archive")
  public List<TopArchiveEntity> getArchive(@RequestParam(required = false) String from,
      @RequestParam(required = false) Integer count) {
    if (StringUtils.isEmpty(from)) {
      return videoDeloveryService.getArchive();
    }
    if (StringUtils.isNotEmpty(from) && count != null) {
      return videoDeloveryService.getArchive(from, count);
    }
    return new ArrayList<>();
  }

  @GetMapping("/premier")
  public List<TopPremierEntity> getPremier(@RequestParam(required = false) String from,
      @RequestParam(required = false) Integer count) {
    if (StringUtils.isEmpty(from)) {
      return videoDeloveryService.getPremier();
    }
    if (StringUtils.isNotEmpty(from) && count != null) {
      return videoDeloveryService.getPremier(from, count);
    }
    return new ArrayList<>();
  }

  @GetMapping("/schedule")
  public List<TopScheduleEntity> getSchedule(@RequestParam(required = false) String from,
      @RequestParam(required = false) Integer count) {
    if (StringUtils.isEmpty(from)) {
      return videoDeloveryService.getSchedule();
    }
    if (StringUtils.isNotEmpty(from) && count != null) {
      return videoDeloveryService.getSchedule(from, count);
    }
    return new ArrayList<>();
  }

  @GetMapping("/channel/{id}")
  public List<YoutubeVideoEntity> getSchedule(@PathVariable String id, @RequestParam(required = false) String from,
      @RequestParam(required = false) Integer count) {
    if (StringUtils.isEmpty(from)) {
      return videoDeloveryService.nativeChannelVideo(id);
    }
    if (StringUtils.isNotEmpty(from) && count != null) {
      return videoDeloveryService.nativeChannelVideo(id, from, count);
    }
    return new ArrayList<>();
  }

}
