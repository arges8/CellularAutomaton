package ca.ca1D;

import ca.Cell;

import java.util.ArrayList;
import java.util.List;

public class Board {
    List<List<Cell1D>> cells;
    int X;
    int Y;
    int counter = 0;
    String binaryRule;

    public Board(int size) {
        this.Y = size;
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
        for (int i = 0; i < Y; ++i) {
            if (i - 1 >= 0 && row.get(i - 1).isActive())
                row.get(i).setLeftActive(true);
            else if (i - 1 < 0 && row.get(Y - 1).isActive())
                row.get(i).setLeftActive(true);
            else
                row.get(i).setLeftActive(false);
            if (i + 1 < Y && row.get(i + 1).isActive())
                row.get(i).setRightActive(true);
            else if (i + 1 >= Y && row.get(0).isActive())
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
}
