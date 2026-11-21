package pl.maksymilianzmijewski.carRental.rental;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.maksymilianzmijewski.carRental.car.Car;
import pl.maksymilianzmijewski.carRental.car.CarStatus;
import pl.maksymilianzmijewski.carRental.common.exception.RentalConflictException;
import pl.maksymilianzmijewski.carRental.common.exception.RentalNotFoundException;
import pl.maksymilianzmijewski.carRental.common.exception.ReservationNotFoundException;
import pl.maksymilianzmijewski.carRental.customer.CustomerRepository;
import pl.maksymilianzmijewski.carRental.rental.dto.RentalRequest;
import pl.maksymilianzmijewski.carRental.rental.dto.RentalResponse;
import pl.maksymilianzmijewski.carRental.reservation.Reservation;
import pl.maksymilianzmijewski.carRental.reservation.ReservationRepository;
import pl.maksymilianzmijewski.carRental.reservation.ReservationStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class RentalService {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private CustomerRepository customerRepository;


    @Transactional
    public RentalResponse rentCar(RentalRequest request) {
        Reservation reservation = reservationRepository.findById(request.getReservationId())
                .orElseThrow(() -> new ReservationNotFoundException(request.getReservationId()));

        if (reservation.getReservationStatus() != ReservationStatus.ACTIVE)
            throw new RentalConflictException("Only an active reservation can be used for a rental");

        if (rentalRepository.existsByReservationReservationId(request.getReservationId()))
            throw new RentalConflictException("This reservation already has a rental associated with it");

        Car car = reservation.getCar();
        if (car.getCarStatus() != CarStatus.AVAILABLE)
            throw new RentalConflictException("The vehicle is not available for release");

        Rental rental = new Rental();
        rental.setReservation(reservation);
        rental.setPickupDate(LocalDate.now());
        rental.setRentalStatus(RentalStatus.ACTIVE);
        car.setCarStatus(CarStatus.RENTED);
        rentalRepository.save(rental);
        return toResponse(rental);
    }

    @Transactional
    public RentalResponse returnCar(Long rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RentalNotFoundException(rentalId));

        if(rental.getRentalStatus() != RentalStatus.ACTIVE)
            throw new RentalConflictException("The rental has already ended");

        LocalDate today = LocalDate.now();
        Reservation reservation = rental.getReservation();
        Car car = reservation.getCar();

        rental.setReturnDate(today);
        rental.setFinalPrice(calculateFinalPrice(reservation, today));
        rental.setRentalStatus(RentalStatus.RETURNED);

        reservation.setReservationStatus(ReservationStatus.COMPLETED);
        car.setCarStatus(CarStatus.AVAILABLE);
        return toResponse(rental);
    }

    @Transactional(readOnly = true)
    public List<RentalResponse> findAll(RentalStatus status) {
        List<Rental> rentals = status == null
                ? rentalRepository.findAll()
                : rentalRepository.findByRentalStatus(status);
        return rentals.stream()
                .map(this::toResponse)
                .toList();
    }

    public RentalResponse findById(Long rentalId) {
        Rental rental =  rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RentalNotFoundException(rentalId));
        return toResponse(rental);
    }


    private RentalResponse toResponse(Rental rental) {
        Reservation reservation = rental.getReservation();
        return new RentalResponse(rental.getId(), reservation.getReservationId(),
                reservation.getCar().getBrand() + " " + reservation.getCar().getModel(),
                reservation.getCustomer().getFirstName() + " " + reservation.getCustomer().getLastName(),
                rental.getPickupDate(), rental.getReturnDate(), rental.getFinalPrice(),
                rental.getRentalStatus());
    }

    /*
    ChronoUnit.DAYS.between(endDate, returnDate) liczy, ile dni minęło od planowanego końca rezerwacji do faktycznego zwrotu.
Zwrot 15.11, koniec rezerwacji 13.11 → 2 (dwa dni spóźnienia).
Zwrot równo w terminie (13.11) → 0.
Zwrot wcześniej (11.11) → -2 (liczba ujemna, bo between(a, b) zwraca ujemną wartość, gdy b jest przed a).
Math.max(0, ...) ucina wartości ujemne do zera. Dzięki temu wcześniejszy zwrot nie daje "ujemnej kary" (zniżki). Wynik to zawsze 0 albo więcej.
lateFee to kara = cena za dobę auta × liczba dni spóźnienia. BigDecimal.valueOf(lateDays) zamienia long na BigDecimal, bo pieniędzy nie mnoży się zwykłymi liczbami. BigDecimal nie ma operatorów * i +, tylko metody .multiply() i .add().
Wynik: totalPrice z rezerwacji + lateFee.

Przykład: auto 200 zł/dobę, rezerwacja 10-13.11 (totalPrice = 600 zł), zwrot 15.11:

lateDays = 2
lateFee = 200 × 2 = 400 zł
finalPrice = 600 + 400 = 1000 zł

Przy zwrocie w terminie lub wcześniej finalPrice = 600 zł (bez zwrotu pieniędzy za wcześniejszy zwrot).

To jest decyzja biznesowa, nie reguła Javy. Możesz ją zmienić, np. kara = 1,5 × cena doby (.multiply(new BigDecimal("1.5"))) albo częściowy zwrot za wcześniejszy zwrot. W pracy opisz, jaką zasadę przyjąłeś i dlaczego.
     */
    private BigDecimal calculateFinalPrice(Reservation reservation, LocalDate returnDate) {
        long lateDays = Math.max(0, ChronoUnit.DAYS.between(reservation.getEndDate(), returnDate));
        BigDecimal lateFee = reservation.getCar().getPricePerDay()
                .multiply(BigDecimal.valueOf(lateDays));
        return reservation.getTotalPrice().add(lateFee);
    }

}
