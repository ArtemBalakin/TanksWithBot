package Map;

import Blocks.AbstractElement;
import Blocks.Bullet;
import Blocks.SteelWall;
import Blocks.Wall;
import Bot.Bot;
import Exeptions.InvalidMapException;
import Player.Tank;
import utils.Position;
import utils.WallFabric;

import java.io.*;
import java.util.ArrayList;


public class Map implements Serializable {
    protected final ArrayList<Tank> tanks = new ArrayList<>();
    protected final ArrayList<Bullet> bullets = new ArrayList<>();
    protected int size;
    protected AbstractElement[][] elements;


    //TODO
    // Добавить лист боттов
    public Map() {
        try {
            initialize();
        } catch (InvalidMapException e) {
            e.printStackTrace();
        }
    }


    public AbstractElement getBlockAt(int row, int column) {
        try {
            return elements[row][column];
        } catch (ArrayIndexOutOfBoundsException e) {
            return new SteelWall(new Position(0, 0));

        }
    }

    public AbstractElement[][] getElements() {
        return elements;
    }

    public int getSize() {
        return size;
    }

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public ArrayList<Tank> getTanks() {
        return tanks;
    }

    private void initialize() throws InvalidMapException {
        String str;
        int row = 0;
        int column;
        int counter = 0;
        try {
            File file = new File("mapFile.txt");
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            String line = reader.readLine();
            size = Integer.parseInt(line);
            if (size == 0) {
                throw new InvalidMapException("Map size can not be zero");
            }
            elements = new Wall[size][size];
            int count = size * size;
            line = reader.readLine();
            while (line != null && count > 0) {
                System.out.println(line);
                str = line;
                column = 0;
                for (int i = 0; i < str.length(); i++) {
                    if (str.charAt(i) != ' ') {
                        elements[row][column] = WallFabric.createWall(str.charAt(i), new Position(row, column));
                        column++;
                        counter++;
                    }
                }
                row++;
                count -= size;
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (counter < size * size) {
            throw new InvalidMapException("Not enough map elements");
        }
    }


    public Tank addTanks() {
        for (int i = 0; i < elements.length; i++) {
            for (int j = 0; j < elements.length; j++) {
                if (elements[i][j].getKey() == 'C' && !isTankHere(elements[i][j].getPosition())) {
                    Tank tank = new Tank(elements[i][j].getPosition());
                    tanks.add(tank);
                    return tank;
                }
            }
        }
        return null;
    }

    public void addBots() {
        for (int i = 0; i < elements.length; i++) {
            for (int j = 0; j < elements.length; j++) {
                if (elements[i][j].getKey() == 'C' && !isTankHere(elements[i][j].getPosition())) {
                    tanks.add(0, new Bot(elements[i][j].getPosition()));
                    System.out.println("Bot adds");
                    return;
                }
            }
        }
    }

    public boolean isTankHere(Position position) {
        for (Tank t : tanks) {
            if (t.getPosition().equals(position) && t.getLive() > 0) return true;
        }
        return false;
    }
}

