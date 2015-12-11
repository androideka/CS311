package cs311.hw7;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by androideka on 11/9/15.
 */
public class CSCoffeeTask implements CoffeeTask {

    private CSGraph<String, String> ingredients = new CSGraph<String, String>(true);
    private CSGraph<String, String> graph = new CSGraph<String, String>();

    private HashMap<String, ArrayList<String>> forest = new HashMap<String, ArrayList<String>>();

    private CSGraph.CSEdgeMeasure measure;

    public CSCoffeeTask()
    {
        //ingredients.addVertex("981", "A");
        ingredients.addVertex("0", "981");
        //ingredients.addVertex("1653", "B");
        ingredients.addVertex("1", "1653");
        //ingredients.addVertex("524", "C");
        ingredients.addVertex("2", "524");
        //ingredients.addVertex("1864", "D");
        ingredients.addVertex("3", "1864");
        //ingredients.addVertex("1119", "E");
        ingredients.addVertex("4", "1119");
        //ingredients.addVertex("1104", "F");
        ingredients.addVertex("5", "1104");
        ingredients.setNumberVertices(6);
        //ingredients.addEdge("981", "524", "1");
        ingredients.addEdge("0", "2", "1");
        //ingredients.addEdge("981", "1104", "1");
        ingredients.addEdge("0", "5", "1");
        //ingredients.addEdge("1653", "524", "1");
        ingredients.addEdge("1", "2", "1");
        //ingredients.addEdge("1653", "1864", "1");
        ingredients.addEdge("1", "3", "1");
        //ingredients.addEdge("524", "1864", "1");
        ingredients.addEdge("2", "3", "1");
        //ingredients.addEdge("524", "1119", "1");
        ingredients.addEdge("2", "4", "1");
        //ingredients.addEdge("1104", "524", "1");
        ingredients.addEdge("5", "2", "1");
        //ingredients.addEdge("1104", "1119", "1");
        ingredients.addEdge("5", "4", "1");
    }

    /**
     * You must construct a graph representing the ingredient
     * dependencies specified in the homework and then use
     * topological sort to find a valid sorting.
     *
     * The list returned is the list of vertex ids of the locations
     * of each ingredient in the valid sorting.
     */
    public List<Integer> getSortedIngredientLocations()
    {
        LinkedList<Integer> topo = new LinkedList<Integer>();
        ListIterator li = ingredients.topologicalSort().listIterator();
        while(li.hasNext())
        {
            String next = (String)li.next();
            Integer node = 0;
            if(next.equals("0")) node = 981;
            if(next.equals("1")) node = 1653;
            if(next.equals("2")) node = 524;
            if(next.equals("3")) node = 1864;
            if(next.equals("4")) node = 1119;
            if(next.equals("5")) node = 1104;
            topo.add(node);
            li.remove();
        }
        return topo;
    }

    /**
     * Given a File to the Ames data and an ordering of
     * ingredient location vertex ids, you are to parse the Ames file
     * and create a directed graph, then find the shortest route from
     * your location picking up the ingredients in the order specified,
     * and then delivering them to Jim's location.
     *
     * You are to use the distance provided in each edge of the Ames
     * data as the weights of the edges.
     *
     * The list returned is the order of vertex ids visited in the
     * shortest path starting with your location and ending with
     * Jim's location.
     */
    public List<Integer> getShortestRoute(File amesFile, List<Integer> orderingList)
    {
        parseFile(amesFile, false);
        orderingList.add(0, 1067);
        orderingList.add(orderingList.size(), 826);
        ListIterator li = orderingList.listIterator();
        List<Integer> shortestRoute = new ArrayList<Integer>();
        System.out.println("ORDER: " + orderingList.toString());
        String node = "" + li.next();
        System.out.println(graph.getNumVertices());
        System.out.println(graph.getNumEdges());
        while(li.hasNext())
        {
            String next = "" + li.next();
            System.out.println("FINDING " + next + " FROM " + node);
            shortestRoute.addAll(graph.shortestPath(node, next, measure));
            node = next;
            li.remove();
        }
        shortestRoute.add(826);
        System.out.println(shortestRoute.toString());
        System.out.println(shortestRoute.size());
        return shortestRoute;
    }

    /**
     * Given a File to the Ames data, you are to parse the file
     * and create an undirected graph, then find a minimum spanning
     * tree of the city, and return the total cost of the spanning
     * tree.
     *
     * Use the distance of every edge in the Ames file as the cost
     * of the edges. The total cost is the sum of all the edge costs
     * of the edges in the minimum spanning tree.
     */
    public double getMSTCost(File amesFile)
    {
        parseFile(amesFile, false);
        CSGraph<String, String> mst = (CSGraph<String, String>)graph.minimumSpanningTree(measure);
        Collection<EdgeData<String>> edges = mst.edgeDataList.values();
        double total = 0.0;
        for(EdgeData edge : edges)
        {
            total += Double.parseDouble((String)edge.getData());
        }
        System.out.println(total / 2 );
        return total / 2;
    }

    public void parseFile(File amesFile, boolean directed)
    {
        try
        {
            graph = new CSGraph<String, String>(directed);
            Scanner fileScanner = new Scanner(amesFile);
            fileScanner.next();
            int numVertices = fileScanner.nextInt();
            int vertexID, edgeSource, edgeTarget;
            String vertexData, newLine, next, edgeData;
            graph.setNumberVertices(numVertices);
            boolean vertices = true;
            fileScanner.nextLine();
            while(fileScanner.hasNextLine())
            {
                newLine = fileScanner.nextLine();
                Scanner scanner = new Scanner(newLine);
                scanner.useDelimiter(",|:");
                next = scanner.next();
                if(next.equals("EDGES"))
                {
                    vertices = false;
                }
                else if(vertices)
                {
                    vertexData = "";
                    vertexID = Integer.parseInt(next);
                    vertexData += scanner.next() + ",";
                    vertexData += scanner.next();
                    graph.addVertex("" + vertexID, vertexData);
                }
                else
                {
                    edgeSource = Integer.parseInt(next);
                    edgeTarget = Integer.parseInt(scanner.next());
                    edgeData = scanner.next();

                    graph.addEdge("" + edgeSource, "" + edgeTarget, edgeData);
                }
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }
}
