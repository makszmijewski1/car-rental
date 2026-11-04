package pl.maksymilianzmijewski.carRental.common.exception;

public class ReservationConflictException extends RuntimeException{
    public ReservationConflictException(String message){
        super(message);
    }
}
