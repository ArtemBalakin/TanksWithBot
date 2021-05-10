package Blocks;

import Server.Server;
import utils.Position;
import Player.Tank;

public class Bullet extends AbstractElement {
    private boolean isAlive = true;
    private boolean isAddedYet = false;


    public Bullet(String path, Position position) {
        super(position, path);
    }

    public boolean isAlive() {
        return isAlive;
    }


    public void fly() {
        while (isAlive) {
            Position position = this.position;
            switch (this.pathToSimpleTexture) {
                case "Textures\\bullet_down.png":
                    position = new Position(this.position.getRow() + 1, this.position.getColumn());
                    break;
                case "Textures\\bullet_up.png":
                    position = new Position(this.position.getRow() - 1, this.position.getColumn());
                    break;
                case "Textures\\bullet_left.png":
                    position = new Position(this.position.getRow(), this.position.getColumn() - 1);
                    break;
                case "Textures\\bullet_right.png":
                    position = new Position(this.position.getRow(), this.position.getColumn() + 1);
                    break;
            }
            if (!Server.getMap().getBlockAt(position.getRow(), position.getColumn()).isBulletCanCross) {
                Server.getMap().getBlockAt(position.getRow(), position.getColumn()).damageEllement();
                isAlive = false;
                break;
            }
            if (Server.getMap().isTankHere(position))
                for (Tank t : Server.getMap().getTanks()) {
                    if (t.position.equals(position)) {
                        t.damageEllement();
                        isAlive = false;
                        break;
                    }
                }

            this.position = position;
            Server.sendMapToClient();
            Thread.yield();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Server.sendMapToClient();
    }

    public boolean getStatus() {
        return isAddedYet;
    }

    public void changeStatus() {
        isAddedYet = true;
    }
}
