package pl.maksymilianzmijewski.carRental.reservation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.maksymilianzmijewski.carRental.reservation.dto.MyReservationRequest;
import pl.maksymilianzmijewski.carRental.reservation.dto.ReservationRequest;
import pl.maksymilianzmijewski.carRental.reservation.dto.ReservationResponse;
import pl.maksymilianzmijewski.carRental.security.CurrentUserService;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class MyReservationController {
    private final ReservationService reservationService;
    private final CurrentUserService currentUserService;

    @PostMapping
    public ResponseEntity<ReservationResponse> create(@Valid @RequestBody MyReservationRequest request) {
        Long customerId = currentUserService.getCurrentCustomerId();
        ReservationResponse reservationResponse = reservationService.createReservation(new ReservationRequest(request.getCarId(), customerId, request.getStartDate(), request.getEndDate()));
        return new ResponseEntity<>(reservationResponse, HttpStatus.OK);
    }

    @GetMapping("/my")
    public ResponseEntity<List<ReservationResponse>> getMineReservations() {
        List<ReservationResponse> reservationResponseList = reservationService.getReservationsByCustomer(currentUserService.getCurrentCustomerId());
        return new ResponseEntity<>(reservationResponseList, HttpStatus.OK);
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<ReservationResponse> getReservationByReservationId(@PathVariable Long reservationId) {
        ReservationResponse reservationResponse = reservationService.findReservationByReservationIdForCustomer(reservationId, currentUserService.getCurrentCustomerId());
        return new ResponseEntity<>(reservationResponse, HttpStatus.OK);
    }

    @PutMapping("/{reservationId}/cancel")
    public ResponseEntity<ReservationResponse> cancel(@PathVariable Long reservationId) {
        ReservationResponse reservationResponse = reservationService.cancelReservationForCustomer(reservationId, currentUserService.getCurrentCustomerId());
        return new ResponseEntity<>(reservationResponse, HttpStatus.OK);
    }
}
