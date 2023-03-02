package com.strategy.api.persistence.padron;

import com.strategy.api.base.converters.JpaConverter;
import com.strategy.api.domain.padron.model.Militante;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created on 6/14/22.
 */
@Component
public class MilitanteJpaConverter implements JpaConverter<Militante, MilitanteEntity> {
    @Override
    public Militante fromJpaEntity(MilitanteEntity jpaEntity) {
        if(jpaEntity == null) {
            return null;
        }

        return Militante.builder()
                .cedula(jpaEntity.getCedula())
                .apellido1(jpaEntity.getApellido1())
                .apellido2(jpaEntity.getApellido2())
                .nombres(jpaEntity.getNombres())
                .pld(!StringUtils.isEmpty(jpaEntity.getPld()))
                .sexo(jpaEntity.getSexo())
                .build();

    }

    @Override
    public MilitanteEntity toJpaEntity(Militante domainObject) {
        if(domainObject == null) {
            return null;
        }
        return MilitanteEntity.builder()
                .cedula(domainObject.getCedula())
                .apellido1(domainObject.getApellido1())
                .apellido2(domainObject.getApellido2())
                .nombres(domainObject.getNombres())
                .pld(BooleanUtils.isTrue(domainObject.getPld()) ? "PADRON CILOE" : null)
                .sexo(domainObject.getSexo())
                .build();
    }
}
