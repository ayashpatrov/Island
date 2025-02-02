package main.java.com.javarush.yashpatrov.island.configuration;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LifeCycleSettings {
    private String stopCondition;
    private int beatDurationMs;
    private int beatCount;
    private int threadsCount;
}
