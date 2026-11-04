package pl.maksymilianzmijewski.carRental.common.exception;

public class ReservationNotFoundException extends RuntimeException{
    public ReservationNotFoundException(Long reservationId){
        super("There is no reservation with ID: " + reservationId);
    }
}
