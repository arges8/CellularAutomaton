package ca.ca2D.tile;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile extends StackPane {
    private Rectangle border;
    public Tile(Color color) {
        border = new Rectangle(6,6);
        border.setFill(color);
        border.setStroke(Color.BLACK);
        getChildren().addAll(border);
    }
    public void setColor(Color color) {
        border.setFill(color);
    }
}
