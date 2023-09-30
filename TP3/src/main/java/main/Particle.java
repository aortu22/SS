package main;

import java.util.ArrayList;
import java.util.List;

public class Particle implements Comparable<Particle> {
    private final int id;
    private final List<Particle> neighbours = new ArrayList<>();
    private final Position position = new Position(0,0);

    private final double radio;

    public Particle(int id, double radio){
        this.id = id;
        this.radio = radio;
    }

    public void setPosition(double x,double y){
        position.setPosition(x,y);
    }

    public void addNeighbour(Particle particle){
        if (!neighbours.contains(particle)) {
            neighbours.add(particle);
        }
    }

    public double getRadio() {
        return radio;
    }

    public int getId() {
        return id;
    }

    public double getX(){
        return position.getX();
    }

    public double getY(){
        return position.getY();
    }

    public List<Particle> getListNeighbours(){
        return this.neighbours;
    }

    public String getNeighbours() {
        StringBuilder n = new StringBuilder();
        for (Particle particle : neighbours){
            n.append(particle.id);
            n.append(", ");
        }
        return n.toString();
    }
    public boolean isSomeNeighbour(Particle otherParticle, double Rc, double length){
        return isNeighbour(otherParticle, Rc) || isNeighbour(otherParticle, Rc, 0, length) || isNeighbour(otherParticle, Rc, 0, -length) || isNeighbour(otherParticle, Rc, length, length) || isNeighbour(otherParticle, Rc, length, -length) || isNeighbour(otherParticle, Rc, length, 0);
    }

    public boolean isNeighbour(Particle otherParticle, double Rc){
        double x1 = otherParticle.getX();
        double y1 = otherParticle.getY();
        return Math.sqrt(Math.pow(getX() - x1, 2) + Math.pow(getY() - y1, 2))-radio- otherParticle.radio <= Rc;
    }

    public boolean isNeighbour(Particle otherParticle,double Rc, double correctionX, double correctionY){
        double x1 = otherParticle.getX();
        double y1 = otherParticle.getY();
        return Math.sqrt(Math.pow((getX()-correctionX) - x1, 2) + Math.pow((getY()-correctionY)-y1,2))-radio- otherParticle.radio <= Rc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Particle particle = (Particle) o;
        return id == particle.id;
    }

    @Override
    public String toString() {
        return "Particle{" +
                "id=" + id +
                ", x=" + getX() +
                ", y=" + getY() +
                '}';
    }

    @Override
    public int compareTo(Particle other) {
        // Implementa la lógica de comparación basada en el campo 'value'
        return Integer.compare(this.id, other.id);
    }
}
