package com.iskwhdys.newlives.app.delivery;

import java.util.List;

import com.iskwhdys.newlives.app.youtube.YoutubeVideoLogic;
import com.iskwhdys.newlives.domain.delivery.TopArchiveRepository;
import com.iskwhdys.newlives.domain.delivery.TopLiveEntity;
import com.iskwhdys.newlives.domain.delivery.TopLiveRepository;
import com.iskwhdys.newlives.domain.delivery.TopUploadEntity;
import com.iskwhdys.newlives.domain.delivery.TopUploadRepository;
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
    @Autowired
    TopUploadRepository topUploadRepository;
    @Autowired
    TopArchiveRepository topArchiveRepository;

    @Transactional
    public void updateLive() {
        log.info("Start updateLive");

        topLiveRepository.deleteAll();
        for (var v : youtubeVideoRepository.nativeTopLive()) {
            var t = new TopLiveEntity();
            t.setChannel(v.getChannel());
            t.setDislikes(v.getDislikes());
            t.setId(v.getId());
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

        topUploadRepository.deleteAll();
        var data = youtubeVideoRepository.nativeTopPremier();
        data.addAll(youtubeVideoRepository.nativeTopUpload());

        for (var v : data) {
            var t = new TopUploadEntity();
            t.setChannel(v.getChannel());
            t.setDislikes(v.getDislikes());
            t.setId(v.getId());
            t.setLikes(v.getLikes());
            if (YoutubeVideoLogic.isTypeUpload(v)) {
                t.setUploadDate(v.getPublished());
            } else {
                t.setUploadDate(v.getLiveSchedule());
            }
            t.setThumbnailUrl(v.getThumbnailUrl());
            t.setTitle(v.getTitle());
            t.setViews(v.getViews());

            topUploadRepository.save(t);
        }
    }

}
