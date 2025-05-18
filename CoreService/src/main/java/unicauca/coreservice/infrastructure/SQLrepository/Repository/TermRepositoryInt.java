package unicauca.coreservice.infrastructure.SQLrepository.Repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import unicauca.coreservice.application.out.TermRepositoryOutInt;
import unicauca.coreservice.domain.model.OptionalWrapper;
import unicauca.coreservice.domain.model.Term;
import unicauca.coreservice.infrastructure.SQLrepository.JPARepository.JPAConfigurationRepository;
import unicauca.coreservice.infrastructure.SQLrepository.JPARepository.JPATermRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.TermEntity;
import unicauca.coreservice.infrastructure.SQLrepository.mapper.TermMapper;

import java.util.List;

@AllArgsConstructor
@Repository
public class TermRepositoryInt implements TermRepositoryOutInt {

    private final JPATermRepository termRepository;
    private final JPAConfigurationRepository configurationRepository;

    @Override
    public OptionalWrapper<Term> add(Term newTerm) {
        try{
            newTerm.setId(null);
            TermEntity termEntity = TermMapper.toTermEntity(newTerm);
            return new OptionalWrapper<>(
                    TermMapper.toTerm(termRepository.save(termEntity))
                    );
        } catch (Exception e) {
            return new OptionalWrapper<>(e);
        }
    }

    @Override
    public List<Term> listAll() {
        return termRepository.findAll().stream()
                .map(TermMapper::toTerm)
                .toList();
    }

    @Override
    public OptionalWrapper<Term> getActiveTerm() {
        TermEntity activeTerm = configurationRepository.getReferenceById(1).getActiveTerm();
        return new OptionalWrapper<>(TermMapper.toTerm(activeTerm));
    }
}
