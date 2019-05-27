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
    int germsPerRow = 90;
    int germsPerCol = 10;
    boolean MC = false;

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
        if(verifyAllGermsAreActive()) {
            for(int i=0; i<Y; ++i) {
                for(int j=0; j<X; ++j) {
                    setGermType(cells.get(i).get(j), j, i);
                }
            }
        } else {
            for (int i = 0; i < Y; ++i) {
                for (int j = 0; j < X; ++j) {
                    if (!cells.get(i).get(j).isActive()) {
                        setGermType(cells.get(i).get(j), j, i);
                    }
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

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getGermsPerRow() {
        return germsPerRow;
    }

    public void setGermsPerRow(int germsPerRow) {
        this.germsPerRow = germsPerRow;
    }

    public int getGermsPerCol() {
        return germsPerCol;
    }

    public void setGermsPerCol(int germsPerCol) {
        this.germsPerCol = germsPerCol;
    }

    public void nucleation(Nucleations nuc) {
        switch (nuc) {
            case HOMOGENEUS: {
                int divX = X / germsPerRow;
                int divY = Y / germsPerCol;
                for (int i = 0; i < Y; i += divY) {
                    for (int j = 0; j < X; j += divX) {
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
            case RADIUS: {
                Random rand = new Random();
                int divX = X / radius;
                int divY = Y / radius;
                int[][] cubes = new int[divY][divX];
                int tmpNoOfCells = X * Y / (2 * radius * radius);
                while (tmpNoOfCells > 0) {
                    for (int i = 0; i < divY; ++i) {
                        if (tmpNoOfCells < 0)
                            break;
                        for (int j = 0; j < divX; ++j) {
                            if (tmpNoOfCells < 0)
                                break;
                            int randX = rand.nextInt(radius);
                            int randY = rand.nextInt(radius);
                            int cellX = j * radius + randX;
                            int cellY = i * radius + randY;
                            if (checkNearestCubes(j, i, cellX, cellY, cubes)) {
                                cells.get(cellY).get(cellX).createNewGerm();
                                cubes[i][j]++;
                                tmpNoOfCells--;
                            }
                        }
                    }
                }
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
        int energySum = 0;
        for (int i = 0; i < noOfTypes + 1; ++i) {
            if(MC && indexes[i]>0 && germ.getType() != i)
            {
                energySum++;
            }
            if (indexes[i] > maxSum) {
                maxSum = indexes[i];
                maxIndex = i;
            }
        }
        germ.setDominantNeighborhood(maxIndex);
        if(MC)
            germ.setEnergy(energySum);
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
        if (x >= 0 && x < X && y >= 0 && y < Y) {
            if (cells.get(y).get(x).isActive())
                indexes[cells.get(y).get(x).getType()]++;
        } else if (x >= 0 && x < X && y >= 0) {
            if (cells.get(0).get(x).isActive())
                indexes[cells.get(0).get(x).getType()]++;
        } else if (x >= 0 && x < X && y < Y) {
            if (cells.get(Y - 1).get(x).isActive())
                indexes[cells.get(Y - 1).get(x).getType()]++;
        } else if (x >= 0 && y >= 0 && y < Y) {
            if (cells.get(y).get(0).isActive())
                indexes[cells.get(y).get(0).getType()]++;
        } else if (x < X && y >= 0 && y < Y) {
            if (cells.get(y).get(X - 1).isActive())
                indexes[cells.get(y).get(X - 1).getType()]++;
        } else if (x >= 0 && y >= 0) {
            if (cells.get(0).get(0).isActive())
                indexes[cells.get(0).get(0).getType()]++;
        } else if (x < X && y < Y) {
            if (cells.get(Y - 1).get(X - 1).isActive())
                indexes[cells.get(Y - 1).get(X - 1).getType()]++;
        } else if (x >= 0 && y < Y) {
            if (cells.get(Y - 1).get(0).isActive())
                indexes[cells.get(Y - 1).get(0).getType()]++;
        } else if (x < X && y >= 0) {
            if (cells.get(0).get(X - 1).isActive())
                indexes[cells.get(0).get(X - 1).getType()]++;
        }
    }

    boolean checkNearestCubes(int x, int y, int randX, int randY, int[][] cubes) {
        int divY = cubes.length;
        int divX = cubes[0].length;

        if (!findGrainsInCubes(x, y, randX, randY, cubes))
            return false;
        if (x - 1 >= 0 && y - 1 >= 0) {
            if (!findGrainsInCubes(x - 1, y - 1, randX, randY, cubes))
                return false;
        }
        if (y - 1 >= 0) {
            if (!findGrainsInCubes(x, y - 1, randX, randY, cubes))
                return false;
        }
        if (x + 1 < divX && y - 1 >= 0) {
            if (!findGrainsInCubes(x + 1, y - 1, randX, randY, cubes))
                return false;
        }
        if (x + 1 < divX) {
            if (!findGrainsInCubes(x + 1, y, randX, randY, cubes))
                return false;
        }
        if (x + 1 < divX && y + 1 < divY) {
            if (!findGrainsInCubes(x + 1, y + 1, randX, randY, cubes))
                return false;
        }
        if (y + 1 < divY) {
            if (!findGrainsInCubes(x, y + 1, randX, randY, cubes))
                return false;
        }
        if (x - 1 >= 0 && y + 1 < divY) {
            if (!findGrainsInCubes(x - 1, y + 1, randX, randY, cubes))
                return false;
        }
        if (x - 1 >= 0) {
            if (!findGrainsInCubes(x - 1, y, randX, randY, cubes))
                return false;
        }
        return true;
    }

    boolean findGrainsInCubes(int checkedX, int checkedY, int cellX, int cellY, int[][] cubes) {
        if (cubes[checkedY][checkedX] > 0) {
            int counter = cubes[checkedY][checkedX];
            while (counter > 0) {
                for (int i = checkedY * radius; i < checkedY * radius + radius; ++i) {
                    for (int j = checkedX * radius; j < checkedX * radius + radius; ++j) {
                        if (cells.get(i).get(j).isActive()) {
                            double tmp = Math.sqrt(((i - cellY) * (i - cellY)) + ((j - cellX) * (j - cellX)));
                            if (tmp <= radius) {
                                return false;
                            } else
                                counter--;
                        }
                    }
                }
            }
        }
        return true;
    }
    boolean verifyAllGermsAreActive() {
        for(int i=0; i<Y; ++i) {
            for(int j=0; j<X; ++j) {
                if(!cells.get(i).get(j).isActive()) {
                    MC = false;
                    return false;
                }
            }
        }
        MC = true;
        return true;
    }
}
