package cs311.hw7;

/**
 * Created by androideka on 11/13/15.
 */
public class EdgeData<T>
{
    private String sourceLabel;
    private String targetLabel;
    private T data;
    private State.States state;

    public EdgeData(String sourceLabel, String targetLabel, T data, State.States state)
    {
        this.sourceLabel = sourceLabel;
        this.targetLabel = targetLabel;
        this.data = data;
        this.state = state;
    }

    public String getSourceLabel()
    {
        return sourceLabel;
    }

    public String getTargetLabel()
    {
        return targetLabel;
    }

    public T getData()
    {
        return data;
    }

    public State.States getState()
    {
        return state;
    }

    public void setState(State.States state)
    {
        this.state = state;
    }
}
