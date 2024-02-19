package com.app.car.mapper;

import com.app.car.dto.rental.CompletedRentalDto;
import com.app.car.dto.rental.RentalDto;
import com.app.car.model.Rental;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RentalMapper {
    @Mapping(source = "car.id", target = "carId")
    @Mapping(source = "user.id", target = "userId")
    RentalDto toDto(Rental rental);

    List<RentalDto> toDtoList(List<Rental> rentals);

    @Mapping(source = "carId", target = "car.id")
    @Mapping(source = "userId", target = "user.id")
    @Mapping(target = "actualReturnDate", ignore = true)
    Rental toEntity(RentalDto rentalDto);

    @Mapping(source = "car.id", target = "carId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "actualReturnDate", target = "actualReturnDate")
    CompletedRentalDto toCompletedDto(Rental rental);
}
