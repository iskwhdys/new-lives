package com.iskwhdys.newlives.app.delivery;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import com.iskwhdys.newlives.domain.delivery.TopArchiveEntity;
import com.iskwhdys.newlives.domain.delivery.TopArchiveRepository;
import com.iskwhdys.newlives.domain.delivery.TopLiveEntity;
import com.iskwhdys.newlives.domain.delivery.TopLiveRepository;
import com.iskwhdys.newlives.domain.delivery.TopUploadEntity;
import com.iskwhdys.newlives.domain.delivery.TopUploadRepository;
import com.iskwhdys.newlives.domain.youtube.YoutubeVideoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service
public class VideoDeloveryService {

    @Autowired
    YoutubeVideoRepository youtubeVideoRepository;

    @Autowired
    TopLiveRepository topLiveRepository;
    @Autowired
    TopUploadRepository topUploadRepository;
    @Autowired
    TopArchiveRepository topArchiveRepository;

    public List<TopLiveEntity> getLive() {
        return topLiveRepository.findAll();
    }

    public List<TopUploadEntity> getUpdate() {
        return topUploadRepository.findAll();
    }

    public List<TopArchiveEntity> getArchive() {
        return topArchiveRepository.findAll();
    }

    public List<TopUploadEntity> getUpdate(String from, int count) {
        LocalDateTime time = LocalDateTime.parse(from, DateTimeFormatter.ISO_DATE_TIME);
        return youtubeVideoRepository.nativeUpload(time, count).stream().map(VideoDeliveryLogic::createTopUpload)
                .collect(Collectors.toList());
    }

    public List<TopUploadEntity> getArchive(String from, int count) {
        LocalDateTime time = LocalDateTime.parse(from, DateTimeFormatter.ISO_DATE_TIME);
        return youtubeVideoRepository.nativeUpload(time, count).stream().map(VideoDeliveryLogic::createTopUpload)
                .collect(Collectors.toList());
    }
}
