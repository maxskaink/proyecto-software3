package unicauca.coreservice.infrastructure.SQLrepository.Repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import unicauca.coreservice.application.out.TermRepositoryOutInt;
import unicauca.coreservice.domain.model.OptionalWrapper;
import unicauca.coreservice.domain.model.Term;
import unicauca.coreservice.infrastructure.SQLrepository.JPARepository.JPAConfiguracionRepository;
import unicauca.coreservice.infrastructure.SQLrepository.JPARepository.JPAPeriodoRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.PeriodoEntity;
import unicauca.coreservice.infrastructure.SQLrepository.mapper.PeriodoMapper;

import java.util.List;

@AllArgsConstructor
@Repository
public class TermRepositoryInt implements TermRepositoryOutInt {

    private final JPAPeriodoRepository repository;
    private final JPAConfiguracionRepository repositoryConfiguracion;

    @Override
    public OptionalWrapper<Term> add(Term newTerm) {
        try{
            newTerm.setId(null);
            PeriodoEntity periodo = PeriodoMapper.toEntity(newTerm);
            return new OptionalWrapper<>(
                    PeriodoMapper.toPeriodo(repository.save(periodo))
                    );
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public List<Term> listAll() {
        return repository.findAll().stream()
                .map(PeriodoMapper::toPeriodo)
                .toList();
    }

    @Override
    public OptionalWrapper<Term> getActiveTerm() {
        PeriodoEntity actualPeriodo = repositoryConfiguracion.getReferenceById(1).getPeriodoActual();
        return new OptionalWrapper<>(PeriodoMapper.toPeriodo(actualPeriodo));
    }
}
