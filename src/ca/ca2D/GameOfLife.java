package ca.ca2D;

import ca.Board;
import ca.ca2D.cell.CellGoL;
import ca.ca2D.tile.Tile;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;

public class GameOfLife extends Board {
    List<List<CellGoL>> cells;

    public GameOfLife(int x, int y) {
        X = x;
        Y = y;
        cells = new ArrayList<>(y);
        for(int i=0; i<y; ++i)
        {
            cells.add(new ArrayList<CellGoL>(x));
            int iterator = 0;
            for(int j=0; j<x; ++j)
            {
                if(iterator % 5 == 0)
                    cells.get(i).add(new CellGoL(true));
                else
                    cells.get(i).add(new CellGoL());
                iterator++;
            }
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
                    if (i + 1 < Y)
                    {
                        counter += cells.get(i + 1).get(X - 1).isActive() ? 1 : 0;
                    }
                }
                if (i - 1 >= 0 && cells.get(i - 1).get(j).isActive())
                    counter++;
                if (i - 1 >= 0 && j + 1 < X)
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

    @Override
    public void draw(List<Tile> tiles) {
        int iterator = 0;
        for(int i=0; i<Y; ++i)
        {
            for(int j=0; j<X; ++j)
            {
                if(cells.get(i).get(j).isActive())
                    tiles.get(iterator).setColor(new Color(28.0 / 255.0, 153.0 / 255.0, 231.0 / 255.0, 1));
                else
                    tiles.get(iterator).setColor(Color.WHITE);
                iterator++;
            }
        }
    }

}

