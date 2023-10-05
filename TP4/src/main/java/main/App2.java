package main;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class App2 {
    public static final double maxT = 180.0;

    public static void deleteOutput(String dt, String N) {
        String output = "src/main/python/output_2_" + N +"_"+dt+".txt";
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
        try (FileReader reader = new FileReader("src/main/java/main/dynamicOutput_2.txt");
             FileWriter writer = new FileWriter("src/main/java/main/dynamic_2.txt")) {

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
        String jsonFilePathStatic = "src/main/java/main/static_2.txt";
        String jsonFilePathDynamic = "src/main/java/main/dynamic_2.txt";
        reloadDynamicOutput();
        Particle particle = null;
        double R = 0.0;
        double M = 0.0;
        double L = 0;
        int N = 0;
        double dT = 0.0;
        double dTEscritura = 0.0;
        List<Particle> particleList = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(jsonFilePathStatic));

            // Leer las primeras 3 líneas y guardarlas en variables especiales
            R = Double.parseDouble(br.readLine());
            M = Double.parseDouble(br.readLine());
            L = Double.parseDouble(br.readLine());
            N = Integer.parseInt(br.readLine());
            dT = Double.parseDouble(br.readLine());
            dTEscritura = Double.parseDouble(br.readLine());
            // Leer y retornar solo el primer valor de cada par de valores
            int i = 0;
            while( i < N){
                Particle newParticle = new Particle(i,R,M);
                particleList.add(newParticle);
                i++;
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(jsonFilePathDynamic));
            String linea;

            br.readLine();
            int i = 0;
            while((linea = br.readLine()) != null ){
                String[] valores = linea.split(" ");
                double x = Double.parseDouble(valores[0]);
                particleList.get(i).setPosition(x,0);
                particleList.get(i).setSpeed(Double.parseDouble(valores[1]));
                particleList.get(i).setAcceleration(0);
                particleList.get(i).setGearPredictorTp2();
                i++;
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ParticleLineSim sim = new ParticleLineSim(particleList,dTEscritura,dT,L, N);
        deleteOutput(String.valueOf(dT), String.valueOf(N));
        sim.updateOutput();
        double t = 0.00;
        while(t + dT < maxT){
            t += dT;
            sim.moveParticlesAndRecalculateForces(t,dT,L);
            sim.updateDynamicAndOutput(t);
        }

    }

}
