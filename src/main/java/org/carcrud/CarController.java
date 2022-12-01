package org.carcrud;

import lombok.extern.slf4j.Slf4j;
import org.carcrud.model.Brand;
import org.carcrud.model.Car;
import org.carcrud.model.FuelType;
import org.utils.Read;

import java.io.IOException;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import static org.carcrud.CarRepository.closeConnection;
import static org.utils.Patterns.askStringWhileDoesNotMatchToPattern;
import static org.utils.Read.*;
import static org.utils.Utils.listInSeparatedLines;

@Slf4j
public class CarController {
    static CarController carController = new CarController();
    static CarRepository repository = new CarRepository();
    enum Action {
        CREATE(() -> carController.create()),
        READ_ALL(() -> carController.read());
//        SEARCH(()-> carController.search());
//        UPDATE(() -> carController.update());
//                DELETE(() -> carController.deleteCar());
        final Runnable action;

        Action(Runnable runnable) {
            action = runnable;
        }
    }
    private void read() {
        log.debug(listInSeparatedLines(repository.read()));
    }

    enum CarField {
        VIN(() -> {
            try {
                return askStringWhileDoesNotMatchToPattern(
                        Pattern.compile("^(?i)[a-z0-9]{17}$"),
                        "Enter VIN(17 chars)");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }),
        FUEL_TYPE(() -> {
            try {
                return readEnumValue(FuelType.values()).toString();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }),
        BRAND(() -> {
            try {
                return readEnumValue(Brand.values()).toString();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }),
        MODEL(() -> {
            try {
                return readString("Enter model");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }),
        YEAR_OF(() -> {
            try {
                return askStringWhileDoesNotMatchToPattern(
                        Pattern.compile("^\\d{4}$"),
                        "Enter year"
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }),
        MILEAGE(() -> {
            try {
                return String.valueOf(Read.readPositiveNumber("Enter mileage"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }),
        STATE_NUMBER(() -> {
            try {
                return askStringWhileDoesNotMatchToPattern(
                        Pattern.compile("^(?i)[A-Z\\dАВЕIКМНОРСТХ]{6,8}$"),
                        "Enter state number(number_plate)"
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }),
        ADDITIONAL_DESCRIPTION(() -> {
            try {
                return Read.read("Enter additional description");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        private static String getValue(CarField field) {
            return field.valueFromUser.get();
        }

        final Supplier<String> valueFromUser;

        CarField(Supplier<String> valueFromUser) {
            this.valueFromUser = valueFromUser;
        }
    }

    public static void start() {
        try {
            do {
                readEnumValue(Action.values()).action.run();
            } while (inputEqualsYes("Continue?"));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        closeConnection();
    }

    static HashMap<CarField, Consumer<Car>> setters = new HashMap<>();

    static {
        setters.put(CarField.VIN, car -> car.setVIN(CarField.VIN.valueFromUser.get()));
        setters.put(CarField.FUEL_TYPE, car -> car.setFuelType(FuelType.valueOf(CarField.FUEL_TYPE.valueFromUser.get())));
        setters.put(CarField.BRAND, car -> car.setBrand(Brand.valueOf(CarField.BRAND.valueFromUser.get())));
        setters.put(CarField.MODEL, car -> car.setModel(CarField.MODEL.valueFromUser.get()));
        setters.put(CarField.YEAR_OF, car -> car.setYearOf(Integer.parseInt(CarField.YEAR_OF.valueFromUser.get())));
        setters.put(CarField.MILEAGE, car -> car.setMileage(Integer.parseInt(CarField.MILEAGE.valueFromUser.get())));
        setters.put(CarField.STATE_NUMBER, car -> car.setStateNumber(CarField.STATE_NUMBER.valueFromUser.get()));
        setters.put(CarField.ADDITIONAL_DESCRIPTION, car -> car.setAdditionalDescription(CarField.ADDITIONAL_DESCRIPTION.valueFromUser.get()));
    }

    public static HashMap<CarField, Function<Car, Object>> getters = new HashMap<>();

    static {
        getters.put(CarField.VIN, Car::getVIN);
        getters.put(CarField.FUEL_TYPE, Car::getFuelType);
        getters.put(CarField.BRAND, Car::getBrand);
        getters.put(CarField.MODEL, Car::getModel);
        getters.put(CarField.YEAR_OF, Car::getYearOf);
        getters.put(CarField.MILEAGE, Car::getMileage);
        getters.put(CarField.STATE_NUMBER, Car::getStateNumber);
        getters.put(CarField.ADDITIONAL_DESCRIPTION, Car::getAdditionalDescription);
    }

    private void create() {
        Car car = new Car();
        setters.values().forEach(setter -> setter.accept(car));
        log.info(car + " is created");
        repository.createCar(car);
    }

    //remove
    //update
    //delete
}
