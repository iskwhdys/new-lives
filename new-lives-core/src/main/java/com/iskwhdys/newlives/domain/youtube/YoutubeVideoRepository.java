package com.iskwhdys.newlives.domain.youtube;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface YoutubeVideoRepository
        extends JpaRepository<YoutubeVideoEntity, String>, JpaSpecificationExecutor<YoutubeVideoEntity> {

    List<YoutubeVideoEntity> findByEnabledTrueAndIdNotIn(List<String> id);

    List<YoutubeVideoEntity> findByEnabledTrueAndTypeEquals(String type);

}
