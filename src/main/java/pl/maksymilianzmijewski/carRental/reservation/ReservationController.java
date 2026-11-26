package pl.maksymilianzmijewski.carRental.reservation;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.maksymilianzmijewski.carRental.reservation.dto.ReservationRequest;
import pl.maksymilianzmijewski.carRental.reservation.dto.ReservationResponse;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping("/admin/reservations")
    public ResponseEntity<ReservationResponse> createReservation(
            @Valid @RequestBody ReservationRequest reservationRequest) {
        ReservationResponse reservationResponse = reservationService.createReservation(reservationRequest);
        return new ResponseEntity<>(reservationResponse, HttpStatus.OK);
    }

    @GetMapping("/admin/reservations")
    public ResponseEntity<List<ReservationResponse>> getAllReservations(@RequestParam(name = "status", required = false) ReservationStatus reservationStatus){
        List<ReservationResponse> reservationResponseList = reservationService.getAllReservations(reservationStatus);
        return new ResponseEntity<>(reservationResponseList, HttpStatus.OK);
    }

    @GetMapping("/admin/reservations/{reservationId}")
    public ResponseEntity<ReservationResponse> getReservationById(@PathVariable Long reservationId){
        ReservationResponse reservationResponse = reservationService.getReservationById(reservationId);
        return new ResponseEntity<>(reservationResponse, HttpStatus.OK);
    }

    @GetMapping("/admin/reservations/customer/{customerId}")
    public ResponseEntity<List<ReservationResponse>> getReservationsByCustomer(@PathVariable Long customerId){
        List<ReservationResponse> reservationResponseList = reservationService.getReservationsByCustomer(customerId);
        return new ResponseEntity<>(reservationResponseList, HttpStatus.OK);
    }

    @PutMapping("/admin/reservations/cancel/{reservationId}")
    public ResponseEntity<ReservationResponse> cancelReservation(@PathVariable Long reservationId){
        ReservationResponse reservationResponse = reservationService.cancelReservation(reservationId);
        return new ResponseEntity<>(reservationResponse, HttpStatus.OK);
    }
}
