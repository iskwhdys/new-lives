package com.iskwhdys.newlives.domain.youtube;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface YoutubeChannelRepository
        extends JpaRepository<YoutubeChannelEntity, String>, JpaSpecificationExecutor<YoutubeChannelEntity> {

    List<YoutubeChannelEntity> findByEndDateIsNullOrEndDateAfter(LocalDate date);

    List<YoutubeChannelEntity> findByEnabledTrueAndEndDateIsNullOrEndDateAfter(LocalDate date);

    List<YoutubeChannelEntity> findByEnabledFalseAndEndDateIsNullOrEndDateAfter(LocalDate date);

}
