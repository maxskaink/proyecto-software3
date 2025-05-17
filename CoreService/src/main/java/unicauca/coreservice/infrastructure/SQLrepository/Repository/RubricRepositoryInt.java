package unicauca.coreservice.infrastructure.SQLrepository.Repository;

import unicauca.coreservice.application.out.RubricRepositoryOutInt;
import unicauca.coreservice.domain.model.OptionalWrapper;
import unicauca.coreservice.infrastructure.SQLrepository.entity.RubricEntity;

import java.util.List;

public class RubricRepositoryInt implements RubricRepositoryOutInt {

    @Override
    public OptionalWrapper<RubricEntity> add(RubricEntity newRubric) {
        return null;
    }

    @Override
    public List<RubricEntity> listAll() {
        return List.of();
    }

    @Override
    public OptionalWrapper<RubricEntity> getById(Integer id) {
        return null;
    }

    @Override
    public OptionalWrapper<RubricEntity> getBySubjectOutcomeId(Integer subjectOutcomeId) {
        return null;
    }

    @Override
    public OptionalWrapper<RubricEntity> remove(Integer id) {
        return null;
    }
}
