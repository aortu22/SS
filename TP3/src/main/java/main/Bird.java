package main;

import java.util.ArrayList;
import java.util.List;

public class Bird extends Particle{
    private double Vx = 0;
    private double Vy = 0;
    private double v = 0;
    private double m = 0;
    private List<Collision> collisionList;


// birds with radius and weight
    public Bird(int id, double v, double m,double radius) {
        super(id, radius);
        this.v = v;
        this.m = m;
        this.collisionList=new ArrayList<>();
    }

    public void setDirection(double theta){
        this.Vx = v * Math.cos(Math.toRadians(theta));
        this.Vy = v * Math.sin(Math.toRadians(theta));
    }


    public void setNextPosition(double deltaTime){
        double x=getX()+deltaTime*Vx;
        double y=getY()+deltaTime*Vy;
        setPosition(x,y);
    }

    public double getV() {
        return v;
    }

    public double getVx(){
        return Vx;
    }

    public double getVy(){return Vy;
    }

    public void setVx(double Vx){
        this.Vx = Vx;
    }

    public void setVy(double Vy){
        this.Vy = Vy;
    }

    public double getM() {
        return m;
    }

    public double birdColisionTime(Bird colisionBird){
        double deltaVx= colisionBird.getVx() - getVx();
        double deltaVy= colisionBird.getVy() - getVy();

        double deltaRx= colisionBird.getX() - getX();
        double deltaRy= colisionBird.getY() - getY();

        double deltaRdeltaR = Math.pow(deltaRx,2) + Math.pow(deltaRy,2);
        double deltaVdeltaV = Math.pow(deltaVx,2) + Math.pow(deltaVy,2);
        double deltaVdeltaR = (deltaVx*deltaRx)+ (deltaVy*deltaRy);

        double phi = getRadio() + colisionBird.getRadio();

        double d = Math.pow(deltaVdeltaR, 2.0) - deltaVdeltaV*(deltaRdeltaR - Math.pow(phi, 2.0));
        if(deltaVdeltaR >= 0 ||  d < 0){
            return Double.MAX_VALUE-100000;
        }
        return -(deltaVdeltaR + Math.sqrt(d))/deltaVdeltaV;
    }

    public List<Collision> getCollisionList() {
        return collisionList;
    }

    public void setCollisionList(List<Collision> collisionList) {
        this.collisionList = collisionList;
    }
}
