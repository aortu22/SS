package main;

import java.util.ArrayList;
import java.util.List;

public class Particle implements Comparable<Particle> {
    private final int id;
    private final List<Particle> neighbours = new ArrayList<>();
    private Position position = new Position(0,0);
    private double xPlainPosition;
    private double speed;
    private double angle;
    private double acceleration = -1;
    private double limitSpeed;
    private final double M;

    private double radio;

    public Particle(int id, double radio,double M){
        this.id = id;
        this.radio = radio;
        this.M = M;
    }

    public double getRadio() {
        return radio;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void setRadio(double radio) {
        this.radio = radio;
    }

    public boolean collidesWith(Particle p) {
        return Math.sqrt(Math.pow(position.getX() - p.getX(),2) + Math.pow(position.getY() - p.getY(),2)) < 2 * getRadio();
    }

    public double getAngleToOtherParticle(Particle otherParticle){
        return Math.toDegrees(Math.atan2(otherParticle.getY() - getPosition().getY(), otherParticle.getX() - getPosition().getX()));
    }





    public void setLimitSpeed(double speed){
        this.limitSpeed = speed;
    }

    public double getxPlainPosition() {
        return xPlainPosition;
    }

    public double getLimitSpeed(){
        return limitSpeed;
    }

    public double getM() {
        return M;
    }

    public void setAcceleration(double acceleration){
        this.acceleration=acceleration;
    }

    public double getAcceleration(double acceleration){
        return acceleration;
    }

    public void setPosition(double x, double y){
        position.setPosition(x,y);
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed){
        this.speed = speed;
    }

    public double getX(){
        return position.getX();
    }

    public double getY(){
        return position.getY();
    }

    public int getId() {
        return id;
    }

    public Position getPosition() {
        return position;
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
