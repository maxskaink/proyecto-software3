package unicauca.coreservice.infrastructure.SQLrepository.Repository;

import unicauca.coreservice.application.out.CriterionRepositoryOutInt;
import unicauca.coreservice.domain.exception.NotFound;
import unicauca.coreservice.domain.model.Criterion;
import unicauca.coreservice.domain.model.OptionalWrapper;
import unicauca.coreservice.infrastructure.SQLrepository.JPARepository.JPACriterionRepository;
import unicauca.coreservice.infrastructure.SQLrepository.JPARepository.JPARubricRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.CriterionEntity;
import unicauca.coreservice.infrastructure.SQLrepository.entity.RubricEntity;
import unicauca.coreservice.infrastructure.SQLrepository.mapper.CriterionMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Repository;

import lombok.AllArgsConstructor;
import unicauca.coreservice.infrastructure.SQLrepository.mapper.RubricMapper;

@AllArgsConstructor
@Repository
public class CriterionRepository implements CriterionRepositoryOutInt {

    private final JPACriterionRepository criterionRepository;
    private final JPARubricRepository rubricRepository;

    @Override
    public OptionalWrapper<Criterion> add(Criterion newCriterion) {
        try {
            newCriterion.setId(null);
            newCriterion.setLevels(new ArrayList<>());

            CriterionEntity criterionEntity = CriterionMapper.toCriterionEntity(newCriterion);

            Integer rubricId = newCriterion.getRubric().getId();
            RubricEntity rubricEntity = rubricRepository.getReferenceById(rubricId);

            criterionEntity.setRubric(rubricEntity);
            rubricEntity.getCriteria().add(criterionEntity);

            CriterionEntity saved = criterionRepository.save(criterionEntity);

            Criterion criterion = CriterionMapper.toCriterion(saved);
            criterion.setRubric(RubricMapper.toRubric(rubricEntity));

            return new OptionalWrapper<>(criterion);
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }
    @Override
    public List<Criterion> listAll() {
        return criterionRepository.findAll().stream()
                .map(entity -> {
                    Criterion criterion = CriterionMapper.toCriterion(entity);
                    criterion.setRubric(RubricMapper.toRubric(entity.getRubric()));
                    return criterion;
                })
                .toList();
    }

    @Override
    public OptionalWrapper<Criterion> getById(Integer id) {
        try {
            CriterionEntity entity = criterionRepository.findById(id)
                    .orElseThrow(() -> new NotFound("Criterion with id " + id + " was not found"));
            Criterion criterion = CriterionMapper.toCriterion(entity);
            criterion.setRubric(RubricMapper.toRubric(entity.getRubric()));
            return new OptionalWrapper<>(criterion);
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public List<Criterion> listByRubricId(Integer rubricId) {
        return criterionRepository.findAll().stream()
                .filter(criterion -> Objects.equals(criterion.getRubric().getId(), rubricId))
                .map(entity -> {
                    Criterion criterion = CriterionMapper.toCriterion(entity);
                    criterion.setRubric(RubricMapper.toRubric(entity.getRubric()));
                    return criterion;
                })
                .toList();
    }

    @Override
    public OptionalWrapper<Criterion> remove(Integer id) {
        try {
            CriterionEntity entity = criterionRepository.findById(id)
                    .orElseThrow(() -> new NotFound("Criterion with id " + id + " was not found"));

            RubricEntity rubric = entity.getRubric();
            rubric.getCriteria().remove(entity);

            Criterion criterion = CriterionMapper.toCriterion(entity);
            criterion.setRubric(RubricMapper.toRubric(rubric));

            criterionRepository.delete(entity);

            return new OptionalWrapper<>(criterion);
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }


    @Override
    public OptionalWrapper<Criterion> update(Integer id, Criterion newCriterion) {
        try {
            CriterionEntity entity = criterionRepository.findById(id)
                    .orElseThrow(() -> new NotFound("Criterion with id " + id + " was not found"));
            entity.setWeight(newCriterion.getWeight());
            entity.setName(newCriterion.getName());

            CriterionEntity updated = criterionRepository.save(entity);

            Criterion criterion = CriterionMapper.toCriterion(updated);
            criterion.setRubric(RubricMapper.toRubric(updated.getRubric()));

            return new OptionalWrapper<>(criterion);
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }



}
