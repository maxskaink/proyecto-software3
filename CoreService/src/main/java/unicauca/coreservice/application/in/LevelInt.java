package unicauca.coreservice.application.in;

import java.util.List;

import org.springframework.stereotype.Component;

import unicauca.coreservice.domain.model.Level;
import jakarta.validation.constraints.NotNull;


public interface LevelInt {
    /**
     * Add a new level to the repository.
     *
     * @param newLevel The level to be added.
     * @param uid uid of the user to validate access
     * @return An OptionalWrapper containing the added level if successful, or an empty OptionalWrapper if not.
     */
    Level add(
            @NotNull
            Level newLevel,
            @NotNull
            Integer criterionId,
            @NotNull(message = "The uid can not be null")
            String uid
    ) throws Exception;

    /**
     * Retrieve a level by its unique identifier.
     *
     * @param id The unique identifier of the level.
     * @param uid uid of the user to validate access
     * @return An OptionalWrapper containing the found level if successful, or an empty OptionalWrapper if not.
     */
    Level getById(
            @NotNull
            Integer id,
            @NotNull(message = "The uid can not be null")
            String uid
    ) throws Exception;

    /**
     * Retrieve a level by its unique identifier.
     *
     * @param criterionId The unique identifier of the criterion.
     * @param uid uid of the user to validate access
     * @return A List of levels associated with the given criterion ID.
     */
    List<Level> listByCriterionId(
            @NotNull
            Integer criterionId,
            @NotNull(message = "The uid can not be null")
            String uid
    ) throws Exception;

    /**
     * Update a criterion level and retrieves the level updated
     *
     * @param levelId id of the level to update
     * @param newLevel level to update
     * @param uid uid of the user to validate access
     * @return The level updated if  is succesful or the exception if is not
     */
    Level update(
            @NotNull
            Integer levelId,
            @NotNull
            Level newLevel,
            @NotNull(message = "The uid can not be null")
            String uid
    ) throws Exception;

    /**
     * Remove a level from the repository by its unique identifier.
     *
     * @param id The unique identifier of the level to be removed.
     * @param uid uid of the user to validate access
     * @return An OptionalWrapper containing the removed level if successful, or an empty OptionalWrapper if not.
     */
    Level remove(
            @NotNull
            Integer id,
            @NotNull(message = "The uid can not be null")
            String uid
    ) throws Exception;




}

