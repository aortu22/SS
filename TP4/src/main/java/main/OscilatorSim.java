package main;

import java.io.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class OscilatorSim {
    private final Particle particle;
    private final double K;
    private final double Y;
    private double nextT;
    private double dTEscritura;
    private final String method;

    public OscilatorSim(Particle particle,double K,double Y,String method,double dTEscritura) {
        this.particle = particle;
        this.K = K;
        this.Y = Y;
        this.method = method;
        this.nextT = this.dTEscritura = dTEscritura;
    }

    public void recalculatePosition(double t){
        switch (method){
            case "beeman":
                break;
            case "gear":
                break;
            case "verlet":
                break;
        }
    }

    public double[] FiveGearPredictor(double xa, double vel, double acceleration, double xc, double xd, double xe, double dt, double mass, double k, double gamma) {

        double[] coefficients = {3 / 16.0, 251 / 360.0, 1, 11 / 18.0, 1 / 6.0, 1 / 60.0};


        double x = xa + vel * dt + acceleration * Math.pow(dt, 2) / factorial(2) + xc * Math.pow(dt, 3) / factorial(3) + xd * Math.pow(dt, 4) / factorial(4) + xe * Math.pow(dt, 5) / factoria(5);
        double x1 = vel + acceleration * dt + xc * Math.pow(dt, 2) / factorial(2) + xd * Math.pow(dt, 3) / factorial(3) + x5 * Math.pow(dt, 4) / factorial(4);
        double x2 = acceleration + xc * dt + xd * Math.pow(dt, 2) / factorial(2) + xe * Math.pow(dt, 3) / factorial(3);
        double x3 = xc + xd * dt + xe * Math.pow(dt, 2) / factorial(2);
        double x4 = xd + xe * dt;
        double x5=xe;
        double deltaA = (-k * x - gamma * x1) / mass - x2;
        double deltaR2 = (deltaA * Math.pow(dt, 2)) / factorial(2);
        double xCorrected = x + coefficients[0] * deltaR2;
        double velCorrected = x + coefficients[1] * deltaR2 / dt;
        double accelerationCorrected = x + coefficients[2] * deltaR2 * factorial(2) / Math.pow(dt, 2);
        double x3Corrected = x3 + coefficients[3] * deltaR2 * factorial(3) / Math.pow(dt, 3);
        double x4Corrected = x4 + coefficients[4] * deltaR2 * factorial(4) / Math.pow(dt, 4);
        double x5Corrected = x5 + coefficients[5] * deltaR2 * factorial(5) / Math.pow(dt, 5);

        return new double[]{xCorrected, velCorrected, accelerationCorrected, x3Corrected, x4Corrected, x5Corrected};

    }
    public int factorial(int n){
        int factorial=1;
        while(factorial>1){
            factorial*=n;
            n-=1;
        }
        return factorial;
    }
    public static double beemanVelocity(double nextX, double v, double acceleration, double previousAcceleration, double dt, double m, double gamma, double k) {
        return (v - (dt * k * nextX)/(3*m) + (5.0/6)*acceleration*dt - (1.0/6)*previousAcceleration*dt)/(1 + (dt * gamma) / (3*m));
    }

    public  double beemanPosition(double x, double v, double acceleration, double previousAcceleration, double dt) {
        return x + v*dt + (2.0/3)*acceleration*Math.pow(dt, 2) - (1.0/6)*previousAcceleration*Math.pow(dt, 2)
    }

    public  double verletPosition(double x, double force, double previousX, double dt, double mass, double gamma, double k){
        return (2*x - x*(Math.pow(dt, 2) * k) / mass + previousX * (dt*gamma / (2*mass) - 1))/(1 + gamma*dt/(2*mass));
    }

    public double verletVelocity(double nextPosition, double prevPosition, double dt) {
        return (nextPosition-prevPosition)/(2*dt);
    }
    public  double eulerPosition(double x, double vel, double f, double dt, double m) {
        return x + dt*vel + (Math.pow(dt, 2) / (2*m)) * f;
    }

    public  double eulerVelocity(double v, double f, double dt, double m) {
        return v + (dt / m) * f;
    }

    public void updateOutput(){
        try {
            String output = "src/main/python/output.txt";
            String dynamic = "src/main/java/main/dynamic.txt";
            File fileOutput = new File(output);
            if (!fileOutput.exists()) {
                fileOutput.createNewFile();
            }
            // Parameter true to append to what the file already has
            FileWriter fwOutput = new FileWriter(fileOutput, true);
            BufferedWriter bwOutput = new BufferedWriter(fwOutput);

            BufferedReader reader = new BufferedReader(new FileReader(dynamic));
            String linea;
            while ((linea = reader.readLine()) != null) {
                // Escribir cada línea en el archivo destino
                bwOutput.write(linea);
                bwOutput.write(System.lineSeparator()); // Agregar un salto de línea
            }
            bwOutput.write('\n');

            // Cerrar archivos
            reader.close();
            bwOutput.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void updateDynamicAndOutput(Double t){
        if(t < nextT){
            return;
        }
        nextT += dTEscritura;
        Locale locale = new Locale("en", "US");
        try {
            String dynamic = "src/main/java/main/dynamic.txt";
            File file = new File(dynamic);
            // Si el archivo no existe es creado
            if (!file.exists()) {
                file.createNewFile();
            }
            // Parameter false make us write stepping in the information
            FileWriter fw = new FileWriter(file, false);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write( t.toString() + '\n');

            // Ordering particles with id

            // Writting particles information
            StringBuilder particleInfo = new StringBuilder();
            DecimalFormat df = new DecimalFormat("0.0000", new DecimalFormatSymbols(locale));

            particleInfo.append(df.format(particle.getX())).append(' ').append(df.format(particle.getY()));
            particleInfo.append('\n');
            bw.write(particleInfo.toString());
            bw.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        updateOutput();
    }
}
