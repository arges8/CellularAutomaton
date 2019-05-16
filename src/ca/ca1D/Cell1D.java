package ca.ca1D;

import ca.Cell;

public class Cell1D  extends Cell {
    boolean leftActive;
    boolean rightActive;
    public Cell1D(boolean b) {
        super(b);
    }

    public Cell1D() {
    }

    public boolean isLeftActive() {
        return leftActive;
    }

    public void setLeftActive(boolean leftActive) {
        this.leftActive = leftActive;
    }

    public boolean isRightActive() {
        return rightActive;
    }

    public void setRightActive(boolean rightActive) {
        this.rightActive = rightActive;
    }

}
