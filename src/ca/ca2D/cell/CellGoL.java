package ca.ca2D.cell;

import ca.Cell;

public class CellGoL extends Cell {
    int neighbours;

    public CellGoL() {
    }

    public CellGoL(boolean active) {
        super(active);
    }

    public int getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(int neighbours) {
        this.neighbours = neighbours;
    }
}
