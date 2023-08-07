package main;

import java.util.ArrayList;
import java.util.List;

public class Cell {
    private final int id;
    private final int row;
    private final int col;
    private Particle heads;
    private final List<Particle> particleList = new ArrayList<>();
    private final boolean isRightSide;
    private final boolean isBottomSide;

    public Cell(int row, int col, int id, int m, int l){
        this.id = id;
        this.row = row;
        this.col = col;
        this.isRightSide = col == (l / m - 1);
        this.isBottomSide = row == (l / m - 1);
    }
     public void addParticle(Particle particle){
        if( particleList.size() == 0){
            heads = particle;
        }
        particleList.add(particle);
     }

     public List<Particle> getParticles(){
        return particleList;
     }

     public Particle getHeads(){
        return heads;
     }

     public boolean isCellRightSide(){
        return isRightSide;
     }

     public boolean isCellBottomSide(){
        return isBottomSide;
     }

    @Override
    public String toString(){
        return String.valueOf(id);
    }

}
