package Bot;

import Blocks.Bullet;
import Player.Tank;
import Server.Server;
import Map.Map;
import utils.Position;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Bot extends Tank {
    private static ExecutorService service = Executors.newCachedThreadPool();
    private String path;

    public Bot(Position position) {
        super(position);
        this.pathToSimpleTexture = "Textures\\botRight.png";
    }

    public void fillAdjMatrix(boolean[][] adjMatrix, ArrayList<Position> vertices) {
        for (int i = 0; i < vertices.size() - 1; i++) {
            for (int j = i + 1; j < vertices.size(); j++) {
                if (Math.abs(vertices.get(i).getRow() - vertices.get(j).getRow()) == 1 &&
                        vertices.get(i).getColumn() - vertices.get(j).getColumn() == 0 ||
                        Math.abs(vertices.get(i).getColumn() - vertices.get(j).getColumn()) == 1 &&
                                vertices.get(i).getRow() - vertices.get(j).getRow() == 0
                ) {
                    adjMatrix[i][j] = true;
                    adjMatrix[j][i] = true;
                }
            }
        }
    }

    public ArrayList<Position> getVertices() {
        Map map = Server.getMap();
        ArrayList<Position> vertices = new ArrayList<>();

        for (int i = 0; i < map.getSize(); i++) {
            for (int j = 0; j < map.getSize(); j++) {
                char currCeilKey = map.getBlockAt(i, j).getKey();
                if (currCeilKey == 'C' || currCeilKey == 'T' || currCeilKey == 'B') {
                    vertices.add(new Position(i, j));
                }
            }
        }

        return vertices;
    }

    public int getVertex(ArrayList<Position> vertices, Position position) {
        for (int i = 0; i < vertices.size(); i++) {
            if (position.getRow() == vertices.get(i).getRow() &&
                    position.getColumn() == vertices.get(i).getColumn()
            ) {
                return i;
            }
        }
        return vertices.size();
    }

    public ArrayList<Integer> bfs(int n, int a, int b, boolean[][] g) {
        Vector<Integer> from = new Vector<>(n);
        Vector<Integer> used = new Vector<>(n);
        Vector<Integer> dist = new Vector<>(n);
        from.setSize(n);
        used.setSize(n);
        dist.setSize(n);

        for (int i = 0; i < n; i++) {
            from.set(i, -1);
            used.set(i, 0);
            dist.set(i, 0);
        }

        Queue<Integer> q = new ArrayDeque<>();
        q.offer(a);
        dist.set(a, 0);
        used.set(a, 1);

        while (!q.isEmpty()) {
            int w = q.peek();
            q.remove();
            for (int i = 0; i < n; i++) {
                if (g[w][i] && used.get(i) == 0) //если есть ребро из нашей вершины в i-тую и мы в ней не были
                {
                    dist.set(i, dist.get(w) + 1);
                    from.set(i, w);

                    q.offer(i); // иначе - обходим и ее тоже

                    used.set(i, 1);
                }
            }
        }

        if (used.get(b) == 1) { // если мы вышли, потому что дошли до искомой
            Vector<Integer> path = new Vector<>();

            while (from.get(b) != -1) {
                path.add(b);
                b = from.get(b);
            }

            path.add(b);

            ArrayList<Integer> result = new ArrayList<>();

            for (int i = path.size() - 2; i >= 0; i--) {
                result.add(path.get(i));
            }

            return result;
        } else {
            System.out.println("Пути нету!");
            return null;
        }
    }

    public boolean canWeShoot() {
        Map map = Server.getMap();
        Position playerPosition = null;
        for (Tank t : map.getTanks()) {
            if (!(t instanceof Bot))
                playerPosition = t.getPosition();
        }
        if (playerPosition != null)
            if (position.getRow() == playerPosition.getRow()) {
                if (position.getColumn() > playerPosition.getColumn() && this.pathToSimpleTexture.equals("Textures\\botLeft.png")) {
                    boolean f = true;
                    for (int i = playerPosition.getColumn(); i <= position.getColumn(); i++) {
                        if (map.getBlockAt(position.getRow(), i).getKey() == 'S') {
                            f = false;
                            break;
                        }
                    }
                    if (f) {
                        return true;
                    }
                } else if (this.pathToSimpleTexture.equals("Textures\\botRight.png")) {
                    boolean f = true;
                    for (int i = position.getColumn(); i <= playerPosition.getColumn(); i++) {
                        if (map.getBlockAt(position.getRow(), i).getKey() == 'S') {
                            f = false;
                            break;
                        }
                    }
                    if (f) {
                        return true;
                    }
                }
            } else if (position.getColumn() == playerPosition.getColumn()) {
                if (position.getRow() > playerPosition.getRow() && this.pathToSimpleTexture.equals("Textures\\botUp.png")) {
                    boolean f = true;
                    for (int i = playerPosition.getRow(); i <= position.getRow(); i++) {
                        if (map.getBlockAt(position.getRow(), i).getKey() == 'S') {
                            f = false;
                            break;
                        }
                    }
                    if (f) {
                        return true;
                    }
                } else if (this.pathToSimpleTexture.equals("Textures\\botDown.png")) {
                    boolean f = true;
                    for (int i = position.getRow(); i <= playerPosition.getRow(); i++) {
                        if (map.getBlockAt(position.getRow(), i).getKey() == 'S') {
                            f = false;
                            break;
                        }
                    }
                    if (f) {
                        return true;
                    }
                }
            } else {
                return false;
            }

        return false;
    }


    public void performNextStep(Position nextPosition) {
        if (canWeShoot()) {
            Bullet bullet = this.fire();
            Server.getMap().getBullets().add(bullet);
            if (bullet != null) {
                Runnable r = () -> bullet.fly();
                service.execute(r);
            }
            Server.sendMapToClient();
            return;
        }

        int xCurr = position.getColumn();
        int yCurr = position.getRow();
        int xNext = nextPosition.getColumn();
        int yNext = nextPosition.getRow();

        if (xCurr < xNext) {
            moveRight();
        } else if (xCurr > xNext) {
            moveLeft();
        } else if (yCurr > yNext) {
            moveUp();
        } else if (yCurr < yNext) {
            moveDown();
        }
    }

    public void step() {
        Map map = Server.getMap();
        ArrayList<Position> vertices = getVertices(); // вершины графа
        int n = vertices.size(); // количество вершин графа
        boolean[][] adjMatrix = new boolean[n][n]; // матрица смежности
        fillAdjMatrix(adjMatrix, vertices); // заполняем матрицу смежности

        Position playerPosition = new Position(
                map.getTanks().get(1).getPosition().getRow(),
                map.getTanks().get(1).getPosition().getColumn()
        );
        Position currentPlayerPosition = map.getTanks().get(1).getPosition();

        int start = getVertex(vertices, position); // стартовая вершина
        int end = getVertex(vertices, playerPosition); // конечная вершина

        ArrayList<Integer> verticesPath = bfs(n, start, end, adjMatrix);

        while (this.live > 0) {

            int i = 0;

            while (i < verticesPath.size()) {

                if (!currentPlayerPosition.equals(playerPosition)) {
                    start = getVertex(vertices, position);
                    end = getVertex(vertices, currentPlayerPosition);

                    verticesPath = bfs(n, start, end, adjMatrix);

                    while (verticesPath == null) {
                        currentPlayerPosition = map.getTanks().get(1).getPosition();
                        start = getVertex(vertices, position);
                        end = getVertex(vertices, currentPlayerPosition);
                        verticesPath = bfs(n, start, end, adjMatrix);
                    }

                    playerPosition.setColumn(currentPlayerPosition.getColumn());
                    playerPosition.setRow(currentPlayerPosition.getRow());

                    i = 0;
                    continue;
                }

                performNextStep(vertices.get(verticesPath.get(i)));
                i++;

                Server.sendMapToClient();

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        Thread.yield();
    }

    @Override
    public void moveRight() {
        super.moveRight();
        this.pathToSimpleTexture = "Textures\\botRight.png";
    }

    @Override
    public void moveLeft() {
        super.moveLeft();
        this.pathToSimpleTexture = "Textures\\botLeft.png";
    }

    @Override
    public void moveUp() {
        super.moveUp();
        this.pathToSimpleTexture = "Textures\\botUp.png";
    }

    @Override
    public void moveDown() {
        super.moveDown();
        this.pathToSimpleTexture = "Textures\\botDown.png";
    }

    @Override
    public Bullet fire() {
        Position position;
        if (this.getLive() > 0) {
            if (pathToSimpleTexture.equals("Textures\\botLeft.png")) {
                position = new Position(this.getPosition().getRow(), this.getPosition().getColumn());
                return new Bullet("Textures\\bullet_left.png", position);
            } else if (pathToSimpleTexture.equals("Textures\\botDown.png")) {
                position = new Position(this.getPosition().getRow(), this.getPosition().getColumn());
                return new Bullet("Textures\\bullet_down.png", position);
            } else if (pathToSimpleTexture.equals("Textures\\botUp.png")) {
                position = new Position(this.getPosition().getRow(), this.getPosition().getColumn());
                return new Bullet("Textures\\bullet_up.png", position);
            } else if (pathToSimpleTexture.equals("Textures\\botRight.png")) {
                position = new Position(this.getPosition().getRow(), this.getPosition().getColumn());
                return new Bullet("Textures\\bullet_right.png", position);
            }
            return new Bullet("Textures\\bullet_right.png", new Position(Server.getMap().getSize(),
                    Server.getMap().getSize()));
        }
        return null;
    }
}