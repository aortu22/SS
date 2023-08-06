package main;

import java.util.List;

public class Grid {
    private final int rows;
    private final int columns;
    private final Cell[][] grid;
    private final int cellLength;
    private final boolean spherical;

    public Grid(int size, int length, boolean spherical){
        this.rows = size;
        this.columns = size;
        this.cellLength = length/size;
        this.spherical = spherical;
        grid = new Cell[size][size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                int id = col + row * size;
                grid[row][col] = new Cell(row, col, id, size, length);
            }
        }
    }

    public Cell getCell(int row, int column){
        return grid[row][column];
    }

    public void addToCell(Particle particle){
        grid[(int) Math.floor(particle.getY() / cellLength)][(int) Math.floor(particle.getX() / cellLength)].addParticle(particle);
    }

    public List<Particle> getParticles(int row,int col){
        return grid[row][col].getParticles();
    }
}

