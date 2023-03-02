package com.strategy.api.base.resources;

import com.strategy.api.base.model.jpa.BaseEntity;
import com.strategy.api.base.services.BaseService;
import com.strategy.api.base.utils.SortUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created on 4/22/18.
 */
@RestController
public abstract class BaseResource<T extends BaseEntity, ID extends Serializable> implements Sortable {

    protected final static String PAGE_SIZE = "25";
    protected final static String DEFAULT_PAGE = "0";

    @Autowired
    private MessageSource messageSource;


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<List<T>> list(@RequestParam final Map<String, Object> queryMap) {
        return ResponseEntity
                .ok(getService().search(queryMap));
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<T> search(@RequestParam final Map<String, Object> queryMap,
                          @RequestParam(name = "_page", required = false, defaultValue = DEFAULT_PAGE) final Integer page,
                          @RequestParam(name = "_size", required = false, defaultValue = PAGE_SIZE) final Integer size,
                          @RequestParam(name = "_sort", required = false, defaultValue = "") String sort
    ) {

        if (StringUtils.isEmpty(sort))
            sort = "-" + this.getSortableFields()[0];

        return getService().search(queryMap, PageRequest.of(page, size, SortUtils.processSort(sort, getSortableFields())));

    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity save(@RequestBody @Validated T request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(getService().save(request));

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<T> edit(@RequestBody @Validated T entity, @PathVariable("id") final ID id) {

        T responseEntity = this.getService().update(entity);
        return ResponseEntity.ok(responseEntity);

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getOne(@PathVariable("id") final ID id) {
        T object = getService().getOne(id);

        return ResponseEntity.ok(object);

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") final ID id) {
        this.getService().delete(id);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@RequestParam("ids") final ID[] ids) {
        for (ID id : ids)
            this.getService().delete(id);
    }

    public abstract BaseService<T, ID> getService();

    public abstract String[] getSortableFields();

    protected String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }

    protected String getMessage(String code){
        return getMessage(code, null);
    }
}
