package com.iskwhdys.newlives.domain.delivery;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TopLiveRepository
        extends JpaRepository<TopLiveEntity, String>, JpaSpecificationExecutor<TopLiveEntity> {
    List<TopLiveEntity> findAllByOrderByLiveStartDescIdAsc();

}
