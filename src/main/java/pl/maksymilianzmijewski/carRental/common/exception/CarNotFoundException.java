package pl.maksymilianzmijewski.carRental.common.exception;

public class CarNotFoundException extends RuntimeException{
    public CarNotFoundException(Long carId){
        super("There is no car with ID: " + carId);
    }
}
