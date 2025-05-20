package unicauca.coreservice.application.out;

import unicauca.coreservice.domain.model.Level;
import unicauca.coreservice.domain.model.OptionalWrapper;

import java.util.List;

public interface LevelRepositoryOutInt {

    /**
     * Add a new level to the repository.
     *
     * @param newLevel The level to be added.
     * @return An OptionalWrapper containing the added level if successful, or an empty OptionalWrapper if not.
     */
    OptionalWrapper<Level> add(Level newLevel);

    /**
     * Retrieve a level by its unique identifier.
     *
     * @param id The unique identifier of the level.
     * @return An OptionalWrapper containing the found level if successful, or an empty OptionalWrapper if not.
     */
    OptionalWrapper<Level> getById(Integer id);

    /**
     * Retrieve a level by its unique identifier.
     *
     * @param criterionId The unique identifier of the criterion.
     * @return An OptionalWrapper containing the found level if successful, or an empty OptionalWrapper if not.
     */
    OptionalWrapper<Level> getByCriterionId(Integer criterionId);

    /**
     * Remove a level from the repository by its unique identifier.
     *
     * @param id The unique identifier of the level to be removed.
     * @return An OptionalWrapper containing the removed level if successful, or an empty OptionalWrapper if not.
     */
    OptionalWrapper<Level> remove(Integer id);

    /**
     * List all levels in the repository.
     *
     * @return A list of all levels in the repository.
     */
    List<Level> listAll();
}
