package pl.maksymilianzmijewski.carRental.car;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.maksymilianzmijewski.carRental.car.dto.CarRequest;
import pl.maksymilianzmijewski.carRental.car.dto.CarResponse;
import pl.maksymilianzmijewski.carRental.common.exception.CarNotFoundException;

import java.util.List;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private ModelMapper modelMapper;

    public CarResponse createCar(CarRequest carRequest) {
        Car car = modelMapper.map(carRequest, Car.class);
        car.setCarStatus(CarStatus.AVAILABLE);
        Car savedCar = carRepository.save(car);
        return modelMapper.map(savedCar, CarResponse.class);
    }

    public List<CarResponse> getAllCars() {
        List<CarResponse> carResponseList = carRepository.findAll()
                .stream()
                .map(car -> modelMapper.map(car, CarResponse.class))
                .toList();
        return carResponseList;
    }

    public CarResponse getCarById(Long carId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new CarNotFoundException(carId));

        return modelMapper.map(car, CarResponse.class);
    }

    public CarResponse updateCar(Long carId, CarRequest carRequest) {
        Car savedCar = carRepository.findById(carId)
                .orElseThrow(() -> new CarNotFoundException(carId));

        Car car = modelMapper.map(carRequest, Car.class);
        car.setCarId(carId);
        car.setCarStatus(CarStatus.AVAILABLE);
        savedCar = carRepository.save(car);
        return modelMapper.map(savedCar, CarResponse.class);
    }

    public CarResponse deleteCar(Long carId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new CarNotFoundException(carId));

        carRepository.delete(car);
        return modelMapper.map(car, CarResponse.class);
    }
}
