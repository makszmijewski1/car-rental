package pl.maksymilianzmijewski.carRental.report.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.maksymilianzmijewski.carRental.car.CarStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FleetStatusCount {
    private CarStatus carStatus;
    private Long count;
}
