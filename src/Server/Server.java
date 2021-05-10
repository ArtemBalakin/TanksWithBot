package Server;


import Receiver.Receiver;
import Map.Map;
import Player.Tank;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server extends Application {
    private static final ExecutorService fixed = Executors.newFixedThreadPool(2);
    private static final ArrayList<ServerClientThread> clientThreads = new ArrayList<>();
    private static Map map = new Map();
    private static ServerSocket server;


    public static void main(String[] args) {
        try {
            server = new ServerSocket(8888);
            map.addBots();

            fixed.execute(Server::addClients);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void sendMapToClient() {
        try {
            for (ServerClientThread thread : clientThreads) {
                thread.sendAnswers();
            }
        } catch (ConcurrentModificationException e) {
            return;
        }
    }

    public synchronized static void killClient(Tank tank) {
        for (ServerClientThread thread : clientThreads) {
            if (thread.getTank().equals(tank)) {
                thread.killClient();
                break;
            }
        }
    }

    public static Map getMap() {
        return map;
    }

    private static void addClients() {
        Socket serverClient;
        while (true) {
            try {
                serverClient = server.accept();
                Tank tank = Server.getMap().addTanks();
                Receiver.startBots();
                sendMapToClient();
                ServerClientThread sct = new ServerClientThread(serverClient, tank);
                clientThreads.add(sct);
                sct.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Thread.yield();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

    }
}
