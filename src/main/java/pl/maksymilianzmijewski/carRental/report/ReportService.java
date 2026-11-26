package pl.maksymilianzmijewski.carRental.report;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.maksymilianzmijewski.carRental.car.CarRepository;
import pl.maksymilianzmijewski.carRental.car.CarStatus;
import pl.maksymilianzmijewski.carRental.rental.RentalRepository;
import pl.maksymilianzmijewski.carRental.rental.RentalStatus;
import pl.maksymilianzmijewski.carRental.report.dto.FleetStatusCount;
import pl.maksymilianzmijewski.carRental.report.dto.RevenueReport;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final CarRepository carRepository;
    private final RentalRepository rentalRepository;

    @Transactional(readOnly = true)
    public List<FleetStatusCount> getFleetStatus() {
        Map<CarStatus, Long> counts = carRepository.countByStatus().stream()
                .collect(Collectors.toMap(FleetStatusCount::getCarStatus, FleetStatusCount::getCount));

        return Arrays.stream(CarStatus.values())
                .map(s -> new FleetStatusCount(s, counts.getOrDefault(s, 0L)))
                .toList();
    }

    @Transactional(readOnly = true)
    public RevenueReport getRevenue(LocalDate from, LocalDate to) {
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("Data początkowa nie może być po dacie końcowej");
        }
        BigDecimal sum = rentalRepository.sumRevenue(RentalStatus.RETURNED, from, to);
        Long count = rentalRepository.countReturned(RentalStatus.RETURNED, from, to);

        return new RevenueReport(from, to, count, sum != null ? sum : BigDecimal.ZERO);
    }
}
