package main;


import java.util.ArrayList;
import java.util.List;

public class Particle {
    private final int id;
    private final List<Particle> neighbours = new ArrayList<>();
    private final double x;
    private final double y;
    private final double radio;

    public Particle(id id, double radio ){
        this.id = id;
        this.radio = radio;
    }

    public void setPosition(double x,double y){
        this.x = x;
        this.y = y;
    }

    public void addNeighbour(Particle particle){
        if (!neighbours.contains(particle)) {
            neighbours.add(particle);
        }
    }

    public int getId() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Particle particle = (Particle) o;
        return id == particle.id;
    }
}
