package com.strategy.api.persistence.respositories;

import com.strategy.api.persistence.padron.PhotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created on 6/14/22.
 */
@Repository
public interface PhotoJpaRepository extends JpaRepository<PhotoEntity, String> {
}
