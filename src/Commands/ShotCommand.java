package Commands;

import Receiver.Receiver;

public class ShotCommand extends MoveTankCommand{
    @Override
    public void execute() {
        Receiver.fire(this.getTank());
    }
}
