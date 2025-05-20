package unicauca.coreservice.application.in;

import jakarta.validation.constraints.NotNull;
import unicauca.coreservice.domain.model.Term;

import java.util.List;

public interface TermInt {
    /**
     * Adds the provided term to the current instance.
     *
     * @param term the Term instance to be added; must not be null
     * @return the resulting Term after performing the add operation
     */
    Term add(@NotNull(message="Term can not be null") Term term) throws Exception;

    /**
     * Retrieves a list of all Term objects associated with the implementing entity.
     *
     * @return a list of Term instances
     */
    List<Term> listAll();

    /**
     * Retrieves the currently active term for the implementing entity.
     *
     * @return the active Term, or null if no active term is set
     */
    Term getActiveTerm() throws Exception;

    /**
     * Sets the currently active term to the one identified by the given term ID.
     *
     * @param termId the unique identifier of the term to be set as active; must not be null
     * @return the Term instance corresponding to the newly active term
     */
    Term setActiveTerm(@NotNull(message="TermId can not be null") Integer termId) throws Exception;
}
