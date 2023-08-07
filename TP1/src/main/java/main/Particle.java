package main;


import java.util.ArrayList;
import java.util.List;

public class Particle {
    private final String id;
    private final List<Particle> neighbours = new ArrayList<>();
    private final double x;
    private final double y;
    private final double radio;

    public Particle(String id,double x,double y, double radio ){
        this.id = id;
        this.x = x;
        this.y = y;
        this.radio = radio;
    }

    public void addNeighbour(Particle particle){
        neighbours.add(particle);
    }

    public String getId() {
        return id;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public String getNeighbours() {
        StringBuilder n = new StringBuilder();
        for (Particle particle : neighbours){
            n.append(particle.id);
            n.append(", ");
        }
        return n.toString();
    }

    public boolean isNeighbour(Particle otherParticle, double Rc){
        double x1 = otherParticle.getX();
        double y1 = otherParticle.getY();
        return Math.sqrt(Math.pow(x - x1, 2) + Math.pow(y - y1, 2))-radio- otherParticle.radio <= Rc;
    }

    public boolean isNeighbour(Particle otherParticle,double Rc, double correctionX, double correctionY){
        double x1 = otherParticle.getX();
        double y1 = otherParticle.getY();
        return Math.sqrt(Math.pow((x-correctionX) - x1, 2) + Math.pow((y-correctionY)-y1,2))-radio- otherParticle.radio <= Rc;
    }
}
