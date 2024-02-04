package com.app.car.service;

import com.app.car.model.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CarService {
    Car addCar(Car car);
    Page<Car> getAllCars(Pageable pageable);
    Car getCarById(Long id);
    Car updateCar(Long id, Car updatedCar);
    void deleteCar(Long id);
}
