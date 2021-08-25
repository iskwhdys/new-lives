package com.iskwhdys.newlives.app.youtube;

import java.time.LocalDateTime;

import com.iskwhdys.newlives.domain.youtube.YoutubeVideoEntity;

public class YoutubeVideoLogic {

    public static final String TYPE_NEW = "new";
    public static final String TYPE_UPLOAD = "upload";
    public static final String TYPE_LIVE = "live";
    public static final String TYPE_PREMIER = "premier";
    public static final String TYPE_ERROR = "error";

    public static final String STATUS_RESERVE = "reserve";
    public static final String STATUS_STREAM = "stream";
    public static final String STATUS_ARCHIVE = "archive";
    public static final String STATUS_NONE = "none";

    public static final String UPLOAD_STATUS_UPLOADED = "uploaded";
    public static final String UPLOAD_STATUS_PROCESSED = "processed";

    public static final String PRIVACY_STATUS_UNLISTED = "unlisted"; // メン限

    private YoutubeVideoLogic() {
    }

    public static YoutubeVideoEntity createNewVideo() {
        var v = new YoutubeVideoEntity();
        v.setType(TYPE_NEW);
        v.setStatus(STATUS_NONE);
        v.setCreateDate(LocalDateTime.now());
        return v;
    }

    public static boolean isTypeNew(YoutubeVideoEntity v) {
        return TYPE_NEW.equals(v.getType());
    }

    public static boolean isTypeUpload(YoutubeVideoEntity v) {
        return TYPE_UPLOAD.equals(v.getType());
    }

    public static boolean isTypeLive(YoutubeVideoEntity v) {
        return TYPE_LIVE.equals(v.getType());
    }

    public static boolean isTypePremier(YoutubeVideoEntity v) {
        return TYPE_PREMIER.equals(v.getType());
    }

    public static boolean isStatusReserve(YoutubeVideoEntity v) {
        return STATUS_RESERVE.equals(v.getStatus());
    }

    public static boolean isStatusStream(YoutubeVideoEntity v) {
        return STATUS_STREAM.equals(v.getStatus());
    }

    public static boolean isStatusArchive(YoutubeVideoEntity v) {
        return STATUS_ARCHIVE.equals(v.getStatus());
    }

    public static String updateType(YoutubeVideoEntity v) {
        if (v.getLiveSchedule() == null && v.getLiveStart() == null && v.getLiveEnd() == null) {
            return TYPE_UPLOAD;
        }

        // 公開中/公開後のプレミア
        if (v.getPublished() != null && v.getPublished().equals(v.getLiveStart())) {
            return TYPE_PREMIER;
        }

        // 公開前のプレミア(アップロード処理後)
        if (v.getLiveSchedule() != null && v.getLiveStart() == null && v.getDuration() != 0) {
            return TYPE_PREMIER;
        }

        // 公開前のプレミア(アップロード処理前)
        // アップロード処理後に再度判断する

        return TYPE_LIVE;
    }

    public static String updateStatus(YoutubeVideoEntity v) {
        if (TYPE_UPLOAD.equals(v.getType())) {
            return STATUS_NONE;
        }
        if (v.getLiveEnd() != null) {
            return STATUS_ARCHIVE;
        }
        if (v.getLiveEnd() == null && v.getLiveStart() != null) {
            if (TYPE_PREMIER.equals(v.getType())
                    && LocalDateTime.now().isAfter(v.getLiveStart().plusSeconds(v.getDuration()))) {
                // 稀にliveEndが設定されないプレミア公開がある。それらは開始予定+動画時間経過後アーカイブとする。
                return STATUS_ARCHIVE;
            } else {
                return STATUS_STREAM;
            }
        }
        return STATUS_RESERVE;
    }

}
