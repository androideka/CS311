package cs311.hw7;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;

/**
 * Created by androideka on 11/11/15.
 */
public class test {
    public static void main(String[] args) throws FileNotFoundException {
        CSGraph<String, String> graph = new CSGraph<String, String>(true);
        File file = new File("/home/androideka/ames.txt");
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
        CSGraph ingredients = new CSGraph(true);
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
        LinkedList<String> list = (LinkedList<String>)graph.topologicalSort();
        while(list.listIterator().hasNext())
        {
            System.out.println(list.listIterator().next());
        }
    }
}
