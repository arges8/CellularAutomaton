package ca.ca2D;

import ca.Cell;
import ca.ca2D.cell.CellGoL;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GameOfLife {
    int X;
    int Y;
    List<List<CellGoL>> cells;

    public GameOfLife(int x, int y) {
        X = x;
        Y = y;
        cells = new ArrayList<>(y);
        for(int i=0; i<y; ++i)
        {
            cells.add(new ArrayList<CellGoL>(x));
        }
    }

    public void checkNeighbours() {
        int counter;
        for(int i=0; i<Y; ++i)
        {
            for(int j=0; j<X; ++j)
            {
                counter = 0;
                if (i-1 >=0 && j-1>= 0)
                    counter+= cells.get(i - 1).get(j - 1).isActive() ? 1 : 0;
                else if (j == 0)
                {
                    counter += cells.get(i).get(X - 1).isActive() ? 1 : 0;
                    if (i - 1 >= 0)
                    {
                        counter += cells.get(i - 1).get(X - 1).isActive() ? 1 : 0;
                    }
                    if (i + 1 < X)
                    {
                        counter += cells.get(i + 1).get(X - 1).isActive() ? 1 : 0;
                    }
                }
                if (i - 1 >= 0 && cells.get(i - 1).get(j).isActive())
                    counter++;
                if (i - 1 >= 0 && j + 1 < Y)
                    counter += cells.get(i - 1).get(j + 1).isActive() ? 1 : 0;
                else if (j == X - 1)
                {
                    counter += cells.get(i).get(0).isActive() ? 1 : 0;
                    if (i - 1 >= 0)
                    {
                        counter += cells.get(i - 1).get(0).isActive() ? 1 : 0;
                    }
                    if (i + 1 < Y)
                    {
                        counter += cells.get(i + 1).get(0).isActive() ? 1 : 0;
                    }
                }
                if (j + 1 < X)
                    counter += cells.get(i).get(j + 1).isActive() ? 1 : 0;
                if (i + 1 < Y && j + 1 < X)
                    counter += cells.get(i + 1).get(j + 1).isActive() ? 1 : 0;
                if (i + 1 < Y)
                    counter += cells.get(i + 1).get(j).isActive() ? 1 : 0;
                if (i + 1 < Y && j - 1 >= 0)
                    counter += cells.get(i + 1).get(j - 1).isActive() ? 1: 0;
                if (j - 1 >= 0)
                    counter += cells.get(i).get(j - 1).isActive() ? 1 : 0;

                cells.get(i).get(j).setNeighbours(counter);
            }
        }
    }
    public void play() {
        checkNeighbours();
        for(int i=0; i < Y; ++i)
        {
            for(int j=0; j<X; ++j)
            {
                CellGoL tmpCell = cells.get(i).get(j);
                if(tmpCell.isActive() && (tmpCell.getNeighbours() < 2 || tmpCell.getNeighbours() > 3))
                    tmpCell.setActive(false);
                else if(!tmpCell.isActive() && tmpCell.getNeighbours() == 3)
                    tmpCell.setActive(true);
            }
        }
    }

}
