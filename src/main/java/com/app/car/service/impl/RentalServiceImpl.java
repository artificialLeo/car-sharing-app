package com.app.car.service.impl;

import com.app.car.dto.rental.RentalDto;
import com.app.car.exception.InsufficientInventoryException;
import com.app.car.exception.RentalNotFoundException;
import com.app.car.mapper.RentalMapper;
import com.app.car.model.Car;
import com.app.car.model.Rental;
import com.app.car.repository.RentalRepository;
import com.app.car.service.CarService;
import com.app.car.service.RentalService;
import com.app.car.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {
    private final RentalRepository rentalRepository;
    private final CarService carService;
    private final RentalMapper rentalMapper;

    @Override
    public RentalDto addRental(RentalDto rentalDto) {
        Car car = carService.getCarById(rentalDto.getCarId());
        if (car.getInventory() > 0) {
            car.setInventory(car.getInventory() - 1);
            carService.updateCar(car.getId(), car);
            Rental addedRental = rentalRepository
                    .save(rentalMapper.toEntity(rentalDto));
            return rentalMapper.toDto(addedRental);
        } else {
            throw new InsufficientInventoryException("Car inventory is insufficient with id : "
                    + car.getId());
        }
    }

    @Override
    public List<RentalDto> getRentalsByUserAndStatus(Long userId, boolean isActive) {
        List<Rental> rentals = rentalRepository.findByUser_IdAndReturnDateIsNull(userId);
        return rentalMapper.toDtoList(rentals);
    }

    @Override
    public RentalDto getRentalById(Long id) {
        Rental rental = rentalRepository.findById(id).orElse(null);
        return rentalMapper.toDto(rental);
    }

    @Override
    public RentalDto returnCar(Long rentalId) {
        Rental rental = rentalRepository
                .findById(rentalId)
                .orElseThrow(()
                        -> new RentalNotFoundException("Rental not found or already returned with id : "
                        + rentalId));

        rental.setActualReturnDate(LocalDate.now());

        Car car = carService.getCarById(rental.getCar().getId());
        car.setInventory(car.getInventory() + 1);
        carService.updateCar(car.getId(), car);

        rentalRepository.save(rental);
        return rentalMapper.toDto(rental);
    }

}

