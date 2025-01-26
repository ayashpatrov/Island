package main.java.com.javarush.yashpatrov.island.util;

import main.java.com.javarush.yashpatrov.island.model.Island;

public interface Renderer extends Runnable {
    void render(Island island);
}
