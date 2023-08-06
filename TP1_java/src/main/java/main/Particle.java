package main;


import java.util.ArrayList;
import java.util.List;

public class Particle {
    private final int id;
    private final List<Particle> neighbours = new ArrayList<>();
    private final double x;
    private final double y;
    private final double radio;

    public Particle(int id,double x,double y, double radio ){
        this.id = id;
        this.x = x;
        this.y = y;
        this.radio = radio;
    }

    public void addNeighbour(Particle particle){
        neighbours.add(particle);
    }

    public List<Particle> getNeighbours(){
        return neighbours;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public boolean isNeighbour(Particle otherParticle,double Rc){
        double x1 = otherParticle.getX();
        double y1 = otherParticle.getY();
        return Math.sqrt(Math.pow(x - x1, 2) + Math.pow(y - y1, 2)) <= Rc;
    }

    public boolean isNeighbour(Particle otherParticle,double Rc, double correctionX, double correctionY){
        double x1 = otherParticle.getX();
        double y1 = otherParticle.getY();
        return Math.sqrt(Math.pow(x - (x1 - correctionX), 2) + Math.pow(y - (y1 - correctionY, 2)) <= Rc;
    }
}
