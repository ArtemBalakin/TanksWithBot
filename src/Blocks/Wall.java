package Blocks;

import utils.Position;

public abstract class Wall extends AbstractElement {

    public Wall(char key, Position position, String pathToSimpleTexture) {
        super(position, pathToSimpleTexture);
        this.key = key;
    }

}


