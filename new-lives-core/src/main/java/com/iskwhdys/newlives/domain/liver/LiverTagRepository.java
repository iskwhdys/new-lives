package com.iskwhdys.newlives.domain.liver;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LiverTagRepository
        extends JpaRepository<LiverTagEntity, String>, JpaSpecificationExecutor<LiverEntity> {

    List<LiverTagEntity> findByIdKeyAndIdValue(String key, String value);

}
