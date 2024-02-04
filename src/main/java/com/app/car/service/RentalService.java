package com.app.car.service;

import com.app.car.dto.rental.CompletedRentalDto;
import com.app.car.dto.rental.RentalDto;

import java.util.List;

public interface RentalService {
    RentalDto addRental(RentalDto rentalDTO);
    List<RentalDto> getRentalsByUserAndStatus(Long userId, boolean carReturned);
    RentalDto getRentalById(Long id);
    CompletedRentalDto returnCar(Long rentalId);
}
