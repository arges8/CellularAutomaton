package ca.ca2D.cell;

import ca.Cell;

public class CellGoL extends Cell {
    int neighbours;

    public CellGoL() {
    }

    public CellGoL(boolean active) {
        super(active);
    }

    @Override
    public int createNewGerm() {
        return 0;
    }

    public int getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(int neighbours) {
        this.neighbours = neighbours;
    }
}
