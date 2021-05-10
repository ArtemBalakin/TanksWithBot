package Receiver;

import Server.Server;
import Bot.Bot;
import Blocks.Bullet;
import Player.Tank;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Receiver {
    private static ExecutorService service = Executors.newCachedThreadPool();

    public static void moveRight(Tank tank) {
        tank.moveRight();
        Server.sendMapToClient();
    }


    public static void moveLeft(Tank tank) {
        tank.moveLeft();
        Server.sendMapToClient();
    }


    public static void moveUp(Tank tank) {
        tank.moveUp();
        Server.sendMapToClient();
    }


    public static void moveDown(Tank tank) {
        tank.moveDown();
        Server.sendMapToClient();
    }

    public static void fire(Tank tank) {
        Bullet bullet = tank.fire();
        Server.getMap().getBullets().add(bullet);
        if (bullet != null) {
            Runnable r = () -> bullet.fly();
            service.execute(r);
        }
    }

    public static void startBots() {
        for (Tank t : Server.getMap().getTanks()) {
            if (t instanceof Bot) {
                Runnable r = ((Bot) t)::step;
                service.execute(r);
            }
        }
    }

}
