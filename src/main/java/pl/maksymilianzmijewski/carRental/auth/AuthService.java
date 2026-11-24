package pl.maksymilianzmijewski.carRental.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.maksymilianzmijewski.carRental.auth.dto.AuthResponse;
import pl.maksymilianzmijewski.carRental.auth.dto.LoginRequest;
import pl.maksymilianzmijewski.carRental.auth.dto.RegisterRequest;
import pl.maksymilianzmijewski.carRental.auth.dto.RegisterResponse;
import pl.maksymilianzmijewski.carRental.common.exception.DuplicateResourceException;
import pl.maksymilianzmijewski.carRental.customer.Customer;
import pl.maksymilianzmijewski.carRental.customer.CustomerRepository;
import pl.maksymilianzmijewski.carRental.security.JwtService;
import pl.maksymilianzmijewski.carRental.user.User;
import pl.maksymilianzmijewski.carRental.user.UserRepository;
import pl.maksymilianzmijewski.carRental.user.UserRole;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;

    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        String email = request.getEmail().trim().toLowerCase();

        if(userRepository.existsByEmail(email) || customerRepository.existsByEmail(email))
            throw new DuplicateResourceException("Account with this email already exists");

        Customer customer = new Customer();
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());
        customer.setPhoneNumber(request.getPhone());
        customer.setDriverLicenseNumber(request.getDriverLicenseNumber());
        Customer savedCustomer = customerRepository.save(customer);

        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setUserRole(UserRole.CUSTOMER);
        user.setCustomer(savedCustomer);
        User savedUser = userRepository.save(user);

        return new RegisterResponse(savedUser.getUserId(), savedUser.getEmail(), savedUser.getUserRole());
    }

    public AuthResponse login(LoginRequest loginRequest){
        String email = loginRequest.getEmail().trim().toLowerCase();

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, loginRequest.getPassword())
        );

        User user = userRepository.findByEmail(email).orElseThrow();
        String token = jwtService.generateToken(user.getEmail(), user.getUserRole());
        return new AuthResponse(token, user.getEmail(), user.getUserRole());
    }
}
