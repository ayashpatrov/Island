package main.java.com.javarush.yashpatrov.island.model;

import main.java.com.javarush.yashpatrov.island.configuration.Settings;

public class Island {
    private Location[][] locations;

    public Island(int rows, int columns) {
        this.locations = new Location[rows][columns];
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                locations[row][column] = new Location(this, row, column);
            }
        }
        System.out.printf("Создан остров размером %sx%s\n", columns, rows);
    }

    public Location[][] getLocations() {
        return this.locations;
    }



}
