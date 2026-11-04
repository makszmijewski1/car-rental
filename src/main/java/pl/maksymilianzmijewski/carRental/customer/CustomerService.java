package pl.maksymilianzmijewski.carRental.customer;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;
import pl.maksymilianzmijewski.carRental.common.exception.CustomerNotFoundException;
import pl.maksymilianzmijewski.carRental.common.exception.DuplicateResourceException;
import pl.maksymilianzmijewski.carRental.customer.dto.CustomerRequest;
import pl.maksymilianzmijewski.carRental.customer.dto.CustomerResponse;

import java.util.List;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ModelMapper modelMapper;


    public CustomerResponse createCustomer(CustomerRequest customerRequest) {
        if(customerRepository.existsByEmail(customerRequest.getEmail())){
            throw new DuplicateResourceException("Client with this email already exist");
        }
        Customer customer = modelMapper.map(customerRequest, Customer.class);
        Customer savedCustomer = customerRepository.save(customer);
        return modelMapper.map(savedCustomer, CustomerResponse.class);
    }

    public List<CustomerResponse> getAllCustomers() {
        List<CustomerResponse> customerResponseList = customerRepository.findAll()
                .stream()
                .map(customer -> modelMapper.map(customer, CustomerResponse.class))
                .toList();

        return customerResponseList;
    }

    public CustomerResponse getCustomerById(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        return modelMapper.map(customer, CustomerResponse.class);
    }

    public CustomerResponse updateCustomer(Long customerId, CustomerRequest customerRequest) {
        Customer savedCustomer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        Customer customer = modelMapper.map(customerRequest, Customer.class);
        customer.setCustomerId(customerId);
        savedCustomer = customerRepository.save(customer);
        return modelMapper.map(savedCustomer, CustomerResponse.class);
    }

    public CustomerResponse deleteCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        customerRepository.delete(customer);
        return modelMapper.map(customer, CustomerResponse.class);
    }
}
