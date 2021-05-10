package Blocks;

import utils.Position;

public class BrickWall extends Wall {
    public BrickWall(Position position) {
        super('B', position, "Textures\\Battle_City_bricks.png");
        this.live = 4;
    }
}
