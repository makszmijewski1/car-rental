package pl.maksymilianzmijewski.carRental.common.exception;

public class RentalConflictException extends RuntimeException{
    public RentalConflictException(String message) {
        super(message);
    }
}
