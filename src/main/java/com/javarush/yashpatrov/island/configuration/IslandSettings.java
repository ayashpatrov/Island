package main.java.com.javarush.yashpatrov.island.configuration;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IslandSettings {
    private int rowsCount;
    private int columnsCount;
}
