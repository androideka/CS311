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
        CSGraph<String, String> graph = new CSGraph<String, String>(true);
        File file = new File("/home/androideka/ames2.txt");
        Scanner fileScanner = new Scanner(file);
        fileScanner.next();
        int numVertices = fileScanner.nextInt();
        //System.out.println("numVertices = " + numVertices);
        int vertexID, edgeSource, edgeTarget;
        String vertexData, newLine, next, edgeData;
        graph.setNumberVertices(numVertices);
        boolean vertices = true;
        fileScanner.nextLine();
        while(fileScanner.hasNextLine())
        {
            newLine = fileScanner.nextLine();
            //System.out.println("newLine = " + newLine);
            Scanner scanner = new Scanner(newLine);
            scanner.useDelimiter(",|:");
            next = scanner.next();
            //System.out.println("next = " + next);
            if(next.equals("EDGES"))
            {
                //System.out.println("All vertices added.");
                vertices = false;
            }
            else if(vertices)
            {
                vertexData = "";
                vertexID = Integer.parseInt(next);
                //System.out.println("vertexID = " + vertexID);
                vertexData += scanner.next() + ",";
                vertexData += scanner.next();
                //System.out.println("vertexData = " + vertexData);
                graph.addVertex("" + vertexID, vertexData);
            }
            else
            {
                edgeSource = Integer.parseInt(next);
                //System.out.println("edgeSource = " + edgeSource);
                edgeTarget = Integer.parseInt(scanner.next());
                //System.out.println("edgeTarget = " + edgeTarget);
                edgeData = scanner.next();
                while(scanner.hasNext())
                {
                    edgeData += "," + scanner.next();
                }
                //System.out.println("edgeData = " + edgeData);
                graph.addEdge("" + edgeSource, "" + edgeTarget, edgeData);
            }
        }
        CSGraph<String, String> ingredients = new CSGraph(true);
        ingredients.addVertex("0", "A");
        ingredients.addVertex("1", "B");
        ingredients.addVertex("2", "C");
        ingredients.addVertex("3", "D");
        ingredients.addVertex("4", "E");
        ingredients.addVertex("5", "F");
        ingredients.setNumberVertices(6);
        ingredients.addEdge("0", "2", "1");
        ingredients.addEdge("0", "5", "1");
        ingredients.addEdge("1", "2", "1");
        ingredients.addEdge("1", "3", "1");
        ingredients.addEdge("2", "3", "1");
        ingredients.addEdge("2", "4", "1");
        ingredients.addEdge("5", "2", "1");
        ingredients.addEdge("5", "4", "1");
        LinkedList<String> topo = (LinkedList<String>)ingredients.topologicalSort();
        ListIterator<String> li = topo.listIterator();
        while(li.hasNext())
        {
            System.out.println(li.next());
            li.remove();
        }
        System.out.println(graph.getNumVertices());
        System.out.println(graph.getNumEdges());
        System.out.println(graph.getNeighbors("0"));
        System.out.println(graph.getNeighbors("228"));
        System.out.println(graph.getNeighbors("2199"));
        System.out.println(graph.getNeighbors("2198"));
        System.out.println(graph.getEdgeData("0", "228"));
        System.out.println(graph.getVertexData("0"));
        System.out.println(graph.getNumVertices());
        System.out.println(graph.getNeighbors("0"));
        System.out.println(graph.getVertices());
    }
}
