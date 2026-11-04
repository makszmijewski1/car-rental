package pl.maksymilianzmijewski.carRental.reservation.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequest {
    @NotNull
    private Long carId;

    @NotNull
    private Long customerId;

    @NotNull
    @FutureOrPresent
    private LocalDate startDate;

    @NotNull
    @Future
    private LocalDate endDate;
}
