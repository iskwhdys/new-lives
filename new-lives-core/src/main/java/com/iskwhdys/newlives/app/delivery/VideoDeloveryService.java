package com.iskwhdys.newlives.app.delivery;

import com.iskwhdys.newlives.domain.delivery.TopLiveEntity;
import com.iskwhdys.newlives.domain.delivery.TopLiveRepository;
import com.iskwhdys.newlives.domain.youtube.YoutubeVideoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.var;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class VideoDeloveryService {

    @Autowired
    YoutubeVideoRepository youtubeVideoRepository;

    @Autowired
    TopLiveRepository topLiveRepository;

    @Transactional
    public void updateLive() {
        log.info("Start updateLive");

        topLiveRepository.deleteAll();
        for (var v : youtubeVideoRepository.nativeByLiveStreams()) {
            var t = new TopLiveEntity();
            t.setId(v.getId());
            t.setChannel(v.getChannel());
            t.setDislikes(v.getDislikes());
            t.setLikes(v.getLikes());
            t.setLiveStart(v.getLiveStart());
            t.setLiveViews(v.getLiveViews());
            t.setThumbnailUrl(v.getThumbnailUrl());
            t.setTitle(v.getTitle());
            topLiveRepository.save(t);
        }
    }

    @Transactional
    public void updateUpload() {
        log.info("Start updateUpload");

        topLiveRepository.deleteAll();
        for (var v : youtubeVideoRepository.nativeByLiveStreams()) {
            var t = new TopLiveEntity();
            t.setId(v.getId());
            t.setChannel(v.getChannel());
            t.setDislikes(v.getDislikes());
            t.setLikes(v.getLikes());
            t.setLiveStart(v.getLiveStart());
            t.setLiveViews(v.getLiveViews());
            t.setThumbnailUrl(v.getThumbnailUrl());
            t.setTitle(v.getTitle());
            topLiveRepository.save(t);
        }
    }

}
