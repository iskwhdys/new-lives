package com.iskwhdys.newlives.domain.youtube;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface YoutubeChannelRepository
                extends JpaRepository<YoutubeChannelEntity, String>, JpaSpecificationExecutor<YoutubeChannelEntity> {

}
