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
    private double previousAcceleration;
    private final double K_CONST = 2500;
    private double[] gearPredictor;
    double[] gearCoefficients = {3 / 16.0, 251 / 360.0, 1, 11 / 18.0, 1 / 6.0, 1 / 60.0};

    private double previousX = -1;
    private final double M;
    private double Y;
    private double K;
    private double error;

    private final double radio;

    public Particle(int id, double radio,double M){
        this.id = id;
        this.radio = radio;
        this.M = M;
        this.error = 0;
    }

    public boolean collidesWith(Particle p, Double dt) {
        double deltaRx = this.getX() - p.getX();
        double deltaVx = this.speed - p.speed;

        double sigma = this.radio + p.radio;

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

    private double getForce(Particle p1) {
        return K_CONST * (Math.abs(p1.getX()-this.getX()) - (2*p1.radio)) * (Math.signum(p1.getX()-this.getX()));
    }

    public double getTotalForces(List<Particle> particleList, double dt) {
            double colisionForceSum = 0.0;
            for(Particle otherParticle: particleList) {
                if(!this.equals(otherParticle) && this.collidesWith(otherParticle, dt)) {
                    colisionForceSum += getForce(otherParticle);
                }
            }
            return ((limitSpeed - speed) + colisionForceSum);

//        double totalForce = 0;
//        for (Particle particle : particleList) {
//            if (!particle.equals(this)) {
//                if (Math.abs(particle.getX() - getX()) <= 2 * radio) { //Hay colision
//                    totalForce += (K_CONST * (Math.abs(particle.getX() - getX()) - 2 * radio) * (Math.signum(particle.getX() - getX())));
//                } else if (L-Math.abs(particle.getX() - getX()) <= 2 *radio) {
//                    totalForce += (K_CONST * (L-Math.abs(particle.getX() - getX()) - 2 * radio) * (Math.signum(L - particle.getX() - getX())));
//                }
//            }
//        }
//        if (L - getX() < 2 * radio) { //En este caso esta cerca del borde derecho de la linea
//            for (Particle particle : particleList) {
//                if (!particle.equals(this)) {
//                    if (Math.abs(particle.getX() - getX()) <= 2 * radio) { //Hay colision
//                        totalForce += (K_CONST * (Math.abs(particle.getX() - getX()) - 2 * radio) * ( Math.signum(particle.getX() - getX())));
//                    }
//                    else if (Math.abs(particle.getX() + L - getX()) < 2 * radio) {
//                        totalForce += (K_CONST * (Math.abs((particle.getX() + L) - getX()) - 2 * radio) * ( Math.signum((particle.getX() + L) - getX())));
//                    }
//                }
//            }
//        } else if (getX() < 2 * radio) { //En este caso esta cerca del borde izquierdo
//            for (Particle particle : particleList) {
//                if (!particle.equals(this)) {
//                    if (Math.abs(particle.getX() - getX()) <= 2 * radio) { //Hay colision
//                        totalForce += (K_CONST * (Math.abs(particle.getX() - getX()) - 2 * radio) * ( Math.signum(particle.getX() - getX()) ));
//                    }
//                    else if ( Math.abs(particle.getX() - (getX() + L)) < 2 * radio) {
//                        totalForce += (K_CONST * (Math.abs(particle.getX() - (getX() + L)) - 2 * radio) * ( Math.signum(particle.getX() - (getX() + L)) ));
//                    }
//                }
//            }
//        } else {
//            for (Particle particle : particleList) {
//                if (!particle.equals(this)) {
//                    if (Math.abs(particle.getX() - getX()) <= 2 * radio) { //Hay colision
//                        totalForce += (K_CONST * (Math.abs(particle.getX() - getX()) - 2 * radio) * (Math.signum(particle.getX() - getX())));
//                    }
//                }
//            }
//        }
//        totalForce += ( limitSpeed - speed );
//        return totalForce;
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

    public void setY(double Y){
        this.Y = Y;
    }

    public void setK(double K){
        this.K = K;
    }

    public double getM() {
        return M;
    }

    public void setAcceleration(double acceleration){
        this.previousAcceleration = this.acceleration == -1 ? acceleration : this.acceleration;
        this.acceleration=acceleration;
    }

    public void setGearPredictor(){
        double x = position.getX() ;
        double xa = speed;
        double xb = acceleration;
        double xc = (-K*speed - Y*acceleration)/M;
        double xd = (-K*acceleration - Y*xc)/M;
        double xe = (-K*xc - Y*xd)/M;

        this.gearPredictor = new double[]{x,xa,xb,xc,xd,xe};
    }

    public void setGearPredictorTp2(){
        double x = position.getX() ;
        double xa = speed;
        double xb = 0;
        double xc = 0;
        double xd = 0;
        double xe = 0;

        this.gearPredictor = new double[]{x,xa,xb,xc,xd,xe,x};
    }

    public double getAcceleration(double acceleration){
        return acceleration;
    }

    public void setPosition(double x, double y){
        this.previousX = this.previousX == -1 ? 0 : this.position.getX();
        position.setPosition(x,y);
    }

    public void setError(double error){
        this.error = error;
    }

    public double getError(){
        return error;
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

    public double analyticSolutionComparison(double time) {
        return 1*Math.exp(-time*(Y/(2*M)))*Math.cos(Math.pow(K/M - Math.pow(Y, 2)/(4*Math.pow(M,2)),0.5)*time);
    }


    public void beemanVelocity(double dt) {
        setSpeed((speed - (K*position.getX()*dt)/(3*M) + (5.0/6)*acceleration*dt - (1.0/6)*previousAcceleration*dt)/(1+(Y*dt)/(3*M)));
    }

    public void setBeemanPosition(double dt){
        setPosition(beemanPosition(dt), position.getY());
    }

    public double beemanPosition(double dt) {
        return position.getX() + speed*dt + (2.0/3)*acceleration*Math.pow(dt, 2) -(1.0/6)*previousAcceleration*Math.pow(dt, 2);
    }
    public void setVerletPosition(double dt){
        setPosition(verletPosition(dt), position.getY());
    }
    public  double verletPosition(double dt){
        double x = position.getX();
        return 2*x - previousX + Math.pow(dt, 2)*acceleration; //diapo 13
    }

    public void verletVelocity(double dt) { //diapo 15
        setSpeed((position.getX()*(2*M-K* Math.pow(dt,2))-2*M*previousX)/(2*dt*M+Y*Math.pow(dt,2)));
    }

    public void setEulerPosition(double dt){
        setPosition(eulerPosition(dt), position.getY());
    }
    public double eulerPosition(double dt) {
        double f = -K*position.getX() - Y*speed;
        return position.getX() + dt*speed + (Math.pow(dt, 2) / (2*M)) * f;
    }

    public void eulerVelocity(double dt) {
        double f = -K*position.getX() - Y*speed;
        double newSpeed = speed + (dt / M) * f;
        setSpeed(newSpeed);
        setAcceleration((-K*position.getX() - Y*newSpeed)/M);
    }

    public void FiveGearPredictor(double dt) {
        double x = gearPredictor[0];
        double xa = gearPredictor[1];
        double xb = gearPredictor[2];
        double xc = gearPredictor[3];
        double xd = gearPredictor[4];
        double xe = gearPredictor[5];

//        Prediction to order 5
        double x0 = x + xa * dt + xb * Math.pow(dt, 2) / factorial(2) + xc * Math.pow(dt, 3) / factorial(3) + xd * Math.pow(dt, 4) / factorial(4) + xe * Math.pow(dt, 5) / factorial(5);
        double x1 = xa + xb * dt + xc * Math.pow(dt, 2) / factorial(2) + xd * Math.pow(dt, 3) / factorial(3) + xe * Math.pow(dt, 4) / factorial(4);
        double x2 = xb + xc * dt + xd * Math.pow(dt, 2) / factorial(2) + xe * Math.pow(dt, 3) / factorial(3);
        double x3 = xc + xd * dt + xe * Math.pow(dt, 2) / factorial(2);
        double x4 = xd + xe * dt;

//        Evaluate
        double deltaA =( (-K * x0 - Y * x1) / M )- x2;
        double deltaR2 = (deltaA * Math.pow(dt, 2)) / factorial(2);

//        Corrected
        double x0Corrected= x0 + gearCoefficients[0] * deltaR2;
        double x1Corrected = x1 + gearCoefficients[1] * deltaR2 / dt;
        double x2Corrected = x2 + gearCoefficients[2] * deltaR2 * factorial(2) / Math.pow(dt, 2);
        double x3Corrected = x3 + gearCoefficients[3] * deltaR2 * factorial(3) / Math.pow(dt, 3);
        double x4Corrected = x4 + gearCoefficients[4] * deltaR2 * factorial(4) / Math.pow(dt, 4);
        double x5Corrected = xe + gearCoefficients[5] * deltaR2 * factorial(5) / Math.pow(dt, 5);

        this.gearPredictor = new double[]{x0Corrected, x1Corrected, x2Corrected, x3Corrected, x4Corrected, x5Corrected};
        setPosition(x0Corrected, position.getY());
        setSpeed(x1Corrected);
        setAcceleration(x2Corrected);
    }

    public void setxPlainPosition(double xPlainPosition) {
        this.xPlainPosition = xPlainPosition;
    }

    public void FiveGearPredictorTp2(double dt, double L, List<Particle> particleList) {
        double x = gearPredictor[0];
        double xa = gearPredictor[1];
        double xb = gearPredictor[2];
        double xc = gearPredictor[3];
        double xd = gearPredictor[4];
        double xe = gearPredictor[5];
        double xP = gearPredictor[6];

//        Prediction to order 5
        double x0 = (x + xa * dt + xb * Math.pow(dt, 2) / factorial(2) + xc * Math.pow(dt, 3) / factorial(3) + xd * Math.pow(dt, 4) / factorial(4) + xe * Math.pow(dt, 5) / factorial(5)) % L;
        x0 += x0 < 0 ? L : 0.0;
        double x1 = xa + xb * dt + xc * Math.pow(dt, 2) / factorial(2) + xd * Math.pow(dt, 3) / factorial(3) + xe * Math.pow(dt, 4) / factorial(4);
        double x2 = xb + xc * dt + xd * Math.pow(dt, 2) / factorial(2) + xe * Math.pow(dt, 3) / factorial(3);
        double x3 = xc + xd * dt + xe * Math.pow(dt, 2) / factorial(2);
        double x4 = xd + xe * dt;
        double xPlain = xP + xa * dt + xb * Math.pow(dt, 2) / factorial(2) + xc * Math.pow(dt, 3) / factorial(3) + xd * Math.pow(dt, 4) / factorial(4) + xe * Math.pow(dt, 5) / factorial(5);

        setPosition(x0, position.getY());
        setSpeed(x1);
        setAcceleration(x2);
        setxPlainPosition(xPlain);
//        Evaluate
//        double deltaA = getTotalForces(particleList,L)/getM() - x2;
        double deltaA = getTotalForces(particleList,dt)/getM() - x2;
        double deltaR2 = (deltaA * Math.pow(dt, 2)) / factorial(2);

//        Corrected
        double x0Corrected= (x0 + gearCoefficients[0] * deltaR2) %L;
        x0Corrected += x0Corrected < 0 ? L : 0.0;
        double x1Corrected = x1 + gearCoefficients[1] * deltaR2 / dt;
        double x2Corrected = x2 + gearCoefficients[2] * deltaR2 * factorial(2) / Math.pow(dt, 2);
        double x3Corrected = x3 + gearCoefficients[3] * deltaR2 * factorial(3) / Math.pow(dt, 3);
        double x4Corrected = x4 + gearCoefficients[4] * deltaR2 * factorial(4) / Math.pow(dt, 4);
        double x5Corrected = xe + gearCoefficients[5] * deltaR2 * factorial(5) / Math.pow(dt, 5);
        double xPlainCorrected= xPlain + gearCoefficients[0] * deltaR2;

        this.gearPredictor = new double[]{x0Corrected, x1Corrected, x2Corrected, x3Corrected, x4Corrected, x5Corrected, xPlainCorrected};
        setPosition(x0Corrected, position.getY());
        setSpeed(x1Corrected);
        setAcceleration(x2Corrected);
        setxPlainPosition(xPlainCorrected);
    }


    private int factorial(int n){
        int factorial=1;
        while(n>1){
            factorial*=n;
            n--;
        }
        return factorial;
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
