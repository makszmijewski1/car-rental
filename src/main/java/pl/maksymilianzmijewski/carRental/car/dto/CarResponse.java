package pl.maksymilianzmijewski.carRental.car.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.maksymilianzmijewski.carRental.car.CarStatus;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarResponse {
    private Long carId;
    private String brand;
    private String model;
    private Integer productionYear;
    private BigDecimal pricePerDay;
    private CarStatus carStatus;
}
