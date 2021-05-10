package Client;


import Commands.AbstractCommand;
import Serializer.Serializer;
import Map.Map;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {
    private static DataInputStream inStream;
    private static DataOutputStream outStream;
    private Socket socket;
    private ExecutorService fixed = Executors.newFixedThreadPool(2);
    private BufferedReader br;

    public Client(String address) {
        try {
            socket = new Socket(address, 8888);
            inStream = new DataInputStream(socket.getInputStream());
            outStream = new DataOutputStream(socket.getOutputStream());
            br = new BufferedReader(new InputStreamReader(System.in));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendCommand(AbstractCommand command) {
        try {
            outStream.write(Serializer.serialize(command));
            outStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void start() {
        fixed.execute(this::receiveMessage);
    }

    private void receiveMessage() {
        while (true) {
            try {
                byte[] bytes = new byte[100000];
                inStream.read(bytes);
                Map map = (Map) Serializer.deserialize(bytes);
                Game.setMap(map);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(0);
            }
            Thread.yield();
        }
    }
}

