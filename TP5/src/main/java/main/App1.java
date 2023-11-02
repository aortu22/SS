package main;

import java.io.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

public class App1
{

    public static double dT = 0.0;
    public static int id = 2;
    public static double tau = 0.0;
//  TODO: set according time c - time b (in excel for acc) or timetime b - time a (in excel for deacc)
    public static double maxT = 2.4;
//  TODO: set acceleration o deacceleration

//    public static String str = "acceleration";
    public static String str = "deacceleration";

    public static void deleteOutput(){
        DecimalFormat df_time = new DecimalFormat("#.#");
        String t = df_time.format(tau);

        String output = "src/main/python/output_"+id+"_"+t+"_"+str+".txt";
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
//        String jsonFilePathDynamic = "src/main/java/main/dynamic_1.txt";

//        Particle particle = null;
        double dTEscritura = 0.0;
        double rMin = 0.0;
        double rMax = 0.0;
        double B = 0.0;
        Pedestrian testPedestrian = null;
        while (tau + 0.1 < 3.0){
            reloadDynamicOutput();
            tau += 0.1;
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

//                double tau = Double.parseDouble(br.readLine());
                double x = 0;
                double y = 0;
                double d = Double.parseDouble(br.readLine());
                double vMax = Double.parseDouble(br.readLine());
                List<Position> targetList = new ArrayList<>();
            //  TODO: SETEAR TARGET (10,0) SI ES ACELERACION o (d,0) SI ES FRENADO

//                targetList.add(new Position(10,0));
                targetList.add(new Position(d,0));
            // TODO: SETEAR rMin si es aceleracion o rMax si es desaceleracion (segundo lugar)

//                testPedestrian = new Pedestrian(1,rMin,1,targetList,rMin,rMax,tau,dT,B,d);
                testPedestrian = new Pedestrian(1,rMax,1,targetList,rMin,rMax,tau,dT,B,d);
                testPedestrian.setLimitSpeed(vMax);
            //  TODO: SETEAR SPEED EN 0 SI ES ACELERACION o VMAX SI ES FRENADO

//                testPedestrian.setSpeed(0.0);
                testPedestrian.setSpeed(vMax);
                testPedestrian.setAngle(0.0);
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            PedestrianSim sim = new PedestrianSim(testPedestrian,null,dTEscritura,dT);
            deleteOutput();
            sim.updateOutput(id,tau, str);
            double t = 0.00;
            while(t < maxT){
                t += dT;
                sim.advancePedestrian(t);
                sim.updateDynamicAndOutput(t, id, tau, str);
            }
        }


    }

}
