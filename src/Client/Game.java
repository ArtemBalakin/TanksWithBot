package Client;

import Blocks.AbstractElement;
import Blocks.Bullet;
import Commands.*;
import Exeptions.InvalidMapException;
import Map.Map;
import Player.Tank;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import utils.Position;

import java.net.MalformedURLException;

public class Game extends Application {
    private static Map map;
    private final Pane root = new Pane();
    private Scene scene;
    private Client client;

    public Game() {
        client = new Client("localhost");
        client.start();
        client.sendCommand(new GetDataCommand());
        while (true) {
            if (map != null) {
                scene = new Scene(root, map.getSize() * 64, map.getSize() * 64, Color.BLACK);
                break;
            }
            Thread.yield();
        }
    }

    public Map getMap() {
        return map;
    }

    public static void setMap(Map map) {
        Game.map = map;
    }

    @Override
    public void start(Stage primaryStage) {

        root.setStyle("-fx-background-color: BLACK");
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP: {
                    client.sendCommand(new GoUpCommand());
                    break;
                }
                case DOWN: {
                    client.sendCommand(new GoDownCommand());
                    break;
                }
                case LEFT: {
                    client.sendCommand(new GoLeftCommand());
                    break;
                }
                case RIGHT: {
                    client.sendCommand(new GoRightCommand());
                    break;
                }
                case SPACE:
                    client.sendCommand(new ShotCommand());
                    break;
            }
        });
        AnimationTimer at = new AnimationTimer() {
            @Override
            public void handle(long now) {
                try {
                    reDraw();
                } catch (MalformedURLException | InvalidMapException e) {
                    e.printStackTrace();
                }
            }

        };
        at.start();
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void addBlocks() {
        for (AbstractElement[] wallArray : map.getElements()) {
            for (AbstractElement wall : wallArray) {
                if (wall != null) {
                    Position position = wall.getPosition();
                    ImageView imageView = wall.getTexture();
                    imageView.setTranslateX(position.getColumn() * 64);
                    imageView.setTranslateY(position.getRow() * 64);
                    root.getChildren().add(imageView);
                }
            }
        }
    }

    private void addTank() {
        if (map != null)
            for (Tank tank : map.getTanks()) {
                if (tank.getLive() <= 0) continue;
                ImageView imageView = tank.getTexture();
                imageView.setTranslateX(tank.getPosition().getColumn() * 64);
                imageView.setTranslateY((tank.getPosition().getRow() * 64));
                root.getChildren().add(imageView);
            }
    }


    private void addBullets() {
        if(map!=null)
        if (map.getBullets() != null) {
            for (Bullet bullet : map.getBullets()) {
                if (bullet != null) {
                    if (bullet.isAlive()) {
                        ImageView imageView = bullet.getTexture();
                        imageView.setTranslateX(bullet.getPosition().getColumn() * 64);
                        imageView.setTranslateY((bullet.getPosition().getRow() * 64));
                        root.getChildren().add(imageView);
                        bullet.changeStatus();
                    }
                }
            }
        }
    }

    private void reDraw() throws MalformedURLException, InvalidMapException {
        if (root.getChildren() != null) {
            root.getChildren().clear();
        }
        if (map != null) {
            addBlocks();
            addTank();
            addBullets();
        }
    }

}

