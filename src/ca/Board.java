package ca;

import ca.ca2D.tile.Tile;

import java.util.List;

public abstract class Board {
    protected int X;
    protected int Y;

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }

    public abstract void checkNeighbours();
    public abstract void play();
    public abstract void draw(List<Tile> tiles);

}
