package main.java.com.javarush.yashpatrov.island.model.animals.predator;

import main.java.com.javarush.yashpatrov.island.model.Location;
import main.java.com.javarush.yashpatrov.island.model.animals.Animal;
import main.java.com.javarush.yashpatrov.island.model.enums.CreatureType;

public class Predator extends Animal {
    public Predator(Location location, CreatureType creatureType) {
        super(location, creatureType);
    }
}
