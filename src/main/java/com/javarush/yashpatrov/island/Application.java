package main.java.com.javarush.yashpatrov.island;

import main.java.com.javarush.yashpatrov.island.service.IslandManager;

public class Application {
    public static void main(String[] args) {
        IslandManager islandManager = new IslandManager();
        islandManager.play();
    }
}