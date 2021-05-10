package Blocks;

import Receiver.Receiver;
import Server.Server;
import Bot.Bot;
import utils.Position;
import Player.Tank;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.Serializable;
import java.net.MalformedURLException;

public abstract class AbstractElement implements Serializable {

    protected static String pathToBrokenTexture;
    protected String pathToSimpleTexture;
    protected char key;
    protected float live = Float.POSITIVE_INFINITY;
    protected boolean isTankCanCross = false;
    protected boolean isBulletCanCross = false;
    protected Position position;

    public AbstractElement(Position position, String pathToSimpleTexture) {
        this.position = position;
        pathToBrokenTexture = "roadWall.jpg";
        this.pathToSimpleTexture = pathToSimpleTexture;
    }

    public float getLive() {
        return live;
    }

    public String getPathToSimpleTexture() {
        return pathToSimpleTexture;
    }


    public void changeTexture(String pathToTexture) {
        pathToSimpleTexture = pathToTexture;
    }

    public Position getPosition() {
        return position;
    }

    public char getKey() {
        return key;
    }

    public boolean getIsTankCanCross() {
        return isTankCanCross;
    }

    public boolean getIsBulletCanCross() {
        return isBulletCanCross;
    }

    public ImageView getTexture() {
        File file = new File(pathToSimpleTexture);
        try {
            String localUrl = file.toURI().toURL().toString();
            Image image = new Image(localUrl);
            return new ImageView(image);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void damageEllement() {
        if (live > 0) {
            live--;
            if (live == 0) {
                brokeElement();
            }
        }
    }

    private void brokeElement() {
        if (this instanceof Tank) {
            Server.getMap().getTanks().remove(this);
            Server.killClient((Tank) this);
        }
        if (this instanceof Bot) {
            Server.getMap().addBots();
            Receiver.startBots();
        }
        changeTextureToBroken();
        changeIsBulletCanCross();
        changeIsTankCanCross();
    }

    private void changeTextureToBroken() {
        changeTexture(pathToBrokenTexture);
    }

    protected void changeIsBulletCanCross() {
        this.isBulletCanCross = !isBulletCanCross;
    }

    protected void changeIsTankCanCross() {
        this.isTankCanCross = !isTankCanCross;
    }
}
