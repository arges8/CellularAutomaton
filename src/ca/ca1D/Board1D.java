package ca.ca1D;

import ca.Board;

import ca.ca2D.tile.Tile;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Board1D extends Board {
    List<List<Cell1D>> cells;
    int counter = 0;
    String binaryRule;

    public Board1D(int size) {
        this.X = size;
        this.Y = 110;
        setState(SimulationState.FRESH);
        cells = new ArrayList<>();
        List<Cell1D> tmp = new ArrayList<>(size);
        for (int i = 0; i < size; ++i) {
            if (i == size / 2)
                tmp.add(new Cell1D(true));
            else
                tmp.add(new Cell1D());
        }
        cells.add(tmp);
    }

    public void setRule(int rule) {
        String binary = Integer.toString(rule, 2);
        String zeros = "00000000";
        int diff = 8 - binary.length();
        String fullBinary = zeros.substring(0, diff) + binary;
        this.binaryRule = fullBinary;
    }

    public void checkNeighbours() {
        List<Cell1D> row = cells.get(counter);
        for (int i = 0; i < X; ++i) {
            if (i - 1 >= 0 && row.get(i - 1).isActive())
                row.get(i).setLeftActive(true);
            else if (i - 1 < 0 && row.get(X - 1).isActive())
                row.get(i).setLeftActive(true);
            else
                row.get(i).setLeftActive(false);
            if (i + 1 < X && row.get(i + 1).isActive())
                row.get(i).setRightActive(true);
            else if (i + 1 >= X && row.get(0).isActive())
                row.get(i).setRightActive(true);
            else
                row.get(i).setRightActive(false);
        }
    }

    public void play() {
        checkNeighbours();
        for (Cell1D cell : cells.get(counter)) {
            if ('1' == (binaryRule.charAt(0))) {
                if (cell.isActive() && cell.isLeftActive() && cell.isRightActive()) {
                    cell.setActive(true);
                    continue;
                }
            } else {
                if (cell.isActive() && cell.isLeftActive() && cell.isRightActive()) {
                    cell.setActive(false);
                    continue;
                }
            }
            if ('1' == (binaryRule.charAt(1))) {
                if (cell.isActive() && cell.isLeftActive() && !cell.isRightActive()) {
                    cell.setActive(true);
                    continue;
                }
            } else {
                if (cell.isActive() && cell.isLeftActive() && !cell.isRightActive()) {
                    cell.setActive(false);
                    continue;
                }
            }
            if ('1' == (binaryRule.charAt(2))) {
                if (!cell.isActive() && cell.isLeftActive() && cell.isRightActive()) {
                    cell.setActive(true);
                    continue;
                }
            } else {
                if (!cell.isActive() && cell.isLeftActive() && cell.isRightActive()) {
                    cell.setActive(false);
                    continue;
                }
            }
            if ('1' == (binaryRule.charAt(3))) {
                if (!cell.isActive() && cell.isLeftActive() && !cell.isRightActive()) {
                    cell.setActive(true);
                    continue;
                }
            } else {
                if (!cell.isActive() && cell.isLeftActive() && !cell.isRightActive()) {
                    cell.setActive(false);
                    continue;
                }
            }
            if ('1' == (binaryRule.charAt(4))) {
                if (cell.isActive() && !cell.isLeftActive() && cell.isRightActive()) {
                    cell.setActive(true);
                    continue;
                }
            } else {
                if (cell.isActive() && !cell.isLeftActive() && cell.isRightActive()) {
                    cell.setActive(false);
                    continue;
                }
            }
            if ('1' == (binaryRule.charAt(5))) {
                if (cell.isActive() && !cell.isLeftActive() && !cell.isRightActive()) {
                    cell.setActive(true);
                    continue;
                }
            } else {
                if (cell.isActive() && !cell.isLeftActive() && !cell.isRightActive()) {
                    cell.setActive(false);
                    continue;
                }
            }
            if ('1' == (binaryRule.charAt(6))) {
                if (!cell.isActive() && !cell.isLeftActive() && cell.isRightActive()) {
                    cell.setActive(true);
                    continue;
                }
            } else {
                if (!cell.isActive() && !cell.isLeftActive() && cell.isRightActive()) {
                    cell.setActive(false);
                    continue;
                }
            }
            if ('1' == (binaryRule.charAt(7))) {
                if (!(cell.isActive() && cell.isLeftActive() && cell.isRightActive())) {
                    cell.setActive(true);
                    continue;
                }
            } else {
                if (!(cell.isActive() && cell.isLeftActive() && cell.isRightActive())) {
                    cell.setActive(false);
                    continue;
                }
            }
        }
        List<Cell1D> tmp = new ArrayList<>(cells.get(counter));
        counter++;
        cells.add(tmp);
    }

    public int getCounter() {
        return counter;
    }

    public List<Cell1D> getActualRow() {
        return cells.get(counter);
    }

    public String getBinaryRule() {
        return binaryRule;
    }

    public void draw(List<Tile> tiles) {
        int iter = 0;
        if (counter < Y) {
            for (int i = counter * X; i < (counter + 1) * X; ++i) {
                if (cells.get(counter).get(iter).isActive())
                    tiles.get(i).setColor(new Color(28.0 / 255.0, 153.0 / 255.0, 231.0 / 255.0, 1));
                iter++;
            }
        } else
            setState(SimulationState.FINISHED);
    }
}
