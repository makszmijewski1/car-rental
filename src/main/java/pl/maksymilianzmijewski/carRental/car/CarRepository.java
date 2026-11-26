package pl.maksymilianzmijewski.carRental.car;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.maksymilianzmijewski.carRental.report.dto.FleetStatusCount;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    @Query("""
       SELECT new pl.maksymilianzmijewski.carRental.report.dto.FleetStatusCount(c.carStatus, COUNT(c))
       FROM Car c
       GROUP BY c.carStatus
       """)
    List<FleetStatusCount> countByStatus();
}
