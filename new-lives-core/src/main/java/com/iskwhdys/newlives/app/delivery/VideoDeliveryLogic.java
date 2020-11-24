package com.iskwhdys.newlives.app.delivery;

import com.iskwhdys.newlives.app.youtube.YoutubeVideoLogic;
import com.iskwhdys.newlives.domain.delivery.TopArchiveEntity;
import com.iskwhdys.newlives.domain.delivery.TopLiveEntity;
import com.iskwhdys.newlives.domain.delivery.TopPremierEntity;
import com.iskwhdys.newlives.domain.delivery.TopScheduleEntity;
import com.iskwhdys.newlives.domain.delivery.TopUploadEntity;
import com.iskwhdys.newlives.domain.youtube.YoutubeVideoEntity;

public class VideoDeliveryLogic {

    private VideoDeliveryLogic() {
    }

    public static TopLiveEntity createTopLive(YoutubeVideoEntity v) {
        var t = new TopLiveEntity();
        t.setChannel(v.getChannel());
        t.setDislikes(v.getDislikes());
        t.setId(v.getId());
        t.setLikes(v.getLikes());
        t.setLiveStart(v.getLiveStart());
        t.setLiveViews(v.getLiveViews());
        t.setThumbnailUrl(v.getThumbnailUrl());
        t.setTitle(v.getTitle());
        return t;
    }

    public static TopUploadEntity createTopUpload(YoutubeVideoEntity v) {
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
        return t;
    }

    public static TopArchiveEntity createTopArchive(YoutubeVideoEntity v) {
        var t = new TopArchiveEntity();
        t.setChannel(v.getChannel());
        t.setDislikes(v.getDislikes());
        t.setId(v.getId());
        t.setLikes(v.getLikes());
        t.setLiveStart(v.getLiveStart());
        t.setThumbnailUrl(v.getThumbnailUrl());
        t.setTitle(v.getTitle());
        t.setViews(v.getViews());
        return t;
    }

    public static TopPremierEntity createTopPremier(YoutubeVideoEntity v) {
        var t = new TopPremierEntity();
        t.setChannel(v.getChannel());
        t.setDislikes(v.getDislikes());
        t.setDuration(v.getDuration());
        t.setId(v.getId());
        t.setLikes(v.getLikes());
        t.setLiveSchedule(v.getLiveSchedule());
        t.setThumbnailUrl(v.getThumbnailUrl());
        t.setTitle(v.getTitle());
        return t;
    }

    public static TopScheduleEntity createTopSchedule(YoutubeVideoEntity v) {
        var t = new TopScheduleEntity();
        t.setChannel(v.getChannel());
        t.setDislikes(v.getDislikes());
        t.setId(v.getId());
        t.setLikes(v.getLikes());
        t.setLiveSchedule(v.getLiveSchedule());
        t.setThumbnailUrl(v.getThumbnailUrl());
        t.setTitle(v.getTitle());
        return t;
    }
}
