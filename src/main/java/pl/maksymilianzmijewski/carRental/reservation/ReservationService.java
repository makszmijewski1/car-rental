package pl.maksymilianzmijewski.carRental.reservation;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.maksymilianzmijewski.carRental.car.Car;
import pl.maksymilianzmijewski.carRental.car.CarRepository;
import pl.maksymilianzmijewski.carRental.car.CarStatus;
import pl.maksymilianzmijewski.carRental.common.exception.CarNotFoundException;
import pl.maksymilianzmijewski.carRental.common.exception.CustomerNotFoundException;
import pl.maksymilianzmijewski.carRental.common.exception.ReservationConflictException;
import pl.maksymilianzmijewski.carRental.customer.Customer;
import pl.maksymilianzmijewski.carRental.customer.CustomerRepository;
import pl.maksymilianzmijewski.carRental.reservation.dto.ReservationRequest;
import pl.maksymilianzmijewski.carRental.reservation.dto.ReservationResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ModelMapper modelMapper;


    public ReservationResponse createReservation(@Valid ReservationRequest reservationRequest) {
        if (!reservationRequest.getEndDate().isAfter(reservationRequest.getStartDate())) {
            throw new IllegalArgumentException("The end date must be after the start date");
        }

        Car car = carRepository.findById(reservationRequest.getCarId())
                .orElseThrow(() -> new CarNotFoundException(reservationRequest.getCarId()));

        Customer customer = customerRepository.findById(reservationRequest.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException(reservationRequest.getCustomerId()));

        if(car.getCarStatus() == CarStatus.IN_SERVICE)
            throw new ReservationConflictException("Car in service");

        boolean overlaps = reservationRepository.existsOverlapping(car.getCarId(), reservationRequest.getStartDate(),
                reservationRequest.getEndDate(), ReservationStatus.ACTIVE);
        if(overlaps)
            throw new ReservationConflictException("The car is already reserved for this period.");

        Reservation reservation = new Reservation();
        reservation.setCar(car);
        reservation.setCustomer(customer);
        reservation.setStartDate(reservationRequest.getStartDate());
        reservation.setEndDate(reservationRequest.getEndDate());
        reservation.setTotalPrice(calculatePrice(car, reservationRequest.getStartDate(), reservationRequest.getEndDate()));
        reservation.setReservationStatus(ReservationStatus.ACTIVE);
        reservation.setPricePerDay(car.getPricePerDay());

        Reservation savedReservation = reservationRepository.save(reservation);

        return modelMapper.map(savedReservation, ReservationResponse.class);
    }

    private BigDecimal calculatePrice(Car car, LocalDate start, LocalDate end) {
        long days = ChronoUnit.DAYS.between(start, end);
        return car.getPricePerDay().multiply(BigDecimal.valueOf(days));
    }
}
