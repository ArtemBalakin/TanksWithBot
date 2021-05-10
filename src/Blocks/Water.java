package Blocks;

import utils.Position;

public class Water extends Wall {
    public Water(Position position) {
        super('W', position, "Textures\\Battle_City_water.png");
        this.isBulletCanCross = true;
    }
}
