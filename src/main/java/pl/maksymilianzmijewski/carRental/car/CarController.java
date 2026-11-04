package pl.maksymilianzmijewski.carRental.car;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.maksymilianzmijewski.carRental.car.dto.CarRequest;
import pl.maksymilianzmijewski.carRental.car.dto.CarResponse;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CarController {

    @Autowired
    private CarService carService;

    @PostMapping("/admin/car")
    public ResponseEntity<CarResponse> createCar(@Valid @RequestBody CarRequest carRequest){
        CarResponse carResponse = carService.createCar(carRequest);
        return new ResponseEntity<>(carResponse, HttpStatus.CREATED);
    }

    @GetMapping("/public/cars")
    public ResponseEntity<List<CarResponse>> getAllCars(){
        List<CarResponse> carResponseList = carService.getAllCars();
        return new ResponseEntity<>(carResponseList, HttpStatus.OK);
    }

    @GetMapping("public/car/{carId}")
    public ResponseEntity<CarResponse> getCarById(@PathVariable Long carId){
        CarResponse carResponse = carService.getCarById(carId);
        return new ResponseEntity<>(carResponse, HttpStatus.OK);
    }

    @PutMapping("/admin/car/{carId}")
    public ResponseEntity<CarResponse> updateCar(@PathVariable Long carId, @RequestBody CarRequest carRequest){
        CarResponse carResponse = carService.updateCar(carId, carRequest);
        return new ResponseEntity<>(carResponse, HttpStatus.OK);
    }

    @DeleteMapping("/admin/car/{carId}")
    public ResponseEntity<CarResponse> deleteCar(@PathVariable Long carId){
        CarResponse carResponse = carService.deleteCar(carId);
        return new ResponseEntity<>(carResponse, HttpStatus.OK);
    }
}
