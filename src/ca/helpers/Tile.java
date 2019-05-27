package ca.helpers;

import ca.Cell;
import ca.ca2D.cell.Germ;
import ca.controllers.Controller;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class Tile extends StackPane {
    public static final int PIXEL_SIZE = 10;
    static boolean GOL;
    private Rectangle border;
    public Tile(Color color) {
        GOL = true;
        border = new Rectangle(PIXEL_SIZE,PIXEL_SIZE);
        border.setFill(color);
        border.setStroke(Color.BLACK);
        getChildren().addAll(border);

        setOnMouseClicked(event -> {
            Cell cell = Controller.board.getCell((int)(getTranslateX()/PIXEL_SIZE), (int)(getTranslateY()/PIXEL_SIZE));
            if(cell != null && GOL) {
                border.setFill(new Color(28.0 / 255.0, 153.0 / 255.0, 231.0 / 255.0, 1));
                cell.setActive(true);
            } else if(cell != null) {
                int type = cell.createNewGerm();
                border.setFill(Germ.getColorsMap().get(type));
            }
        });
    }
    public void setColor(Color color) {
        border.setFill(color);
    }
    public void setColor(int type) {
        GOL = false;
        border.setFill(Germ.getColorsMap().get(type));
    }
}
