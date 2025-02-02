package main.java.com.javarush.yashpatrov.island.service;

import main.java.com.javarush.yashpatrov.island.model.Location;
import main.java.com.javarush.yashpatrov.island.model.enums.CreatureType;
import main.java.com.javarush.yashpatrov.island.util.CreatureFactory;

import java.util.concurrent.Callable;

public class GenerateCreature implements Callable<Void> {
    private CreatureType creatureType;
    private Location location;
    private static CreatureFactory creatureFactory = new CreatureFactory();
    public GenerateCreature(Location location, CreatureType creatureType) {
        this.creatureType = creatureType;
        this.location = location;
    }

    @Override
    public Void call() throws Exception {
        location.addCreature(creatureFactory.create(creatureType, location));
        return null;
    }
}
