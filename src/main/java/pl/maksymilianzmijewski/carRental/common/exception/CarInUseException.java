package pl.maksymilianzmijewski.carRental.common.exception;

public class CarInUseException extends RuntimeException{
    public CarInUseException(String message) {
        super(message);
    }
}
