package ca;

import ca.ca2D.tile.Tile;

import java.util.List;

public abstract class Board {
    public enum SimulationState {
        FRESH, NOT_FRESH, FINISHED
    }

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

    public abstract void checkNeighbours();

    public abstract void play();

    public abstract void draw(List<Tile> tiles);

}
