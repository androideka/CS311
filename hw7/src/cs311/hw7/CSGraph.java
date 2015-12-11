package cs311.hw7;

import com.sun.javafx.geom.Edge;
import com.sun.xml.internal.bind.v2.runtime.output.SAXOutput;

import java.util.*;

/**
 * Created by androideka on 11/9/15.
 * A simple generic graph data structure in which every vertex
 * in the graph is identified by a String label. This graph also
 * supports attaching an arbitrary data object to each vertex and
 * edge.
 *
 * @param <S>  The type of data stored in every VERTEX.
 *
 * @param <T>  The type of data stored in every EDGE.
 */
public class CSGraph<S,T> implements Graph<S,T>
{

    private boolean isDirected;
    private int edgeCount, vertexCount;

    private int processed = 0;

    private HashMap<String, ArrayList<String>> neighbors;

    private HashMap<String, VertexData<S>> vertexDataList = new HashMap<String, VertexData<S>>();
    public HashMap<String, EdgeData<T>> edgeDataList = new HashMap<String, EdgeData<T>>();

    private LinkedList<EdgeData> orderedEdges = new LinkedList<EdgeData>();

    private Stack<String> dfs = new Stack<String>();

    Comparator<EdgeData> comparator = new Comparator<EdgeData>() {
        @Override
        public int compare(EdgeData o1, EdgeData o2) {
            double diff = Double.parseDouble((String)o1.getData()) - Double.parseDouble((String)o2.getData());
            if(diff > 0)
            {
                return 1;
            }
            if(diff < 0)
            {
                return -1;
            }
            return 0;
        }
    };

    public CSGraph()
    {
        this.isDirected = false;
    }

    public CSGraph(boolean isDirected)
    {
        this.isDirected = isDirected;
    }


    /**
     * If this returns true, the graph is a directed
     * graph. If false, it is undirected.
     */
    public boolean isDirected()
    {
        return isDirected;
    }

    /**
     * Adds a new vertex to the graph that is identified by the
     * label and has the given vertex data attached to it.
     *
     * @param vertexLabel  The identifying label of the graph.
     *
     * @param vertexData   The data attached to the vertex.
     */
    public void addVertex(String vertexLabel, S vertexData)
    {
        VertexData<S> vertex = new VertexData<S>(vertexLabel, vertexData, State.States.UNDISCOVERED);
        vertexDataList.put(vertexLabel, vertex);
        vertexCount++;
    }

    /**
     * Removes the vertex and all edges associated with it
     * from the graph.
     *
     * @param vertexLabel The vertex to be removed.
     */
    public void removeVertex(String vertexLabel)
    {
        vertexDataList.remove(vertexLabel);
        ArrayList<String> vertexNeighbors = neighbors.get(vertexLabel);
        for(int i = 1; i < vertexNeighbors.size(); i++)
        {
            neighbors.get(vertexNeighbors.get(i)).remove(vertexLabel);
        }
        Set edges = edgeDataList.keySet();
        Stack<String> edgeStack = new Stack<String>();
        edgeStack.addAll(edges);
        String edge;
        while(!edgeStack.isEmpty())
        {
            edge = edgeStack.pop();
            if(edge.contains(vertexLabel))
            {
                edgeDataList.remove(edge);
            }
        }
        vertexNeighbors.clear();

        vertexCount--;
    }

