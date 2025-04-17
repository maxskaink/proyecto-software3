package unicauca.coreservice.infrastructure.SQLrepository.Repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import unicauca.coreservice.application.out.AsignaturaRepositoryOut;
import unicauca.coreservice.domain.model.Asignatura;
import unicauca.coreservice.domain.model.OptionalWrapper;
import unicauca.coreservice.infrastructure.SQLrepository.JPARepository.JPAAsignaturaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.AsignaturaEntity;
import unicauca.coreservice.infrastructure.SQLrepository.mapper.AsignaturaMapper;

import java.util.List;

@Repository
@AllArgsConstructor
public class AsignaturaRepository implements AsignaturaRepositoryOut {

    private final JPAAsignaturaRepository JPAasignatura;

    @Override
    public OptionalWrapper<Asignatura> addAsignatura(Asignatura newAsignatura) {

        try{
            AsignaturaEntity result = this.JPAasignatura.save(AsignaturaMapper.toEntity(newAsignatura));
            return new OptionalWrapper<>(AsignaturaMapper.toAsignatura(result));
        }catch (Exception e){
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public List<Asignatura> getAsignaturas() {
        return List.of();
    }

    @Override
    public OptionalWrapper<Asignatura> getById(Integer id) {
        return null;
    }

    @Override
    public OptionalWrapper<Asignatura> updateById(Integer id, Asignatura newAsignatura) {
        return null;
    }

    @Override
    public OptionalWrapper<Asignatura> removeAsignatura(Integer id) {
        return null;
    }
}
