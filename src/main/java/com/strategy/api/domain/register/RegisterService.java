package com.strategy.api.domain.register;

import com.strategy.api.auth.utils.AuthUtils;
import com.strategy.api.domain.padron.model.Militante;
import com.strategy.api.domain.simpatizantes.Simpatizante;
import com.strategy.api.domain.simpatizantes.repositories.SimpatizanteRepository;
import com.strategy.api.domain.users.User;
import com.strategy.api.domain.users.UserRepository;
import com.strategy.api.events.SympathizerAddedEvent;
import com.strategy.api.events.SympathizerRemovedEvent;
import com.strategy.api.persistence.simpatizantes.entitites.SimpatizanteEntity;
import com.strategy.api.persistence.simpatizantes.repositories.SimpatizanteJpaRepository;
import com.strategy.api.persistence.users.UserEntity;
import com.strategy.api.web.register.dto.EditRequest;
import com.strategy.api.web.register.dto.ReassignRequest;
import com.strategy.api.web.register.dto.RegisterRequest;
import com.strategy.api.web.register.exceptions.CoordinatorNotExistsException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created on 6/13/22.
 */
@Service
public class RegisterService {
    private final SimpatizanteRepository simpatizanteRepository;
    private final UserRepository userRepository;
    private final SimpatizanteJpaRepository simpatizanteJpaRepository;
    private final ApplicationEventPublisher eventPublisher;


    public RegisterService(final SimpatizanteRepository simpatizanteRepository,
                           final UserRepository userRepository,
                           final SimpatizanteJpaRepository simpatizanteJpaRepository,
                           final ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = applicationEventPublisher;
        this.simpatizanteRepository = simpatizanteRepository;
        this.userRepository = userRepository;
        this.simpatizanteJpaRepository = simpatizanteJpaRepository;
    }

    @Transactional
    public void register(RegisterRequest request) {

        Long parentId = request.getRegisteredById() != null ? request.getRegisteredById().getId() : getParentId();

        Long userId;
        if(parentId != null) {
            userId = simpatizanteJpaRepository.getUserIdById(parentId)
                    .orElseThrow();
        } else {
            userId = AuthUtils.getCurrentUser().getId();
        }


        Simpatizante simpatizante = Simpatizante
                .builder()
                .phoneNumber(request.getPhone())
                .whatsApp(request.getWhatsApp())
                .militante(Militante.builder()
                        .cedula(request.getCedula())
                        .build())
                .registeredBy(Optional.ofNullable(parentId).map(id  -> Simpatizante.builder().id(id).build()).orElse(null))
                .build();

        simpatizanteRepository.save(simpatizante);
        userRepository.increaseRegisteredCount(User.builder().id(userId).build(), 1);

        eventPublisher.publishEvent(
                SympathizerAddedEvent.builder()
                        .source(this)
                        .cedula(request.getCedula())
                        .build());
    }

    @Transactional
    public void reassign(ReassignRequest request) {
        SimpatizanteEntity entity = simpatizanteJpaRepository.getOne(request.getSympathizerId());
        Long newCoordinatorSympathizerId = simpatizanteJpaRepository.getIdByUser(request.getCoordinatorUserId())
                .orElseThrow(CoordinatorNotExistsException::new);

        if(entity.getRegisteredBy()!= null) {
            Long oldCoordinatorUserId = simpatizanteJpaRepository.getUserIdById(entity.getRegisteredBy().getId())
                    .orElseThrow(CoordinatorNotExistsException::new);
            userRepository.increaseRegisteredCount(oldCoordinatorUserId, -1);
        }

        Optional<Long> sympathizerUserIdOptional = simpatizanteJpaRepository.getUserIdById(entity.getId());

        sympathizerUserIdOptional.ifPresent(suId -> userRepository.updateCreatedBy(suId, request.getCoordinatorUserId()));


        userRepository.increaseRegisteredCount(request.getCoordinatorUserId(), 1);
        entity.setRegisteredBy(SimpatizanteEntity.builder().id(newCoordinatorSympathizerId).build());
        simpatizanteJpaRepository.save(entity);

        eventPublisher.publishEvent(
                SympathizerAddedEvent.builder()
                        .source(this)
                        .cedula(entity.getMilitante().getCedula())
                        .build());
    }

    public Long getParentId() {
        UserEntity userEntity = AuthUtils.getCurrentUser();
        Long parentId;
        if(User.Role.SUPERADMIN.equals(userEntity.getRole())) {
            parentId = null;
        } else {
            parentId = simpatizanteJpaRepository.getIdByUser(userEntity.getId())
                    .orElse(null);
        }
        return parentId;
    }

    public void edit(EditRequest request) {
        SimpatizanteEntity simpatizante = simpatizanteJpaRepository.getOne(request.getId());
        simpatizante.setPhoneNumber(request.getPhone());
        simpatizante.setWhatsApp(request.getWhatsApp());
        simpatizanteJpaRepository.save(simpatizante);
        eventPublisher.publishEvent(
                SympathizerAddedEvent.builder()
                        .source(this)
                        .cedula(simpatizante.getMilitante().getCedula())
                        .build());
    }

    @Transactional
    public void delete(Long id) {
        SimpatizanteEntity simpatizante = simpatizanteJpaRepository.getOne(id);
        userRepository.deleteByCedula(simpatizante.getMilitante().getCedula());
        simpatizanteJpaRepository.deleteAllUnderSimpatizante(simpatizante.getMilitante().getCedula());
        //simpatizanteJpaRepository.deleteById(simpatizante.getId());
        eventPublisher.publishEvent(
                SympathizerRemovedEvent.builder()
                        .source(this)
                        .cedula(simpatizante.getMilitante().getCedula())
                        .build());
    }
}
