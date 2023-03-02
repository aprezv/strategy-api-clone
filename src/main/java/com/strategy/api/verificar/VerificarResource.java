package com.strategy.api.verificar;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created on 9/9/22.
 */
@RestController
@RequestMapping("/verificar")
public class VerificarResource {
    private final VerificarJpaRepo jpaRepo;
    private final ModelMapper modelMapper;

    public VerificarResource(VerificarJpaRepo jpaRepo, ModelMapper modelMapper) {
        this.jpaRepo = jpaRepo;
        this.modelMapper = modelMapper;
    }


    @GetMapping
    void verificar() {

        RestTemplate template = new RestTemplate();
        String baseUrl = "https://apiconsulta.verificate.do/api/Consulta/GetByIdentification?Identification=";

        List<IVerificarResponse> list = jpaRepo.getVerificar();


        File csvOutputFile = new File("Resultado.csv");
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {

            list.forEach(l -> {
                ResponseEntity response = template.getForEntity(String.format("%s%s", baseUrl, l.getCedula()), Object.class);
                response.getStatusCode();
                VerificarResponse r = modelMapper.map(l, VerificarResponse.class);
                r.setVerificate(response.getStatusCode().equals(HttpStatus.OK));
                System.out.println(String.format("Cedula: %s -> %s", l.getCedula(), response.getStatusCode().equals(HttpStatus.OK) ? "Si" : "No"));
                pw.println(convertToCSV(r));

            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    public String convertToCSV(VerificarResponse data) {
        return String.format("%s,%s", data.getCedula(), data.getVerificate());
    }
}
