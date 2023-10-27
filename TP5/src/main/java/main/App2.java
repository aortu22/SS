package main;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class App2 {
    public static final double maxT = 180.0;
    public static double dT = 0.0;

    public static void deleteOutput() {
        String output = "src/main/python/output_2.txt";
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
        String jsonFilePathUnaffiliated = "src/main/java/main/unaffiliated.txt";

        reloadDynamicOutput();
        double dTEscritura = 0.0;
        double rMin = 0.0;
        double rMax = 0.0;
        double B = 0.0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(jsonFilePathStatic));

            /*
            dT
            dTEscritura
            Rmin
            Rmax
            B
             */
            // Leer las primeras 3 líneas y guardarlas en variables especiales
            dT = Double.parseDouble(br.readLine());
            dTEscritura = Double.parseDouble(br.readLine());
            rMin = Double.parseDouble(br.readLine());
            rMax = Double.parseDouble(br.readLine());
            B = Double.parseDouble(br.readLine());
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Pedestrian testPedestrian = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(jsonFilePathDynamic));
            String linea;

            double tau = Double.parseDouble(br.readLine());
            double x = 0;
            double y = 0;
            double d = Double.parseDouble(br.readLine());
            List<Position> targetList = new ArrayList<>();
            targetList.add(new Position(0,0));
            testPedestrian = new Pedestrian(1,rMin,1,targetList,rMin,rMax,tau,dT,B,d);
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Particle> unaffiliatedList = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(jsonFilePathUnaffiliated));
            String linea;

            br.readLine();
            int i = 0;
            while((linea = br.readLine()) != null ){
                Particle particle = new Particle(i,rMax,1);
                //NO SE EL ORDEN PERO ACA HAY QUE LEER EL ARCHIVO Y OBTENER LA POSICION Y LA DIRECCION DE CADA UNO
                i++;
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        PedestrianSim sim = new PedestrianSim(testPedestrian,null,dTEscritura,dT);
        deleteOutput();
        sim.updateOutput();
        double t = 0.00;
        while(t < maxT){
            t += dT;
            sim.advancePedestrian(t);
            sim.updateDynamicAndOutput(t);
        }

    }

}
