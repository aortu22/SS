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
        this.theta = theta;
    }

    public double getTheta() {
        return theta;
    }

    @Override
    public void addNeighbour(Particle particle) {
        if(particle instanceof Bird){
            neighbours.add((Bird) particle);
        }else {
            super.addNeighbour(particle);
        }
    }

    public void updateAngles(double error){
        double sinProm = getThetaNeighboursProm(true);
        double cosProm = getThetaNeighboursProm(false);

        Random random = new Random();
        double minValue = -error/2;
        double maxValue = error/2;
        double deltaTheta = minValue + (maxValue - minValue + 1) * random.nextDouble();

        // Clean neighbours, the code that calls this must make sure to set them again
        this.neighbours.clear();
        this.futureAngle =  Math.atan2(sinProm, cosProm) + deltaTheta;
        if(this.futureAngle < 0){
            this.futureAngle += 360;
        }
    }

//    Update the variables with futures vals and clean to start again
    public void setFutureAngle() {
//        super.setPosition(this.futurePosition.getX(), this.futurePosition.getY());
//        this.futurePosition =  new Position(0,0);
        this.theta = this.futureAngle;
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
        double x=getX()+v*deltaTime*+Math.cos(Math.toRadians(theta));
        double y=getY()+v*deltaTime*+Math.sin(Math.toRadians(theta));
        setPosition(x>gridSize?x-gridSize:(x<0?x+gridSize:x),y>gridSize?y-gridSize:(y<0?y+gridSize:y));
    }

    public double getV(){
        return v;
    }
}
