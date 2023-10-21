package main;

import java.util.ArrayList;
import java.util.List;

public class Particle implements Comparable<Particle> {
    private final int id;
    private final List<Particle> neighbours = new ArrayList<>();
    private Position position = new Position(0,0);
    private double xPlainPosition;
    private double speed;
    private double acceleration = -1;
    private double limitSpeed;

    private final double M;

    private final double radio;

    public Particle(int id, double radio,double M){
        this.id = id;
        this.radio = radio;
        this.M = M;
    }

    public boolean collidesWith(Particle p, Double dt) {
        double deltaRx = getX() - p.getX();
        double deltaVx = speed - p.speed;

        double sigma = radio + p.radio;

        double dv_dr = (deltaRx * deltaVx);
        if (dv_dr >= 0) {
            return false;
        }

        double dv2 = (deltaVx * deltaVx);
        double dr2 = (deltaRx * deltaRx);
        double d = Math.pow(dv_dr, 2) - dv2 * (dr2 - Math.pow(sigma, 2));
        if (d < 0) {
            return false;
        }

        return (-(dv_dr + Math.sqrt(d)) / dv2 ) < dt;
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
