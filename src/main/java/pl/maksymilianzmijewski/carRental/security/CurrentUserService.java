package pl.maksymilianzmijewski.carRental.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.maksymilianzmijewski.carRental.user.User;
import pl.maksymilianzmijewski.carRental.user.UserRepository;

@Service
@RequiredArgsConstructor
public class CurrentUserService {
    private final UserRepository userRepository;

    @Transactional
    public Long getCurrentCustomerId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new AccessDeniedException("There is no user"));

        if(user.getCustomer() == null)
            throw new AccessDeniedException("This account has no associated client");
        return user.getCustomer().getCustomerId();
    }
}
