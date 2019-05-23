package ca.helpers;

import ca.Cell;
import ca.controllers.Controller;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile extends StackPane {
    public static final int PIXEL_SIZE = 10;
    private Rectangle border;
    public Tile(Color color) {
        border = new Rectangle(PIXEL_SIZE,PIXEL_SIZE);
        border.setFill(color);
        border.setStroke(Color.BLACK);
        getChildren().addAll(border);

        setOnMouseClicked(event -> {
            Cell cell = Controller.board.getCell((int)(getTranslateX()/PIXEL_SIZE), (int)(getTranslateY()/PIXEL_SIZE));
            if(cell != null) {
                border.setFill(new Color(28.0 / 255.0, 153.0 / 255.0, 231.0 / 255.0, 1));
                cell.setActive(true);
            }
        });
    }
    public void setColor(Color color) {
        border.setFill(color);
    }
}
