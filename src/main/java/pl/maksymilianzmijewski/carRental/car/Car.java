package pl.maksymilianzmijewski.carRental.car;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carId;
    private String brand;
    private String model;
    private Integer productionYear;
    private BigDecimal pricePerDay;

    @Enumerated(EnumType.STRING)
    private CarStatus carStatus;
}
