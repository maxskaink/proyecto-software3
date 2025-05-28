package unicauca.microsubject.infrastructure.SQLrepository.Repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import unicauca.microsubject.application.out.SubjectOutcomeRepositoryOutInt;
import unicauca.microsubject.domain.exception.NotFound;
import unicauca.microsubject.domain.model.OptionalWrapper;
import unicauca.microsubject.domain.model.SubjectOutcome;
import unicauca.microsubject.infrastructure.SQLrepository.JPARepository.*;
import unicauca.microsubject.infrastructure.SQLrepository.mapper.SubjectOutcomeMapper;

@Repository
@AllArgsConstructor
public class SubjectOutcomeRepository implements SubjectOutcomeRepositoryOutInt {

    private final JPASubjectOutcomeRepository subjectOutcomeRepository;
    private final JPAConfigurationRepository configurationRepository;

    @Override
    public OptionalWrapper<SubjectOutcome> getById(Integer id) {
        try{
            return new OptionalWrapper<>(SubjectOutcomeMapper.toSubjectOutcome(
                    subjectOutcomeRepository.findActiveSubjectOutcomeById(id)
                            .orElseThrow(() -> new NotFound("SubjectOutcome with id " + id + " was not found"))
            ));
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }

}
