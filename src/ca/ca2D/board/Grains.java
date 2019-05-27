package ca.ca2D.board;

import ca.Board;
import ca.Cell;
import ca.ca2D.cell.Germ;
import ca.helpers.Tile;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Grains extends Board {
    List<List<Germ>> cells;
    int radius;
    int germsPerRow = 10;
    int germsPerCol = 12;

    public enum Nucleations {
        HOMOGENEUS, RADIUS, RANDOM, BANNED
    }

    public enum NeighborhoodType {
        VON_NEUMANN, PENTAGONAL_RAND, HEXAGONAL_RIGHT,
        HEXAGONAL_LEFT, HEXAGONAL_RAND, MOORE
    }

    private NeighborhoodType neighborhood;

    public Grains(int x, int y) {
        this.X = x;
        this.Y = y;
        setState(SimulationState.FRESH);
        cells = new ArrayList<>(y);
        for (int i = 0; i < Y; ++i) {
            cells.add(new ArrayList<>(x));
            for (int j = 0; j < X; ++j) {
                cells.get(i).add(new Germ());
            }
        }
    }

    @Override
    public void checkNeighbours() {
        for (int i = 0; i < Y; ++i) {
            for (int j = 0; j < X; ++j) {
                if (!cells.get(i).get(j).isActive()) {
                    setGermType(cells.get(i).get(j), j, i);
                }
            }
        }
    }

    @Override
    public void play() {
        checkNeighbours();
        for (int i = 0; i < Y; ++i) {
            for (int j = 0; j < X; ++j) {
                if (!cells.get(i).get(j).isActive()) {
                    cells.get(i).get(j).setType();
                    if (cells.get(i).get(j).getType() != 0)
                        cells.get(i).get(j).setActive(true);
                }
            }
        }
    }

    @Override
    public void draw(List<Tile> tiles) {
        int iterator = 0;
        for (int i = 0; i < Y; ++i) {
            for (int j = 0; j < X; ++j) {
                if (cells.get(i).get(j).isActive())
                    tiles.get(iterator).setColor(cells.get(i).get(j).getType());
                else
                    tiles.get(iterator).setColor(Color.WHITE);
                iterator++;
            }
        }
    }

    @Override
    public Cell getCell(int x, int y) {
        return cells.get(y).get(x);
    }

    public void nucleation(Nucleations nuc) {
        switch (nuc) {
            case HOMOGENEUS: {
                int divX = X / germsPerRow;
                int divY = Y / germsPerCol;
                for (int i = 0; i < Y; i += divX) {
                    for (int j = 0; j < X; j += divY) {
                        cells.get(i).get(j).createNewGerm();
                    }
                }
                break;
            }
            case RANDOM: {
                Random rand = new Random();
                for (int i = 0; i < Y; i++) {
                    for (int j = 0; j < X; j++) {
                        if (rand.nextInt(100) > 90)
                            cells.get(i).get(j).createNewGerm();
                    }
                }
                break;
            }
            case BANNED: {
                cells.get(Y / 2).get(X / 2).createNewGerm();
                break;
            }
        }
    }

    private void setGermType(Germ germ, int x, int y) {
        int noOfTypes = Germ.getCounter();
        int[] indexes = new int[noOfTypes + 1];

        switch (neighborhood) {
            case VON_NEUMANN: {
                checkPosition(x, y - 1, indexes);
                checkPosition(x + 1, y, indexes);
                checkPosition(x, y + 1, indexes);
                checkPosition(x - 1, y, indexes);
                break;
            }
            case PENTAGONAL_RAND: {
                Random rand = new Random();
                switch (rand.nextInt(4)) {
                    case 0: {
                        checkPosition(x, y - 1, indexes);
                        checkPosition(x - 1, y - 1, indexes);
                        checkPosition(x - 1, y, indexes);
                        checkPosition(x - 1, y + 1, indexes);
                        checkPosition(x, y + 1, indexes);
                        break;
                    }
                    case 1: {
                        checkPosition(x, y - 1, indexes);
                        checkPosition(x + 1, y - 1, indexes);
                        checkPosition(x + 1, y, indexes);
                        checkPosition(x + 1, y + 1, indexes);
                        checkPosition(x, y + 1, indexes);
                        break;
                    }
                    case 2: {
                        checkPosition(x - 1, y, indexes);
                        checkPosition(x - 1, y - 1, indexes);
                        checkPosition(x, y - 1, indexes);
                        checkPosition(x + 1, y - 1, indexes);
                        checkPosition(x + 1, y, indexes);
                        break;
                    }
                    case 3: {
                        checkPosition(x - 1, y, indexes);
                        checkPosition(x - 1, y + 1, indexes);
                        checkPosition(x, y + 1, indexes);
                        checkPosition(x + 1, y + 1, indexes);
                        checkPosition(x + 1, y, indexes);
                        break;
                    }
                }
                break;
            }
            case HEXAGONAL_LEFT: {
                checkPosition(x, y - 1, indexes);
                checkPosition(x + 1, y, indexes);
                checkPosition(x, y + 1, indexes);
                checkPosition(x - 1, y, indexes);
                checkPosition(x + 1, y - 1, indexes);
                checkPosition(x - 1, y + 1, indexes);
                break;
            }
            case HEXAGONAL_RIGHT: {
                checkPosition(x, y - 1, indexes);
                checkPosition(x + 1, y, indexes);
                checkPosition(x, y + 1, indexes);
                checkPosition(x - 1, y, indexes);
                checkPosition(x - 1, y - 1, indexes);
                checkPosition(x + 1, y + 1, indexes);
                break;
            }
            case HEXAGONAL_RAND: {
                Random rand = new Random();
                switch (rand.nextInt(2)) {
                    case 0: {
                        checkPosition(x, y - 1, indexes);
                        checkPosition(x + 1, y, indexes);
                        checkPosition(x, y + 1, indexes);
                        checkPosition(x - 1, y, indexes);
                        checkPosition(x + 1, y - 1, indexes);
                        checkPosition(x - 1, y + 1, indexes);
                        break;
                    }
                    case 1: {
                        checkPosition(x, y - 1, indexes);
                        checkPosition(x + 1, y, indexes);
                        checkPosition(x, y + 1, indexes);
                        checkPosition(x - 1, y, indexes);
                        checkPosition(x - 1, y - 1, indexes);
                        checkPosition(x + 1, y + 1, indexes);
                        break;
                    }
                }
                break;
            }
            case MOORE: {
                checkPosition(x, y - 1, indexes);
                checkPosition(x + 1, y, indexes);
                checkPosition(x, y + 1, indexes);
                checkPosition(x - 1, y, indexes);
                checkPosition(x - 1, y - 1, indexes);
                checkPosition(x + 1, y + 1, indexes);
                checkPosition(x + 1, y - 1, indexes);
                checkPosition(x - 1, y + 1, indexes);
                break;
            }
        }
        int maxSum = -1;
        int maxIndex = 0;
        for (int i = 0; i < noOfTypes + 1; ++i) {
            if (indexes[i] > maxSum) {
                maxSum = indexes[i];
                maxIndex = i;
            }
        }
        germ.setDominantNeighborhood(maxIndex);
    }


    public void setNeighborhood(NeighborhoodType neighborhood) {
        this.neighborhood = neighborhood;
    }

    void checkPosition(int x, int y, int[] indexes) {
        if (!isPeriodicBoundaryConditions()) {
            if (x >= 0 && x < X && y >= 0 && y < Y && cells.get(y).get(x).isActive())
                indexes[cells.get(y).get(x).getType()]++;
        } else
            checkPositionPeriodic(x, y, indexes);
    }

    void checkPositionPeriodic(int x, int y, int[] indexes) {
        if (x >= 0 && x < X && y >= 0 && y < Y ) {
            if(cells.get(y).get(x).isActive())
                indexes[cells.get(y).get(x).getType()]++;
        }
        else if (x >= 0 && x < X && y >= 0) {
            if(cells.get(0).get(x).isActive())
                indexes[cells.get(0).get(x).getType()]++;
        }
        else if (x >= 0 && x < X && y < Y) {
            if(cells.get(Y - 1).get(x).isActive())
                indexes[cells.get(Y - 1).get(x).getType()]++;
        }
        else if (x >= 0 && y >= 0 && y < Y ) {
            if(cells.get(y).get(0).isActive())
                indexes[cells.get(y).get(0).getType()]++;
        }
        else if (x < X && y >= 0 && y < Y) {
            if(cells.get(y).get(X - 1).isActive())
                indexes[cells.get(y).get(X - 1).getType()]++;
        }
        else if (x >= 0 && y >= 0) {
            if (cells.get(0).get(0).isActive())
                indexes[cells.get(0).get(0).getType()]++;
        }
        else if (x < X && y < Y ) {
            if (cells.get(Y - 1).get(X - 1).isActive())
                indexes[cells.get(Y - 1).get(X - 1).getType()]++;
        }
        else if (x >= 0 && y < Y) {
            if (cells.get(Y - 1).get(0).isActive())
                indexes[cells.get(Y - 1).get(0).getType()]++;
        }
        else if (x < X && y >= 0) {
            if (cells.get(0).get(X - 1).isActive())
                indexes[cells.get(0).get(X - 1).getType()]++;
        }
    }
}
