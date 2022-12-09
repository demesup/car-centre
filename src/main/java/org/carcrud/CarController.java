package org.carcrud;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.carcrud.model.Car;
import org.carcrud.model.CarField;
import org.utils.exception.ExitException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.carcrud.CarController.Action.*;
import static org.carcrud.CarRepository.closeConnection;
import static org.utils.Patterns.askStringWhileDoesNotMatchToPattern;
import static org.utils.Read.*;
import static org.utils.Utils.listInSeparatedLines;
import static org.utils.Utils.numberedArray;

@Slf4j
public class CarController {
    static CarController carController = new CarController();
    static CarRepository repository = new CarRepository();

    public static void start() {
        log.info("Car controller started working");
        try {
            while (true){
                workWithCars();
            }
        }catch (Exception e) {
            log.error(e.getMessage());
        }
        closeConnection();
        log.info("Car controller finished working");
    }

    private static void workWithCars() throws IOException {
        readEnumValue(Action.values()).action.run();
    }

    @SneakyThrows
    private void update() {
        var where = getMap(SEARCH);
        var matching = repository.search(where);
        log.debug(listInSeparatedLines(matching));
        Map<String, String> update;
        if (!inputEqualsYes("Update all?")) {
            int index = readNumber(matching.size(), "Enter index");
            where = carFieldMap(matching.get(index));
        }
        update = getMap(UPDATE);
        repository.update(where, update);
        log.info("Car(s) is(are) updated if exist(s)");
    }

    @SneakyThrows
    private void delete() {
        Map<String, String> map = getMap(DELETE);
        repository.delete(map);
        log.info("Car(s) is(are) deleted if exists");
    }

    @SneakyThrows
    private void search() {
        var map = getMap(SEARCH);
        log.debug(listInSeparatedLines(repository.search(map)));
    }

    private void read() {
        log.debug(listInSeparatedLines(repository.read()));
    }

    private void create() {
        Car car = new Car();
        Arrays.stream(CarField.values()).forEach(f-> f.userSetter(car));
        repository.create(car);
        log.info(car + "is added to table if not exists");
    }

    private Map<String, String> carFieldMap(Car car) {
        return Arrays.stream(CarField.values()).collect(Collectors.toMap(f -> f.name().toLowerCase(), f -> f.getGetter().apply(car).toString()));
    }


    private static Map<String, String> getMap(Action action) throws IOException {
        log.debug(numberedArray(CarField.values()));
        var indexes =
                Arrays.stream(askStringWhileDoesNotMatchToPattern(
                        Pattern.compile("^\\s?\\d(\\s\\d)?\\s?"),
                        "Enter indexes separated by space to " + action.name() + " by. Example: 0 2 4 7")
                        .split("\\s")).map(Integer::parseInt).toList();
        return Arrays.stream(CarField.values())
                .filter(field -> indexes.contains(field.ordinal()))
                .collect(Collectors.toMap(field -> field.name().toLowerCase(), CarField::valueFromUser));
    }


    enum Action {
        CREATE(() -> carController.create()),
        READ_ALL(() -> carController.read()),
        SEARCH(() -> carController.search()),
        UPDATE(() -> carController.update()),
        DELETE(() -> carController.delete()),
        EXIT(()-> {throw new ExitException();});
        final Runnable action;

        Action(Runnable runnable) {
            action = runnable;
        }
    }
}
