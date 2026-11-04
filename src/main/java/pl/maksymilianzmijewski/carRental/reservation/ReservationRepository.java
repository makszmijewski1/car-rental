package pl.maksymilianzmijewski.carRental.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("""
           SELECT COUNT(r) > 0 FROM Reservation r
           WHERE r.car.id = :carId
             AND r.reservationStatus = :status
             AND r.startDate < :end
             AND r.endDate > :start
           """)
    boolean existsOverlapping(@Param("carId") Long carId,
                              @Param("start") LocalDate start,
                              @Param("end") LocalDate end,
                              @Param("status") ReservationStatus status);

    List<Reservation> findByCustomerCustomerId(Long customerId);
}
