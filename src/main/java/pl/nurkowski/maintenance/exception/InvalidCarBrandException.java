package pl.nurkowski.maintenance.exception;

public class InvalidCarBrandException extends RuntimeException {
    public InvalidCarBrandException(String message) {
        super(message);
    }
}
