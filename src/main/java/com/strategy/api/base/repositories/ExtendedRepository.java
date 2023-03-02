package com.strategy.api.base.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.io.Serializable;

/**
 * Created on 4/22/18.
 */
@NoRepositoryBean
public interface ExtendedRepository <T, ID extends Serializable> extends JpaRepository<T, ID>,
        JpaSpecificationExecutor<T>, Repository<T, ID>, CrudRepository<T, ID> {
}
