package Player;

import Blocks.Bullet;
import Server.Server;
import utils.Position;

import java.util.Objects;

public class Tank extends AbstractPlayer {
    private static int Index = 0;
    private final int index;

    public Tank(Position position) {
        super(position, "Textures\\tankImageRight.png");
        this.index = Index;
        Index++;
        key = 'P';
        isBulletCanCross = false;
        isTankCanCross = false;
        live = 10;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public void moveRight() {
        pathToSimpleTexture = "Textures\\tankImageRight.png";
        if (Server.getMap().getBlockAt(position.getRow(), position.getColumn() + 1).getIsTankCanCross() &&
                !Server.getMap().isTankHere(new Position(position.getRow(), position.getColumn() + 1))) {
            position.setColumn(position.getColumn() + 1);
        }
        if (Server.getMap().getBlockAt(position.getRow(), position.getColumn()).getKey() == 'T') {
            hideTank();
        }
        this.changeTexture(pathToSimpleTexture);
    }

    @Override
    public void moveLeft() {
        this.pathToSimpleTexture = "Textures\\tankImageLeft.png";
        if (Server.getMap().getBlockAt(position.getRow(), position.getColumn() - 1).getIsTankCanCross() &&
                !Server.getMap().isTankHere(new Position(position.getRow(), position.getColumn() - 1))) {
            position.setColumn(position.getColumn() - 1);
        }
        if (Server.getMap().getBlockAt(position.getRow(), position.getColumn()).getKey() == 'T') {
            hideTank();
        }
        this.changeTexture(pathToSimpleTexture);
    }


    @Override
    public void moveUp() {
        this.pathToSimpleTexture = "Textures\\tankImageUp.png";
        if (Server.getMap().getBlockAt(position.getRow() - 1, position.getColumn()).getIsTankCanCross() &&
                !Server.getMap().isTankHere(new Position(position.getRow() - 1, position.getColumn()))) {
            position.setRow((position.getRow() - 1));
        }
        if (Server.getMap().getBlockAt(position.getRow(), position.getColumn()).getKey() == 'T') {
            hideTank();
        }
        this.changeTexture(pathToSimpleTexture);
    }

    @Override
    public void moveDown() {
        this.pathToSimpleTexture = "Textures\\tankImageDown.png";
        if (Server.getMap().getBlockAt(position.getRow() + 1, position.getColumn()).getIsTankCanCross() &&
                !Server.getMap().isTankHere(new Position(position.getRow() + 1, position.getColumn()))) {
            position.setRow(position.getRow() + 1);
        }
        if (Server.getMap().getBlockAt(position.getRow(), position.getColumn()).getKey() == 'T') {
            hideTank();
        }
        this.changeTexture(pathToSimpleTexture);
    }

    public Bullet fire() {
        Position position;
        if (this.getLive() > 0) {
            if (pathToSimpleTexture.equals("Textures\\tankImageLeft.png")) {
                position = new Position(this.getPosition().getRow(), this.getPosition().getColumn());
                return new Bullet("Textures\\bullet_left.png", position);
            } else if (pathToSimpleTexture.equals("Textures\\tankImageDown.png")) {
                position = new Position(this.getPosition().getRow(), this.getPosition().getColumn());
                return new Bullet("Textures\\bullet_down.png", position);
            } else if (pathToSimpleTexture.equals("Textures\\tankImageUp.png")) {
                position = new Position(this.getPosition().getRow(), this.getPosition().getColumn());
                return new Bullet("Textures\\bullet_up.png", position);
            } else if (pathToSimpleTexture.equals("Textures\\tankImageRight.png")) {
                position = new Position(this.getPosition().getRow(), this.getPosition().getColumn());
                return new Bullet("Textures\\bullet_right.png", position);
            }
            return new Bullet("Textures\\bullet_right.png", new Position(Server.getMap().getSize(), Server.getMap().getSize()));
        }
        return null;
    }

    private void hideTank() {
        setHidesTexture();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tank)) return false;
        Tank tank = (Tank) o;
        return getIndex() == tank.getIndex();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIndex());
    }

    public void setHidesTexture() {
        pathToSimpleTexture = "Textures\\Battle_City_trees.png";
    }
}