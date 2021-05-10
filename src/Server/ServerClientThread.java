package Server;


import Commands.AbstractCommand;
import Commands.GetDataCommand;
import Commands.MoveTankCommand;
import Serializer.Serializer;
import Map.Map;
import Player.Tank;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerClientThread extends Thread {
    private final ExecutorService fixed = Executors.newFixedThreadPool(3);
    private final ArrayList<AbstractCommand> commands = new ArrayList<>();
    private boolean isAlive = true;
    private Tank tank;
    private Socket serverClient;
    private DataOutputStream outStream;
    private DataInputStream inStream;

    public ServerClientThread(Socket inSocket, Tank tank) {
        serverClient = inSocket;
        this.tank=tank;
        try {
            outStream = new DataOutputStream(serverClient.getOutputStream());
            inStream = new DataInputStream(serverClient.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Tank getTank() {
        return tank;
    }

    public void run() {
        fixed.execute(this::receiveCommand);
        fixed.execute(this::executeCommand);
        fixed.execute(this::sendAnswers);
    }


    public void sendAnswers() {
        if (Server.getMap() != null) {
            try {
                Map map = Server.getMap();
                byte[] bytes = Serializer.serialize(map);
                outStream.write(bytes);
                outStream.flush();
            } catch (IOException ignored) {
            }
        }
        Thread.yield();
    }

    public void killClient() {


        this.interrupt();
        isAlive = false;
        fixed.shutdown();
    }

    private synchronized void receiveCommand() {
        while (isAlive) {
            try {
                byte[] bytes = new byte[100000];
                inStream.read(bytes);
                AbstractCommand command = (AbstractCommand) Serializer.deserialize(bytes);
                commands.add(command);
            } catch (IOException ignored) {

            }
            Thread.yield();
        }
    }

    private void executeCommand() {
        while (isAlive) {
            while (commands.size() > 0) {
                AbstractCommand command = commands.get(0);
                if (!(command instanceof GetDataCommand) && command != null) {
                    if (tank != null) {
                        ((MoveTankCommand) command).setTank(tank);
                        command.execute();
                    }
                }
                commands.remove(0);
            }
            Thread.yield();
        }
    }
}
