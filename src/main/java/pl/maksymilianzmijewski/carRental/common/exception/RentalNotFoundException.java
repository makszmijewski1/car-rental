package pl.maksymilianzmijewski.carRental.common.exception;

public class RentalNotFoundException extends RuntimeException{
    public RentalNotFoundException(Long rentalId) {
        super("There is no rental with ID: " + rentalId);
    }
}
