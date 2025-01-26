package main.java.com.javarush.yashpatrov.island.model.animals;

import lombok.Setter;
import main.java.com.javarush.yashpatrov.island.model.enums.Action;
import main.java.com.javarush.yashpatrov.island.model.enums.CreatureType;
import main.java.com.javarush.yashpatrov.island.model.Location;
import main.java.com.javarush.yashpatrov.island.model.Creature;
import main.java.com.javarush.yashpatrov.island.model.enums.Direction;
import main.java.com.javarush.yashpatrov.island.util.CreatureFactory;
import main.java.com.javarush.yashpatrov.island.util.RandomEvents;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

public abstract class Animal extends Creature {
    protected double satiety;
    protected double fullSatiety;
    private final int speed;
    @Setter
    private boolean isFucking = false;
    protected final static double SATIETY_THRESHOLD = 0.8; // на основании этого коэффициента животное принимает решение есть или плодиться
    protected final static double HUNGER_STRENGTH = 0.1; // сколько процентов сытости животное теряет за ход

    public Animal(Location location, CreatureType creatureType) {
        super(location, creatureType);
        this.fullSatiety = settings.getFullSatiety();
        this.satiety = fullSatiety * SATIETY_THRESHOLD; // рождается не полностью сытым
        this.speed = settings.getSpeed();
    }

    public void move() {
        synchronized (location) {
            List<Location> possibleMoveDirections = getPossibleMoveDirections(speed);
            Location nextLocation = getNextLocation(possibleMoveDirections);
            if (nextLocation != null) {
                moveTowardLocation(this, nextLocation);
            } else {
                //System.err.printf("Животное %s, не нашло куда ему идти\n", this.creatureType);
            }
        }
    }

    public void setLocation(Location newLocation) {
        this.location = newLocation;
    }

    public Action makeDecision() {
        if (satiety == 0) {
            return Action.DIE;
        }

        if (satiety <= fullSatiety * SATIETY_THRESHOLD) {
            return Action.EAT;
        }

        if (satiety > fullSatiety * SATIETY_THRESHOLD) {
            return Action.REPRODUCE;
        }

        return Action.MOVE;
    }

    public void eat() {
        synchronized (location) {
            //Получить список классов, тех животных кого я могу есть.
            HashMap<CreatureType, Integer> eatingChance = getSettings().getEatingChance();

            //Получить список классов, тех животных, котрые представлены на клетке.
            ConcurrentMap<CreatureType, ConcurrentLinkedQueue<Creature>> allAnimals = location.getAllCreatures();

            //Получить фактический список классов, кого я могу съеть.
            HashMap<CreatureType, Integer> whoIWillEat = new HashMap<>();
            for (Map.Entry<CreatureType, ConcurrentLinkedQueue<Creature>> record : allAnimals.entrySet()) {
                if (eatingChance != null) {
                    if (eatingChance.containsKey(record.getKey()) && !record.getValue().isEmpty()) {
                        whoIWillEat.put(record.getKey(), eatingChance.get(record.getKey()));
                    }
                } else {
                    System.out.printf("%s никого не ест\n", record.getKey());
                }
            }
            //Выбрать кого я всё таки буду есть.
            if (!whoIWillEat.isEmpty()) {
                for (Map.Entry<CreatureType, Integer> food : whoIWillEat.entrySet()) {
                    if (RandomEvents.isEventHappening(food.getValue())) {
                        //Убавить количество этого класса животных на локации.
                        try {
                            Creature eaten = location.removeCreature(food.getKey());
                            satiety = Math.min(satiety + eaten.getWeight(), fullSatiety);
                            //System.out.printf("%s сожрал %s\n", this.creatureType, food.getKey());
                        } catch (NoSuchElementException e) {
                            //System.err.printf("%s ускользнул из лап %s\n", food.getKey(), this.creatureType);
                        }
                    }
                }
            } else {
                //System.err.printf("%s нечего жрать!\n", this.creatureType);
                move();
            }
        }
    }

    public void starve() {
        satiety = Math.max(satiety - fullSatiety * HUNGER_STRENGTH, 0);
    }

    public void reproduce() {
        //System.out.printf("%s хочет размножится\n", this.creatureType);
        synchronized (location) {
            if (!isFucking) {
                isFucking = true;
                Optional<Animal> pair = location.findPair(this.getCreatureType());
                pair.ifPresent(x -> {
                    if (!x.equals(this)) {
                        x.giveBirth();
                    } else {
                        //System.out.println("Пара не найдена");
                        move();
                    }
                });
            }
        }
    }

    public void die() {
        synchronized (location) {
            location.removeCreature(this);
        }
    }

    public void giveBirth() {
        CreatureFactory creatureFactory = new CreatureFactory();
        isFucking = true;
        if (location.hasRoom(this.getCreatureType())) {
            location.addCreature(creatureFactory.create(creatureType, location));
        } else {
            move();
        }
    }

    public boolean isFucking() {
        return isFucking;
    }

    public void executeLifeCycle() {
        if (RandomEvents.isEventHappening(100)) {
            starve();
        }

        Action currentAction = makeDecision();
        switch (currentAction) {
            case EAT -> eat();
            case MOVE -> move();
            case REPRODUCE -> reproduce();
            case DIE -> die();
        }
    }

    protected void moveTowardLocation(Animal animal, Location targetLocation) {
        /*System.out.println("Животное " + animal.getClass().getSimpleName() +
                " переместилось из клетки " + animal.getLocation().getRow() + ", " + animal.getLocation().getColumn() +
        " в клетку " + targetLocation.getRow() + ", " + targetLocation.getColumn());*/
        if (targetLocation != null) {
            animal.getLocation().removeCreature(animal);
            animal.setLocation(targetLocation);
            targetLocation.addCreature(animal);
        }
    }

    protected Location getNextLocation(List<Location> locations) {
        if (locations == null || locations.isEmpty()) {
            return null;
        } else {
            Random random = new Random();
            return locations.get(random.nextInt(0, locations.size()));
        }
    }

    protected List<Location> getPossibleMoveDirections(int speed) {
        List<Location> locations = new ArrayList<>();

        for (int i = 1; i <= speed; i++) {
            for (Direction direction : Direction.values()) {
                if (location.nextCellExist(direction, i) && location.neighborHasRoomForClass(direction, i, this.getCreatureType())) {
                    locations.add(location.getNextNeighbor(direction, i));
                }
            }

        }
        return locations;
    }
}