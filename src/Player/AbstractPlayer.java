package Player;

import Blocks.AbstractElement;
import Interfaces.Moves;
import utils.Position;

public abstract class AbstractPlayer extends AbstractElement implements Moves {
    public AbstractPlayer(Position position, String pathToSimpleTexture) {
        super(position, pathToSimpleTexture);
    }
}
