package pl.maksymilianzmijewski.carRental.common.exception;

public class CustomerNotFoundException extends RuntimeException{
    public CustomerNotFoundException(Long id){
        super("There is no customer with ID: " + id);
    }

}
