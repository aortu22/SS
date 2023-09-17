package main;

import java.util.*;
import java.io.*;

public class App
{

    public static final double maxT = 10;
    public static final double writingPeriod = 0.01;
    public static int N = 0;

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
        try (FileReader reader = new FileReader("src/main/java/main/dynamicOutput.txt");
             FileWriter writer = new FileWriter("src/main/java/main/dynamic.txt")) {

            int character;
            while ((character = reader.read()) != -1) {
                writer.write(character);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Wall> initializeWalls(double L,double L_fixed){
        List<Wall> wallList = new ArrayList<>();
        wallList.add(new Wall(0.0,0.0,L_fixed,0.0));
        wallList.add(new Wall(0.0,0.0,0.0,L_fixed));
        wallList.add(new Wall(0.0,L_fixed,L_fixed,L_fixed));
        wallList.add(new Wall(L_fixed,L_fixed,L_fixed,L +(L_fixed - L )/2));
        wallList.add(new Wall(L_fixed,0.0,L_fixed,(L_fixed - L )/2));
        wallList.add(new Wall(L_fixed,(L_fixed - L )/2,L_fixed * 2,(L_fixed - L )/2));
        wallList.add(new Wall(L_fixed * 2,(L_fixed - L ) / 2,L_fixed * 2,L + ((L_fixed - L ) / 2 )));
        wallList.add(new Wall(L_fixed,L + ((L_fixed - L ) / 2),L_fixed*2,L +( (L_fixed - L ) / 2)));
        return wallList;
    }

    public static List<Vertix> initializeVertix(double L,double L_fixed){
        List<Vertix> vertixList = new ArrayList<>();
        vertixList.add(new Vertix(-1, L_fixed, (L_fixed-L)/2));
        vertixList.add(new Vertix(-2, L_fixed, L_fixed-(L_fixed-L)/2));
        return vertixList;
    }
    public static void main( String[] args )
    {
        String jsonFilePathStatic = "src/main/java/main/static.txt";
        String jsonFilePathDynamic = "src/main/java/main/dynamic.txt";
        double v = 0.03; //desp lo pedimos por input o algo
        reloadDynamicOutput();
        List<Double> orderStatList = new ArrayList<>();
        List<Bird> birdList = new ArrayList<>();
        double L = 0.0;
        double L_fixed = 0.0;
        double M = 0.0;
        int N = 0;
        double R = 0.0;
        double V = 0.0;
        double m = 1.0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(jsonFilePathStatic));

            // Leer las primeras 3 líneas y guardarlas en variables especiales
            L = Double.parseDouble(br.readLine());
            L_fixed = Double.parseDouble(br.readLine());
            M = Double.parseDouble(br.readLine());
            N = Integer.parseInt(br.readLine());
            R = Double.parseDouble(br.readLine());
            V = Double.parseDouble(br.readLine());
            App.N = N;
            // Leer y retornar solo el primer valor de cada par de valores
            int i = 0;
            while (br.readLine() != null) {
                Bird bird = new Bird(i, v, m, R);
                birdList.add(bird);
                i++;
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(jsonFilePathDynamic));
            String linea;

            // Saltar la primera línea
            br.readLine();
            int i = 0;
            while ((linea = br.readLine()) != null) {
                String[] valores = linea.split(" ");
                double x = Double.parseDouble(valores[0]);
                double y = Double.parseDouble(valores[1]);
                double Vx = Double.parseDouble(valores[2]);
                double Vy = Double.parseDouble(valores[3]);
                birdList.get(i).setPosition(x,y);
                birdList.get(i).setDirection(Vx,Vy);
                i++;
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Wall> wallList = initializeWalls(L,L_fixed);
        List<Vertix> vertixList = initializeVertix(L,L_fixed);
        EventDrivenSim sim=new EventDrivenSim(birdList, vertixList, wallList,writingPeriod,L);
        deleteOutput();
//        deleteImpulse();
        sim.updateOutput();
        double t = 0.00;
        sim.addImpulseForL();
        while(t < maxT){
            sim.recalculateCollisions();
            Collision newCollision = sim.calculateNextStep();
            t = newCollision.getCollisionTime();
            sim.moveAllParticles(newCollision);
            sim.updateDynamicAndOutput(t,N);
            sim.recalculateDirections(newCollision);
        }

    }

}
