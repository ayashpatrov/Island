package main.java.com.javarush.yashpatrov.island.util;

import java.util.concurrent.ThreadLocalRandom;

public class RandomEvents {
    public static boolean isEventHappening(int chance) {
        return ThreadLocalRandom.current().nextInt(100) < chance;
    }
}
