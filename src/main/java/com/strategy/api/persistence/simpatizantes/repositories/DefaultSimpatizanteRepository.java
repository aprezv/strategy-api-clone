package com.strategy.api.persistence.simpatizantes.repositories;

import com.strategy.api.domain.padron.model.Militante;
import com.strategy.api.domain.simpatizantes.Simpatizante;
import com.strategy.api.domain.simpatizantes.repositories.SimpatizanteRepository;
import com.strategy.api.persistence.padron.MilitanteEntity;
import com.strategy.api.persistence.simpatizantes.entitites.SimpatizanteEntity;
import com.strategy.api.persistence.users.UserEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created on 6/13/22.
 */
@Repository
public class DefaultSimpatizanteRepository implements SimpatizanteRepository {
    private final SimpatizanteJpaRepository jpaRepo;

    public DefaultSimpatizanteRepository(final SimpatizanteJpaRepository jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public Simpatizante save(Simpatizante simpatizante) {

        SimpatizanteEntity entity = SimpatizanteEntity.builder()
                .militante(MilitanteEntity.builder().cedula(simpatizante.getMilitante().getCedula()).build())
                .phoneNumber(simpatizante.getPhoneNumber())
                .whatsApp(simpatizante.getWhatsApp())
                .lat(simpatizante.getLat())
                .lng(simpatizante.getLng())
                .registeredBy(Optional.ofNullable(simpatizante.getRegisteredBy()).map(r->SimpatizanteEntity.builder().id(r.getId()).build()).orElse(null))
                .build();

        jpaRepo.save(entity);

        return Simpatizante.builder()
                .id(entity.getId())
                .build();
    }

    @Override
    public Simpatizante findByCedula(String cedula) {
        return jpaRepo.findByMilitanteCedula(cedula)
                .map( entity -> Simpatizante.builder()
                        .whatsApp(entity.getWhatsApp())
                        .phoneNumber(entity.getPhoneNumber())
                        .id(entity.getId())
                        .lat(entity.getLat())
                        .lng(entity.getLng())
                        .militante(Militante.builder()
                                .cedula(entity.getMilitante().getCedula())
                                .apellido1(entity.getMilitante().getApellido1())
                                .apellido2(entity.getMilitante().getApellido2())
                                .nombres(entity.getMilitante().getNombres())
                                .build())
                        .build())
                .orElse(null);
    }

    @Override
    public Simpatizante findById(Long id) {
        return jpaRepo.findById(id)
                .map( entity -> Simpatizante.builder()
                        .whatsApp(entity.getWhatsApp())
                        .phoneNumber(entity.getPhoneNumber())
                        .id(entity.getId())
                        .militante(Militante.builder()
                                .cedula(entity.getMilitante().getCedula())
                                .apellido1(entity.getMilitante().getApellido1())
                                .apellido2(entity.getMilitante().getApellido2())
                                .nombres(entity.getMilitante().getNombres())
                                .build())
                        .build())
                .orElse(null);
    }

    @Override
    public Boolean simpatizanteExiste(String cedula) {
        return jpaRepo.existsSimpatizanteEntityByMilitanteCedula(cedula);
    }

    @Override
    public boolean existsInSe(String cedula) {
        return jpaRepo.existsSimpatizanteEntityByMilitanteCedulaAndIsSeIsTrue(cedula);
    }

}
