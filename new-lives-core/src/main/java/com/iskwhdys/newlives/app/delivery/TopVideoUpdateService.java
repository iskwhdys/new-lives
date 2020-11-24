package com.iskwhdys.newlives.app.delivery;

import java.time.LocalDateTime;

import com.iskwhdys.newlives.domain.delivery.TopArchiveRepository;
import com.iskwhdys.newlives.domain.delivery.TopLiveRepository;
import com.iskwhdys.newlives.domain.delivery.TopPremierRepository;
import com.iskwhdys.newlives.domain.delivery.TopScheduleRepository;
import com.iskwhdys.newlives.domain.delivery.TopUploadRepository;
import com.iskwhdys.newlives.domain.youtube.YoutubeVideoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TopVideoUpdateService {

    @Autowired
    YoutubeVideoRepository youtubeVideoRepository;

    @Autowired
    TopLiveRepository topLiveRepository;
    @Autowired
    TopUploadRepository topUploadRepository;
    @Autowired
    TopArchiveRepository topArchiveRepository;
    @Autowired
    TopPremierRepository topPremierRepository;
    @Autowired
    TopScheduleRepository topScheduleRepository;

    @Transactional
    public void updateLive() {
        log.info("Start updateLive");
        topLiveRepository.deleteAll();
        for (var v : youtubeVideoRepository.nativeTopLive()) {
            topLiveRepository.save(VideoDeliveryLogic.createTopLive(v));
        }
    }

    @Transactional
    public void updateUpload() {
        log.info("Start updateUpload");
        topUploadRepository.deleteAll();
        var data = youtubeVideoRepository.nativeTopUpload();
        if (data.isEmpty()) {
            data = youtubeVideoRepository.nativeUpload(LocalDateTime.now(), 6);
        }
        for (var v : data) {
            topUploadRepository.save(VideoDeliveryLogic.createTopUpload(v));
        }

    }

    @Transactional
    public void updateArchive() {
        log.info("Start updateArchive");
        topArchiveRepository.deleteAll();
        for (var v : youtubeVideoRepository.nativeTopArchive()) {
            topArchiveRepository.save(VideoDeliveryLogic.createTopArchive(v));
        }
    }

    @Transactional
    public void updatePremier() {
        log.info("Start updatePremier");
        topPremierRepository.deleteAll();
        for (var v : youtubeVideoRepository.nativePremier(LocalDateTime.now().minusDays(1), 6)) {
            topPremierRepository.save(VideoDeliveryLogic.createTopPremier(v));
        }
    }

    @Transactional
    public void updateSchedule() {
        log.info("Start updateSchedule");
        topScheduleRepository.deleteAll();
        for (var v : youtubeVideoRepository.nativeTopSchedule()) {
            topScheduleRepository.save(VideoDeliveryLogic.createTopSchedule(v));
        }
    }

}
