package ca;

import ca.ca1D.Board1D;
import ca.ca2D.GameOfLife;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollBar;
import javafx.stage.Stage;

import java.io.IOException;

public class SettingsController {

    private static Board tmpBoard;

    @FXML
    private ObservableList rules = FXCollections.observableArrayList(30, 60, 90, 110, 255);

    @FXML
    private ChoiceBox<Integer> ruleBox;

    @FXML
    private ScrollBar sizeScroll;

    @FXML
    private ScrollBar sizeScrollx;

    @FXML
    private ScrollBar sizeScrolly;

    @FXML
    private Button applyButton1D;

    @FXML
    private Button applyButton2D;

    @FXML
    public void initialize() {
        ruleBox.setValue(90);
        ruleBox.setItems(rules);
        sizeScroll.setMin(5);
        sizeScroll.setMax(140);
        sizeScroll.setValue(140);
        sizeScrollx.setMin(5);
        sizeScrollx.setMax(140);
        sizeScrollx.setValue(140);
        sizeScrolly.setMin(5);
        sizeScrolly.setMax(110);
        sizeScrolly.setValue(110);
        Board1D tmp = new Board1D((int) sizeScroll.getValue());
        tmp.setRule(ruleBox.getValue());
        tmpBoard = tmp;
    }

    @FXML
    public Board initialSettings() {
        return tmpBoard;
    }

    @FXML
    public void applyButton1DAction() {
        FXMLLoader mainGUI = new FXMLLoader(getClass().getResource("ca1d.fxml"));
        Controller mainController = mainGUI.getController();
        Board1D tmp = new Board1D((int) sizeScroll.getValue());
        tmp.setRule(ruleBox.getValue());
        mainController.setBoard(tmp);
    }

    @FXML
    public void applyButton2DAction(ActionEvent event) throws IOException {
        FXMLLoader mainGUI = new FXMLLoader(getClass().getResource("ca1d.fxml"));
        Parent root = mainGUI.load();
        Controller mainController = mainGUI.getController();
        GameOfLife tmp = new GameOfLife((int) sizeScrollx.getValue(), (int) sizeScrolly.getValue());
        mainController.setBoard(tmp);
    }


    public Board changeSettings(Board board, Parent root) {
        Stage window = new Stage();
        window.setScene(new Scene(root));
        window.show();
        tmpBoard = board;
            applyButton1D.setOnAction(e -> {
                System.out.println("BBBBBB");
                Board1D tmp = new Board1D((int) sizeScroll.getValue());
                tmp.setRule(ruleBox.getValue());
                tmpBoard = tmp;
                window.close();
            });
            applyButton2D.setOnAction(e -> {
                System.out.println("CCCCC");
                GameOfLife tmp = new GameOfLife((int) sizeScrollx.getValue(), (int) sizeScrolly.getValue());
                tmpBoard = tmp;
                window.close();
            });

        return tmpBoard;
    }
}
