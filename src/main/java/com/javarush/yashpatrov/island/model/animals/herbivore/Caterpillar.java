package main.java.com.javarush.yashpatrov.island.model.animals.herbivore;

import main.java.com.javarush.yashpatrov.island.model.Location;
import main.java.com.javarush.yashpatrov.island.model.enums.Action;
import main.java.com.javarush.yashpatrov.island.model.enums.CreatureType;

public class Caterpillar extends Herbivore {
    public Caterpillar(Location location, CreatureType creatureType) {
        super(location, creatureType);
    }

    public Action makeDecision() {
        return Action.REPRODUCE;
    }
}
