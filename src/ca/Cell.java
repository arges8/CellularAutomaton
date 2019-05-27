package ca;

public abstract class Cell {
    protected boolean active;

    public Cell() {
    }

    public Cell(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public abstract int createNewGerm();
}
