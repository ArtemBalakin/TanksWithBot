package Commands;

import Receiver.Receiver;

public class GoLeftCommand extends MoveTankCommand {
    @Override
    public void execute() {
        Receiver.moveLeft(this.getTank());
    }
}
