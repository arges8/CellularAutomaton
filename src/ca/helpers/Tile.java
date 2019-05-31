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
    public void setColor(int number, boolean energy) {
        switch (number) {
            case 0: {
                border.setFill(new Color(0.32, 0.76, 0.95, 1));
                break;
            }
            case 1: {
                border.setFill(new Color(0.11, 0.50, 0.86, 1));
                break;
            }
            case 2: {
                border.setFill(new Color(0.10, 0.35, 0.99, 1));
                break;
            }
            case 3: {
                border.setFill(new Color(0.18, 0.21, 0.31, 1));
                break;
            }
            case 4: {
                border.setFill(new Color(0.07, 0.25, 0.13, 1));
                break;
            }
            case 5: {
                border.setFill(new Color(0.16, 0.54, 0.16, 1));
                break;
            }
            case 6: {
                border.setFill(new Color(0.21, 0.76, 0.40, 1));
                break;
            }
            case 7: {
                border.setFill(new Color(0.97, 0.11, 0.11, 1));
                break;
            }
            case 8: {
                border.setFill(new Color(0.32, 0.04, 0.04, 1));
                break;
            }
        }
    }
    public static void setGOL(boolean b) {
        GOL = b;
    }
}
