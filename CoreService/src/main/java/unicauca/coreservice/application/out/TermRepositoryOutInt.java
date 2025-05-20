package unicauca.coreservice.application.out;

import unicauca.coreservice.domain.model.OptionalWrapper;
import unicauca.coreservice.domain.model.Term;

import java.util.List;

public interface TermRepositoryOutInt {
    /**
     * Adds a new Term to the repository.
     *
     * @param newTerm the instance of the new Term to be added
     * @return an OptionalWrapper containing the added Term instance or the exception if the operation fails
     */
    OptionalWrapper<Term> add(Term newTerm);

    /**
     * Retrieves a list of all available Term instances.
     *
     * @return a List of Term instances or an empty list if no Term instances are available.
     */
    List<Term> listAll();

    /**
     * Retrieves the current active Term.
     *
     * @return an OptionalWrapper containing the active Term instance, or an exception if the retrieval fails.
     */
    OptionalWrapper<Term> getActiveTerm();

    /**
     * Sets the active Term in the repository based on the provided term ID.
     *
     * @param termId the unique identifier of the Term to be set as active
     * @return an OptionalWrapper containing the updated active Term, or an exception if the operation fails
     */
    OptionalWrapper<Term> setActiveTerm(Integer termId);
}
