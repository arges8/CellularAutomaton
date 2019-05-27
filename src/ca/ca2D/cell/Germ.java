package ca.ca2D.cell;

import ca.Cell;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Random;

public class Germ extends Cell {
    int type;
    private static HashMap<Integer, Color> colorsMap = new HashMap<>();
    static int counter = 0;
    int dominantNeighborhood;
    int currentEnergy;
    boolean firstEnergyCalculated;
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

    public void setEnergy(int tmpEnergy) {
        if(!firstEnergyCalculated) {
            currentEnergy = tmpEnergy;
            firstEnergyCalculated = true;
            return;
        }
        int deltaE = tmpEnergy - currentEnergy;
        if(deltaE <= 0) {
            currentEnergy = tmpEnergy;
            setType();
        }
        else {
            Random rand = new Random();
            double p = Math.exp(-(double)deltaE/kt);
            if(rand.nextFloat() < p) {
                currentEnergy = tmpEnergy;
                setType();
            }
        }
    }
}
