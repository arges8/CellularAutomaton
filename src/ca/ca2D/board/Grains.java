package ca.ca2D.board;

import ca.Board;
import ca.Cell;
import ca.ca2D.cell.Germ;
import ca.controllers.Controller;
import ca.helpers.Tile;
import javafx.scene.paint.Color;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Grains extends Board {
    public static final String XLS_DATA_FILE =  "E:\\Projects\\CellularAutomaton\\src\\ca\\resources\\rekrystalizacja.xls";
    private double criticalDislocationDensity;
    List<List<Germ>> cells;
    int radius;
    int germsPerRow = 90;
    int germsPerCol = 10;
    boolean MC = false;
    boolean firstLoop = false;
    Workbook workbook;
    int iterator = 1;
    List<Map.Entry<Double, Double>> timeToRoList;

    public enum Nucleations {
        HOMOGENEUS, RADIUS, RANDOM, BANNED, EMPTY
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
        readFromXlsFile();
        cells = new ArrayList<>(y);
        for (int i = 0; i < Y; ++i) {
            cells.add(new ArrayList<>(x));
            for (int j = 0; j < X; ++j) {
                cells.get(i).add(new Germ());
            }
        }
        criticalDislocationDensity = 4.215840142323416E12 / ((double)X*Y);
    }

    @Override
    public void checkNeighbours() {
        if(Controller.startRec && iterator < timeToRoList.size()) {
            MC = false;
            dislocationGun(timeToRoList.get(iterator).getValue() - timeToRoList.get(iterator-1).getValue());
            dislocationNucleation();
            for (int i = 0; i < Y; ++i) {
                for (int j = 0; j < X; ++j) {
                    setGermType(cells.get(i).get(j), j, i);
                }
            }
            dislocationNucleation();
            for (int i = 0; i < Y; ++i) {
                for (int j = 0; j < X; ++j) {
                    if(cells.get(i).get(j).check) {
                        cells.get(i).get(j).setRecrystalized(true);
                        cells.get(i).get(j).setType(Germ.getDisId());
                        cells.get(i).get(j).setDislocationDensity(0);
                    }
                }
            }
            iterator++;
        }
        else if (verifyAllGermsAreActive() && MC) {
            for (int i = 0; i < Y; ++i) {
                for (int j = 0; j < X; ++j) {
                    setGermType(cells.get(i).get(j), j, i);
                }
            }

            for (int i = 0; i < Y; ++i) {
                for (int j = 0; j < X; ++j) {
                    Random rand = new Random();
                    if (rand.nextInt(100) > 60)
                        cells.get(i).get(j).hypotheticalEnergy();
                }
            }
        } else if(!Controller.startRec){
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
                if(!Controller.energy) {
                    if (cells.get(i).get(j).isActive())
                        tiles.get(iterator).setColor(cells.get(i).get(j).getType());
                    else
                        tiles.get(iterator).setColor(Color.WHITE);
                    iterator++;
                } else {
                    tiles.get(iterator).setColor(cells.get(i).get(j).getCurrentEnergy(),true);
                    iterator++;
                }
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

    public boolean isMC() {
        return MC;
    }

    public void setMC(boolean MC) {
        this.MC = MC;
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
                        if (rand.nextInt(100) > 98)
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
            case EMPTY: {
                Tile.setGOL(false);
                break;
            }
        }
    }

    private void setGermType(Germ germ, int x, int y) {
        int noOfTypes = Germ.getCounter();
        int[] indexes = new int[noOfTypes + 1];

        switch (neighborhood) {
            case VON_NEUMANN: {
                if(germ.neighboursCoordinates.size() == 0)
                {
                    germ.neighboursCoordinates.add(checkPosition(x, y - 1, indexes));
                    germ.neighboursCoordinates.add(checkPosition(x + 1, y, indexes));
                    germ.neighboursCoordinates.add(checkPosition(x, y + 1, indexes));
                    germ.neighboursCoordinates.add(checkPosition(x - 1, y, indexes));
                } else {
                    checkPosition(x, y - 1, indexes);
                    checkPosition(x + 1, y, indexes);
                    checkPosition(x, y + 1, indexes);
                    checkPosition(x - 1, y, indexes);
                }
                break;
            }
            case PENTAGONAL_RAND: {
                Random rand = new Random();
                switch (rand.nextInt(4)) {
                    case 0: {
                        if(germ.neighboursCoordinates.size() == 0) {
                            germ.neighboursCoordinates.add(checkPosition(x, y - 1, indexes));
                            germ.neighboursCoordinates.add(checkPosition(x - 1, y - 1, indexes));
                            germ.neighboursCoordinates.add(checkPosition(x - 1, y, indexes));
                            germ.neighboursCoordinates.add(checkPosition(x - 1, y + 1, indexes));
                            germ.neighboursCoordinates.add(checkPosition(x, y + 1, indexes));

                        } else {
                            checkPosition(x, y - 1, indexes);
                            checkPosition(x - 1, y - 1, indexes);
                            checkPosition(x - 1, y, indexes);
                            checkPosition(x - 1, y + 1, indexes);
                            checkPosition(x, y + 1, indexes);
                        }
                        break;
                    }
                    case 1: {
                        if(germ.neighboursCoordinates.size() == 0) {
                            germ.neighboursCoordinates.add(checkPosition(x, y - 1, indexes));
                            germ.neighboursCoordinates.add(checkPosition(x + 1, y - 1, indexes));
                            germ.neighboursCoordinates.add(checkPosition(x + 1, y, indexes));
                            germ.neighboursCoordinates.add(checkPosition(x + 1, y + 1, indexes));
                            germ.neighboursCoordinates.add(checkPosition(x, y + 1, indexes));

                        } else {
                            checkPosition(x, y - 1, indexes);
                            checkPosition(x + 1, y - 1, indexes);
                            checkPosition(x + 1, y, indexes);
                            checkPosition(x + 1, y + 1, indexes);
                            checkPosition(x, y + 1, indexes);
                        }
                        break;
                    }
                    case 2: {
                        if(germ.neighboursCoordinates.size() == 0) {
                            germ.neighboursCoordinates.add(checkPosition(x - 1, y, indexes));
                            germ.neighboursCoordinates.add(checkPosition(x - 1, y - 1, indexes));
                            germ.neighboursCoordinates.add(checkPosition(x, y - 1, indexes));
                            germ.neighboursCoordinates.add(checkPosition(x + 1, y - 1, indexes));
                            germ.neighboursCoordinates.add(checkPosition(x + 1, y, indexes));

                        } else {
                            checkPosition(x - 1, y, indexes);
                            checkPosition(x - 1, y - 1, indexes);
                            checkPosition(x, y - 1, indexes);
                            checkPosition(x + 1, y - 1, indexes);
                            checkPosition(x + 1, y, indexes);
                        }
                        break;
                    }
                    case 3: {
                        if(germ.neighboursCoordinates.size() == 0) {
                            germ.neighboursCoordinates.add(checkPosition(x - 1, y, indexes));
                            germ.neighboursCoordinates.add(checkPosition(x - 1, y + 1, indexes));
                            germ.neighboursCoordinates.add(checkPosition(x, y + 1, indexes));
                            germ.neighboursCoordinates.add(checkPosition(x + 1, y + 1, indexes));
                            germ.neighboursCoordinates.add(checkPosition(x + 1, y, indexes));

                        } else {
                            checkPosition(x - 1, y, indexes);
                            checkPosition(x - 1, y + 1, indexes);
                            checkPosition(x, y + 1, indexes);
                            checkPosition(x + 1, y + 1, indexes);
                            checkPosition(x + 1, y, indexes);
                        }
                        break;
                    }
                }
                break;
            }
            case HEXAGONAL_LEFT: {
                if(germ.neighboursCoordinates.size() == 0) {
                    germ.neighboursCoordinates.add(checkPosition(x, y - 1, indexes));
                    germ.neighboursCoordinates.add(checkPosition(x + 1, y, indexes));
                    germ.neighboursCoordinates.add(checkPosition(x, y + 1, indexes));
                    germ.neighboursCoordinates.add(checkPosition(x - 1, y, indexes));
                    germ.neighboursCoordinates.add(checkPosition(x + 1, y - 1, indexes));
                    germ.neighboursCoordinates.add(checkPosition(x - 1, y + 1, indexes));

                } else {
                    checkPosition(x, y - 1, indexes);
                    checkPosition(x + 1, y, indexes);
                    checkPosition(x, y + 1, indexes);
                    checkPosition(x - 1, y, indexes);
                    checkPosition(x + 1, y - 1, indexes);
                    checkPosition(x - 1, y + 1, indexes);
                }
                break;
            }
            case HEXAGONAL_RIGHT: {
                if(germ.neighboursCoordinates.size() == 0) {
                    germ.neighboursCoordinates.add(checkPosition(x, y - 1, indexes));
                    germ.neighboursCoordinates.add(checkPosition(x + 1, y, indexes));
                    germ.neighboursCoordinates.add(checkPosition(x, y + 1, indexes));
                    germ.neighboursCoordinates.add(checkPosition(x - 1, y, indexes));
                    germ.neighboursCoordinates.add(checkPosition(x - 1, y - 1, indexes));
                    germ.neighboursCoordinates.add(checkPosition(x + 1, y + 1, indexes));

                } else {
                    checkPosition(x, y - 1, indexes);
                    checkPosition(x + 1, y, indexes);
                    checkPosition(x, y + 1, indexes);
                    checkPosition(x - 1, y, indexes);
                    checkPosition(x - 1, y - 1, indexes);
                    checkPosition(x + 1, y + 1, indexes);
                }
                break;
            }
            case HEXAGONAL_RAND: {
                Random rand = new Random();
                switch (rand.nextInt(2)) {
                    case 0: {
                        if(germ.neighboursCoordinates.size() == 0) {
                            germ.neighboursCoordinates.add(checkPosition(x, y - 1, indexes));
                            germ.neighboursCoordinates.add(checkPosition(x + 1, y, indexes));
                            germ.neighboursCoordinates.add(checkPosition(x, y + 1, indexes));
                            germ.neighboursCoordinates.add(checkPosition(x - 1, y, indexes));
                            germ.neighboursCoordinates.add(checkPosition(x + 1, y - 1, indexes));
                            germ.neighboursCoordinates.add(checkPosition(x - 1, y + 1, indexes));

                        } else {
                            checkPosition(x, y - 1, indexes);
                            checkPosition(x + 1, y, indexes);
                            checkPosition(x, y + 1, indexes);
                            checkPosition(x - 1, y, indexes);
                            checkPosition(x + 1, y - 1, indexes);
                            checkPosition(x - 1, y + 1, indexes);
                        }
                        break;
                    }
                    case 1: {
                        if(germ.neighboursCoordinates.size() == 0) {
                            germ.neighboursCoordinates.add(checkPosition(x, y - 1, indexes));
                            germ.neighboursCoordinates.add(checkPosition(x + 1, y, indexes));
                            germ.neighboursCoordinates.add(checkPosition(x, y + 1, indexes));
                            germ.neighboursCoordinates.add(checkPosition(x - 1, y, indexes));
                            germ.neighboursCoordinates.add(checkPosition(x - 1, y - 1, indexes));
                            germ.neighboursCoordinates.add(checkPosition(x + 1, y + 1, indexes));

                        } else {
                            checkPosition(x, y - 1, indexes);
                            checkPosition(x + 1, y, indexes);
                            checkPosition(x, y + 1, indexes);
                            checkPosition(x - 1, y, indexes);
                            checkPosition(x - 1, y - 1, indexes);
                            checkPosition(x + 1, y + 1, indexes);
                        }
                        break;
                    }
                }
                break;
            }
            case MOORE: {
                if(germ.neighboursCoordinates.size() == 0) {
                    germ.neighboursCoordinates.add(checkPosition(x, y - 1, indexes));
                    germ.neighboursCoordinates.add(checkPosition(x + 1, y, indexes));
                    germ.neighboursCoordinates.add(checkPosition(x, y + 1, indexes));
                    germ.neighboursCoordinates.add(checkPosition(x - 1, y, indexes));
                    germ.neighboursCoordinates.add(checkPosition(x - 1, y - 1, indexes));
                    germ.neighboursCoordinates.add(checkPosition(x + 1, y + 1, indexes));
                    germ.neighboursCoordinates.add(checkPosition(x + 1, y - 1, indexes));
                    germ.neighboursCoordinates.add(checkPosition(x - 1, y + 1, indexes));

                } else {
                    checkPosition(x, y - 1, indexes);
                    checkPosition(x + 1, y, indexes);
                    checkPosition(x, y + 1, indexes);
                    checkPosition(x - 1, y, indexes);
                    checkPosition(x - 1, y - 1, indexes);
                    checkPosition(x + 1, y + 1, indexes);
                    checkPosition(x + 1, y - 1, indexes);
                    checkPosition(x - 1, y + 1, indexes);
                }
                break;
            }
        }
        int maxSum = -1;
        int maxIndex = 0;
        int energySum = 0;
        List<Integer> takeRandIndex = new ArrayList<>(8);
        for (int i = 0; i < noOfTypes + 1; ++i) {
            if (indexes[i] > 0) {
                for (int j = 0; j < indexes[i]; ++j)
                    takeRandIndex.add(i);
                if(germ.getType() != i)
                    energySum++;
            }
            if (firstLoop && indexes[i] > 0 && germ.getType() != i) {
                energySum++;
            }
            if (indexes[i] > maxSum) {
                maxSum = indexes[i];
                maxIndex = i;
            }
        }
        germ.setDominantNeighborhood(maxIndex);
        if (firstLoop) {
            germ.setEnergy(energySum);
            germ.setNeighbours(takeRandIndex);
        }
        if(Controller.startRec)
        {
            germ.setCurrentEnergy(energySum);
            boolean recrystal = true;
            boolean neirec = false;
            if(germ.neighboursCoordinates.size() > 0) {
                for (Map.Entry map : germ.neighboursCoordinates) {
                    if(cells.get((int) map.getKey()).get((int) map.getValue()).isRecrystalized())
                        neirec = true;
                    if (cells.get((int) map.getKey()).get((int) map.getValue()).getDislocationDensity() >
                            germ.getDislocationDensity()) {
                        recrystal = false;
                    }
                }
                if (recrystal && neirec)
                    germ.check = true;
            }
        }
    }


    public void setNeighborhood(NeighborhoodType neighborhood) {
        this.neighborhood = neighborhood;
    }

    Map.Entry<Integer, Integer> checkPosition(int x, int y, int[] indexes) {
        if (!isPeriodicBoundaryConditions()) {
            if (x >= 0 && x < X && y >= 0 && y < Y && cells.get(y).get(x).isActive()) {
                indexes[cells.get(y).get(x).getType()]++;
                return Map.entry(y,x);
            }
            return null;
        } else
            return checkPositionPeriodic(x, y, indexes);

    }

    Map.Entry<Integer, Integer> checkPositionPeriodic(int x, int y, int[] indexes) {
        if (x >= 0 && x < X && y >= 0 && y < Y) {
            if (cells.get(y).get(x).isActive())
                indexes[cells.get(y).get(x).getType()]++;
            return Map.entry(y,x);
        } else if (x >= 0 && x < X && y >= 0) {
            if (cells.get(0).get(x).isActive())
                indexes[cells.get(0).get(x).getType()]++;
            return Map.entry(0,x);
        } else if (x >= 0 && x < X && y < Y) {
            if (cells.get(Y - 1).get(x).isActive())
                indexes[cells.get(Y - 1).get(x).getType()]++;
            return Map.entry(Y-1,x);
        } else if (x >= 0 && y >= 0 && y < Y) {
            if (cells.get(y).get(0).isActive())
                indexes[cells.get(y).get(0).getType()]++;
            return Map.entry(y,0);
        } else if (x < X && y >= 0 && y < Y) {
            if (cells.get(y).get(X - 1).isActive())
                indexes[cells.get(y).get(X - 1).getType()]++;
            return Map.entry(y,X-1);
        } else if (x >= 0 && y >= 0) {
            if (cells.get(0).get(0).isActive())
                indexes[cells.get(0).get(0).getType()]++;
            return Map.entry(0,0);
        } else if (x < X && y < Y) {
            if (cells.get(Y - 1).get(X - 1).isActive())
                indexes[cells.get(Y - 1).get(X - 1).getType()]++;
            return Map.entry(Y-1,X-1);
        } else if (x >= 0 && y < Y) {
            if (cells.get(Y - 1).get(0).isActive())
                indexes[cells.get(Y - 1).get(0).getType()]++;
            return Map.entry(Y-1,0);
        } else if (x < X && y >= 0) {
            if (cells.get(0).get(X - 1).isActive())
                indexes[cells.get(0).get(X - 1).getType()]++;
            return Map.entry(0,X-1);
        }
        return null;
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
        for (int i = 0; i < Y; ++i) {
            for (int j = 0; j < X; ++j) {
                if (!cells.get(i).get(j).isActive()) {
                    firstLoop = false;
                    return false;
                }
            }
        }
        firstLoop = true;
        Controller.rec = true;
        return true;
    }
    public void readFromXlsFile() {
        Sheet sheet;
        int firstRow = 4;
        try {
            workbook = WorkbookFactory.create(new File(XLS_DATA_FILE));
        } catch (IOException io) {
            System.out.println(io.getStackTrace());
        }
        sheet = workbook.getSheetAt(0);
        Row row;
        timeToRoList = new LinkedList<>();
        int noOfRows = sheet.getLastRowNum();
        for(int i=0; i<=noOfRows-firstRow; ++i) {
            row = sheet.getRow(firstRow+i);
            timeToRoList.add(Map.entry(row.getCell(0).getNumericCellValue(), row.getCell(1).getNumericCellValue()));
        }
    }
    void dislocationGun(double deltaRo) {
        double avrgDislocation = (deltaRo / ((double) (X * Y)));
        double disForAll = 0.3 * avrgDislocation;
        double disForRand = 0.7 * deltaRo;
        double randPack = 0.0001 * disForRand;
        for (int i = 0; i < Y; ++i) {
            for (int j = 0; j < X; ++j) {
                cells.get(i).get(j).increaseDislocationDensity(disForAll);
            }
        }
        Random rand = new Random();
        int randX;
        int randY;
        while (true) {
            if (disForRand < randPack) {
                break;
            }
            randX = rand.nextInt(X);
            randY = rand.nextInt(Y);
            if (cells.get(randY).get(randX).getCurrentEnergy() > 0) {
                if (rand.nextInt(100) < 80) {
                    cells.get(randY).get(randX).increaseDislocationDensity(randPack);
                    disForRand -= randPack;
                }
            } else {
                if (rand.nextInt(100) < 20) {
                    cells.get(randY).get(randX).increaseDislocationDensity(randPack);
                    disForRand -= randPack;
                }
            }
        }
    }
    void dislocationNucleation() {
        if(Germ.isDislocationNucleationOccured()) {
            for (int i = 0; i < Y; ++i) {
                for (int j = 0; j < X; ++j) {
                    if(!cells.get(i).get(j).isRecrystalized() &&
                            cells.get(i).get(j).getDislocationDensity() >= criticalDislocationDensity) {
                        cells.get(i).get(j).setRecrystalized(true);
                        cells.get(i).get(j).setType(Germ.getDisId());
                        cells.get(i).get(j).setDislocationDensity(0);
                    }
                }
            }
        } else {
            for (int i = 0; i < Y; ++i) {
                for (int j = 0; j < X; ++j) {
                    if(!cells.get(i).get(j).isRecrystalized() &&
                            cells.get(i).get(j).getDislocationDensity() >= criticalDislocationDensity) {
                        if(!Germ.isDislocationNucleationOccured()) {
                            cells.get(i).get(j).createNewGerm();
                            cells.get(i).get(j).setRecrystalized(true);
                            cells.get(i).get(j).setDislocationDensity(0);
                            Germ.setDislocationNucleationOccured(true);
                            Germ.setDisId(cells.get(i).get(j).getType());
                        } else {
                            cells.get(i).get(j).setRecrystalized(true);
                            cells.get(i).get(j).setType(Germ.getDisId());
                            cells.get(i).get(j).setDislocationDensity(0);
                        }
                    }
                }
            }
        }
    }
}
