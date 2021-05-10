package Commands;

import Receiver.Receiver;

public class GoRightCommand extends MoveTankCommand {
    @Override
    public void execute() {
        Receiver.moveRight(this.getTank());
    }
}
