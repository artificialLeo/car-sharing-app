package com.app.car.service.impl;

import com.app.car.dto.rental.CompletedRentalDto;
import com.app.car.dto.rental.RentalDto;
import com.app.car.exception.*;
import com.app.car.mapper.RentalMapper;
import com.app.car.model.Car;
import com.app.car.model.Rental;
import com.app.car.repository.RentalRepository;
import com.app.car.service.CarService;
import com.app.car.service.RentalService;
import com.app.car.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {
    private final RentalRepository rentalRepository;
    private final CarService carService;
    private final TelegramNotificationServiceImpl telegramNotificationService;
    private final RentalMapper rentalMapper;

    @Override
    public RentalDto addRental(RentalDto rentalDto) throws TelegramApiException {
        Car car = carService.getCarById(rentalDto.getCarId());
        telegramNotificationService.sendNotification("DTO added!!!");

        List<Rental> activeRentalsForCar = rentalRepository.findByCar_IdAndActualReturnDateIsNull(car.getId());
        if (!activeRentalsForCar.isEmpty()) {
            throw new CarAlreadyRentedException("Car with ID " + car.getId() + " is already rented and not returned yet.");
        }

        if (car.getInventory() > 0) {
            car.setInventory(car.getInventory() - 1);
            carService.updateCar(car.getId(), car);
            Rental addedRental = rentalRepository.save(rentalMapper.toEntity(rentalDto));
            return rentalMapper.toDto(addedRental);
        } else {
            throw new InsufficientInventoryException("Car inventory is insufficient with ID: " + car.getId());
        }
    }


    @Override
    public List<RentalDto> getRentalsByUserAndStatus(Long userId, boolean carReturned) {
        List<Rental> rentals = rentalRepository.findByUserIdAndActualReturnDateIsNull(userId, carReturned);

        if (rentals.isEmpty()) {
            throw new NoRentalsFoundException("No rentals found for user with ID: "
                    + userId + " and carReturned: " + carReturned);
        }

        return rentalMapper.toDtoList(rentals);
    }

    @Override
    public RentalDto getRentalById(Long id) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new RentalNotFoundException("Rental not found with id: " + id));

        return rentalMapper.toDto(rental);
    }

    @Override
    public CompletedRentalDto returnCar(Long rentalId) {
        Rental rental = rentalRepository
                .findById(rentalId)
                .orElseThrow(() ->
                        new RentalNotFoundException("Rental not found with id: " + rentalId));

        if (rental.getActualReturnDate() != null) {
            throw new RentalReturnedException("Rental with id " + rentalId + " has already been returned.");
        }

        rental.setActualReturnDate(LocalDate.now());

        Car car = carService.getCarById(rental.getCar().getId());
        car.setInventory(car.getInventory() + 1);
        carService.updateCar(car.getId(), car);

        rentalRepository.save(rental);
        return rentalMapper.toCompletedDto(rental);
    }
}
