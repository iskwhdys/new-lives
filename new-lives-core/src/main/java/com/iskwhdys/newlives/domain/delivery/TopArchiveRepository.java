package com.iskwhdys.newlives.domain.delivery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TopArchiveRepository
        extends JpaRepository<TopArchiveEntity, String>, JpaSpecificationExecutor<TopArchiveEntity> {

}
