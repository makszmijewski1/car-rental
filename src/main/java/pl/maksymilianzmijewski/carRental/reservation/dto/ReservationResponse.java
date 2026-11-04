package pl.maksymilianzmijewski.carRental.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.maksymilianzmijewski.carRental.reservation.ReservationStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponse {
    private Long reservationId;
    private Long carId;
    private Long customerId;
    private String carBrand;
    private String carModel;
    private String customerFirstName;
    private String customerLastName;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal totalPrice;
    private BigDecimal pricePerDay;
    private ReservationStatus reservationStatus;
}
