package ma.appsegov.solutionservice;

import ma.appsegov.solutionservice.DTO.SolutionDTO;
import ma.appsegov.solutionservice.model.Solution;
import ma.appsegov.solutionservice.repository.SolutionRepository;
import ma.appsegov.solutionservice.service.RequestService;
import ma.appsegov.solutionservice.service.SolutionService;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class SolutionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SolutionServiceApplication.class, args);
    }

    //@Bean
    CommandLineRunner start(SolutionRepository solutionRepository) {
        return args -> {

            solutionRepository.save( Solution.builder().name("E-parapheur").build() );
            solutionRepository.save( Solution.builder().name("Plateformes de réclamations").build() );
            solutionRepository.save( Solution.builder().name("Gestion des RDV").build() );
            solutionRepository.save( Solution.builder().name("Identité numérique").build() );
            solutionRepository.save( Solution.builder().name("Watiqa").build() );
        };
    }
}
