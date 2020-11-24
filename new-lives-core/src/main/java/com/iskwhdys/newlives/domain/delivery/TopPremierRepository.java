package com.iskwhdys.newlives.domain.delivery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TopPremierRepository
                extends JpaRepository<TopPremierEntity, String>, JpaSpecificationExecutor<TopPremierEntity> {

}
