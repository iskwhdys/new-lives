package com.iskwhdys.newlives.pres;

import java.time.LocalDate;
import java.util.List;

import com.iskwhdys.newlives.domain.youtube.YoutubeChannelEntity;
import com.iskwhdys.newlives.domain.youtube.YoutubeChannelRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/channel")
public class ChannelController {
  @Autowired
  YoutubeChannelRepository youtubeChannelRepository;

  @GetMapping("")
  public List<YoutubeChannelEntity> getChannel() {
    return youtubeChannelRepository.findByEndDateIsNullOrEndDateAfter(LocalDate.now());
  }

  @GetMapping("/{id}")
  public YoutubeChannelEntity getChannel(@PathVariable String id) {
    return youtubeChannelRepository.findById(id).orElse(null);
  }

}
