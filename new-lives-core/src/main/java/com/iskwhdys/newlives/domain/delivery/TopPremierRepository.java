package com.iskwhdys.newlives.domain.delivery;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TopPremierRepository
        extends JpaRepository<TopPremierEntity, String>, JpaSpecificationExecutor<TopPremierEntity> {
    List<TopPremierEntity> findAllOrderByLiveScheduleAscIdAsc();

}
