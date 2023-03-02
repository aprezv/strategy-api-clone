package com.strategy.api.base.services;

import com.strategy.api.base.exceptions.EntityNotFoundException;
import com.strategy.api.base.model.jpa.WithId;
import com.strategy.api.base.repositories.ExtendedRepository;
import com.strategy.api.base.specifications.SpecificationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created on 4/22/18.
 */
@Service
public abstract class BaseService<T extends WithId, ID extends Serializable> {

    protected Class<T> clazz;

    @Autowired
    private MessageSource messageSource;

    public BaseService(Class<T> clazz) {
        this.clazz = clazz;
    }

    public Class<T> getClazz() {
        return this.clazz;
    }

    public Page<T> search(final Map<String, Object> queryMap, final Pageable pageable) {
        Specification<T> specification = new SpecificationBuilder<>(this.clazz).buildFromQueryMap(queryMap);

        Page<T> result = getRepository().findAll(specification, pageable);

        return result;
    }

    public List<T> search(final Map<String, Object> queryMap) {
        Specification<T> specification = new SpecificationBuilder<T>(this.clazz).buildFromQueryMap(queryMap);

        List<T> result = getRepository().findAll(specification);

        return result;
    }

    public List<T> findByPropertyValue(final String propertyName, final Object propertyValue) {
        Specification<T> specification = (Specification<T>) (root, query, builder) -> {
            Predicate predicate = builder.conjunction();
            predicate.getExpressions().add(builder.equal(root.get(propertyName),propertyValue));
            return predicate;
        };

        List<T> result = getRepository().findAll(specification);

        return result;
    }

    public List<T> findByPropertyValueAndIdNotEqualTo(final String propertyName, final Object propertyValue, final Object id) {
        Specification<T> specification = (Specification<T>) (root, query, builder) -> {
            Predicate predicate = builder.conjunction();
            predicate.getExpressions().add(builder.equal(root.get(propertyName),propertyValue));
            predicate.getExpressions().add(builder.notEqual(root.get("id"),id));
            return predicate;
        };

        List<T> result = getRepository().findAll(specification);

        return result;
    }

    public Object save(T entity){
        return getRepository().save(entity);
    }

    @Transactional(readOnly = true)
    public T getOne(final ID id) {
        return getRepository().findById(id).orElseThrow(()->new EntityNotFoundException(getClazz().getSimpleName(),id));
    }

    @Transactional(readOnly = true)
    public T getOne(final Specification<T> specification) {
        return getRepository().findOne(specification).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<T> findAll() {
        return getRepository().findAll();
    }

    @Transactional(readOnly = true)
    public List<T> findAll(final Iterable<ID> ids) {
        return getRepository().findAllById(ids);
    }

    @Transactional(readOnly = true)
    public List<T> findAll(final Sort sort) {
        return getRepository().findAll(sort);
    }


    @Transactional(readOnly = true)
    public Page<T> findAll(final Pageable pageable) {
        return getRepository().findAll(pageable);
    }


    @Transactional(readOnly = true)
    public List<T> findAll(final Specification<T> specification) {
        return getRepository().findAll(specification);
    }

    @Transactional(readOnly = true)
    public Page<T> findAll(final Specification<T> specification, final Pageable pageable) {
        return getRepository().findAll(specification, pageable);
    }

    @Transactional(readOnly = true)
    public List<T> findAll(final Specification<T> specification, final Sort sort) {
        return getRepository().findAll(specification, sort);
    }


    @Transactional
    public T create(final T entity) {
        return getRepository().save(entity);
    }

    @Transactional
    public T update(final T entity) {
        return getRepository().save(entity);
    }

    @Transactional
    public List<T> update(final Iterable<T> data) {
        return getRepository().saveAll(data);
    }

    @Transactional
    public void delete(final T entity) {
        getRepository().delete(entity);
    }

    @Transactional
    public void delete(final ID id) {
        getRepository().deleteById(id);
    }

    @Transactional
    public void delete(final Iterable<T> ids) {
        getRepository().deleteInBatch(ids);
    }

    @Transactional
    public void deleteAll() {
        getRepository().deleteAllInBatch();
    }


    public abstract ExtendedRepository<T, ID> getRepository();

    protected String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }

    protected String getMessage(String code){
        return getMessage(code, null);
    }
}
