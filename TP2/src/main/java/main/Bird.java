package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Bird extends Particle{
    private double theta = 0;
    private double v = 0;
    private List<Bird> neighbours = new ArrayList<>();

    private double futureAngle = 0;
//    private Position futurePosition = new Position(0,0);

    public Bird(int id, double v) {
        super(id, 0);
        this.v = v;
    }

    public void setTheta(double theta) {
        this.theta = Math.toRadians(theta);
    }

    public double getTheta() {
        return theta;
    }

    public void cleanNeighbours(){
        neighbours.clear();
    }
    @Override
    public void addNeighbour(Particle particle) {
        if(particle instanceof Bird){
            neighbours.add((Bird) particle);
        }else {
            super.addNeighbour(particle);
        }
    }

    public void updateAngles(double noise){
        double sinProm = getThetaNeighboursProm(true);
        double cosProm = getThetaNeighboursProm(false);

        double minValue = -noise/2;
        double maxValue = noise/2;
        double deltaTheta=minValue + (maxValue - minValue) * Math.random();

        // Clean neighbours, the code that calls this must make sure to set them again
        this.neighbours.clear();
        this.futureAngle =  Math.toDegrees(Math.atan2(sinProm, cosProm)) + deltaTheta;
        if(this.futureAngle < 0){
            this.futureAngle += 360;
        }
    }

//    Update the variables with futures vals and clean to start again
    public void setFutureAngle() {
        setTheta(this.futureAngle);
        this.futureAngle = 0;
    }

    private double getThetaNeighboursProm(boolean isSin){
        double prom = 0;
        // Including the given particle
        if(isSin){
            prom+=Math.sin(this.theta);
        }else{
            prom+=Math.cos(this.theta);
        }
        // And its neighbours
        for (Bird n: neighbours) {
            if(isSin){
                prom+=Math.sin(n.theta);
            }else{
                prom+=Math.cos(n.theta);
            }
        }

        return prom/(neighbours.size() + 1);
    }
    
    public void setNextPosition(double gridSize,double deltaTime){
        double x=getX()+v*deltaTime*Math.cos(theta);
        double y=getY()+v*deltaTime*Math.sin(theta);
        setPosition(x>gridSize?x-gridSize:(x<0?x+gridSize:x),y>gridSize?y-gridSize:(y<0?y+gridSize:y));
    }

    public double getV(){
        return v;
    }
}
