package pl.maksymilianzmijewski.carRental.common.exception;

public class CustomerNotFoundException extends RuntimeException{
    public CustomerNotFoundException(Long customerId){
        super("There is no customer with ID: " + customerId);
    }

}
