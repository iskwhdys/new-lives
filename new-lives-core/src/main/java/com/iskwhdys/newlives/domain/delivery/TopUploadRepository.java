package com.iskwhdys.newlives.domain.delivery;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TopUploadRepository
        extends JpaRepository<TopUploadEntity, String>, JpaSpecificationExecutor<TopUploadEntity> {
    List<TopUploadEntity> findAllOrderByPublishedtDescIdAsc();
}
