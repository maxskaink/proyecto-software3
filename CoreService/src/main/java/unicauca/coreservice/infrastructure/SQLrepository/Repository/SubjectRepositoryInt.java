package unicauca.coreservice.infrastructure.SQLrepository.Repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import unicauca.coreservice.application.out.SubjectRepositoryOutInt;
import unicauca.coreservice.domain.model.Subject;
import unicauca.coreservice.domain.model.OptionalWrapper;
import unicauca.coreservice.infrastructure.SQLrepository.JPARepository.JPAAsignaturaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.SubjectEntity;
import unicauca.coreservice.infrastructure.SQLrepository.mapper.AsignaturaMapper;

import java.util.List;

@Repository
@AllArgsConstructor
public class SubjectRepositoryInt implements SubjectRepositoryOutInt {

    private final JPAAsignaturaRepository JPAasignatura;

    @Override
    public OptionalWrapper<Subject> add(Subject newSubject) {

        try{
            newSubject.setId(null);
            SubjectEntity result = this.JPAasignatura.save(AsignaturaMapper.toEntity(newSubject));
            return new OptionalWrapper<>(AsignaturaMapper.toAsignatura(result));
        }catch (Exception e){
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public List<Subject> listAll() {
        return List.of();
    }

    @Override
    public OptionalWrapper<Subject> getById(Integer id) {
        return null;
    }

    @Override
    public OptionalWrapper<Subject> update(Integer id, Subject newSubject) {
        return null;
    }

    @Override
    public OptionalWrapper<Subject> remove(Integer id) {
        return null;
    }
}
