package com.iskwhdys.newlives.domain.youtube;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface YoutubeChannelRepository
        extends JpaRepository<YoutubeChannelEntity, String>, JpaSpecificationExecutor<YoutubeChannelEntity> {

    List<YoutubeChannelEntity> findByEnabledFalse();

    List<YoutubeChannelEntity> findByEnabledTrue();
}
