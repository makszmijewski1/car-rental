package pl.maksymilianzmijewski.carRental.customer;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.maksymilianzmijewski.carRental.customer.dto.CustomerRequest;
import pl.maksymilianzmijewski.carRental.customer.dto.CustomerResponse;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @PostMapping("/public/customer")
    public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody
                                           CustomerRequest customerRequest){
        CustomerResponse customerResponse = customerService.createCustomer(customerRequest);
        return new ResponseEntity<>(customerResponse, HttpStatus.CREATED);
    }

    @GetMapping("/admin/customers")
    public ResponseEntity<List<CustomerResponse>> getAllCustomers(){
        List<CustomerResponse> customerResponseList = customerService.getAllCustomers();
        return new ResponseEntity<>(customerResponseList, HttpStatus.OK);
    }

    @GetMapping("/admin/customers/{customerId}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable Long customerId){
        CustomerResponse customerResponse = customerService.getCustomerById(customerId);
        return new ResponseEntity<>(customerResponse, HttpStatus.OK);
    }

    @PutMapping("/admin/customers/{customerId}")
    public ResponseEntity<CustomerResponse> updateCustomer(@PathVariable Long customerId,
                                                           @RequestBody CustomerRequest customerRequest){
        CustomerResponse customerResponse = customerService.updateCustomer(customerId, customerRequest);
        return new ResponseEntity<>(customerResponse, HttpStatus.OK);
    }

    @DeleteMapping("/admin/customers/{customerId}")
    public ResponseEntity<CustomerResponse> deleteCustomer(@PathVariable Long customerId){
        CustomerResponse customerResponse = customerService.deleteCustomer(customerId);
        return new ResponseEntity<>(customerResponse, HttpStatus.OK);
    }
}
