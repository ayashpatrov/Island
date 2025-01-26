package main.java.com.javarush.yashpatrov.island.model;

import main.java.com.javarush.yashpatrov.island.configuration.Settings;
import main.java.com.javarush.yashpatrov.island.model.enums.CreatureType;

public abstract class Creature {
    protected String icon;
    protected double weight;
    protected Location location;
    protected CreatureType creatureType;
    protected CreatureSettings settings;

    public Creature(Location location, CreatureType creatureType) {
        setCreatureSettings();
        this.icon = settings.getIcon();
        this.weight = settings.getWeight();
        this.location = location;
        this.creatureType = creatureType;
    }

    public CreatureType getCreatureType() {
        return this.creatureType;
    }

    public Location getLocation(){
        return this.location;
    }

    public String getIcon() {
        return this.icon;
    }

    public double getWeight() {
        return this.weight;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    private void setCreatureSettings() {
        settings = Settings.getInstance().getClassSettings(this.getClass(), CreatureSettings.class);
    }

    public CreatureSettings getSettings() {
        return settings;
    }
}
