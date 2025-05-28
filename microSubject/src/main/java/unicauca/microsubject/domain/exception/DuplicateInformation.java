package unicauca.microsubject.domain.exception;

import org.springframework.dao.DataIntegrityViolationException;

public class DuplicateInformation extends DataIntegrityViolationException {
    public DuplicateInformation(String message) {
        super(message);
    }
}
