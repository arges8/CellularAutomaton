package ca.ca2D.cell;

import ca.Cell;
import ca.controllers.Controller;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Germ extends Cell {
    int type;
    private static HashMap<Integer, Color> colorsMap = new HashMap<>();
    static int counter = 0;
    int dominantNeighborhood;
    List<Integer> neighbours;
    int currentEnergy;
    int tmpEnergy;
    boolean firstEnergyCalculated = true;
    static double kt = 3;
    public Germ() {

    }

    public Germ(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType() {
        this.type = getDominantNeighborhood();
    }

    public void setType(int type) {
        this.type = type;
    }

    public static double getKt() {
        return kt;
    }

    public static void setKt(double kt) {
        Germ.kt = kt;
    }

    public int createNewGerm() {
        Random rand = new Random();
        counter++;
        type = counter;
        active = true;
        Color color;
        while(true) {
            color = new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 1);
            if(!colorsMap.containsValue(color))
                break;
        }
        colorsMap.put(type, color);
        return type;
    }

    public int getDominantNeighborhood() {
        return dominantNeighborhood;
    }

    public void setDominantNeighborhood(int dominantNeighborhood) {
        this.dominantNeighborhood = dominantNeighborhood;
    }

    public static int getCounter() {
        return counter;
    }

    public static HashMap<Integer, Color> getColorsMap() {
        return colorsMap;
    }

    public List<Integer> getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(List<Integer> neighbours) {
        this.neighbours = new ArrayList<>(neighbours);
    }

    public void setEnergy(int tmpEnergy) {
        if(firstEnergyCalculated) {
            this.currentEnergy = tmpEnergy;
            firstEnergyCalculated = false;
        }
    }
    public void checkDeltaEnergy(int t) {
        int deltaE = tmpEnergy - currentEnergy;
        if(deltaE <= 0) {
            currentEnergy = tmpEnergy;
            setType(t);
        }
        else {
            Random rand = new Random();
            double p = Math.exp(-(double)deltaE/kt);
            if(rand.nextFloat() < p) {
                currentEnergy = tmpEnergy;
                setType(type);
            }
        }
    }
    public void hypotheticalEnergy() {
        int noOfNeighbours = neighbours.size();
        Random rand = new Random();
        int hyptheticalType = neighbours.get(rand.nextInt(noOfNeighbours));
        int E = 0;
        for(int i : neighbours) {
            if(hyptheticalType != i)
                E++;
        }
        tmpEnergy = E;
        checkDeltaEnergy(hyptheticalType);
    }
}
