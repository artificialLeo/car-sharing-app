package com.app.car.repository;

import com.app.car.dto.car.CarShortInfoDto;
import com.app.car.model.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {
    Page<Car> findAllBy(Pageable pageable);
}
