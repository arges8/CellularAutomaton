package ca.controllers;

import ca.Board;
import ca.ca1D.Board1D;
import ca.ca2D.GameOfLife;
import com.jfoenix.controls.JFXRadioButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;

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
    private Label size1D;

    @FXML
    private Label sizex;

    @FXML
    private Label sizey;

    @FXML
    private JFXRadioButton periodicBC;

    @FXML
    private JFXRadioButton absorbingBC;

    @FXML
    private JFXRadioButton periodicBC1D;

    @FXML
    private JFXRadioButton absorbingBC1D;

    private ToggleGroup group;

    private ToggleGroup GoLgroup;

    @FXML
    public void initialize() {
        group = new ToggleGroup();
        GoLgroup = new ToggleGroup();
        ruleBox.setValue(90);
        ruleBox.setItems(rules);
        sizeScroll.setMin(5);
        sizeScroll.setMax(140);
        sizeScroll.setValue(140);
        size1D.setText(Integer.toString((int) sizeScroll.getValue()));
        sizeScrollx.setMin(5);
        sizeScrollx.setMax(140);
        sizeScrollx.setValue(140);
        sizex.setText(Integer.toString((int) sizeScrollx.getValue()));
        sizeScrolly.setMin(5);
        sizeScrolly.setMax(110);
        sizeScrolly.setValue(110);
        sizey.setText(Integer.toString((int) sizeScrolly.getValue()));
        setToggleGroups(periodicBC1D, absorbingBC1D, group);
        setToggleGroups(periodicBC, absorbingBC, GoLgroup);
        Board1D tmp = new Board1D((int) sizeScroll.getValue());
        tmp.setRule(ruleBox.getValue());
        tmp.setPeriodicBoundaryConditions(periodicBC1D.isSelected());
        tmpBoard = tmp;
    }

    @FXML
    public Board initialSettings() {
        return tmpBoard;
    }

    @FXML
    public void applyButton1DAction() throws IOException {
        FXMLLoader mainGUI = new FXMLLoader(getClass().getResource("../templates/ca1d.fxml"));
        mainGUI.load();
        Controller mainController = mainGUI.getController();
        Board1D tmp = new Board1D((int) sizeScroll.getValue());
        tmp.setRule(ruleBox.getValue());
        tmp.setPeriodicBoundaryConditions(periodicBC1D.isSelected());
        mainController.setBoard(tmp);
    }

    @FXML
    public void applyButton2DAction() throws IOException {
        FXMLLoader mainGUI = new FXMLLoader(getClass().getResource("../templates/ca1d.fxml"));
        mainGUI.load();
        Controller mainController = mainGUI.getController();
        GameOfLife tmp = new GameOfLife((int) sizeScrollx.getValue(), (int) sizeScrolly.getValue());
        tmp.setPeriodicBoundaryConditions(periodicBC.isSelected());
        mainController.setBoard(tmp);
    }

    @FXML
    public void setSize1DLabel() {
        size1D.setText(Integer.toString((int) sizeScroll.getValue()));
    }

    @FXML
    public void setSizexLabel() {
        sizex.setText(Integer.toString((int) sizeScrollx.getValue()));
    }

    @FXML
    public void setSizeyLabel() {
        sizey.setText(Integer.toString((int) sizeScrolly.getValue()));
    }

    public void setToggleGroups(JFXRadioButton selected, JFXRadioButton notSelected, ToggleGroup g) {
        selected.setToggleGroup(g);
        selected.setSelected(true);
        selected.setSelectedColor(new Color(28.0 / 255.0, 153.0 / 255.0, 231.0 / 255.0, 1));
        notSelected.setToggleGroup(g);
        notSelected.setSelected(false);
        notSelected.setSelectedColor(new Color(28.0 / 255.0, 153.0 / 255.0, 231.0 / 255.0, 1));

    }
}
