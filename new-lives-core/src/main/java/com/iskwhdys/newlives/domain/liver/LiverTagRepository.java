package com.iskwhdys.newlives.domain.liver;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LiverTagRepository
        extends JpaRepository<LiverTagEntity, String>, JpaSpecificationExecutor<LiverEntity> {

    Optional<LiverTagEntity> findByIdKeyAndValue(String key, String value);

}
