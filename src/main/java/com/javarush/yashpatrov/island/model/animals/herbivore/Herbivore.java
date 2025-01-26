package main.java.com.javarush.yashpatrov.island.model.animals.herbivore;

import main.java.com.javarush.yashpatrov.island.model.Location;
import main.java.com.javarush.yashpatrov.island.model.animals.Animal;
import main.java.com.javarush.yashpatrov.island.model.enums.CreatureType;

public class Herbivore extends Animal {
    public Herbivore(Location location, CreatureType creatureType) {
        super(location, creatureType);
    }
}
