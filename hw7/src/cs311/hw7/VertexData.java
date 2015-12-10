package cs311.hw7;

/**
 * Created by androideka on 11/13/15.
 */
public class VertexData<S>
{
    private String vertexID;
    private String parent;
    private S data;
    private State.States state;
    private int component;

    public VertexData(String vertexID, S data, State.States state)
    {
        this.vertexID = vertexID;
        this.data = data;
        this.state = state;
    }

    public String getVertexID()
    {
        return vertexID;
    }

    public S getData()
    {
        return data;
    }

    public State.States getState()
    {
        return state;
    }

    public int getComponent()
    {
        return component;
    }

    public void setState(State.States state)
    {
        this.state = state;
    }

    public void setParent(String parent)
    {
        this.parent = parent;
    }

    public void setComponent(int component)
    {
        this.component = component;
    }
}
