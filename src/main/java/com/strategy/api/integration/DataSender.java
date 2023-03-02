package com.strategy.api.integration;

import com.strategy.api.domain.simpatizantes.Simpatizante;
import com.strategy.api.events.SympathizerAddedEvent;
import com.strategy.api.events.SympathizerRemovedEvent;
import com.strategy.api.logging.LogEvents;
import com.strategy.api.logging.LogKeys;
import com.strategy.api.persistence.oauth.entities.TokenEntity;
import com.strategy.api.persistence.oauth.repositories.TokenRepository;
import com.strategy.api.persistence.simpatizantes.entitites.SimpatizanteExternalResponse;
import com.strategy.api.persistence.simpatizantes.repositories.SimpatizanteJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import static net.logstash.logback.argument.StructuredArguments.kv;

/**
 * Created on 9/5/22.
 */
@Slf4j
@Component
public class DataSender {
    private final String baseUrl;
    private final String username;
    private final String password;
    private final RestTemplate restTemplate;
    private final TokenRepository repository;
    private final SimpatizanteJpaRepository jpaRepository;

    public DataSender(@Value("${integration.baseUrl}") String baseUrl,
                      @Value("${integration.username}") String username,
                      @Value("${integration.password}") String password,
                      TokenRepository repository, SimpatizanteJpaRepository jpaRepository) {
        this.baseUrl = baseUrl;
        this.repository = repository;
        this.password = password;
        this.username = username;
        this.jpaRepository = jpaRepository;
        this.restTemplate = new RestTemplate();
    }


    @EventListener
    public void sendSympathizer(SympathizerAddedEvent event) {
        if(true){
            return;
        }
        log.info("Enviando Datos");

        try {
            String token = getAccessToken();

            SimpatizanteExternalResponse externalResponse = jpaRepository.getForExternalPlatform(event.getCedula());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.setBearerAuth(token);

            SimpatizanteDto request =
                    SimpatizanteDto.builder()
                            .Cedula(externalResponse.getCedula())
                            .Telefono(externalResponse.getTelefono())
                            .WhatsApp(externalResponse.getWhatsapp())
                            .PLD(externalResponse.getPld().toString())
                            .CedulaCoordinador(Optional.ofNullable(externalResponse.getCedula_coordinador()).orElse(""))
                            .Plataforma(externalResponse.getPlataforma())
                            .Rol(Optional.ofNullable(externalResponse.getRol()).orElse(""))
                            .IDColegio(externalResponse.getId_colegio())
                            .CodigoRecinto(externalResponse.getCodigo_recinto())
                            .Simpatizantes("0")
                            .build();

            String url = String.format("%s/%s", baseUrl, "api/CargaData/CargaDataRow");
            HttpEntity<SimpatizanteDto> entity = new HttpEntity<>(request, headers);


            ResponseEntity<SimpatizanteDto> response = restTemplate.postForEntity(url, entity, SimpatizanteDto.class);
            log.info("Datos Enviados",
                    kv("datos", request),
                    kv("response", response)
            );
        } catch (Exception e) {
            log.error("Error enviando datos",
                    kv(LogKeys.EVENT_NAME, LogEvents.EXCEPTION),
                    kv(LogKeys.EXCEPTION_MESSAGE, e.getMessage()),
                    kv(LogKeys.EXCEPTION_CAUSE, e.getCause()),
                    kv("Cedula", event.getCedula())
            );
            e.printStackTrace();
        }

    }

    @EventListener
    public void removeSympathizer(SympathizerRemovedEvent event) {
        if(true){
            return;
        }
        try {
            String token = getAccessToken();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.setBearerAuth(token);


            String url = String.format("%s/%s", baseUrl, "api/CargaData/Delete?Cedula=" + event.getCedula());
            HttpEntity<SimpatizanteDto> entity = new HttpEntity<>(headers);
            restTemplate.exchange(url, HttpMethod.GET, entity, SimpatizanteDto.class);
            log.info("Simpatizante Eliminado",
                    kv("datos", event.getCedula())
            );
        } catch (Exception e) {
            log.error("Error eliminando simpatizante",
                    kv(LogKeys.EVENT_NAME, LogEvents.EXCEPTION),
                    kv(LogKeys.EXCEPTION_MESSAGE, e.getMessage()),
                    kv(LogKeys.EXCEPTION_CAUSE, e.getCause()),
                    kv("Cedula", event.getCedula())
            );
            e.printStackTrace();
        }

    }


    private String getAccessToken() {
        TokenEntity tokenEntity = repository.findById(1L)
                .orElse(null);

        if (tokenEntity == null || needsRefresh(tokenEntity)) {


            Map<String, String> request =
                    Map.of(
                            "AccountLogin", username,
                            "AccountPass", password,
                            "AppId", "1"
                    );


            String url = String.format("%s/%s", baseUrl, "api/AccessToken/GetAppToken");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity entity = new HttpEntity<>(request, headers);

            ResponseEntity<TokenResponse> tokenResponseResponseEntity = restTemplate.postForEntity(url, entity, TokenResponse.class);
            TokenResponse tokenResponse = tokenResponseResponseEntity.getBody();

            tokenEntity = new TokenEntity();
            tokenEntity.setId(1L);
            tokenEntity.setAccessToken(tokenResponse.getToken());
            tokenEntity.setExpiresIn(24L * 60L * 60);
            repository.save(tokenEntity);
            return tokenEntity.getAccessToken();

        }

        return tokenEntity.getAccessToken();

    }


    private boolean needsRefresh(TokenEntity token) {
        if (token == null) {
            return true;
        }

        if (token.getLastModifiedDate() == null) {
            return false;
        }
        if (token.getAccessToken() == null) {
            return true;
        }
        long seconds = (new Date().getTime() - token.getLastModifiedDate().getTime()) / 1000;
        return (seconds + 1000) > token.getExpiresIn();
    }

}
