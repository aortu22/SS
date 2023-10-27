package main;

import java.util.List;

public class Pedestrian extends Particle{
    private final List<Position> targetList;
    private Position nextTarget;

    public Position getNextTarget() {
        return nextTarget;
    }

    private double rMin;
    private double rMax;
    private final double rInteraction = 1.0;
    private double tau;
    private double deltaT;
    private double B;

    public double getD() {
        return D;
    }

    private double D;

    public double getrInteraction() {
        return rInteraction;
    }

    public Pedestrian(int id, double radio, double M, List<Position> targetList, double rMin, double rMax, double tau, double deltaT, double B,double D) {
        super(id, radio, M);
        this.targetList = targetList;
        this.nextTarget = targetList.get(0);
        this.rMin = rMin;
        this.rMax = rMax;
        this.tau = tau;
        this.deltaT = deltaT;
        this.B = B;
        this.D = D;
    }

    //Dado a que Rmin es V=0 y Rmax es v = vLimit =>  ((R - Rmin) / (Rmax-Rmin)) * VLim = V
    public double calculateSpeedWithR(){
        return Math.pow(((getRadio() - rMin) / (rMax - rMin)),B) * getLimitSpeed();
    }

    public void udapteIncreaseR(){
        setRadio( getRadio() + rMax/ (tau/deltaT)  );
    }

    public void updateDecreseR(){
        setRadio( getRadio() - rMax/ (tau/deltaT)  );
    }

    public void resetR(){
        setRadio(rMin);
    }

    public double calculateDistance(Particle otherParticle){
        return Math.sqrt(Math.pow(getPosition().getX() - otherParticle.getPosition().getX(),2) + Math.pow(getPosition().getY() - otherParticle.getPosition().getY(),2));
    }

    public void updatePosition(double dt){
        setPosition( getPosition().getX() + getSpeed() * Math.cos(getAngle()) * dt, getPosition().getY() + getSpeed() * Math.sin(getAngle()) * dt );
    }

//    public double calcularTiempoColision(Particle particle,
//            double[] posicionParticula1, double[] velocidadParticula1, double radioParticula1,
//            double[] posicionParticula2, double[] velocidadParticula2, double radioParticula2) {
//
//
//
//        double[] velocidadRelativa = new double[]{getSpeed()*Math.cos(getAngle()) - particle.getSpeed()*Math.cos(getAngle()) velocidadParticula2[0],
//                velocidadParticula1[1] - velocidadParticula2[1]};
//
//        double[] posicionRelativa = new double[]{posicionParticula1[0] - posicionParticula2[0],
//                posicionParticula1[1] - posicionParticula2[1]};
//
//        double distanciaInicial = Math.sqrt(
//                Math.pow(posicionRelativa[0], 2) + Math.pow(posicionRelativa[1], 2)
//        ) - (rInteraction + rInteraction);
//
//        double velocidadRelativaMagnitud = Math.sqrt(
//                Math.pow(velocidadRelativa[0], 2) + Math.pow(velocidadRelativa[1], 2)
//        );
//
//        double tiempoColision = distanciaInicial / velocidadRelativaMagnitud;
//
//        // Verificar si colisionarán en el tiempo calculado
//        double distanciaEnColision = tiempoColision * velocidadRelativaMagnitud;
//        boolean colisionaran = distanciaEnColision <= 2 * rInteraction ;
//
//        if (colisionaran) {
//            return tiempoColision;
//        } else {
//            return -1; // No colisionarán en el tiempo calculado
//        }
//    }


}
