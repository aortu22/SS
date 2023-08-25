package main;

import java.util.HashSet;
import java.util.Set;

public class Cell {
    private final int row;
    private final int col;
    private final double l;
    private final int m;
    private final Set<Particle> particleList = new HashSet<>();

    public Cell(int row, int col, int m, double l){
        this.row = row;
        this.col = col;
        this.l=l;
        this.m=m;
    }
     public void addParticle(Particle particle){
        particleList.add(particle);
     }
     public int getRow(){
        return row;
     }
    public int getCol(){
        return col;
    }

    public void cleanCell(){
        particleList.clear();
    }
     public Set<Particle> getParticles(){
        return particleList;
     }
     public int getId(){return row*m+col;}

    @Override
    public String toString(){
        return String.valueOf(row*m+col);
    }

}
