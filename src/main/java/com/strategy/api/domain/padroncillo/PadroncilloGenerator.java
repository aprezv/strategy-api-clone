package com.strategy.api.domain.padroncillo;

import com.strategy.api.fileuploader.models.FileUploadRequest;
import com.strategy.api.fileuploader.services.FileServiceS3;
import com.strategy.api.persistence.padron.MunicipioEntity;
import com.strategy.api.persistence.padron.MunicipioJpaRepository;
import com.strategy.api.persistence.respositories.PadroncilloJpaRepository;
import com.strategy.api.persistence.respositories.PadroncilloResponse;
import com.strategy.api.reports.pdf.PadroncilloReport;
import com.strategy.api.web.padroncillo.GeneratePadroncilloRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import static net.logstash.logback.argument.StructuredArguments.kv;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created on 9/2/22.
 */
@Slf4j
@Service
public class PadroncilloGenerator {
    private final PadroncilloJpaRepository padroncilloJpaRepository;
    private final FileServiceS3 fileServiceS3;
    private final MunicipioJpaRepository municipioJpaRepository;

    public PadroncilloGenerator(final PadroncilloJpaRepository padroncilloJpaRepository,
                                final FileServiceS3 fileServiceS3, MunicipioJpaRepository municipioJpaRepository) {
        this.padroncilloJpaRepository = padroncilloJpaRepository;
        this.fileServiceS3 = fileServiceS3;
        this.municipioJpaRepository = municipioJpaRepository;
    }


    public Resource generate(GeneratePadroncilloRequest request) {
        PadroncilloResponse simpatizante = padroncilloJpaRepository.getSimpatizante(request.getIdSimpatizante());
        List<PadroncilloResponse> padroncillo = padroncilloJpaRepository.getPadroncillo(request.getIdSimpatizante());

        return PadroncilloReport.generateReport(simpatizante, padroncillo);

    }

    public GenRes generate(Long id) {
        PadroncilloResponse simpatizante = padroncilloJpaRepository.getSimpatizante(id);
        List<PadroncilloResponse> padroncillo = padroncilloJpaRepository.getPadroncillo(id);

        Resource resource = PadroncilloReport.generateReport(simpatizante, padroncillo);

        return GenRes.builder()
                .resource(resource)
                .simpatizante(simpatizante)
                .count(padroncillo.size())
                .build();

    }

    public void uploadPadroncillo(PadroncilloResponse simpatizante,
                                  Resource resource,
                                  String provincia,
                                  String municipio,
                                  String circ
                                  ) {
        try {

            String fileName = String.format("%s.pdf", simpatizante.getCedula());
            String key = String.format(
                    "%s/%s/%s%s",
                    Optional.ofNullable(simpatizante.getProvincia()).orElse(provincia),
                    Optional.ofNullable(simpatizante.getMunicipio()).orElse(municipio),
                    Optional.ofNullable(circ).map(c -> "CIRC - " + c + "/").orElse(""),
                    fileName);

            fileServiceS3.uploadFile(
                    FileUploadRequest.builder()
                            .inputStream(resource.getInputStream())
                            .fileName(fileName)
                            .key(key)
                            .publicAccess(true)
                            .contentType("application/pdf")
                            .contentLength(resource.contentLength())
                            .build());

            log.info("Padroncillo subido correctamente", kv("cedula", simpatizante.getCedula()) );

        } catch (Exception e) {
            log.error("Error subiendo padroncillo ", kv("cedula", simpatizante.getCedula()));
            log.error("Error subiendo padroncillo ", e);
        }
    }

    public Integer generarPorProcinvia(GeneratePadroncilloRequest request) {
        Long idMunicipio = request.getIdSimpatizante();

        List<Long> idSimpatizantes = padroncilloJpaRepository.getIdSimpatizantes(idMunicipio);
        MunicipioEntity municipioEntity = municipioJpaRepository.getOne(idMunicipio.intValue());
        String municipio = municipioEntity.getDescripcion();
        String procinvia = municipioEntity.getProvincia().getDescripcion();

        Executors.newSingleThreadExecutor().submit(()->{
            int total = idSimpatizantes.size();
            AtomicInteger count = new AtomicInteger(1);
            idSimpatizantes.forEach(
                    id -> {
                        log.info("Generando padroncillo {} {} {} {}",
                                kv("simpatizanteId", id ),
                                kv("municipio",municipio ),
                                kv("registro", String.format("%s de %s", count.getAndIncrement(), total) ),
                                kv("provincia", procinvia)
                        );
                        GenRes res = generate(id);
                        String circ = null;
                        if(BooleanUtils.isTrue(request.getByCirc())) {
                            circ = res.getSimpatizante().getCirc();
                        }

                        if(res.count > 0) {
                            uploadPadroncillo(res.simpatizante, res.resource, procinvia, municipio, circ);
                        } else {
                            log.warn("Coordinador no tiene simpatizantes {}",
                                    kv("cedula", res.simpatizante.getCedula() ));
                        }

                    }
            );

        });

        return idSimpatizantes.size();

    }
}
