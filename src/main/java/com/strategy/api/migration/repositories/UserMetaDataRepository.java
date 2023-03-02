package com.strategy.api.migration.repositories;

import com.strategy.api.migration.entities.UserMetaDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

/**
 * Created on 6/20/22.
 */
public interface UserMetaDataRepository extends JpaRepository<UserMetaDataEntity, Long> {

    @Query(nativeQuery = true, value = "select * from wp7i_usermeta where meta_value like '%coordinador%'")
    List<UserMetaDataEntity> getCoordinadores();

    @Query(nativeQuery = true, value = "select count (1) from wp7i_usermeta where meta_value like '%coordinador%'")
    long countCoordinadores();

    @Query(nativeQuery = true, value = "select * from wp7i_usermeta where meta_value like '%coordinador%' order by umeta_id")
    Stream<UserMetaDataEntity> streamCoordinadores();
}
