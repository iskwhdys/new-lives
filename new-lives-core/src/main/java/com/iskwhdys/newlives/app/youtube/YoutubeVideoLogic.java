package com.iskwhdys.newlives.app.youtube;

import com.iskwhdys.newlives.domain.youtube.YoutubeVideoEntity;

public class YoutubeVideoLogic {

    public static final String TYPE_NEW = "new";
    public static final String TYPE_UPLOAD = "upload";
    public static final String TYPE_LIVE = "live";
    public static final String TYPE_PREMIER = "premier";
    public static final String STATUS_RESERVE = "reserve";
    public static final String STATUS_STREAMING = "streaming";
    public static final String STATUS_ARCHIVE = "archive";

    private YoutubeVideoLogic() {
    }

    public static YoutubeVideoEntity createNewVideo() {
        var video = new YoutubeVideoEntity();
        video.setType(TYPE_NEW);
        return video;
    }

    public static boolean isNew(YoutubeVideoEntity video) {
        return TYPE_NEW.equals(video.getType());
    }

    public static boolean isUpload(YoutubeVideoEntity video) {
        return TYPE_UPLOAD.equals(video.getType());
    }

    public static boolean isLive(YoutubeVideoEntity video) {
        return TYPE_LIVE.equals(video.getType());
    }

    public static boolean isPremier(YoutubeVideoEntity video) {
        return TYPE_PREMIER.equals(video.getType());
    }

    public static boolean isReserve(YoutubeVideoEntity video) {
        return STATUS_RESERVE.equals(video.getStatus());
    }

    public static boolean isStreaming(YoutubeVideoEntity video) {
        return STATUS_STREAMING.equals(video.getStatus());
    }

    public static boolean isArchive(YoutubeVideoEntity video) {
        return STATUS_ARCHIVE.equals(video.getStatus());
    }

    public static void updateTypeAndStatus(YoutubeVideoEntity video) {

    }

}
