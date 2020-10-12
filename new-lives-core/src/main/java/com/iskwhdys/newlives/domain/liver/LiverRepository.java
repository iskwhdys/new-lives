package com.iskwhdys.newlives.domain.liver;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LiverRepository extends JpaRepository<LiverEntity, String>, JpaSpecificationExecutor<LiverEntity> {

    Optional<LiverEntity> findById(String id);

    Optional<LiverEntity> findByYoutube(String youtubeId);

}