    /**
     * Adds an edge to the graph from the source vertex to the
     * target vertex. The edge also has the given data stored
     * in it.
     *
     * If the graph is undirected, both vertices become
     * neighbors of one another. If it is directed, the target
     * vertex is added as a neighbor to source.
     *
     * @param sourceLabel  The label of the source vertex of
     *                     the edge.
     *
     * @param targetLabel  The label of the target vertex of
     *                     the edge.
     *
     * @param edgeData     The data attached to the edge.
     */
    public void addEdge(String sourceLabel, String targetLabel, T edgeData)
    {
        EdgeData<T> edge = new EdgeData<T>(sourceLabel, targetLabel, edgeData, State.States.UNPROCESSED);
        edgeDataList.put(sourceLabel + "," + targetLabel, edge);
        ArrayList<String> vertexNeighbors = neighbors.get(sourceLabel);
        if(!vertexNeighbors.contains(targetLabel))
        {
            vertexNeighbors.add(targetLabel);
        }
        if(!isDirected)
        {
            EdgeData<T> returnEdge = new EdgeData<T>(targetLabel, sourceLabel, edgeData, State.States.UNPROCESSED);
            edgeDataList.put(targetLabel + "," + sourceLabel, returnEdge);
            vertexNeighbors = neighbors.get(targetLabel);
            if(!vertexNeighbors.contains(sourceLabel))
            {
                vertexNeighbors.add(sourceLabel);
            }
            edgeCount++;
        }
        orderedEdges.add(edge);
        orderedEdges.sort(comparator);
        edgeCount++;
    }

    /**
     * Returns the edge data associated with this edge.
     *
     * @param sourceLabel  The source vertex of the edge.
     * @param targetLabel  The target vertex of the edge.
     */
    public T getEdgeData(String sourceLabel, String targetLabel)
    {
        return edgeDataList.get(sourceLabel + "," + targetLabel).getData();
    }

    /**
     * Returns the vertex data associated with this vertex.
     *
     * @param label  The label of the vertex.
     */
    public S getVertexData(String label)
    {
        return vertexDataList.get(label).getData();
    }

    /**
     * Returns the number of vertices in the graph.
     */
    public int getNumVertices()
    {
        return vertexCount;
    }

    /**
     * Returns the number of edges in the graph.
     */
    public int getNumEdges()
    {
        return edgeCount;
    }

    /**
     * Returns a collection of the labels of all the vertices
     * in the graph.
     */
    public Collection<String> getVertices()
    {
        return vertexDataList.keySet();
    }

    /**
     * Returns a collection of all the adjacent vertices of the
     * given vertex.
     *
     * @param label  The label of the vertex.
     */
    public Collection<String> getNeighbors(String label)
    {
        ArrayList<String> vertexNeighbors = neighbors.get(label);
        ListIterator<String> li = vertexNeighbors.listIterator();
        Collection<String> neighborCollection = new Stack<String>();
        while(li.hasNext())
        {
            neighborCollection.add(li.next());
        }
        ArrayList<EdgeData> edges = new ArrayList<EdgeData>();
        edges.addAll(edgeDataList.values());
        if(!isDirected()){
            for(EdgeData edge : edges)
            {
                if(edge.getTargetLabel().equals(label) && !neighborCollection.contains(edge.getSourceLabel()))
                {
                    neighborCollection.add(edge.getSourceLabel());
                }
            }
        }
        return neighborCollection;
    }

    /**
     * Returns a valid topological sort if the graph is a
     * directed acyclic graph and returns null otherwise.
     */
    public List<String> topologicalSort()
    {
        // TODO: Find cycles
        if(!isDirected())
        {
            System.out.println("Can't topological sort an undirected graph.");
            return null;
        }
        else
        {
            ArrayList<LinkedList<String>> topologicalSort = new ArrayList<LinkedList<String>>();
            while(processed < vertexCount)
            {
                for(int i = 0; i < vertexDataList.size(); i++)
                {
                    if(vertexDataList
                            .get(vertexDataList.keySet().toArray()[i])
                            .getState() == State.States.UNDISCOVERED)
                    {
                        depthFirstSearch((String)vertexDataList.keySet().toArray()[i]);
                    }
                }
                ListIterator<String> listIterator = dfs.listIterator();
                LinkedList<String> component = new LinkedList<String>();
                while(listIterator.hasNext())
                {
                    String next = listIterator.next();
                    component.add(next);
                }
                topologicalSort.add(component);
                dfs = new Stack<String>();
            }
            int i = 0;
            Stack<String> sort = new Stack<String>();
            while(i < topologicalSort.size())
            {
                LinkedList<String> component = topologicalSort.get(i);
                while(!component.isEmpty())
                {
                    String node = component.pop();
                    sort.push(node);
                }
                i++;
            }
            LinkedList<String> master = new LinkedList<String>();
            while(!sort.isEmpty())
            {
                master.add(sort.pop());
            }
            return master;
        }
    }

