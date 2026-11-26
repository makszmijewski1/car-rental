package pl.maksymilianzmijewski.carRental.reservation;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.maksymilianzmijewski.carRental.car.Car;
import pl.maksymilianzmijewski.carRental.car.CarRepository;
import pl.maksymilianzmijewski.carRental.car.CarStatus;
import pl.maksymilianzmijewski.carRental.common.exception.CarNotFoundException;
import pl.maksymilianzmijewski.carRental.common.exception.CustomerNotFoundException;
import pl.maksymilianzmijewski.carRental.common.exception.ReservationConflictException;
import pl.maksymilianzmijewski.carRental.common.exception.ReservationNotFoundException;
import pl.maksymilianzmijewski.carRental.customer.Customer;
import pl.maksymilianzmijewski.carRental.customer.CustomerRepository;
import pl.maksymilianzmijewski.carRental.rental.RentalRepository;
import pl.maksymilianzmijewski.carRental.reservation.dto.ReservationRequest;
import pl.maksymilianzmijewski.carRental.reservation.dto.ReservationResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;


@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
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

    @Transactional(readOnly = true)
    public List<ReservationResponse> getAllReservations(ReservationStatus reservationStatus) {
        List<Reservation> reservations = (reservationStatus == null)
                ? reservationRepository.findAll()
                : reservationRepository.findByReservationStatus(reservationStatus);

        return reservations.stream()
                .map(reservation -> modelMapper.map(reservation, ReservationResponse.class))
                .toList();
    }

    @Transactional(readOnly = true)
    public ReservationResponse getReservationById(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException(reservationId));

        return modelMapper.map(reservation, ReservationResponse.class);
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> getReservationsByCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        List<ReservationResponse> reservationResponseList = reservationRepository.findByCustomerCustomerId(customerId)
                .stream()
                .map(reservation -> modelMapper.map(reservation, ReservationResponse.class))
                .toList();
        return reservationResponseList;
    }

    @Transactional
    public ReservationResponse cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException(reservationId));

        if(reservation.getReservationStatus() != ReservationStatus.ACTIVE)
            throw new ReservationConflictException("Only an active reservation can be cancelled");

        if (rentalRepository.existsByReservationReservationId(reservationId)) {
            throw new ReservationConflictException("A reservation that has an associated rental cannot be cancelled");
        }

        reservation.setReservationStatus(ReservationStatus.CANCELLED);
        return modelMapper.map(reservation, ReservationResponse.class);
    }


    // TO JEST DLA KLIENTA ZALOGOWANEGO, POBIERA JEGO ID Z KONTA NIE REQUESTA
    @Transactional
    public ReservationResponse findReservationByReservationIdForCustomer(Long reservationId, Long customerId){
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException(reservationId));

        if (!reservation.getCustomer().getCustomerId().equals(customerId)) {
            throw new ReservationNotFoundException(reservationId);
        }
        return modelMapper.map(reservation, ReservationResponse.class);
    }

    // TO JEST DLA KLIENTA ZALOGOWANEGO, POBIERA JEGO ID Z KONTA NIE REQUESTA
    @Transactional
    public ReservationResponse cancelReservationForCustomer(Long reservationId, Long customerId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException(reservationId));

        if (!reservation.getCustomer().getCustomerId().equals(customerId)) {
            throw new ReservationNotFoundException(reservationId);
        }
        return cancelReservation(reservationId);
    }




}
