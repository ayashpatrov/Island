package main.java.com.javarush.yashpatrov.island.util;

import main.java.com.javarush.yashpatrov.island.model.Creature;
import main.java.com.javarush.yashpatrov.island.model.Location;
import main.java.com.javarush.yashpatrov.island.model.animals.herbivore.*;
import main.java.com.javarush.yashpatrov.island.model.animals.predator.*;
import main.java.com.javarush.yashpatrov.island.model.enums.CreatureType;
import main.java.com.javarush.yashpatrov.island.model.plants.Grass;

public class CreatureFactory {
    public Creature create(CreatureType type, Location location) {
        return switch (type) {
            case BOAR -> new Boar(location, type);
            case BUFFALO -> new Buffalo(location, type);
            case CATERPILLAR -> new Caterpillar(location, type);
            case DEER -> new Deer(location, type);
            case DUCK -> new Duck(location, type);
            case GOAT -> new Goat(location, type);
            case HORSE -> new Horse(location, type);
            case MOUSE -> new Mouse(location, type);
            case RABBIT -> new Rabbit(location, type);
            case SHEEP -> new Sheep(location, type);
            case BEAR -> new Bear(location, type);
            case BOA -> new Boa(location, type);
            case EAGLE -> new Eagle(location, type);
            case FOX -> new Fox(location, type);
            case WOLF -> new Wolf(location, type);
            case GRASS -> new Grass(location, type);
        };
    }
}
