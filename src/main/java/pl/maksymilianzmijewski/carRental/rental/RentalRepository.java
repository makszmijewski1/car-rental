package pl.maksymilianzmijewski.carRental.rental;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    boolean existsByReservationReservationId(Long reservationId);
    List<Rental> findByRentalStatus(RentalStatus rentalStatus);
}
