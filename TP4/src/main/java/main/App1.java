package main;

import java.io.*;

public class App1
{

    public static final double maxT = 5.0;
    public static final String method = "beeman";


    public static void deleteOutput(){
        String output = "src/main/python/output.txt";
        String output_dcm = "src/main/python/outputDCM.txt";
        String output_xyz = "src/main/python/output.xyz";
        File fileOutput = new File(output);
        File fileOutputXYZ = new File(output_xyz);
        File fileOutputDCM = new File(output_dcm);
        if (fileOutput.exists()) {
            fileOutput.delete();
        }
        if (fileOutputXYZ.exists()) {
            fileOutputXYZ.delete();
        }
        if (fileOutputDCM.exists()) {
            fileOutputDCM.delete();
        }
    }

    public static void deleteImpulse(){
        String output = "src/main/python/impulse.txt";
        File fileOutput = new File(output);
        if (fileOutput.exists()) {
            fileOutput.delete();
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
        String jsonFilePathDynamic = "src/main/java/main/dynamic_1.txt";
        reloadDynamicOutput();
        Particle particle = null;
        int M = 0;
        double k = 0;
        double Y = 0;
        double dT = 0.0;
        double dTEscritura = 0.0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(jsonFilePathStatic));

            // Leer las primeras 3 líneas y guardarlas en variables especiales
            M = Integer.parseInt(br.readLine());
            k = Double.parseDouble(br.readLine());
            Y = Double.parseDouble(br.readLine());
            dT = Double.parseDouble(br.readLine());
            dTEscritura = Double.parseDouble(br.readLine());
            // Leer y retornar solo el primer valor de cada par de valores
            int i = 0;
            particle = new Particle(1,0,M);
            particle.setPosition(1,0);          //La particula arranca en (1,0) siempre
            particle.setSpeed();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        OscilatorSim sim = new OscilatorSim(particle,k,Y,method,dTEscritura);
        deleteOutput();
        sim.updateOutput();
        double t = 0.00;
        double nextT = t + dTEscritura;
        while(t < maxT){
            t += dT;
            sim.recalculatePosition(t);
            sim.updateDynamicAndOutput(t);
        }

    }

}
