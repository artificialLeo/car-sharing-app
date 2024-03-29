package com.app.car.service.impl;

import com.app.car.dto.car.CarShortInfoDto;
import com.app.car.dto.car.CarUpdateDto;
import com.app.car.exception.car.CarIdNotFoundException;
import com.app.car.mapper.CarMapper;
import com.app.car.model.Car;
import com.app.car.repository.CarRepository;
import com.app.car.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    private final CarMapper carMapper;

    @Override
    public Car addCar(Car car) {
        return carRepository.save(car);
    }

    @Override
    public Page<CarShortInfoDto> getAllCars(Pageable pageable) {
        Page<Car> carPage = carRepository.findAllBy(pageable);
        return carPage.map(carMapper::carToCarShortInfoDto);
    }

    @Override
    public Car getCarById(Long id) {
        return carRepository.findById(id).orElseThrow(()
                -> new CarIdNotFoundException(id));
    }

    @Override
    public Car updateCar(Long id, CarUpdateDto updatedCarDto) {
        Car existingCar = carRepository.findById(id)
                .orElseThrow(()
                        -> new CarIdNotFoundException(id));

        carMapper.updateCarFromDto(updatedCarDto, existingCar);

        return carRepository.save(existingCar);
    }

    @Override
    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }
}
