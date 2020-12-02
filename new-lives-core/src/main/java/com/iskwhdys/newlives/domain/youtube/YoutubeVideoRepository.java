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

        static final String NATIVE_TOP_BASE = "select * from youtube_video yv where yv.privacy_status = 'public' and yv.channel in (select youtube from nijisanji_liver) ";
        static final String NATIVE_TOP_LIVE = "    and yv.type = 'live' and yv.status = 'stream' order by live_start desc";
        static final String NATIVE_TOP_UPLOAD = "  and (yv.type = 'upload' or (yv.type = 'premier' and yv.status = 'archive')) and published > current_timestamp - interval '1 day' order by published desc";
        static final String NATIVE_UPLOAD = "      and (yv.type = 'upload' or (yv.type = 'premier' and yv.status = 'archive')) and published < ?1 order by published desc limit ?2";
        static final String NATIVE_TOP_ARCHIVE = " and yv.type = 'live' and yv.status = 'archive'                     order by live_start desc limit 30";
        static final String NATIVE_ARCHIVE = "     and yv.type = 'live' and yv.status = 'archive' and live_start < ?1 order by live_start desc limit ?2";
        static final String NATIVE_TOP = "         and yv.type = 'premier' and yv.status = 'reserve' and live_schedule > ?1 order by live_schedule desc limit ?2";
        static final String NATIVE_SCHEDULE_TOP = "and yv.type = 'live'    and yv.status = 'reserve' and live_schedule between current_timestamp - interval '1 day' and current_timestamp + interval '3 day' order by live_schedule desc ";
        static final String NATIVE_SCHEDULE = "    and yv.type = 'live'    and yv.status = 'reserve' and live_schedule > ?1 order by live_schedule desc limit ?2";

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

        @Query(value = NATIVE_TOP_BASE + NATIVE_TOP, nativeQuery = true)
        List<YoutubeVideoEntity> nativePremier(LocalDateTime from, int count);

        @Query(value = NATIVE_TOP_BASE + NATIVE_SCHEDULE_TOP, nativeQuery = true)
        List<YoutubeVideoEntity> nativeTopSchedule();

        @Query(value = NATIVE_TOP_BASE + NATIVE_SCHEDULE, nativeQuery = true)
        List<YoutubeVideoEntity> nativeTopSchedule(LocalDateTime from, int count);
}
