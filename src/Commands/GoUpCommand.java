package Commands;

import Receiver.Receiver;

public class GoUpCommand extends MoveTankCommand {
    @Override
    public void execute() {
        Receiver.moveUp(this.getTank());
    }
}
