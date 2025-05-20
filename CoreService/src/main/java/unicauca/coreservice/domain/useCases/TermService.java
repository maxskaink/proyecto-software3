package unicauca.coreservice.domain.useCases;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import unicauca.coreservice.application.in.TermInt;
import unicauca.coreservice.domain.model.OptionalWrapper;
import unicauca.coreservice.domain.model.Term;
import unicauca.coreservice.infrastructure.SQLrepository.Repository.TermRepository;

import java.util.List;

@AllArgsConstructor
@Validated
public class TermService implements TermInt {

    private final TermRepository repositoryTerm;

    @Override
    public Term add(Term term) throws Exception {

        OptionalWrapper<Term> response = repositoryTerm.add(term);

        return response.getValue().orElseThrow(response::getException);
    }

    @Override
    public List<Term> listAll() {
        return repositoryTerm.listAll();
    }

    @Override
    public Term getActiveTerm() throws Exception {

        OptionalWrapper<Term> response = repositoryTerm.getActiveTerm();

        return response.getValue().orElseThrow(response::getException);
    }

    @Override
    public Term setActiveTerm(Integer termId) throws Exception {

        OptionalWrapper<Term> response = repositoryTerm.setActiveTerm(termId);

        return response.getValue().orElseThrow(response::getException);
    }
}
