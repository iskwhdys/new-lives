package com.iskwhdys.newlives.domain.delivery;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TopArchiveRepository
                extends JpaRepository<TopArchiveEntity, String>, JpaSpecificationExecutor<TopArchiveEntity> {

        List<TopArchiveEntity> findAllOrderByLiveStartDescIdAsc();

}
