package com.iskwhdys.newlives.domain.delivery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TopScheduleRepository
                extends JpaRepository<TopScheduleEntity, String>, JpaSpecificationExecutor<TopScheduleEntity> {

}
