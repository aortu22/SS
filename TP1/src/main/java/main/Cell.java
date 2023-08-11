package main;

import com.sun.xml.internal.ws.wsdl.writer.document.Part;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Cell {
    private final int id;
    private final int row;
    private final int col;
    private final double l;
    private Particle heads;
    private final Set<Particle> particleList = new HashSet<>();
    private final boolean isRightSide;
    private final boolean isBottomSide;

    public Cell(int row, int col, int id, int m, double l){
        this.id = id;
        this.row = row;
        this.col = col;
        this.isRightSide = col == (l / m - 1);
        this.isBottomSide = row == (l / m - 1);
        this.l=l;
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
     public Set<Particle> getParticles(){
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
