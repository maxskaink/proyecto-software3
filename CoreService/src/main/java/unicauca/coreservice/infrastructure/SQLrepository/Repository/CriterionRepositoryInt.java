package unicauca.coreservice.infrastructure.SQLrepository.Repository;

import unicauca.coreservice.application.out.CriterionRepositoryOutInt;
import unicauca.coreservice.domain.exception.NotFound;
import unicauca.coreservice.domain.model.Criterion;
import unicauca.coreservice.domain.model.OptionalWrapper;
import unicauca.coreservice.infrastructure.SQLrepository.JPARepository.JPACriterionRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.CriterionEntity;
import unicauca.coreservice.infrastructure.SQLrepository.mapper.CriterionMapper;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Repository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Repository
public class CriterionRepositoryInt implements CriterionRepositoryOutInt {

    private final JPACriterionRepository criterionRepository;
    @Override
    public OptionalWrapper<Criterion> add(Criterion newCriterion) {
        try{
            newCriterion.setId(null);

            CriterionEntity criterionEntity = CriterionMapper.toCriterionEntity(newCriterion);
            return new OptionalWrapper<>(
                    CriterionMapper.toCriterion(criterionRepository.save(criterionEntity))
            );
        }catch (Exception e){
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public List<Criterion> listAll() {
        return criterionRepository.findAll().stream()
                .map(CriterionMapper::toCriterion)
                .toList();
    }

    @Override
    public OptionalWrapper<Criterion> getById(Integer id) {
        try{
            return new OptionalWrapper<>(
                    CriterionMapper.toCriterion(criterionRepository.findById(id).
                    orElseThrow( () -> new NotFound("Criterion with id "+ id +" was not found") )
            ));
        }catch (Exception e){
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public OptionalWrapper<Criterion> getByRubricId(Integer rubricId) {
        try{
            return new OptionalWrapper<>(
                    (criterionRepository.findAll().
                    stream().filter(criterion -> Objects.equals(criterion.getRubric().getId(),rubricId)).
                    map(CriterionMapper::toCriterion).findFirst().
                    orElseThrow( () -> new NotFound("Criterion with rubric id "+ rubricId +" was not found") )
            ));
        }catch (Exception e){
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public OptionalWrapper<Criterion> remove(Integer id) {
        try{
            CriterionEntity criterionEntity = criterionRepository.findById(id)
                    .orElseThrow( () -> new NotFound("Criterion with id "+ id +" was not found") );
            Criterion criterion = CriterionMapper.toCriterion(criterionEntity);
            criterionRepository.delete(criterionEntity);
            
            return new OptionalWrapper<>(criterion);
        }catch (Exception e){
            return new OptionalWrapper<>(e);
        }
    }
}
