package main.java.com.javarush.yashpatrov.island.model.plants;

import main.java.com.javarush.yashpatrov.island.model.Location;
import main.java.com.javarush.yashpatrov.island.model.enums.CreatureType;
import main.java.com.javarush.yashpatrov.island.model.enums.Direction;
import main.java.com.javarush.yashpatrov.island.model.Creature;
import main.java.com.javarush.yashpatrov.island.util.CreatureFactory;
import main.java.com.javarush.yashpatrov.island.util.RandomEvents;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Plant extends Creature {
    public Plant(Location location, CreatureType creatureType) {
        super(location, creatureType);
    }

    /*
     * Этот метод предназначен для распространения травы, но т.к. в задании не требуется, он не используется
     */
    protected List<Direction> getPossibleGrowDirections() {
        List<Direction> directions = new ArrayList<>();
        if (location.nextCellExist(Direction.UP, 0) && location.neighborHasRoomForClass(Direction.UP, 0, this.getCreatureType())) {
            directions.add(Direction.UP);
        }
        if (location.nextCellExist(Direction.DOWN, 0) && location.neighborHasRoomForClass(Direction.DOWN, 0, this.getCreatureType())) {
            directions.add(Direction.DOWN);
        }
        if (location.nextCellExist(Direction.LEFT, 0) && location.neighborHasRoomForClass(Direction.LEFT, 0, this.getCreatureType())) {
            directions.add(Direction.LEFT);
        }
        if (location.nextCellExist(Direction.RIGHT, 0) && location.neighborHasRoomForClass(Direction.RIGHT, 0, this.getCreatureType())) {
            directions.add(Direction.RIGHT);
        }
        if (location.hasRoom(this.getCreatureType())) {
            directions.add(Direction.NONE);
        }

        return directions;
    }

    protected Direction getNextDirection(List<Direction> directions) {
        if (directions == null || directions.isEmpty()) {
            return null;
        } else {
            Random random = new Random();
            return directions.get(random.nextInt(0, directions.size()));
        }
    }

    protected void growTowardDirection(Direction direction, int litterCount) {
        CreatureFactory creatureFactory = new CreatureFactory();
        if (direction != null) {
            Location growingLocation = location;
            //location.getNextNeighbor(direction, 0);
            for (int i = 0; i < litterCount; i++) {
                //growingLocation.addCreature(creatureFactory.create(this.getClass(), growingLocation));
                growingLocation.addCreature(creatureFactory.create(CreatureType.GRASS, growingLocation));
            }
        }
    }

    public void grow() {
        synchronized (location) {
            /*
            Здесь растение заселяет соседние клетки

            List<Direction> possibleGrowDirections = getPossibleGrowDirections();
            Direction nextDirection = getNextDirection(possibleGrowDirections);
            growTowardDirection(nextDirection);
            */

            //Здесь растение просто есть и просто растёт
            if (location.hasRoom(this.getCreatureType())) {
                if (RandomEvents.isEventHappening(50)) {
                    growTowardDirection(Direction.NONE, getSettings().getLitterCount());
                }
            }
        }
    }
}
