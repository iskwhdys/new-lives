package com.iskwhdys.newlives.app.delivery;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

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
        var list = youtubeVideoRepository.nativeTopLive().stream().map(VideoDeliveryLogic::createTopLive)
                .collect(Collectors.toList());
        topLiveRepository.saveAll(list);
    }

    @Transactional
    public void updateUpload() {
        log.info("Start updateUpload");
        topUploadRepository.deleteAll();
        var data = youtubeVideoRepository.nativeTopUpload();
        if (data.isEmpty()) {
            data = youtubeVideoRepository.nativeUpload(LocalDateTime.now(), 6);
        }
        var list = data.stream().map(VideoDeliveryLogic::createTopUpload).collect(Collectors.toList());
        topUploadRepository.saveAll(list);
    }

    @Transactional
    public void updateArchive() {
        log.info("Start updateArchive");
        topArchiveRepository.deleteAll();
        var list = youtubeVideoRepository.nativeTopArchive().stream().map(VideoDeliveryLogic::createTopArchive)
                .collect(Collectors.toList());
        topArchiveRepository.saveAll(list);
    }

    @Transactional
    public void updatePremier() {
        log.info("Start updatePremier");
        topPremierRepository.deleteAll();
        var list = youtubeVideoRepository.nativePremier(LocalDateTime.now().minusDays(1), 6).stream()
                .map(VideoDeliveryLogic::createTopPremier).collect(Collectors.toList());
        topPremierRepository.saveAll(list);
    }

    @Transactional
    public void updateSchedule() {
        log.info("Start updateSchedule");
        topScheduleRepository.deleteAll();
        var list = youtubeVideoRepository.nativeTopSchedule().stream().map(VideoDeliveryLogic::createTopSchedule)
                .collect(Collectors.toList());
        topScheduleRepository.saveAll(list);

    }

}
