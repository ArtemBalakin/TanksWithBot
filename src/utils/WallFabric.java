package utils;

import Blocks.*;

public class WallFabric {
    public static Wall createWall(char key, Position position) {
        if (key == 'S') return new SteelWall(position);
        if (key == 'B') return new BrickWall(position);
        if (key == 'C') return new Road(position);
        if (key == 'T') return new Trees(position);
        return new Water(position);
    }
}
