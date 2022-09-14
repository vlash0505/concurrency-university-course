package ui;

import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.*;
import java.awt.event.*;

import java.util.*;

import java.util.stream.IntStream;

public class GridPanel extends JPanel implements ActionListener {
    private static final int TILE_SIZE = 30;
    private static final int FRAME_WIDTH = 720;
    private static final int FRAME_HEIGHT = 450;
    private static final int NUMBER_OF_BEES = 3;

    private final Tile[][] gridMatrix;
    private final Tile bearTile;

    private boolean isBearFound;

    public GridPanel() {
        this.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));

        bearTile = new Tile(new Random().nextInt(FRAME_WIDTH/TILE_SIZE), new Random().nextInt(FRAME_HEIGHT/TILE_SIZE));

        this.gridMatrix = new Tile[FRAME_WIDTH / TILE_SIZE][FRAME_HEIGHT / TILE_SIZE];
        this.fillGridMatrix();
    }

    /**
     * Method that fills the grid matrix such as each tile corresponds
     * with matrix's indexes.
     */

    public void fillGridMatrix() {
        IntStream.range(0, gridMatrix.length)
                .forEach(x -> IntStream.range(0, gridMatrix[x].length)
                        .forEach(y -> gridMatrix[x][y] = new Tile(x, y)));
    }

    public void performSearchAnimation() {
        final Queue<Tile> firstBeeArea = buildVisitingPath(0);
        final Queue<Tile> secondBeeArea = buildVisitingPath(1);
        final Queue<Tile> thirdBeeArea = buildVisitingPath(2);;

        new Timer(50, createSearchAnimator(firstBeeArea)).start();
        new Timer(50, createSearchAnimator(secondBeeArea)).start();
        new Timer(50, createSearchAnimator(thirdBeeArea)).start();
    }

    public Queue<Tile> buildVisitingPath(int beeIndex) {
        Queue<Tile> queue = new LinkedList<>();

        final int colNum = (FRAME_WIDTH / TILE_SIZE) / NUMBER_OF_BEES;
        for (int i = beeIndex * colNum; i < colNum * (beeIndex + 1); i++) {
            queue.addAll(Arrays.asList(gridMatrix[i]).subList(0, FRAME_HEIGHT / TILE_SIZE));
        }
        return queue;
    }

    private ActionListener createSearchAnimator(Queue<Tile> tiles) {
        return new ActionListener() {
            final Iterator<Tile> visitedIterator = tiles.iterator();
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!visitedIterator.hasNext() || isBearFound) {
                    return;
                }
                Tile currentTile = visitedIterator.next();
                if (currentTile.equals(bearTile)) {
                    isBearFound = true;
                }
                currentTile.setVisited(true);
                repaint();
            }
        };
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintGrid(g);
    }

    public void paintGrid(Graphics g) {
        for (int i = 0; i < FRAME_WIDTH / TILE_SIZE; i++) {
            for (int j = 0; j < FRAME_HEIGHT / TILE_SIZE; j++) {
                g.setColor(Color.WHITE);
                if (gridMatrix[i][j].isVisited()) {
                    g.setColor(Color.CYAN);
                }
                if (gridMatrix[i][j].equals(bearTile)) {
                    g.setColor(Color.BLACK);
                }
                paintTile(g, gridMatrix[i][j]);
            }
        }
    }

    public void paintTile(Graphics g, Tile t) {
        int x = t.getX() * TILE_SIZE;
        int y = t.getY() * TILE_SIZE;
        g.fillRect(x, y, TILE_SIZE, TILE_SIZE);

        g.setColor(Color.BLACK);
        g.drawRect(x, y, TILE_SIZE, TILE_SIZE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {}
}
