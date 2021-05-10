package Commands;

import Receiver.Receiver;

public class GoDownCommand extends MoveTankCommand {
    @Override
    public void execute() {
        Receiver.moveDown(this.getTank());
    }
}
