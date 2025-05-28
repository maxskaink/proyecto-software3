package unicauca.microsubject.infrastructure.SQLrepository.Repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import unicauca.microsubject.application.out.TermRepositoryOutInt;
import unicauca.microsubject.domain.exception.NotFound;
import unicauca.microsubject.domain.model.OptionalWrapper;
import unicauca.microsubject.domain.model.Term;
import unicauca.microsubject.infrastructure.SQLrepository.JPARepository.JPAConfigurationRepository;
import unicauca.microsubject.infrastructure.SQLrepository.JPARepository.JPATermRepository;
import unicauca.microsubject.infrastructure.SQLrepository.entity.ConfigurationEntity;
import unicauca.microsubject.infrastructure.SQLrepository.entity.TermEntity;
import unicauca.microsubject.infrastructure.SQLrepository.mapper.TermMapper;

import java.util.List;

@AllArgsConstructor
@Repository
public class TermRepository implements TermRepositoryOutInt {

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

    @Override
    public OptionalWrapper<Term> setActiveTerm(Integer termId) {

        TermEntity searchTerm = termRepository.findById(termId).orElse(null);

        if(searchTerm == null)
            return new OptionalWrapper<>(new NotFound("Term with id " + termId + " was not found"));

        ConfigurationEntity conf = configurationRepository.getReferenceById(1);
        conf.setActiveTerm(searchTerm);
        configurationRepository.save(conf);

        return new OptionalWrapper<>( TermMapper.toTerm(searchTerm) );
    }
}
