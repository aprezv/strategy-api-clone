package com.strategy.api.persistence.users;

import com.strategy.api.base.repositories.ExtendedRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.ManyToOne;
import java.util.List;
import java.util.Optional;

/**

 */
@Repository
public interface UserJpaRepository extends ExtendedRepository<UserEntity, Long> {
    @Query("SELECT u FROM UserEntity u WHERE lower(trim(u.username)) =  lower(trim(:username))")
    Optional<UserEntity> findByUsername(@Param("username") String username);
    Optional<UserEntity> findByRecoveryToken(String recoveryToken);

    @Modifying
    @Query(nativeQuery = true, value = "update users set active = true, verified = true, recovery_token = null  where recovery_token = ?1")
    void activateAccount(@Param("token") String token);

    @Query(nativeQuery = true, value = "select exists(select recovery_token from users where recovery_token = ?1)")
    boolean tokenExist(@Param("token") String token);

    boolean existsUserEntityByUsername(String username);

    @Modifying
    @Query(nativeQuery = true, value = "update users set registered_count = COALESCE(registered_count,0) + ?2 where id = ?1")
    void increaseRegisteredCount(@Param("userId") Long id, @Param("value") int i);

    List<UserEntity> findAllByPasswordIsNull();

    @Query(nativeQuery = true, value = "select registered_count from  users where id = ?1")
    Integer getRegisteredCount(@Param("userId") Long id);

    @Modifying
    @Query(nativeQuery = true, value = "update users set created_by_id = :createdById, last_modified_by_id = :createdById where id = :userId")
    void updateCreatedBy(@Param("userId") Long suId, @Param("createdById") Long coordinatorUserId);
}
