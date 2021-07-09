package com.iskwhdys.newlives.pres;

import java.util.List;
import java.util.stream.Collectors;

import com.iskwhdys.newlives.domain.liver.LiverEntity;
import com.iskwhdys.newlives.domain.liver.LiverRepository;
import com.iskwhdys.newlives.domain.liver.LiverTagRepository;

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
@RequestMapping("/api/liver")
public class LiverController {
  @Autowired
  LiverRepository liverRepository;
  @Autowired
  LiverTagRepository liverTagRepository;

  @GetMapping("")
  public List<LiverEntity> getLiver(@RequestParam(required = false) String channelId) {
    if (StringUtils.isEmpty(channelId)) {
      return liverRepository.findAll().stream().map(this::getLiverData).collect(Collectors.toList());
    } else {
      return List.of(getLiverDataForChannelId(channelId));
    }
  }

  @GetMapping("/{id}")
  public LiverEntity getLiverFromId(@PathVariable String id) {
    return getLiverDataForId(id);
  }

  private LiverResponse getLiverDataForChannelId(String channelId) {
    var l = liverRepository.findByYoutube(channelId);
    if (l.isEmpty())
      return null;
    return getLiverData(l.get());
  }

  private LiverResponse getLiverDataForId(String id) {
    var l = liverRepository.findById(id);
    if (l.isEmpty())
      return null;
    return getLiverData(l.get());
  }

  private LiverResponse getLiverData(LiverEntity l) {
    var tags = liverTagRepository.findByIdId(l.getId());
    var r = new LiverResponse();
    r.setCompany(l.getCompany());
    r.setDebut(l.getDebut());
    r.setEndDate(l.getEndDate());
    r.setGroup(l.getGroup());
    r.setIcon(l.getIcon());
    r.setId(l.getId());
    r.setKana(l.getKana());
    r.setName(l.getName());
    r.setOfficial(l.getOfficial());
    r.setStartDate(l.getStartDate());
    r.setTwitter(l.getTwitter());
    r.setWiki(l.getWiki());
    r.setYoutube(l.getYoutube());

    var liverTag = tags.stream().filter(t -> t.getId().getId().equals(r.getId())).collect(Collectors.toList());
    for (var tag : liverTag) {
      switch (tag.getId().getKey()) {
        case "youtube":
          r.setYoutube2(tag.getId().getValue());
          break;
        case "twitter":
          r.setTwitter2(tag.getId().getValue());
          break;
        default:
          break;
      }
    }

    return r;
  }

}
