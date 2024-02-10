package com.app.car.mapper;

import com.app.car.dto.car.CarShortInfoDto;
import com.app.car.dto.car.CarUpdateDto;
import com.app.car.model.Car;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CarMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "inventory", ignore = true)
    @Mapping(target = "dailyFee", ignore = true)
    void updateCarFromDto(CarUpdateDto updatedCarDto, @MappingTarget Car existingCar);

    CarUpdateDto toCarUpdateDto(Car car);

    CarShortInfoDto carToCarShortInfoDto(Car car);
}
