package org.carcrud.model;

import lombok.Getter;
import org.utils.Read;

import java.io.IOException;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import static org.utils.Patterns.askStringWhileDoesNotMatchToPattern;
import static org.utils.Read.readEnumValue;
import static org.utils.Read.readString;
@Getter
public enum CarField {
        VIN(() -> {
            try {
                return askStringWhileDoesNotMatchToPattern(
                        Pattern.compile("^(?i)[a-z0-9]{17}$"),
                        "Enter VIN(17 chars)");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, Car::getVIN),
        FUEL_TYPE(() -> {
            try {
                return readEnumValue(FuelType.values()).toString();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, Car::getFuelType),
        BRAND(() -> {
            try {
                return readEnumValue(Brand.values()).toString();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, Car::getBrand),
        MODEL(() -> {
            try {
                return readString("Enter model");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, Car::getModel),
        YEAR_OF(() -> {
            try {
                return askStringWhileDoesNotMatchToPattern(
                        Pattern.compile("^\\d{4}$"),
                        "Enter year"
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, Car::getYearOf),
        MILEAGE(() -> {
            try {
                return String.valueOf(Read.readPositiveNumber("Enter mileage"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, Car::getMileage),
        STATE_NUMBER(() -> {
            try {
                return askStringWhileDoesNotMatchToPattern(
                        Pattern.compile("^(?i)[A-Z\\dАВЕIКМНОРСТХ]{6,8}$"),
                        "Enter state number(number_plate)"
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, Car::getStateNumber),
        ADDITIONAL_DESCRIPTION(() -> {
            try {
                return Read.read("Enter additional description");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, Car::getAdditionalDescription);

        final Supplier<String> valueFromUser;
        final Function<Car, Object> getter;

        CarField(Supplier<String> valueFromUser, Function<Car, Object> getter) {
            this.valueFromUser = valueFromUser;
            this.getter = getter;
        }
    }