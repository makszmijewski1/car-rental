package pl.maksymilianzmijewski.carRental.reservation;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.maksymilianzmijewski.carRental.car.Car;
import pl.maksymilianzmijewski.carRental.customer.Customer;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "car_id")
    private Car car;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal totalPrice;
    private BigDecimal pricePerDay;

    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;
}
