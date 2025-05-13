package unicauca.coreservice.application.out;

import unicauca.coreservice.domain.model.OptionalWrapper;
import unicauca.coreservice.domain.model.Periodo;

import java.util.List;

public interface PeriodoRepositoryOut {
    /**
     * Adds a new Periodo to the repository.
     *
     * @param newPeriodo the instance of the new Periodo to be added
     * @return an OptionalWrapper containing the added Periodo instance or the exception if the operation fails
     */
    OptionalWrapper<Periodo> addPeriodo(Periodo newPeriodo);

    /**
     * Retrieves a list of all available Periodo instances.
     *
     * @return a List of Periodo instances or an empty list if no Periodo instances are available.
     */
    List<Periodo> listPeriodo();

    /**
     * Retrieves the current active Periodo.
     *
     * @return an OptionalWrapper containing the active Periodo instance, or an exception if the retrieval fails.
     */
    OptionalWrapper<Periodo> getActualPeriodo();
}
