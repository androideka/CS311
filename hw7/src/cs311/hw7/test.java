package cs311.hw7;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Scanner;
import java.util.Stack;

/**
 * Created by androideka on 11/11/15.
 */
public class test {
    public static void main(String[] args) throws FileNotFoundException {
        CSCoffeeTask task = new CSCoffeeTask();
        CSGraph<String, String> graph = new CSGraph<String, String>(true);
        File file = new File("/home/androideka/ames2.txt");
        task.getMSTCost(file);
        for(int i = 0; i < graph.getNumEdges(); i++)
        {
            System.out.println(graph.edgeDataList.values().toArray()[i]);
        }
    }
}
