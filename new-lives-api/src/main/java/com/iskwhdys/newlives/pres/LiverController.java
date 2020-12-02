package com.iskwhdys.newlives.pres;

import java.util.List;

import com.iskwhdys.newlives.domain.liver.LiverEntity;
import com.iskwhdys.newlives.domain.liver.LiverRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/liver")
public class LiverController {
  @Autowired
  LiverRepository liverRepository;

  @GetMapping("")
  public List<LiverEntity> getLiver() {
    return liverRepository.findAll();
  }

  @GetMapping("/{id}")
  public LiverEntity getLiver(@PathVariable String id) {
    return liverRepository.findById(id).orElse(null);
  }

}
