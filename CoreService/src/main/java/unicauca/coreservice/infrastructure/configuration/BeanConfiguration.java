package unicauca.coreservice.infrastructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import unicauca.coreservice.application.in.CompetenciaAndRAProgramaUsesCase;
import unicauca.coreservice.application.out.CompAndRAProgramaRepositoryOut;
import unicauca.coreservice.domain.useCase.CompAndRAProgramaService;

@Configuration
public class BeanConfiguration {

    @Bean
    public CompetenciaAndRAProgramaUsesCase createCompetenciaAndRAProgramaUsesCase(
            CompAndRAProgramaRepositoryOut repository
    ){
        return new CompAndRAProgramaService(repository);
    }
}
