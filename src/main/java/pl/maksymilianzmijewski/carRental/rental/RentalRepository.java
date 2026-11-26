package pl.maksymilianzmijewski.carRental.rental;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    boolean existsByReservationReservationId(Long reservationId);
    List<Rental> findByRentalStatus(RentalStatus rentalStatus);

    @Query("""
       SELECT SUM(r.finalPrice) FROM Rental r
       WHERE r.rentalStatus = :status
         AND r.returnDate BETWEEN :from AND :to
       """)
    BigDecimal sumRevenue(@Param("status") RentalStatus status,
                          @Param("from") LocalDate from,
                          @Param("to") LocalDate to);

    @Query("""
       SELECT COUNT(r) FROM Rental r
       WHERE r.rentalStatus = :status
         AND r.returnDate BETWEEN :from AND :to
       """)
    Long countReturned(@Param("status") RentalStatus status,
                       @Param("from") LocalDate from,
                       @Param("to") LocalDate to);
}
