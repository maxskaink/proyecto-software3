package unicauca.microsubject.infrastructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import unicauca.microsubject.application.in.*;
import unicauca.microsubject.application.out.IAuthenticationService;
import unicauca.microsubject.application.out.SubjectRepositoryOutInt;
import unicauca.microsubject.domain.useCases.*;
import unicauca.microsubject.infrastructure.SQLrepository.Repository.*;

@Configuration
public class BeanConfiguration {

    @Bean
    public TeacherAssignmentInt createTeacherAssignment(TeacherAssignmentRepository repository, TermRepository termRepository,
                                                        SubjectRepository subjectRepository){
        return new TeacherAssignmentService(repository, termRepository, subjectRepository);}

    @Bean
    public EvaluatorAssignmentInt createEvaluatorAssignment(EvaluatorAssignmentRepository repository, TermRepository termRepository,
                                                            SubjectOutcomeRepository subjectOutcomeRepository){
        return new EvaluatorAssignmentService(repository, termRepository, subjectOutcomeRepository);
    }
    @Bean
    public SubjectInt createSubjectService(
            IAuthenticationService authenticationService,
            SubjectRepositoryOutInt subjectRepository
    ){
        return new SubjectService(
                authenticationService,
                subjectRepository
        );
    }
}
