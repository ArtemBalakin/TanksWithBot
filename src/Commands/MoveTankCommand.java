package Commands;

import Player.Tank;

public abstract class MoveTankCommand extends AbstractCommand {
    private Tank tank;

    public Tank getTank() {
        return tank;
    }

    public void setTank(Tank tank) {
        this.tank = tank;
    }
}
