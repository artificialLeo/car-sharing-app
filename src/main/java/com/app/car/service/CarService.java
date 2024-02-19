package com.app.car.service;

import com.app.car.dto.car.CarShortInfoDto;
import com.app.car.dto.car.CarUpdateDto;
import com.app.car.model.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CarService {
    Car addCar(Car car);

    Page<CarShortInfoDto> getAllCars(Pageable pageable);

    Car getCarById(Long id);

    Car updateCar(Long id, CarUpdateDto carUpdateDto);

    void deleteCar(Long id);
}
