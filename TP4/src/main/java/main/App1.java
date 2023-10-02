package main;

import java.io.*;

public class App1
{

//    TODO: correr on maxT = 5 para la ppt
    public static final double maxT = 5.0;
    public static String method = "";
    public static double dT = 0.0;


    public static void deleteOutput(){
        String output = "src/main/python/output_"+method+"_1_"+dT+".txt";
        String output_xyz = "src/main/python/output.xyz";
        File fileOutput = new File(output);
        File fileOutputXYZ = new File(output_xyz);
        if (fileOutput.exists()) {
            fileOutput.delete();
        }
        if (fileOutputXYZ.exists()) {
            fileOutputXYZ.delete();
        }
    }

    public static void reloadDynamicOutput(){
        try (FileReader reader = new FileReader("src/main/java/main/dynamicOutput_1.txt");
             FileWriter writer = new FileWriter("src/main/java/main/dynamic_1.txt")) {

            int character;
            while ((character = reader.read()) != -1) {
                writer.write(character);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main( String[] args )
    {
        String jsonFilePathStatic = "src/main/java/main/static_1.txt";
        reloadDynamicOutput();
        Particle particle = null;
        int M = 0;
        double k = 0;
        double Y = 0;
        double dTEscritura = 0.0;
        double A = 1.0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(jsonFilePathStatic));

            // Leer las primeras 3 líneas y guardarlas en variables especiales
            M = Integer.parseInt(br.readLine());
            k = Double.parseDouble(br.readLine());
            Y = Double.parseDouble(br.readLine());
            dT = Double.parseDouble(br.readLine());
            dTEscritura = Double.parseDouble(br.readLine());
            method = br.readLine();
            // Params of diapo 36
            double x = 1;
            particle = new Particle(1,0,M);
            particle.setK(k);
            particle.setY(Y);
            particle.setPosition(x,0);
            double v = -A * Y / (2*M);
            particle.setSpeed(v);
            switch (method) {
                case "beeman" -> {
                    particle.eulerVelocity(dT);
                    particle.setEulerPosition(dT);
                    particle.setAcceleration((-k*particle.getX() -Y*particle.getSpeed())/M);
                }
                case "gear" -> {
                    particle.setGearPredictor();
                }
                case "verlet" -> {
                    particle.eulerVelocity(dT);
                    particle.setEulerPosition(dT);
                    particle.setAcceleration((-k*particle.getX() -Y*particle.getSpeed())/M);
                }
            }
            particle.setPosition(x,0);
            particle.setSpeed(v);
            particle.setAcceleration((-k*x -Y*v)/M);
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        OscilatorSim sim = new OscilatorSim(particle,k,Y,method,dTEscritura,dT);
        deleteOutput();
        sim.updateOutput();
        double t = 0.00;
        while(t < maxT){
            t += dT;
            sim.recalculatePosition(t, dT);
            sim.updateDynamicAndOutput(t);
        }

    }

}
