package pl.maksymilianzmijewski.carRental.reservation;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.maksymilianzmijewski.carRental.reservation.dto.ReservationRequest;
import pl.maksymilianzmijewski.carRental.reservation.dto.ReservationResponse;

@RestController
@RequestMapping("/api")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping("/public/reservations")
    public ResponseEntity<ReservationResponse> createReservation(
            @Valid @RequestBody ReservationRequest reservationRequest) {
        ReservationResponse reservationResponse = reservationService.createReservation(reservationRequest);
        return new ResponseEntity<>(reservationResponse, HttpStatus.OK);
    }
}
