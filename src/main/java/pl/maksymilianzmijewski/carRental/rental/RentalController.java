package pl.maksymilianzmijewski.carRental.rental;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.maksymilianzmijewski.carRental.rental.dto.RentalRequest;
import pl.maksymilianzmijewski.carRental.rental.dto.RentalResponse;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RentalController {

    @Autowired
    private RentalService rentalService;

    @PostMapping("/admin/rentals")
    public ResponseEntity<RentalResponse> rentCar(@Valid @RequestBody RentalRequest rentalRequest){
        RentalResponse rentalResponse = rentalService.rentCar(rentalRequest);
        return new ResponseEntity<>(rentalResponse, HttpStatus.OK);
    }

    @PostMapping("/admin/rentals/return/{rentalId}")
    public ResponseEntity<RentalResponse> returnCar(@PathVariable Long rentalId){
        RentalResponse rentalResponse = rentalService.returnCar(rentalId);
        return new ResponseEntity<>(rentalResponse, HttpStatus.OK);
    }

    /*
    GET /rentals?status=ACTIVE daje aktywne wypożyczenia, a ?status=RETURNED zakończone.
     To od razu pokrywa punkt "przeglądanie aktywnych i zakończonych wypożyczeń" z Twojego zakresu.
     */
    @GetMapping("/admin/rentals")
    public ResponseEntity<List<RentalResponse>> getAll(@RequestParam(required = false) RentalStatus status){
        List<RentalResponse> rentalResponseList = rentalService.findAll(status);
        return new ResponseEntity<>(rentalResponseList, HttpStatus.OK);
    }

    @GetMapping("/admin/rentals/{rentalId}")
    public ResponseEntity<RentalResponse> getById(@PathVariable Long rentalId) {
        RentalResponse rentalResponse = rentalService.findById(rentalId);
        return new ResponseEntity<>(rentalResponse, HttpStatus.OK);
    }
}
