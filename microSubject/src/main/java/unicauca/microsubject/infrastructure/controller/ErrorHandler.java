package unicauca.microsubject.infrastructure.controller;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import unicauca.microsubject.domain.exception.DuplicateInformation;
import unicauca.microsubject.domain.exception.InvalidValue;
import unicauca.microsubject.domain.exception.NotFound;
import unicauca.microsubject.domain.exception.Unauthorized;
import unicauca.microsubject.infrastructure.dto.ErrorDTO;


@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    /**
     * Handler DuplicateInformation exception
     * @param exception exception threw
     * @param request request of the client
     * @return The appropriate error
     */
    @ExceptionHandler(DuplicateInformation.class)
    public ResponseEntity<ErrorDTO> handlerDuplicateInformation(DuplicateInformation exception, WebRequest request){
        ErrorDTO response = new ErrorDTO(
                "DuplicateInformation, some value is already exist",
                exception.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    /**
     * Handler InvalidValue exception
     * @param exception exception threw
     * @param request request of the client
     * @return The appropriate error
     */
    @ExceptionHandler(InvalidValue.class)
    public ResponseEntity<ErrorDTO> handlerInvalidValue( InvalidValue exception, WebRequest request){
        ErrorDTO response = new ErrorDTO(
                "Invalid Value: The values sender are invalid",
                exception.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }
    /**
     * Handler NotFound exception
     * @param exception exception threw
     * @param request request of the client
     * @return The appropriate error
     */
    @ExceptionHandler(NotFound.class)
    public ResponseEntity<ErrorDTO> handlerNotFound( NotFound exception, WebRequest request){
        ErrorDTO response = new ErrorDTO(
                "Not Found: The source doesn't exist",
                exception.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }
    /**
     * Handler Unauthorized exception
     * @param exception exception threw
     * @param request request of the client
     * @return The appropriate error
     */
    @ExceptionHandler(Unauthorized.class)
    public ResponseEntity<ErrorDTO> handlerUnauthorized( Unauthorized exception, WebRequest request){
        ErrorDTO response = new ErrorDTO(
                "Unauthorized: You don't have the permission",
                exception.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDTO> handlerDuplicated(DataIntegrityViolationException exception, WebRequest request){
        ErrorDTO response = new ErrorDTO(
                "Data integrity is being violated",
                exception.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    /**
     * Handler for validation failures on method/service arguments.
     * Triggered when a method parameter annotated with @NotNull, @Size, etc. is invalid.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDTO> handleConstraintViolation(ConstraintViolationException exception, WebRequest request) {
        String message = exception.getConstraintViolations()
                .stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .findFirst()
                .orElse("Validation error");

        ErrorDTO response = new ErrorDTO(
                "Validation failed for service parameters",
                message
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    /**
     * Handler for validation failures on request bodies (DTOs).
     * Triggered when @Valid fails in controller method parameters.
     */
//    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, WebRequest request) {
        String message = exception.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Invalid input");

        ErrorDTO response = new ErrorDTO(
                "Validation failed for request body",
                message
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

}