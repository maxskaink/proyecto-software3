package unicauca.coreservice.infrastructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import unicauca.coreservice.application.in.*;
import unicauca.coreservice.application.out.*;
import unicauca.coreservice.domain.useCases.*;
import unicauca.coreservice.infrastructure.SQLrepository.Repository.*;

@Configuration
public class BeanConfiguration {

    @Bean
    public ProgramCompetencyAndOutcomeInt createProgramCompetencyAndOutcome(
            ProgramCompetencyAndOutcomeRepositoryOutInt repository,
            IAuthenticationService authenticationService
    ){
        return new ProgramCompetencyAndOutcomeService(repository, authenticationService);
    }

    @Bean
    public SubjectCompetencyInt createSubjectCompetency(
            SubjectCompetencyRepositoryOutInt competencyRepository,
            CompetencyToSubjectAssignmentRepositoryOutInt assignRepository,
            TermRepository termRepository,
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
    public TermInt createTerm(TermRepository repository){
        return new TermService(repository);
    }

    @Bean
    public RubricInt createRubric(RubricRepository repository, SubjectOutcomeRepository outcomeRepository){ return new RubricService(repository, outcomeRepository); }

    @Bean
    public CriterionInt createCriterion(CriterionRepository repository, RubricRepository rubricRepository){return new CriterionService(repository, rubricRepository);}

    @Bean
    public LevelInt createLevel(LevelRepository repository, CriterionRepository criterionRepository){return new LevelService(repository, criterionRepository);}

    @Bean
    public TeacherAssignmentInt createTeacherAssignment(TeacherAssignmentRepository repository, TermRepository termRepository, SubjectRepository subjectRepository){return new TeacherAssignmentService(repository, termRepository, subjectRepository);}
}
