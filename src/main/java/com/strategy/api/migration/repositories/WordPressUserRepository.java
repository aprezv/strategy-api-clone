package com.strategy.api.migration.repositories;

import com.strategy.api.migration.entities.WordpressUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.stream.Stream;

/**
 * Created on 6/20/22.
 */
public interface WordPressUserRepository extends JpaRepository<WordpressUserEntity, Long> {
    Stream<WordpressUserEntity> streamAllBy();
}
