package ca.ca2D.tile;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile extends StackPane {
    public Tile(Color color) {
        Rectangle border = new Rectangle(4,4);
        border.setFill(color);
        border.setStroke(Color.BLACK);
        getChildren().addAll(border);
    }
}
