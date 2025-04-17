package unicauca.coreservice.domain.model;

import lombok.Getter;

import java.util.Optional;

/**
 * A more robust Optional for management of possible exception
 * @param <T> The type of the return
 */
@Getter
public class OptionalWrapper<T> {
    private final Optional<T> value;
    private final Exception exception;

    /**
     * The constructor for a successful response
     * @param optional The object to save
     */
    public OptionalWrapper(T optional) {
        this.value = Optional.ofNullable(optional);
        this.exception = null;
    }

    /**
     * The constructor for an exception response
     * @param exception exception to save
     */
    public OptionalWrapper(Exception exception) {
        this.value = Optional.empty();
        this.exception = exception;
    }

    public OptionalWrapper(Optional<T> optional) {
        this.value = optional;
        this.exception = null;
    }
}
