package ca;

import ca.ca2D.tile.Tile;
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

    private static Board board;

    int checkX;

    int checkY;

    @FXML
    private TextField timeSteps;

    @FXML
    private Pane pane;

    private AnimationTimer timer;

    @FXML
    public void initialize() {
        timeSteps.setText("1");
        loadSettingsController();
        changeBoardSize(140, 100);
        checkX = board.getX();
        checkY = board.getY();
        timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                parallelDraw();
                if (board.getState() == Board.SimulationState.FINISHED) {
                    board.setState(Board.SimulationState.FRESH);
                    this.stop();
                }
            }
        };
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
        setProperBoardSize();
        for (int i = 0; i < Integer.parseInt(timeSteps.getText()); ++i) {
            board.draw(tiles);
            board.play();
        }
    }

    @FXML
    void startSimulation() {
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
        int capacity = x * y;
        tiles = new LinkedList<>();
        for (int i = 0; i < capacity; ++i) {
            tiles.add(new Tile(Color.WHITE));
        }
        for (int i = 0; i < capacity; ++i) {
            Tile tile = tiles.get(i);
            tile.setTranslateX(6 * (i % x));
            tile.setTranslateY(6 * (i / x));

        }
        pane.getChildren().clear();
        pane.getChildren().addAll(tiles);
    }

    private void parallelDraw() {
        board.draw(tiles);
        board.play();
    }
}
