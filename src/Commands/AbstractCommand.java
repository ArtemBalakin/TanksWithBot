package Commands;

import java.io.Serializable;

public abstract class AbstractCommand implements Serializable {
    public abstract void execute();
}
