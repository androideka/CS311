package cs311.hw7;

import java.nio.channels.Pipe;
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

    private ArrayList<String>[] neighbors;

    private HashMap<String, VertexData<S>> vertexDataList = new HashMap<String, VertexData<S>>();
    private HashMap<String, EdgeData<T>> edgeDataList = new HashMap<String, EdgeData<T>>();

    private Stack<String> dfs = new Stack<String>();

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
        // TODO: Remove edges to this vertex!!!!!
        vertexDataList.remove(vertexLabel);
        int vertex = Integer.parseInt(vertexLabel);
        ArrayList<String> vertexNeighbors = neighbors[vertex];
        for(int i = 1; i < vertexNeighbors.size(); i++)
        {
            neighbors[Integer.parseInt(vertexNeighbors.get(i))].remove(vertexLabel);
        }
        Stack<String> edges = (Stack)edgeDataList.keySet();
        String edge = edges.pop();
        while(edge != null)
        {
            if(edge.contains(vertexLabel))
            {
                edgeDataList.remove(edge);
            }
            edge = edges.pop();
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
        int vertex = Integer.parseInt(sourceLabel);
        ArrayList<String> vertexNeighbors = neighbors[vertex];
        if(!vertexNeighbors.contains(targetLabel))
        {
            vertexNeighbors.add(targetLabel);
        }
        if(!isDirected)
        {
            EdgeData<T> returnEdge = new EdgeData<T>(targetLabel, sourceLabel, edgeData, State.States.UNPROCESSED);
            edgeDataList.put(targetLabel + "," + sourceLabel, returnEdge);
            vertex = Integer.parseInt(targetLabel);
            vertexNeighbors = neighbors[vertex];
            if(!vertexNeighbors.contains(sourceLabel))
            {
                vertexNeighbors.add(sourceLabel);
            }
            edgeCount++;
        }
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
        int vertex = Integer.parseInt(label);
        ArrayList<String> vertexNeighbors = neighbors[vertex];
        ListIterator<String> li = vertexNeighbors.listIterator();
        Collection<String> neighborCollection = new Stack<String>();
        while(li.hasNext())
        {
            neighborCollection.add(li.next());
        }
        return neighborCollection;
    }

    /**
     * Returns a valid topological sort if the graph is a
     * directed acyclic graph and returns null otherwise.
     */
    public List<String> topologicalSort()
    {
        HashMap<String, VertexData<S>> components = (HashMap)vertexDataList.clone();
        // TODO: Find cycles and return null, deal with disconnected graph...
        if(!isDirected())
        {
            System.out.println("Can't topological sort an undirected graph.");
            return null;
        }
        else
        {
            HashMap<String, VertexData<S>> copy = (HashMap)vertexDataList.clone();
            ArrayList<LinkedList<String>> topologicalSort = new ArrayList<LinkedList<String>>();
            while(!copy.isEmpty())
            {
                System.out.println("keys: " + copy.keySet().toString());
                System.out.println("Root: " + copy.keySet().toArray()[0]);
                depthFirstSearch((String)copy.keySet().toArray()[0], components);
                ListIterator<String> listIterator = dfs.listIterator();
                LinkedList<String> component = new LinkedList<String>();
                while(listIterator.hasNext())
                {
                    System.out.println("WTF");
                    String next = listIterator.next();
                    copy.remove(next);
                    component.add(next);
                }
                System.out.println("Component: " + component.toString());
                topologicalSort.add(component);
                dfs = new Stack<String>();
            }
            int i = 0;
            LinkedList<String> master = new LinkedList<String>();
            while(i < topologicalSort.size())
            {
                LinkedList<String> component = topologicalSort.get(i);
                while(!component.isEmpty())
                {
                    String node = component.removeFirst();
                    System.out.println("Adding node " + node);
                    master.add(node);
                }
                i++;
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
        // TODO
        return null;
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
        // TODO
        return null;
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

    public void depthFirstSearch(String root, HashMap<String, VertexData<S>> nodes)
    {
        if(getNeighbors(root) == null)
        {
            System.out.println("No neighbors for node " + root);
            dfs.push(root);
            return;
        }

        Stack<String> neighbors = (Stack<String>)getNeighbors(root);
        System.out.println("Neighbors: " + neighbors);
        while(!neighbors.isEmpty())
        {
            System.out.println("Picking neighbor " + nodes.get(neighbors.peek()).getVertexID());
            System.out.println("Neighbor state = " + nodes.get(neighbors.peek()).getState());
            if(nodes.get(neighbors.peek()).getState() != State.States.DISCOVERED)
            {
                System.out.println("Next neighbor: " + neighbors.peek());
                nodes.get(neighbors.peek()).setState(State.States.DISCOVERED);
                depthFirstSearch(neighbors.pop(), nodes);
            }
            else return;
        }
    }

    public List<String> breadthFirstSearch()
    {
        return null;
    }

    public void setNumberVertices(int numberVertices)
    {
        neighbors = new ArrayList[numberVertices];
        for(int i = 0; i < numberVertices; i++)
        {
            neighbors[i] = new ArrayList<String>();
        }
    }
}