    /**
     * Returns the shortest path from the start vertex to
     * the destination vertex where "short" is defined by
     * the edge measure object. The path is a list of the
     * vertex labels in the path starting with the start
     * vertex and ending with the end vertex.
     *
     * @param startLabel  Starting vertex label.
     * @param destLabel   Destination vertex label.
     * @param measure     Measure that defines the weight
     *                    of every edge in the graph.
     *
     * @return The shortest path from start to destination.
     */
    public List<String> shortestPath(String startLabel, String destLabel, EdgeMeasure<T> measure)
    {
        // TODO: Dijkstra's
        ArrayList<String> open = new ArrayList<String>();
        ArrayList<String> closed = new ArrayList<String>();
        open.add(startLabel);
        final double[] dist = new double[this.getNumVertices()];
        String[] pred = new String[this.getNumVertices()];
        for(int i = 0; i < getNumVertices(); i++)
        {
            dist[i] = Double.POSITIVE_INFINITY;
            pred[i] = "";
        }
        dist[Integer.parseInt(startLabel)] = 0;
        while(!open.isEmpty())
        {
            String a = open.get(0);
            //System.out.println("Adding " + a + " to closed");
            closed.add(a);
            //System.out.println("Closed: " + closed.toString());
            open.remove(a);
            for(String neighbor : getNeighbors(a))
            {
                //System.out.println(a + " neighbors: " + getNeighbors(a).toString());
                //System.out.println(neighbor);
                if(neighbor.equals(destLabel) || a.equals(destLabel))
                {
                    System.out.println("CLOSED: " + closed.toString());
                    return closed;
                }
                if(!closed.contains(neighbor))
                {
                    double distance = 0.0;
                    if(edgeDataList.get(a + "," + neighbor) == null)
                    {
                        distance = Double.parseDouble(edgeDataList.get(neighbor + "," + a).getData().toString());
                    }
                    else
                    {
                        distance = Double.parseDouble(edgeDataList.get(a + "," + neighbor).getData().toString());
                    }
                    double alt = dist[Integer.parseInt(a)] + distance;
                    if(alt < dist[Integer.parseInt(neighbor)] && !open.contains(neighbor)
                            && !pred[Integer.parseInt(neighbor)].equals(a))
                    {
                        open.add(neighbor);
                        dist[Integer.parseInt(neighbor)] = alt;
                        pred[Integer.parseInt(neighbor)] = a;
                    }
                }
            }
            open.sort(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    double dist1 = dist[Integer.parseInt(o1)];
                    double dist2 = dist[Integer.parseInt(o2)];
                    if(dist1 - dist2 > 0)
                    {
                        return 1;
                    }
                    if(dist1 - dist2 < 0)
                    {
                        return -1;
                    }
                    else
                    {
                        return 0;
                    }
                }
            });
        }
        return closed;
    }

    /**
     * Returns a minimum spanning tree for the graph with
     * respect to the edge measure object. This tree is returned
     * as a undirected graph.
     *
     * @param measure that defines the weight of every edge
     *        in the graph.
     */
    public Graph<S,T> minimumSpanningTree(EdgeMeasure<T> measure)
    {
        System.out.println("HELLO");
        ArrayList<LinkedList<String>> forest = new ArrayList<LinkedList<String>>();
        CSGraph<S, T> mst = new CSGraph<S, T>(false);
        mst.setNumberVertices(this.getNumVertices());
        ArrayList<Subset> subsets = new ArrayList<Subset>();
        for(int i = 0; i < getNumVertices(); i++)
        {
            Subset subset = new Subset();
            subsets.add(subset);
            subsets.get(i).parent = i;
            subsets.get(i).rank = 0;
        }
        ListIterator li = orderedEdges.listIterator();
        while(li.hasNext())
        {
            EdgeData edge = (EdgeData)li.next();
            int x = mst.find(subsets, Integer.parseInt(edge.getSourceLabel()));
            int y = mst.find(subsets, Integer.parseInt(edge.getTargetLabel()));

            if(x == y)
            {
                li.remove();
            }
            else
            {
                mst.Union(subsets, x, y);
                mst.addEdge(edge.getSourceLabel(), edge.getTargetLabel(), (T)edge.getData());
            }
        }
        System.out.println(mst.getNumEdges());
        return mst;
    }

    /// MST helper
    public class Subset
    {
        int parent;
        int rank;
        public Subset()
        {

        }
    }

    /// MST helper
    public int find(ArrayList<Subset> subsets, int i)
    {
        if(subsets.get(i).parent != i)
        {
            subsets.get(i).parent = find(subsets, subsets.get(i).parent);
        }
        return subsets.get(i).parent;
    }

    /// MST helper
    public void Union(ArrayList<Subset> subsets, int x, int y)
    {
        int xRoot = find(subsets, x);
        int yRoot = find(subsets, y);

        if(subsets.get(xRoot).rank < subsets.get(yRoot).rank)
        {
            subsets.get(xRoot).parent = yRoot;
        }
        else if(subsets.get(xRoot).rank > subsets.get(yRoot).rank)
        {
            subsets.get(yRoot).parent = xRoot;
        }
        else
        {
            subsets.get(yRoot).parent = xRoot;
            subsets.get(xRoot).rank++;
        }
    }

    /**
     * Computes the total cost of the graph. The total cost is
     * the sum of the costs of every edge in the graph.
     *
     * @param measure The measure for how to determine the cost
     *                of an edge.
     */
    public double getTotalCost(EdgeMeasure<T> measure)
    {
        // TODO

        return 0.0;
    }

    public void depthFirstSearch(String root)
    {
        if(!hasUndiscoveredNeighbors(root))
        {
            vertexDataList.get(root).setState(State.States.PROCESSED);
            processed++;
            dfs.push(root);
            return;
        }

        Stack<String> neighbors = (Stack)getNeighbors(root);
        while(!neighbors.isEmpty())
        {
            if(vertexDataList.get(neighbors.peek()).getState() == State.States.PROCESSED)
            {
                neighbors.pop();
            }
            else
            {
                depthFirstSearch(neighbors.pop());
            }
        }
    }

    public List<String> breadthFirstSearch()
    {
        return null;
    }

    public void setNumberVertices(int numberVertices)
    {
        neighbors = new HashMap<String, ArrayList<String>>();
        for(int i = 0; i < numberVertices; i++)
        {
            neighbors.put("" + i, new ArrayList<String>());
        }
    }

    public boolean hasUndiscoveredNeighbors(String node)
    {
        Stack<String> neighbors = (Stack)getNeighbors(node);
        if(neighbors.isEmpty())
        {
            return false;
        }

        while(!neighbors.isEmpty())
        {
            String neighbor = neighbors.pop();
            if(vertexDataList.get(neighbor).getState() == State.States.UNDISCOVERED)
            {
                return true;
            }
        }
        return false;
    }

    public CSEdgeMeasure measure;

    public class CSEdgeMeasure implements EdgeMeasure<T>
    {
        public CSEdgeMeasure()
        {

        }
        public double getCost(T edgeData) {
            System.out.println(edgeData.toString());
            return 0.0;
        }
    }
}
