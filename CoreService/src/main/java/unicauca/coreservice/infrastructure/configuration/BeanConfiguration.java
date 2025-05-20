package unicauca.coreservice.infrastructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import unicauca.coreservice.application.in.ProgramCompetencyAndOutcomeInt;
import unicauca.coreservice.application.in.SubjectCompetencyInt;
import unicauca.coreservice.application.in.TermInt;
import unicauca.coreservice.application.out.CompetencyToSubjectAssignmentRepositoryOutInt;
import unicauca.coreservice.application.out.ProgramCompetencyAndOutcomeRepositoryOutInt;
import unicauca.coreservice.application.out.SubjectCompetencyRepositoryOutInt;
import unicauca.coreservice.application.out.SubjectOutcomeRepositoryOutInt;
import unicauca.coreservice.domain.useCases.ProgramCompetencyAndOutcomeService;
import unicauca.coreservice.domain.useCases.SubjectCompetencyService;
import unicauca.coreservice.domain.useCases.SubjectOutcomeService;
import unicauca.coreservice.domain.useCases.TermService;
import unicauca.coreservice.infrastructure.SQLrepository.Repository.TermRepositoryInt;

@Configuration
public class BeanConfiguration {

    @Bean
    public ProgramCompetencyAndOutcomeInt createProgramCompetencyAndOutcome(
            ProgramCompetencyAndOutcomeRepositoryOutInt repository
    ){
        return new ProgramCompetencyAndOutcomeService(repository);
    }

    @Bean
    public SubjectCompetencyInt createSubjectCompetency(
            SubjectCompetencyRepositoryOutInt competencyRepository,
            CompetencyToSubjectAssignmentRepositoryOutInt assignRepository,
            TermRepositoryInt termRepository,
            SubjectOutcomeRepositoryOutInt subjectOutcomeRepository
    ){
        return new SubjectCompetencyService(
                competencyRepository,assignRepository,
                termRepository,subjectOutcomeRepository
        );
    }

    @Bean
    public SubjectOutcomeService createSubjectOutcome(
            SubjectOutcomeRepositoryOutInt repositorySubjectOutcome
    ){
        return new SubjectOutcomeService(
                repositorySubjectOutcome
        );
    }

    @Bean
    public TermInt createTerm(TermRepositoryInt repository){
        return new TermService(repository);
    }
}
