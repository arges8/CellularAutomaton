package ca.controllers;

import ca.Board;
import ca.ca1D.Board1D;
import ca.ca2D.board.GameOfLife;
import ca.ca2D.board.Grains;
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
    private ObservableList patterns = FXCollections.observableArrayList(GameOfLife.patterns.CONST,
            GameOfLife.patterns.GLIDER, GameOfLife.patterns.RANDOM, GameOfLife.patterns.OSCILLATOR, GameOfLife.patterns.EMPTY);

    @FXML
    private ObservableList neighborhoods = FXCollections.observableArrayList(Grains.NeighborhoodType.VON_NEUMANN,
            Grains.NeighborhoodType.PENTAGONAL_RAND, Grains.NeighborhoodType.HEXAGONAL_LEFT, Grains.NeighborhoodType.HEXAGONAL_RIGHT,
            Grains.NeighborhoodType.HEXAGONAL_RAND, Grains.NeighborhoodType.MOORE);

    @FXML
    private ObservableList nucleations = FXCollections.observableArrayList(Grains.Nucleations.HOMOGENEUS,
            Grains.Nucleations.RANDOM, Grains.Nucleations.BANNED);

    @FXML
    private ChoiceBox<Integer> ruleBox;

    @FXML
    private ChoiceBox<GameOfLife.patterns> patternBox;

    @FXML
    private ChoiceBox<Grains.NeighborhoodType> neighborhoodBox;

    @FXML
    private ChoiceBox<Grains.Nucleations> nucleationBox;

    @FXML
    private ScrollBar sizeScroll;

    @FXML
    private ScrollBar sizeScrollx;

    @FXML
    private ScrollBar sizeScrolly;

    @FXML
    private ScrollBar sizeGrainsx;

    @FXML
    private ScrollBar sizeGrainsy;

    @FXML
    private Label size1D;

    @FXML
    private Label sizex;

    @FXML
    private Label sizey;

    @FXML
    private Label grainsx;

    @FXML
    private Label grainsy;

    @FXML
    private JFXRadioButton periodicBC;

    @FXML
    private JFXRadioButton absorbingBC;

    @FXML
    private JFXRadioButton periodicBC1D;

    @FXML
    private JFXRadioButton absorbingBC1D;

    @FXML
    private JFXRadioButton periodicGrainsBC;

    @FXML
    private JFXRadioButton absorbingGrainsBC;

    private ToggleGroup group;

    private ToggleGroup GoLgroup;

    private ToggleGroup grainsGroup;

    @FXML
    public void initialize() {
        group = new ToggleGroup();
        GoLgroup = new ToggleGroup();
        grainsGroup = new ToggleGroup();
        ruleBox.setValue(90);
        ruleBox.setItems(rules);
        patternBox.setValue(GameOfLife.patterns.GLIDER);
        patternBox.setItems(patterns);
        neighborhoodBox.setValue(Grains.NeighborhoodType.VON_NEUMANN);
        neighborhoodBox.setItems(neighborhoods);
        nucleationBox.setValue(Grains.Nucleations.HOMOGENEUS);
        nucleationBox.setItems(nucleations);
        setSizeScroll(sizeScroll, 5, 90, size1D);
        setSizeScroll(sizeScrollx, 5, 90, sizex);
        setSizeScroll(sizeScrolly, 5, 70, sizey);
        setSizeScroll(sizeGrainsx, 5, 90, grainsx);
        setSizeScroll(sizeGrainsy, 5, 70, grainsy);
        setToggleGroups(periodicBC1D, absorbingBC1D, group);
        setToggleGroups(periodicBC, absorbingBC, GoLgroup);
        setToggleGroups(periodicGrainsBC, absorbingGrainsBC, grainsGroup);
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
        tmp.loadPattern(patternBox.getValue());
        mainController.setBoard(tmp);
    }

    @FXML
    public void applyButtonGrainsAction() throws IOException {
        FXMLLoader mainGUI = new FXMLLoader(getClass().getResource("../templates/ca1d.fxml"));
        mainGUI.load();
        Controller mainController = mainGUI.getController();
        Grains tmp = new Grains((int) sizeGrainsx.getValue(), (int)sizeGrainsy.getValue());
        tmp.setNeighborhood(neighborhoodBox.getValue());
        tmp.nucleation(nucleationBox.getValue());
        tmp.setPeriodicBoundaryConditions(periodicGrainsBC.isSelected());
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

    public void setSizeScroll(ScrollBar sb, int min, int max, Label label) {
        sb.setMin(min);
        sb.setMax(max);
        sb.setValue(max);
        label.setText(Integer.toString((int) sb.getValue()));
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
