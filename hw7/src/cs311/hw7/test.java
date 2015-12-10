package cs311.hw7;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by androideka on 11/11/15.
 */
public class test {
    public static void main(String[] args) throws FileNotFoundException {
        CSCoffeeTask task = new CSCoffeeTask();
        File file = new File("/home/androideka/ames2.txt");
        List<Integer> topo = task.getSortedIngredientLocations();
        task.getMSTCost(file);
        task = new CSCoffeeTask();
        task.getShortestRoute(file, topo);
    }
}
