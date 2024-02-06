package com.app.car.dto.car;

import com.app.car.model.enums.CarType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarShortInfoDto {
    private Long id;
    private String model;
    private String brand;
    private CarType type;
}
