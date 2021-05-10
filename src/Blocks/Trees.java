package Blocks;

import utils.Position;

public class Trees extends Wall {
    public Trees(Position position) {
        super('T', position, "Textures\\Battle_City_trees.png");
        isTankCanCross = true;
        isBulletCanCross = true;
    }
}
