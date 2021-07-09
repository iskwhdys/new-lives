package com.iskwhdys.newlives.app.delivery;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import com.iskwhdys.newlives.domain.delivery.TopArchiveEntity;
import com.iskwhdys.newlives.domain.delivery.TopArchiveRepository;
import com.iskwhdys.newlives.domain.delivery.TopLiveEntity;
import com.iskwhdys.newlives.domain.delivery.TopLiveRepository;
import com.iskwhdys.newlives.domain.delivery.TopPremierEntity;
import com.iskwhdys.newlives.domain.delivery.TopPremierRepository;
import com.iskwhdys.newlives.domain.delivery.TopScheduleEntity;
import com.iskwhdys.newlives.domain.delivery.TopScheduleRepository;
import com.iskwhdys.newlives.domain.delivery.TopUploadEntity;
import com.iskwhdys.newlives.domain.delivery.TopUploadRepository;
import com.iskwhdys.newlives.domain.youtube.YoutubeVideoEntity;
import com.iskwhdys.newlives.domain.youtube.YoutubeVideoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VideoDeliveryService {

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

    public List<TopLiveEntity> getLive() {
        return topLiveRepository.findAllOrderByLiveStartDescIdAsc();
    }

    public List<TopUploadEntity> getUpdate() {
        return topUploadRepository.findAllOrderByPublishedtDescIdAsc();
    }

    public List<TopArchiveEntity> getArchive() {
        return topArchiveRepository.findAllOrderByLiveStartDescIdAsc();
    }

    public List<TopPremierEntity> getPremier() {
        return topPremierRepository.findAllOrderByLiveScheduleAscIdAsc();
    }

    public List<TopScheduleEntity> getSchedule() {
        return topScheduleRepository.findAllOrderByLiveScheduleAscIdAsc();
    }

    public List<TopUploadEntity> getUpdate(String from, int count) {
        LocalDateTime time = LocalDateTime.parse(from, DateTimeFormatter.ISO_DATE_TIME);
        return youtubeVideoRepository.nativeUpload(time, count).stream().map(VideoDeliveryLogic::createTopUpload)
                .collect(Collectors.toList());
    }

    public List<TopArchiveEntity> getArchive(String from, int count) {
        LocalDateTime time = LocalDateTime.parse(from, DateTimeFormatter.ISO_DATE_TIME);
        return youtubeVideoRepository.nativeArchive(time, count).stream().map(VideoDeliveryLogic::createTopArchive)
                .collect(Collectors.toList());
    }

    public List<TopPremierEntity> getPremier(String from, int count) {
        LocalDateTime time = LocalDateTime.parse(from, DateTimeFormatter.ISO_DATE_TIME);
        return youtubeVideoRepository.nativePremier(time, count).stream().map(VideoDeliveryLogic::createTopPremier)
                .collect(Collectors.toList());
    }

    public List<TopScheduleEntity> getSchedule(String from, int count) {
        LocalDateTime time = LocalDateTime.parse(from, DateTimeFormatter.ISO_DATE_TIME);
        return youtubeVideoRepository.nativeTopSchedule(time, count).stream().map(VideoDeliveryLogic::createTopSchedule)
                .collect(Collectors.toList());
    }

    public List<YoutubeVideoEntity> nativeChannelVideo(String id) {
        return youtubeVideoRepository.nativeChannelVideo(id);
    }

    public List<YoutubeVideoEntity> nativeChannelVideo(String id, String from, int count) {

        LocalDateTime time = LocalDateTime.parse(from, DateTimeFormatter.ISO_DATE_TIME);
        return youtubeVideoRepository.nativeChannelVideo(id, time, count);
    }
}
