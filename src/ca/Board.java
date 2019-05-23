package ca;

import ca.helpers.Tile;

import java.util.List;

public abstract class Board {
    public enum SimulationState {
        FRESH, NOT_FRESH, FINISHED
    }

    boolean periodicBoundaryConditions = true;
    protected int X;
    protected int Y;
    protected SimulationState state;

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }

    public SimulationState getState() {
        return state;
    }

    public void setState(SimulationState state) {
        this.state = state;
    }

    public boolean isPeriodicBoundaryConditions() {
        return periodicBoundaryConditions;
    }

    public void setPeriodicBoundaryConditions(boolean periodicBoundaryConditions) {
        this.periodicBoundaryConditions = periodicBoundaryConditions;
    }

    public abstract void checkNeighbours();

    public abstract void play();

    public abstract void draw(List<Tile> tiles);

    public abstract Cell getCell(int x, int y);

}
