package com.app.car.service.impl;

import com.app.car.exception.CarNotFoundException;
import com.app.car.model.Car;
import com.app.car.repository.CarRepository;
import com.app.car.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;

    @Override
    public Car addCar(Car car) {
        return carRepository.save(car);
    }

    @Override
    public Page<Car> getAllCars(Pageable pageable) {
        return carRepository.findAll(pageable);
    }

    @Override
    public Car getCarById(Long id) {
        return carRepository.findById(id).orElse(null);
    }

    @Override
    public Car updateCar(Long id, Car updatedCar) {
        Car existingCar = carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException("Car not found with id: "
                        + id));

        if (StringUtils.hasText(updatedCar.getModel())) {
            existingCar.setModel(updatedCar.getModel());
        }

        if (StringUtils.hasText(updatedCar.getBrand())) {
            existingCar.setBrand(updatedCar.getBrand());
        }

        if (updatedCar.getType() != null) {
            existingCar.setType(updatedCar.getType());
        }

        if (updatedCar.getInventory() >= 0) {
            existingCar.setInventory(updatedCar.getInventory());
        }

        if (updatedCar.getDailyFee() != null) {
            existingCar.setDailyFee(updatedCar.getDailyFee());
        }

        return carRepository.save(existingCar);
    }


    @Override
    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }
}

