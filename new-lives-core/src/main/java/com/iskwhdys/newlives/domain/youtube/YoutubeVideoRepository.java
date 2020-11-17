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

        @Query(value = "select * from youtube_video yv  where yv.channel in (select youtube from nijisanji_liver)  and yv.type = 'live' and yv.status = 'stream'", nativeQuery = true)
        List<YoutubeVideoEntity> nativeByLiveStreams();

}
