package pl.maksymilianzmijewski.carRental.rental.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.maksymilianzmijewski.carRental.rental.RentalStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalResponse {
    private Long rentalId;
    private Long reservationId;
    private String carName;
    private String customerName;
    private LocalDate pickupDate;
    private LocalDate returnDate;
    private BigDecimal finalPrice;
    private RentalStatus rentalStatus;
}
