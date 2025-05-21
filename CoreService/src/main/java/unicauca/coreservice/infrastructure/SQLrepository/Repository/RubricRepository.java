package unicauca.coreservice.infrastructure.SQLrepository.Repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import unicauca.coreservice.application.out.RubricRepositoryOutInt;
import unicauca.coreservice.domain.exception.NotFound;
import unicauca.coreservice.domain.model.OptionalWrapper;
import unicauca.coreservice.domain.model.Rubric;
import unicauca.coreservice.infrastructure.SQLrepository.JPARepository.JPARubricRepository;

import unicauca.coreservice.infrastructure.SQLrepository.entity.RubricEntity;
import unicauca.coreservice.infrastructure.SQLrepository.mapper.RubricMapper;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
@Repository
public class RubricRepository implements RubricRepositoryOutInt {
    private final JPARubricRepository rubricRepository;

    @Override
    public OptionalWrapper<Rubric> add(Rubric newRubric) {
        try{
            newRubric.setId(null);

            RubricEntity rubricEntity = RubricMapper.toRubricEntity(newRubric);
            return new OptionalWrapper<>(
                    RubricMapper.toRubric(rubricRepository.save(rubricEntity))
            );

        }catch (Exception e){
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public List<Rubric> listAllBySubjectId(Integer subjectId) {
        return rubricRepository.findByIsActivatedTrue().
                stream().
                filter( rubric -> Objects.equals(rubric.getLearningOutcome().getCompetencyAssignment().getSubject().getId(), subjectId)).
                map(RubricMapper::toRubric).collect(Collectors.toList());
    }

    @Override
    public OptionalWrapper<Rubric> getById(Integer id) {
        try{
            return new OptionalWrapper<>(RubricMapper.toRubric(
                    rubricRepository.findById(id).
                            orElseThrow( () -> new NotFound("Rubric with id " + id + " was not found") )
            ));
        }catch (Exception e){
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public OptionalWrapper<Rubric> getBySubjectOutcomeId(Integer subjectOutcomeId) {
        try{
            return new OptionalWrapper<>(rubricRepository.findByIsActivatedTrue().
                    stream().filter( rubric -> Objects.equals(rubric.getLearningOutcome().getId(),subjectOutcomeId))
                    .map(RubricMapper::toRubric).findFirst());
        }catch (Exception e){
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public OptionalWrapper<Rubric> update(Integer id, Rubric newRubric) {
        try{
            RubricEntity activeRubric = rubricRepository.findActiveRubricById(id)
                    .orElseThrow(()-> new NotFound("Rubric with id " + id + " was not found"));
            activeRubric.setDescription(newRubric.getDescription());
            return new OptionalWrapper<>(RubricMapper.toRubric(rubricRepository.save(activeRubric)));
        }catch (Exception e){
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public OptionalWrapper<Rubric> remove(Integer id) {
        try{
            RubricEntity activeRubric = rubricRepository.findActiveRubricById(id)
                    .orElseThrow(()-> new NotFound("Rubric with id " + id + " was not found"));
            activeRubric.setActivated(false);
            return new OptionalWrapper<>(RubricMapper.toRubric(rubricRepository.save(activeRubric)));
        }catch (Exception e){
            return new OptionalWrapper<>(e);
        }
    }
}
