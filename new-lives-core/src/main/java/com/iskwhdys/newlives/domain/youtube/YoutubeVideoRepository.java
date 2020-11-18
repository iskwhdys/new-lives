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

        public static final String NATIVE_TOP_BASE = "select * from youtube_video yv  where yv.channel in (select youtube from nijisanji_liver) ";

        @Query(value = NATIVE_TOP_BASE + " and yv.type = 'live' and yv.status = 'stream'", nativeQuery = true)
        List<YoutubeVideoEntity> nativeTopLive();

        @Query(value = NATIVE_TOP_BASE
                        + " and yv.type = 'upload' and published > current_timestamp - interval '2 day'", nativeQuery = true)
        List<YoutubeVideoEntity> nativeTopUpload();

        @Query(value = NATIVE_TOP_BASE
                        + " and yv.type = 'premier' and yv.status = 'archive' and live_start > current_timestamp - interval '2 day'", nativeQuery = true)
        List<YoutubeVideoEntity> nativeTopPremier();
}
