package ca.controllers;

import ca.Board;
import ca.helpers.Tile;
import com.jfoenix.controls.JFXRadioButton;
import javafx.animation.AnimationTimer;
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

    public static Board board;

    int checkX;

    int checkY;

    @FXML
    private TextField timeSteps;

    @FXML
    private Pane pane;

    @FXML
    private JFXRadioButton energyOn;

    @FXML
    private JFXRadioButton energyOff;

    private ToggleGroup energyGroup;

    public static boolean energy;

    private AnimationTimer timer;

    @FXML
    public void initialize() {
        timeSteps.setText("1");
        energyGroup = new ToggleGroup();
        loadSettingsController();
        changeBoardSize(90, 70);
        SettingsController.setToggleGroups(energyOff, energyOn, energyGroup);
        checkX = board.getX();
        checkY = board.getY();
        timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                parallelDraw();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (board.getState() == Board.SimulationState.FINISHED) {
                    board.setState(Board.SimulationState.FRESH);
                    this.stop();
                }
            }
        };
    }

    private void loadSettingsController() {
        try {
            FXMLLoader settings = new FXMLLoader(getClass().getResource("../templates/settings.fxml"));
            Parent root = settings.load();

            SettingsController settingsController = settings.getController();
            board = settingsController.initialSettings();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleButtonAction(ActionEvent event) {
        energy = energyOn.isSelected();
        setProperBoardSize();
        for (int i = 0; i < Integer.parseInt(timeSteps.getText()); ++i) {
            board.draw(tiles);
            board.play();
        }
    }

    @FXML
    void startSimulation() {
        energy = energyOn.isSelected();
        setProperBoardSize();
        timer.start();
    }

    @FXML
    void stopSimulation() {
        timer.stop();
    }

    void setProperBoardSize() {
        if (board.getX() != checkX || board.getY() != checkY) {
            changeBoardSize(board.getX(), board.getY());
            checkX = board.getX();
            checkY = board.getY();
        } else if (board.getState() == Board.SimulationState.FRESH) {
            changeBoardSize(board.getX(), board.getY());
            board.setState(Board.SimulationState.NOT_FRESH);
        }
    }

    @FXML
    void submitChanges() {

        try {
            FXMLLoader settings = new FXMLLoader(getClass().getResource("../templates/settings.fxml"));
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
        int capacity = x * y;
        tiles = new LinkedList<>();
        for (int i = 0; i < capacity; ++i) {
            tiles.add(new Tile(Color.WHITE));
        }
        for (int i = 0; i < capacity; ++i) {
            Tile tile = tiles.get(i);
            tile.setTranslateX(Tile.PIXEL_SIZE * (i % x));
            tile.setTranslateY(Tile.PIXEL_SIZE * (i / x));

        }
        pane.getChildren().clear();
        pane.getChildren().addAll(tiles);
    }

    private void parallelDraw() {
        board.draw(tiles);
        board.play();
    }
}
