package com.app.car.controller;

import com.app.car.dto.car.CarShortInfoDto;
import com.app.car.dto.car.CarUpdateDto;
import com.app.car.model.Car;
import com.app.car.service.CarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public Car addCar(@RequestBody Car car) {
        return carService.addCar(car);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER', 'ROLE_MANAGER')")
    public ResponseEntity<Page<CarShortInfoDto>> getAllCars(
            @PageableDefault(
                    sort = "id",
                    direction = Sort.Direction.ASC
            ) Pageable pageable) {
        Page<CarShortInfoDto> carsPage = carService.getAllCars(pageable);
        return ResponseEntity.ok(carsPage);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER', 'ROLE_MANAGER')")
    public Car getCarById(@PathVariable Long id) {
        return carService.getCarById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public Car updateCar(@PathVariable Long id, @Valid @RequestBody CarUpdateDto carUpdateDto) {
        return carService.updateCar(id, carUpdateDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public void deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
    }
}
