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
        public void userSetter(Car car) {
            car.setVIN(this.valueFromUser());
        }

        @Override
        @SneakyThrows
        public String valueFromUser() {
            return askStringWhileDoesNotMatchToPattern(
                    Pattern.compile("^(?i)[a-z0-9]{17}$"),
                    "Enter VIN(17 chars)");
        }
    },
    FUEL_TYPE(Car::getFuelType) {
        @Override
        public void userSetter(Car car) {
            car.setFuelType(this.valueFromUser());
        }

        @Override
        @SneakyThrows
        public FuelType valueFromUser() {
            return readEnumValue(FuelType.values());
        }
    },
    BRAND(Car::getBrand) {
        @Override
        @SneakyThrows
        public Brand valueFromUser() {
            return readEnumValue(Brand.values());
        }

        @Override
        public void userSetter(Car car) {
            car.setBrand(this.valueFromUser());
        }
    },
    MODEL(Car::getModel) {
        @Override
        @SneakyThrows
        public String valueFromUser() {
            return readString("Enter model");
        }

        @Override
        public void userSetter(Car car) {
            car.setModel(this.valueFromUser());
        }
    },
    YEAR_OF(Car::getYearOf) {
        @Override
        @SneakyThrows
        public Integer valueFromUser() {
            return Integer.valueOf(askStringWhileDoesNotMatchToPattern(
                    Pattern.compile("^\\d{4}$"),
                    "Enter year"));
        }

        @Override
        public void userSetter(Car car) {
            car.setYearOf(this.valueFromUser());
        }
    },
    MILEAGE(Car::getMileage) {
        @Override
        @SneakyThrows
        public Integer valueFromUser() {
            return Read.readPositiveNumber("Enter mileage");
        }

        @Override
        public void userSetter(Car car) {
            car.setMileage(this.valueFromUser());
        }
    },
    STATE_NUMBER(Car::getStateNumber) {
        @Override
        @SneakyThrows
        public String valueFromUser() {
            return askStringWhileDoesNotMatchToPattern(
                    Pattern.compile("^(?i)[A-Z\\dАВЕIКМНОРСТХ]{6,8}$"),
                    "Enter state number(number_plate)");
        }

        @Override
        public void userSetter(Car car) {
            car.setStateNumber(this.valueFromUser());
        }
    },

    ADDITIONAL_DESCRIPTION(Car::getAdditionalDescription) {
        @Override
        @SneakyThrows
        public String valueFromUser() {
            return Read.read("Enter additional description");
        }

        @Override
        public void userSetter(Car car) {
            car.setAdditionalDescription(this.valueFromUser());
        }
    };

    public abstract <T> T valueFromUser();

    public abstract void userSetter(Car car);

    final Function<Car, Object> getter;

    CarField(Function<Car, Object> getter) {
        this.getter = getter;
    }
}