package com.app.car.controller;

import com.app.car.dto.rental.CompletedRentalDto;
import com.app.car.dto.rental.RentalDto;
import com.app.car.exception.car.CarInsufficientInventoryException;
import com.app.car.exception.rental.RentalIdNotFoundException;
import com.app.car.service.RentalService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@RestController
@RequestMapping("/rentals")
@RequiredArgsConstructor
public class RentalController {
    private final RentalService rentalService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @Operation(summary = "Add a rental")
    public ResponseEntity<RentalDto> addRental(@Valid @RequestBody RentalDto rentalDto) {
        try {
            RentalDto addedRental = rentalService.addRental(rentalDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(addedRental);
        } catch (CarInsufficientInventoryException e) {
            return ResponseEntity.badRequest().build();
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @Operation(summary = "Get rentals by user and status")
    public ResponseEntity<List<RentalDto>> getRentalsByUserAndStatus(
            @RequestParam Long userId,
            @RequestParam(required = false, defaultValue = "true") boolean isActive) {
        List<RentalDto> rentals = rentalService.getRentalsByUserAndStatus(userId, isActive);
        return ResponseEntity.ok(rentals);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER', 'ROLE_MANAGER')")
    @Operation(summary = "Get rental by ID")
    public ResponseEntity<RentalDto> getRentalById(@PathVariable Long id) {
        try {
            RentalDto rental = rentalService.getRentalById(id);
            return ResponseEntity.ok(rental);
        } catch (RentalIdNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{rentalId}/return")
    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER', 'ROLE_MANAGER')")
    @Operation(summary = "Return a rental car")
    public ResponseEntity<CompletedRentalDto> returnCar(@PathVariable Long rentalId) {
        try {
            CompletedRentalDto returnedRental = rentalService.returnCar(rentalId);
            return ResponseEntity.ok(returnedRental);
        } catch (RentalIdNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

