package com.iskwhdys.newlives.domain.youtube;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface YoutubeVideoRepository
                extends JpaRepository<YoutubeVideoEntity, String>, JpaSpecificationExecutor<YoutubeVideoEntity> {

        List<YoutubeVideoEntity> findByEnabledTrueAndIdNotIn(List<String> id);

        List<YoutubeVideoEntity> findByEnabledTrueAndTypeEquals(String type);

        List<YoutubeVideoEntity> findByEnabledTrueAndStatusEquals(String status);

        List<YoutubeVideoEntity> findByEnabledTrueAndStatusEqualsAndLiveScheduleBetween(String status,
                        LocalDateTime since, LocalDateTime until);

        static final String NATIVE_TOP_BASE = "select * from youtube_video yv where yv.enabled = true and yv.privacy_status = 'public' ";
        static final String NATIVE_TOP_LIVE = "    and yv.status = 'stream' order by live_start desc, id";
        static final String NATIVE_TOP_UPLOAD = "  and (yv.type = 'upload' or (yv.type = 'premier' and yv.status = 'archive')) and published > current_timestamp - interval '1 day' order by published desc, id";
        static final String NATIVE_UPLOAD = "      and (yv.type = 'upload' or (yv.type = 'premier' and yv.status = 'archive')) and published < ?1 order by published desc, id limit ?2";
        static final String NATIVE_TOP_ARCHIVE = " and yv.type = 'live' and yv.status = 'archive'                     order by live_start desc, id limit 30";
        static final String NATIVE_ARCHIVE = "     and yv.type = 'live' and yv.status = 'archive' and live_start < ?1 order by live_start desc, id limit ?2";
        static final String NATIVE_TOP_PREMIER = " and yv.type = 'premier' and yv.status = 'reserve' and live_schedule > ?1 order by live_schedule asc, id limit ?2";
        static final String NATIVE_SCHEDULE_TOP = "and yv.type = 'live'    and yv.status = 'reserve' and live_schedule between current_timestamp - interval '1 day' and current_timestamp + interval '3 day' order by live_schedule asc, id";
        static final String NATIVE_SCHEDULE = "    and yv.type = 'live'    and yv.status = 'reserve' and live_schedule > ?1 order by live_schedule asc, id limit ?2";

        @Query(value = NATIVE_TOP_BASE + NATIVE_TOP_LIVE, nativeQuery = true)
        List<YoutubeVideoEntity> nativeTopLive();

        @Query(value = NATIVE_TOP_BASE + NATIVE_TOP_UPLOAD, nativeQuery = true)
        List<YoutubeVideoEntity> nativeTopUpload();

        @Query(value = NATIVE_TOP_BASE + NATIVE_UPLOAD, nativeQuery = true)
        List<YoutubeVideoEntity> nativeUpload(LocalDateTime from, int count);

        @Query(value = NATIVE_TOP_BASE + NATIVE_TOP_ARCHIVE, nativeQuery = true)
        List<YoutubeVideoEntity> nativeTopArchive();

        @Query(value = NATIVE_TOP_BASE + NATIVE_ARCHIVE, nativeQuery = true)
        List<YoutubeVideoEntity> nativeArchive(LocalDateTime from, int count);

        @Query(value = NATIVE_TOP_BASE + NATIVE_TOP_PREMIER, nativeQuery = true)
        List<YoutubeVideoEntity> nativePremier(LocalDateTime from, int count);

        @Query(value = NATIVE_TOP_BASE + NATIVE_SCHEDULE_TOP, nativeQuery = true)
        List<YoutubeVideoEntity> nativeTopSchedule();

        @Query(value = NATIVE_TOP_BASE + NATIVE_SCHEDULE, nativeQuery = true)
        List<YoutubeVideoEntity> nativeTopSchedule(LocalDateTime from, int count);

        @Query(value = "select * from youtube_video yv where yv.privacy_status = 'public' and yv.channel = ?1 order by published desc limit 10", nativeQuery = true)
        List<YoutubeVideoEntity> nativeChannelVideo(String channelId);

        @Query(value = "select * from youtube_video yv where yv.privacy_status = 'public' and yv.channel = ?1 and published < ?2 order by published desc limit ?3", nativeQuery = true)
        List<YoutubeVideoEntity> nativeChannelVideo(String channelId, LocalDateTime from, int count);
}
