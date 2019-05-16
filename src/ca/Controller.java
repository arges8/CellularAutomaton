package ca;

import ca.ca1D.Board;
import ca.ca1D.Cell1D;
import ca.ca2D.tile.Tile;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import java.util.List;

public class Controller {
    private Board board;

    private WritableImage img;
    @FXML
    private ObservableList rules = FXCollections.observableArrayList(30, 60, 90, 110, 255);

    @FXML
    private JFXButton nextButton;

    @FXML
    private ChoiceBox<Integer> ruleBox;

    @FXML
    private ImageView imageView;

    @FXML
    private ScrollBar sizeScroll;

    @FXML
    private Label value;

    @FXML
    private TextField timeSteps;

    private ToggleGroup group;

    @FXML
    private JFXRadioButton cellular1D;

    @FXML
    private JFXRadioButton gol;

    @FXML
    private Pane pane;

    @FXML
    public void initialize() {
        group = new ToggleGroup();
        cellular1D.setToggleGroup(group);
        cellular1D.setSelected(true);
        cellular1D.setSelectedColor(new Color(28.0 / 255.0, 153.0 / 255.0, 231.0 / 255.0, 1));
        gol.setToggleGroup(group);
        gol.setSelectedColor(new Color(28.0 / 255.0, 153.0 / 255.0, 231.0 / 255.0, 1));
        ruleBox.setValue(90);
        ruleBox.setItems(rules);
        sizeScroll.setMin(5);
        sizeScroll.setMax(230);
        sizeScroll.setValue(230);
        timeSteps.setText("1");
        value.setText(Integer.toString((int) sizeScroll.getValue()));
        board = new Board((int) sizeScroll.getValue());
        board.setRule(ruleBox.getValue());
        img = new WritableImage((int) imageView.getFitWidth(), (int) imageView.getFitHeight());
    }

    @FXML
    void handleButtonAction(ActionEvent event) {
        for (int i = 0; i < Integer.parseInt(timeSteps.getText()); ++i) {
            drawImage(0, board.getCounter(), board.getActualRow());
            imageView.setImage(img);
            board.play();
        }
    }

    @FXML
    void setValueLabel() {
        value.setText(Integer.toString((int) sizeScroll.getValue()));
    }

    @FXML
    void submitChanges() {
        board = new Board((int) sizeScroll.getValue());
        board.setRule(ruleBox.getValue());
        img = new WritableImage((int) imageView.getFitWidth(), (int) imageView.getFitHeight());
    }

    void drawImage(int x, int y, List<Cell1D> list) {
        PixelWriter pw = img.getPixelWriter();
        int nPixels = 4;
        for (int i = 0, j = 0; i < list.size() * nPixels; i += nPixels, j++) {
            if (list.get(j).isActive()) {
                for (int k = 0; k < nPixels; ++k) {
                    pw.setColor(x + i + k, y * nPixels, new Color(28.0 / 255.0, 153.0 / 255.0, 231.0 / 255.0, 1));
                    for (int m = 0; m < 10; ++m) {
                        pw.setColor(x + i + k, y * nPixels + m, new Color(28.0 / 255.0, 153.0 / 255.0, 231.0 / 255.0, 1));
                    }
                }
            } else {
                for (int k = 0; k < nPixels; ++k) {
                    pw.setColor(x + i + k, y * nPixels, new Color(1, 1, 1, 1));
                    for (int m = 0; m < 10; ++m) {
                        pw.setColor(x + i + k, y * nPixels + m, new Color(1, 1, 1, 1));
                    }
                }
            }
        }
    }
    void drawGoLPane(List<Tile> tiles) {
        for(int i=0; i<)
        pane.
    }
}
