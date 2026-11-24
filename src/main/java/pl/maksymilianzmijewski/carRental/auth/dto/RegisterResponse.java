package pl.maksymilianzmijewski.carRental.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.maksymilianzmijewski.carRental.user.UserRole;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponse {
    private Long userId;
    private String email;
    private UserRole userRole;
}
