package main;

import java.util.List;

public class Pedestrian extends Particle{
    private final List<Position> targetList;
    private Position nextTarget;
    private double rMin;
    private double rMax;
    private double tau;
    private double deltaT;
    private double B;

    public Pedestrian(int id, double radio, double M, List<Position> targetList,double rMin, double rMax, double tau, double deltaT, double B) {
        super(id, radio, M);
        this.targetList = targetList;
        this.nextTarget = targetList.get(0);
        this.rMin = rMin;
        this.rMax = rMax;
        this.tau = tau;
        this.deltaT = deltaT;
        this.B = B;
    }

    //Dado a que Rmin es V=0 y Rmax es v = vLimit =>  ((R - Rmin) / (Rmax-Rmin)) * VLim = V
    public double calculateSpeedWithR(){
        return Math.pow(((getRadio() - rMin) / (rMax - rMin)),B) * getLimitSpeed();
    }

    public void udapteNewR(){
        setRadio( getRadio() + rMax/ (tau/deltaT)  );
    }

    public void resetR(){
        setRadio(rMin);
    }



}
