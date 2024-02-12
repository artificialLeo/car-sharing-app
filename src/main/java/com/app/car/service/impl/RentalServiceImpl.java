package com.app.car.service.impl;

import com.app.car.dto.rental.CompletedRentalDto;
import com.app.car.dto.rental.RentalDto;
import com.app.car.exception.car.CarIdAlreadyRentedException;
import com.app.car.exception.car.CarInsufficientInventoryException;
import com.app.car.exception.rental.NoRentalsFoundException;
import com.app.car.exception.rental.RentalIdNotFoundException;
import com.app.car.exception.rental.RentalIdReturnedException;
import com.app.car.mapper.CarMapper;
import com.app.car.mapper.RentalMapper;
import com.app.car.model.Car;
import com.app.car.model.Rental;
import com.app.car.repository.RentalRepository;
import com.app.car.service.CarService;
import com.app.car.service.RentalService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {
    private final RentalRepository rentalRepository;
    private final CarService carService;
    private final TelegramNotificationServiceImpl telegramNotificationService;
    private final RentalMapper rentalMapper;
    private final CarMapper carMapper;

    @Override
    public RentalDto addRental(RentalDto rentalDto) {
        Car car = carService.getCarById(rentalDto.getCarId());

        List<Rental> activeRentalsForCar = rentalRepository
                .findByCar_IdAndActualReturnDateIsNull(car.getId());
        if (!activeRentalsForCar.isEmpty()) {
            throw new CarIdAlreadyRentedException(car.getId());
        }

        if (car.getInventory() > 0) {
            car.setInventory(car.getInventory() - 1);

            carService.updateCar(car.getId(), carMapper.toCarUpdateDto(car));

            Rental addedRental = rentalRepository.save(rentalMapper.toEntity(rentalDto));
            telegramNotificationService.rentalNotification(rentalDto, "New rental added.");
            return rentalMapper.toDto(addedRental);
        } else {
            throw new CarInsufficientInventoryException(car.getId());
        }
    }

    @Override
    public List<RentalDto> getRentalsByUserAndStatus(Long userId, boolean carReturned) {
        List<Rental> rentals = rentalRepository
                .findByUserIdAndActualReturnDateIsNull(userId, carReturned);

        if (rentals.isEmpty()) {
            throw new NoRentalsFoundException(userId, carReturned);
        }

        return rentalMapper.toDtoList(rentals);
    }

    @Override
    public RentalDto getRentalById(Long rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RentalIdNotFoundException(rentalId));

        return rentalMapper.toDto(rental);
    }

    @Override
    public CompletedRentalDto returnCar(Long rentalId) {
        Rental rental = rentalRepository
                .findById(rentalId)
                .orElseThrow(() ->
                        new RentalIdNotFoundException(rentalId));

        if (rental.getActualReturnDate() != null) {
            throw new RentalIdReturnedException(rentalId);
        }

        rental.setActualReturnDate(LocalDate.now());

        Car car = carService.getCarById(rental.getCar().getId());
        car.setInventory(car.getInventory() + 1);
        carService.updateCar(car.getId(), carMapper.toCarUpdateDto(car));

        rentalRepository.save(rental);
        return rentalMapper.toCompletedDto(rental);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void checkOverdueRentals() {
        LocalDate today = LocalDate.now();
        List<Rental> overdueRentals = rentalRepository
                .findByReturnDateBeforeAndActualReturnDateIsNull(today);

        if (overdueRentals.isEmpty()) {
            telegramNotificationService.sendNotification("No rentals overdue today!");
        } else {
            for (Rental overdueRental : overdueRentals) {
                String message = "Overdue rental ID: " + overdueRental.getId()
                        + ". Return date was: " + overdueRental.getReturnDate();
                telegramNotificationService.sendNotification(message);
            }
        }
    }
}
