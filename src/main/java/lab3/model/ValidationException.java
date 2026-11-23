package lab3.model;

import java.util.List;

// Ошибки проверки параметров запроса.
public class ValidationException extends Exception {
    private final List<String> errors;

    public ValidationException(List<String> errors) {
        super(String.join(" ", errors));
        this.errors = List.copyOf(errors);
    }

    public List<String> getErrors() {
        return errors;
    }
}
