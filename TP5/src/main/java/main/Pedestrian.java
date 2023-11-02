package main;

import java.util.List;

public class Pedestrian extends Particle{
    private final List<Position> targetList;
    private Position currentTarget;
    private int currentTargetIndex;
    private double D;
    private double rMin;
    private double rMax;
    private double tauLlegada;
    private double tauPartida;
    private double deltaT;
    private double B;
    public Position getCurrentTarget() {
        return currentTarget;
    }

    public Position setNextTarget(){
        currentTargetIndex++;
        if(targetList.size() < currentTargetIndex + 1){
            return null;
        }
        currentTarget = targetList.get(currentTargetIndex);
        return currentTarget;
    }


    public double getrMin() {
        return rMin;
    }


    public double getD() {
        return D;
    }


    public Pedestrian(int id, double radio, double M, List<Position> targetList, double rMin, double rMax, double tauPartida, double tauLlegada, double deltaT, double B,double D) {
        super(id, radio, M);
        this.targetList = targetList;
        this.currentTarget = targetList.get(0);
        this.currentTargetIndex = 0;
        this.rMin = rMin;
        this.rMax = rMax;
        this.tauPartida = tauPartida;
        this.tauLlegada = tauLlegada;
        this.deltaT = deltaT;
        this.B = B;
        this.D = D;
    }

    //Dado a que Rmin es V=0 y Rmax es v = vLimit =>  ((R - Rmin) / (Rmax-Rmin)) * VLim = V
    public double calculateSpeedWithR(){
        return Math.pow(((getRadio() - rMin) / (rMax - rMin)),B) * getLimitSpeed();
    }

    public void udapteIncreaseR(){
        if( getRadio() < rMax - (rMax/ (tauPartida/deltaT))){
            setRadio( getRadio() + rMax/ (tauPartida/deltaT)  );
        }
    }

    public void updateDecreseR(){
        if(getRadio() > rMin + (rMax/ (tauLlegada/deltaT))){
            setRadio( getRadio() - rMax/ (tauLlegada/deltaT)  );
        }
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

    public double calcularTiempoColision(Particle particle) {
        double angleToParticle = (getAngleToOtherParticle(particle) + 360) % 360;
        //PRIMERO CHEQUEO QUE EL ANGULO LA PARTICULA Q LO CHOQUE NO VENGA DE A ATRAS
        if ((getAngle() - angleToParticle + 360) % 360 > 90 && (angleToParticle - getAngle() + 360) % 360 > 90) {
            return -1;
        }

        double[] velocidadRelativa = new double[]{getSpeed()*Math.cos(getAngle()) - particle.getSpeed()*Math.cos(getAngle()) ,
                getSpeed()*Math.sin(getAngle()) - particle.getSpeed()*Math.sin(getAngle())};

        double[] posicionRelativa = new double[]{getPosition().getX() - particle.getPosition().getX(),
                getPosition().getY() - particle.getPosition().getY()};

        double distanciaInicial = Math.sqrt(
                Math.pow(posicionRelativa[0], 2) + Math.pow(posicionRelativa[1], 2)
        ) - (getRadio() * 5 * 2);

        double velocidadRelativaMagnitud = Math.sqrt(
                Math.pow(velocidadRelativa[0], 2) + Math.pow(velocidadRelativa[1], 2)
        );

        double tiempoColision = distanciaInicial / velocidadRelativaMagnitud;

        // Verificar si colisionarán en el tiempo calculado
        double distanciaEnColision = tiempoColision * velocidadRelativaMagnitud;
        boolean colisionaran = distanciaEnColision <= 2 * getRadio() * 5 ;

        if (colisionaran) {
            return tiempoColision;
        } else {
            return -1; // No colisionarán en el tiempo calculado
        }
    }

    public void setAngleToTarget(){
        setAngle(Math.toDegrees(Math.atan2(currentTarget.getY() - getPosition().getY(), currentTarget.getX() - getPosition().getX())));
    }

}
