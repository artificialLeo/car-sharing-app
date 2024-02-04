package com.app.car.mapper;

import com.app.car.dto.rental.RentalDto;
import com.app.car.model.Rental;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RentalMapper {
    RentalDto toDto(Rental rental);

    List<RentalDto> toDtoList(List<Rental> rentals);

    Rental toEntity(RentalDto rentalDto);
}

