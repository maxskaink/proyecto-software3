package unicauca.coreservice.infrastructure.SQLrepository.Repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import unicauca.coreservice.application.out.PeriodoRepositoryOut;
import unicauca.coreservice.domain.model.OptionalWrapper;
import unicauca.coreservice.domain.model.Periodo;
import unicauca.coreservice.infrastructure.SQLrepository.JPARepository.JPAConfiguracionRepository;
import unicauca.coreservice.infrastructure.SQLrepository.JPARepository.JPAPeriodoRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.PeriodoEntity;
import unicauca.coreservice.infrastructure.SQLrepository.mapper.PeriodoMapper;

import java.util.List;

@AllArgsConstructor
@Repository
public class PeriodoRepository implements PeriodoRepositoryOut {

    private final JPAPeriodoRepository repository;
    private final JPAConfiguracionRepository repositoryConfiguracion;

    @Override
    public OptionalWrapper<Periodo> addPeriodo(Periodo newPeriodo) {
        try{
            newPeriodo.setId(null);
            PeriodoEntity periodo = PeriodoMapper.toEntity(newPeriodo);
            return new OptionalWrapper<>(
                    PeriodoMapper.toPeriodo(repository.save(periodo))
                    );
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public List<Periodo> listPeriodo() {
        return repository.findAll().stream()
                .map(PeriodoMapper::toPeriodo)
                .toList();
    }

    @Override
    public OptionalWrapper<Periodo> getActualPeriodo() {
        PeriodoEntity actualPeriodo = repositoryConfiguracion.getReferenceById(1).getPeriodoActual();
        return new OptionalWrapper<>(PeriodoMapper.toPeriodo(actualPeriodo));
    }
}
