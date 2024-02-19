package com.app.car.service;

import com.app.car.dto.car.CarShortInfoDto;
import com.app.car.dto.car.CarUpdateDto;
import com.app.car.exception.car.CarIdNotFoundException;
import com.app.car.mapper.CarMapper;
import com.app.car.model.Car;
import com.app.car.repository.CarRepository;
import com.app.car.service.impl.CarServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CarServiceImplTest {
    @Mock
    private CarRepository carRepository;

    @Mock
    private CarMapper carMapper;

    @InjectMocks
    private CarServiceImpl carService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("addCar success")
    void addCar_ValidCar_Success() {
        Car car = new Car();
        when(carRepository.save(car)).thenReturn(car);

        Car result = carService.addCar(car);

        Assertions.assertNotNull(result);
        verify(carRepository, times(1)).save(car);
    }

    @Test
    @DisplayName("getAllCars success")
    void getAllCars_ValidPageable_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Car> carPage = new PageImpl<>(Collections.singletonList(new Car()));
        when(carRepository.findAllBy(pageable)).thenReturn(carPage);

        Page<CarShortInfoDto> result = carService.getAllCars(pageable);

        Assertions.assertNotNull(result);
        verify(carRepository, times(1)).findAllBy(pageable);
        verify(carMapper, times(1)).carToCarShortInfoDto(any());
    }

    @Test
    @DisplayName("getCarById success")
    void getCarById_ExistingId_ReturnsCar() {
        Long carId = 1L;
        Car car = new Car();
        when(carRepository.findById(carId)).thenReturn(Optional.of(car));

        Car result = carService.getCarById(carId);

        Assertions.assertNotNull(result);
        verify(carRepository, times(1)).findById(carId);
    }

    @Test
    @DisplayName("getCarById fail")
    void getCarById_NonExistingId_ReturnsNull() {
        Long carId = 1L;
        when(carRepository.findById(carId)).thenReturn(Optional.empty());

        assertThrows(CarIdNotFoundException.class, () -> carService.getCarById(carId));

        verify(carRepository, times(1)).findById(carId);
    }


    @Test
    @DisplayName("updateCar success")
    void updateCar_ValidIdAndDto_Success() {
        Long carId = 1L;
        CarUpdateDto updatedCarDto = new CarUpdateDto();
        Car existingCar = new Car();
        when(carRepository.findById(carId)).thenReturn(Optional.of(existingCar));
        when(carRepository.save(existingCar)).thenReturn(existingCar);

        Car result = carService.updateCar(carId, updatedCarDto);

        Assertions.assertNotNull(result);
        verify(carRepository, times(1)).findById(carId);
        verify(carRepository, times(1)).save(existingCar);
    }

    @Test
    @DisplayName("updateCar fail")
    void updateCar_NonExistingId_ThrowsException() {
        Long carId = 1L;
        CarUpdateDto updatedCarDto = new CarUpdateDto();
        when(carRepository.findById(carId)).thenReturn(Optional.empty());

        assertThrows(CarIdNotFoundException.class, () -> carService.updateCar(carId, updatedCarDto));
        verify(carRepository, times(1)).findById(carId);
    }

    @Test
    @DisplayName("deleteCar success")
    void deleteCar_ValidId_Success() {
        Long carId = 1L;

        carService.deleteCar(carId);

        verify(carRepository, times(1)).deleteById(carId);
    }
}
