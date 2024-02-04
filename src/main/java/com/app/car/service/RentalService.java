package com.app.car.service;

import com.app.car.dto.rental.RentalDto;

import java.util.List;

public interface RentalService {
    RentalDto addRental(RentalDto rentalDTO);
    List<RentalDto> getRentalsByUserAndStatus(Long userId, boolean isActive);
    RentalDto getRentalById(Long id);
    RentalDto returnCar(Long rentalId);
}
