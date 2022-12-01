package org.carcrud.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Car {
    private String VIN;
    private FuelType fuelType;
    private Brand brand;
    private String model;
    private int yearOf;
    private int mileage;
    private String stateNumber;
    private String additionalDescription;
}
