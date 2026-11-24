package pl.maksymilianzmijewski.carRental.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.maksymilianzmijewski.carRental.auth.dto.AuthResponse;
import pl.maksymilianzmijewski.carRental.auth.dto.LoginRequest;
import pl.maksymilianzmijewski.carRental.auth.dto.RegisterRequest;
import pl.maksymilianzmijewski.carRental.auth.dto.RegisterResponse;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

}
