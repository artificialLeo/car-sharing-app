package com.app.car.repository;

import com.app.car.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {

    List<Rental> findByUser_IdAndReturnDateIsNull(Long userId);
}
