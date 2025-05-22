package unicauca.coreservice.infrastructure.SQLrepository.Repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import unicauca.coreservice.application.out.RubricRepositoryOutInt;
import unicauca.coreservice.domain.exception.NotFound;
import unicauca.coreservice.domain.model.OptionalWrapper;
import unicauca.coreservice.domain.model.Rubric;
import unicauca.coreservice.infrastructure.SQLrepository.JPARepository.JPARubricRepository;

import unicauca.coreservice.infrastructure.SQLrepository.JPARepository.JPASubjectOutcomeRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.RubricEntity;
import unicauca.coreservice.infrastructure.SQLrepository.entity.SubjectOutcomeEntity;
import unicauca.coreservice.infrastructure.SQLrepository.mapper.RubricMapper;
import unicauca.coreservice.infrastructure.SQLrepository.mapper.SubjectOutcomeMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
@Repository
public class RubricRepository implements RubricRepositoryOutInt {
    private final JPARubricRepository rubricRepository;
    private final JPASubjectOutcomeRepository subjectOutcomeRepository;

    @Override
    public OptionalWrapper<Rubric> add(Rubric newRubric) {
        try {
            newRubric.setId(null); // Ensure it's a new entity
            newRubric.setCriteria(new ArrayList<>()); // Initialize criteria
    
            // Map domain to entity
            RubricEntity rubricEntity = RubricMapper.toRubricEntity(newRubric);
    
            // Get reference to subject outcome
            Integer subjectOutcomeId = newRubric.getSubjectOutcome().getId();
            SubjectOutcomeEntity subjectOutcomeEntity = subjectOutcomeRepository.getReferenceById(subjectOutcomeId);
    
            // Link both sides manually
            rubricEntity.setSubjectOutcomeId(subjectOutcomeId);
            subjectOutcomeEntity.setRubric(rubricEntity);
    
            // Save rubric
            RubricEntity saved = rubricRepository.save(rubricEntity);
    
            // Use centralized mapping method
            return new OptionalWrapper<>(mapWithSubjectOutcome(saved));
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }
    
    

    @Override
    public List<Rubric> listAllBySubjectId(Integer subjectId) {
        return rubricRepository.findByIsActivatedTrue().
                stream().
                filter( rubric -> Objects.equals(subjectOutcomeRepository.getReferenceById(rubric.getSubjectOutcomeId()) 
                .getCompetencyAssignment().getSubject().getId(), subjectId)).
                map(this::mapWithSubjectOutcome).collect(Collectors.toList());
    }

    @Override
    public OptionalWrapper<Rubric> getById(Integer id) {
        try{
            return new OptionalWrapper<>(mapWithSubjectOutcome(
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
                    stream().filter( rubric -> Objects.equals(rubric.getSubjectOutcomeId(),subjectOutcomeId))
                    .map(this::mapWithSubjectOutcome).findFirst());
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
            return new OptionalWrapper<>(mapWithSubjectOutcome(rubricRepository.save(activeRubric)));
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
            return new OptionalWrapper<>(mapWithSubjectOutcome(rubricRepository.save(activeRubric)));
        }catch (Exception e){
            return new OptionalWrapper<>(e);
        }
    }

    private Rubric mapWithSubjectOutcome(RubricEntity rubricEntity) {
        Rubric rubric = RubricMapper.toRubric(rubricEntity);
        rubric.setSubjectOutcome(
                SubjectOutcomeMapper.toSubjectOutcome(
                        subjectOutcomeRepository.getReferenceById(rubricEntity.getSubjectOutcomeId())
                )
        );

        System.out.println(rubric.toString());
        return rubric;
    }
    
}
