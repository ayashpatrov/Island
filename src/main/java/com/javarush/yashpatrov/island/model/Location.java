package main.java.com.javarush.yashpatrov.island.model;

import lombok.Getter;
import main.java.com.javarush.yashpatrov.island.configuration.LocationSettings;
import main.java.com.javarush.yashpatrov.island.configuration.Settings;
import main.java.com.javarush.yashpatrov.island.model.animals.Animal;
import main.java.com.javarush.yashpatrov.island.model.enums.CreatureType;
import main.java.com.javarush.yashpatrov.island.model.enums.Direction;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

public class Location {
    @Getter
    private final ConcurrentMap<CreatureType, ConcurrentLinkedQueue<Creature>> allCreatures = new ConcurrentHashMap<>();

    private final Island island;
    @Getter
    private final int row;
    @Getter
    private final int column;
    @Getter
    private static LocationSettings settings;

    public Location(Island island, int row, int column) {
        setLocationSettings();
        this.island = island;
        this.row = row;
        this.column = column;
    }

    public ConcurrentLinkedQueue<Creature> getCreaturesByType(CreatureType creatureType) {
        return allCreatures.get(creatureType);
    }

    public void addCreature(Creature creature) {
        ConcurrentLinkedQueue<Creature> creatureList = allCreatures.get(creature.getCreatureType()) != null ? allCreatures.get(creature.getCreatureType()) : new ConcurrentLinkedQueue<>();
        creatureList.add(creature);
        allCreatures.put(creature.getCreatureType(), creatureList);
    }

    public void removeCreature(Creature creature) {
        ConcurrentLinkedQueue<Creature> creatureList = allCreatures.get(creature.getCreatureType());
        creatureList.remove(creature);
        allCreatures.put(creature.getCreatureType(), creatureList);
    }

    public Creature removeCreature(CreatureType creatureType) {
        ConcurrentLinkedQueue<Creature> creatureList = allCreatures.get(creatureType);
        Creature removedCreature = creatureList.remove();
        allCreatures.put(creatureType, creatureList);

        return removedCreature;
    }

    public Location getNextDownLocation(int shift) {
        return island.getLocations()[row + shift > island.getLocations()[0].length ? row : row + shift][column];
    }

    public Location getNextUpLocation(int shift) {
        return island.getLocations()[row - shift < 0 ? row : row - shift][column];
    }

    public Location getNextRightLocation(int shift) {
        return island.getLocations()[row][column + shift > island.getLocations()[0].length ? column : column + shift];
    }

    public Location getNextLeftLocation(int shift) {
        return island.getLocations()[row][column - shift < 0 ? column : column - shift];
    }

    public Location getNextNeighbor(Direction direction, int shift) {
        if (nextCellExist(direction, shift)) {
            switch (direction) {
                case UP -> {
                    return island.getLocations()[row - shift][column];
                }
                case DOWN -> {
                    return island.getLocations()[row + shift][column];
                }
                case LEFT -> {
                    return island.getLocations()[row][column - shift];
                }
                case RIGHT -> {
                    return island.getLocations()[row][column + shift];
                }
                case NONE -> {
                    return this;
                }
            }
        }
        return null;
    }

    public boolean nextCellExist(Direction direction, int shift) {
        switch (direction) {
            case UP -> {
                return row - shift >= 0;
            }
            case DOWN -> {
                return row + shift < island.getLocations().length;
            }
            case LEFT -> {
                return column - shift >= 0;
            }
            case RIGHT -> {
                return column + shift < island.getLocations()[0].length;
            }
            default -> {
                return false;
            }
        }
    }

    /*public boolean hasRoom(Class<? extends Creature> clazz) {
        if (creaturesRegister != null) {
            if (creaturesRegister.get(clazz) != null) {
                return creaturesRegister.get(clazz) < settings.getLimits().get(clazz.getSimpleName());
            }
        }
        return true;
    }*/

    public boolean hasRoom(CreatureType creatureType) {
        if (allCreatures.get(creatureType) != null) {
            if (settings.getLimits().get(creatureType) != null) {
                return allCreatures.get(creatureType).size() < settings.getLimits().get(creatureType);
            } else {
                throw new RuntimeException("Настройка limits для " + creatureType + " не задана");
            }
        }
        return true;
    }

    public boolean neighborHasRoomForClass(Direction direction, int shift, CreatureType creatureType) {
        switch (direction) {
            case UP -> {
                return getNextUpLocation(shift).hasRoom(creatureType);
            }
            case DOWN -> {
                return getNextDownLocation(shift).hasRoom(creatureType);
            }
            case LEFT -> {
                return getNextLeftLocation(shift).hasRoom(creatureType);
            }
            case RIGHT -> {
                return getNextRightLocation(shift).hasRoom(creatureType);
            }
            default -> {
                return false;
            }
        }
    }

    private void setLocationSettings() {
        settings = Settings.getInstance().getCommonSettings().getLocationSettings();
    }

    public Optional<Animal> findPair(CreatureType creatureType) {
        //TODO сомнительный подбор пары, т.к. если я беру постоянно с одной позиции, я могу постоянно натыкаться на себя,
        //по хорошему, мне надо массив возможных пар возвращать, и там уже решать, кто есть кто.
        List<Animal> filteredAnimals = allCreatures.get(creatureType).stream()
                .filter(x -> x instanceof Animal)
                .map(x -> (Animal) x)
                .filter(animal -> !animal.isFucking())
                .toList();

        return filteredAnimals.isEmpty()
                ? Optional.empty()
                : Optional.of(filteredAnimals.getLast());
    }
}
