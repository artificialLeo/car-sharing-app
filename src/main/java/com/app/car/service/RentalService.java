package com.app.car.service;

import com.app.car.dto.rental.CompletedRentalDto;
import com.app.car.dto.rental.RentalDto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public interface RentalService {
    RentalDto addRental(RentalDto rentalDTO) throws TelegramApiException;
    List<RentalDto> getRentalsByUserAndStatus(Long userId, boolean carReturned);
    RentalDto getRentalById(Long id);
    CompletedRentalDto returnCar(Long rentalId);
}
