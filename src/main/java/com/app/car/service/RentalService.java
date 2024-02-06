package com.app.car.service;

import com.app.car.dto.rental.CompletedRentalDto;
import com.app.car.dto.rental.RentalDto;
import java.util.List;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface RentalService {
    RentalDto addRental(RentalDto rentalDto) throws TelegramApiException;

    List<RentalDto> getRentalsByUserAndStatus(Long userId, boolean carReturned);

    RentalDto getRentalById(Long id);

    CompletedRentalDto returnCar(Long rentalId);
}
