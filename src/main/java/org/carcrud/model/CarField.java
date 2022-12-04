package org.carcrud.model;

import lombok.Getter;
import lombok.SneakyThrows;
import org.utils.Read;

import java.util.function.Function;
import java.util.regex.Pattern;

import static org.utils.Patterns.askStringWhileDoesNotMatchToPattern;
import static org.utils.Read.readEnumValue;
import static org.utils.Read.readString;

@Getter
public enum CarField {
    VIN(Car::getVIN) {
        @Override
        @SneakyThrows
        public String valueFromUser()  {
            return askStringWhileDoesNotMatchToPattern(
                    Pattern.compile("^(?i)[a-z0-9]{17}$"),
                    "Enter VIN(17 chars)");
        }
    },
    FUEL_TYPE(Car::getFuelType) {
        @Override
        @SneakyThrows
        public String valueFromUser()  {
            return readEnumValue(FuelType.values()).toString();
        }
    },
    BRAND(Car::getBrand) {
        @Override
        @SneakyThrows
        public String valueFromUser()  {
            return readEnumValue(Brand.values()).toString();
        }
    },
    MODEL(Car::getModel) {
        @Override
        @SneakyThrows
        public String valueFromUser()  {
            return readString("Enter model");
        }
    },
    YEAR_OF(Car::getYearOf) {
        @Override
        @SneakyThrows
        public String valueFromUser()  {
            return askStringWhileDoesNotMatchToPattern(
                    Pattern.compile("^\\d{4}$"),
                    "Enter year");
        }
    },
    MILEAGE(Car::getMileage) {
        @Override
        @SneakyThrows
        public String valueFromUser()  {
            return String.valueOf(Read.readPositiveNumber("Enter mileage"));
        }
    },
    STATE_NUMBER(Car::getStateNumber) {
        @Override
        @SneakyThrows
        public String valueFromUser()  {
            return askStringWhileDoesNotMatchToPattern(
                    Pattern.compile("^(?i)[A-Z\\dАВЕIКМНОРСТХ]{6,8}$"),
                    "Enter state number(number_plate)");
        }
    },

    ADDITIONAL_DESCRIPTION(Car::getAdditionalDescription) {
        @Override
        @SneakyThrows
        public String valueFromUser()  {
            return Read.read("Enter additional description");
        }
    };

    public abstract String valueFromUser();

    final Function<Car, Object> getter;

    CarField(Function<Car, Object> getter) {
        this.getter = getter;
    }
}