package Blocks;

import utils.Position;

public class Road extends Wall {
    public Road(Position position) {
        super('C', position, "Textures\\roadWall.jpg");
        isTankCanCross = true;
        isBulletCanCross = true;
    }
}
