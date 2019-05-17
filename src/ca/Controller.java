package ca;

import ca.ca2D.tile.Tile;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Controller {
    private List<Tile> tiles;

    private static Board board;

    int checkX;

    int checkY;

    @FXML
    private ObservableList rules = FXCollections.observableArrayList(30, 60, 90, 110, 255);

    @FXML
    private JFXButton nextButton;

    @FXML
    private ChoiceBox<Integer> ruleBox;

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
        ruleBox.setValue(90);
        ruleBox.setItems(rules);
        sizeScroll.setMin(5);
        sizeScroll.setMax(230);
        sizeScroll.setValue(230);
        timeSteps.setText("1");
        value.setText(Integer.toString((int) sizeScroll.getValue()));
        loadSettingsController();
        changeBoardSize(140, 100);
        checkX = board.getX();
        checkY = board.getY();
    }

    private void loadSettingsController() {
        try {
            FXMLLoader settings = new FXMLLoader(getClass().getResource("settings.fxml"));
            Parent root = settings.load();

            SettingsController settingsController = settings.getController();
            board = settingsController.initialSettings();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void handleButtonAction(ActionEvent event) {
        if(board.getX() != checkX || board.getY() != checkY)
        {
            changeBoardSize(board.getX(), board.getY());
            checkX = board.getX();
            checkY = board.getY();
        }
        for (int i = 0; i < Integer.parseInt(timeSteps.getText()); ++i) {
            board.draw(tiles);
            board.play();
        }
    }

    @FXML
    void setValueLabel() {
        value.setText(Integer.toString((int) sizeScroll.getValue()));
    }

    @FXML
    void submitChanges() {

        try {
            FXMLLoader settings = new FXMLLoader(getClass().getResource("settings.fxml"));
            Parent root = settings.load();
            Stage window = new Stage();
            window.setScene(new Scene(root));
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @FXML
    public void setBoard(Board newBoard) {
        board = newBoard;
    }

    @FXML
    void changeBoardSize(int x, int y) {
        int capacity = x*y;
        tiles = new LinkedList<>();
        for(int i=0; i<capacity; ++i)
        {
            tiles.add(new Tile(Color.WHITE));
        }
        for(int i=0; i<capacity; ++i)
        {
            Tile tile = tiles.get(i);
            tile.setTranslateX(6 *(i % x));
            tile.setTranslateY(6 *(i / x));

        }
        pane.getChildren().clear();
        pane.getChildren().addAll(tiles);
}
}
