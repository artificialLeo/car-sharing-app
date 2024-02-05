package com.app.car.controller;

import com.app.car.dto.rental.CompletedRentalDto;
import com.app.car.dto.rental.RentalDto;
import com.app.car.exception.InsufficientInventoryException;
import com.app.car.exception.RentalNotFoundException;
import com.app.car.service.RentalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@RestController
@RequestMapping("/rentals")
@RequiredArgsConstructor
public class RentalController {
    private final RentalService rentalService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<RentalDto> addRental(@Valid @RequestBody RentalDto rentalDto) {
        try {
            RentalDto addedRental = rentalService.addRental(rentalDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(addedRental);
        } catch (InsufficientInventoryException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<List<RentalDto>> getRentalsByUserAndStatus(
            @RequestParam Long userId,
            @RequestParam(required = false, defaultValue = "true") boolean isActive) {
        List<RentalDto> rentals = rentalService.getRentalsByUserAndStatus(userId, isActive);
        return ResponseEntity.ok(rentals);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER', 'ROLE_MANAGER')")
    public ResponseEntity<RentalDto> getRentalById(@PathVariable Long id) {
        try {
            RentalDto rental = rentalService.getRentalById(id);
            return ResponseEntity.ok(rental);
        } catch (RentalNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/{rentalId}/return")
    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER', 'ROLE_MANAGER')")
    public ResponseEntity<CompletedRentalDto> returnCar(@PathVariable Long rentalId) {
        try {
            CompletedRentalDto returnedRental = rentalService.returnCar(rentalId);
            return ResponseEntity.ok(returnedRental);
        } catch (RentalNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
