package main;

import java.io.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class App2 {
    public static final double maxT = 180.0;
    public static double dT = 0.0;
    public static int id = 0;
    public static double tau = 0.0;
    public static String str = "acceleration";
    public static double d = 2.0;
    public static double vMax = 1.53;


    public static void deleteOutput() {
        String output = "src/main/python/ej_2c/simulation_output.txt";
        String output2 = "src/main/python/ej_2c/output_pedestrian.txt";
        File fileOutput = new File(output);
        if (fileOutput.exists()) {
            fileOutput.delete();
        }

        File fileOutput2 = new File(output2);
        if (fileOutput2.exists()) {
            fileOutput2.delete();
        }
    }

    public static void initiateOutput(double t,List<Particle> unaffiliatedPedestrian,Pedestrian respondingPedestrian) {
        Locale locale = new Locale("en", "US");
        try {
            String dynamic = "src/main/python/ej_2c/simulation_output.txt";
            File file = new File(dynamic);
            // Si el archivo no existe es creado
            if (!file.exists()) {
                file.createNewFile();
            }
            // Parameter false make us write stepping in the information
            FileWriter fw = new FileWriter(file, false);
            BufferedWriter bw = new BufferedWriter(fw);


            bw.write("frame\tx\ty\tid\ttime\tvelocity\n");

            DecimalFormat df = new DecimalFormat("0.0000", new DecimalFormatSymbols(locale));


            for(Particle particle : unaffiliatedPedestrian){
                StringBuilder particleInfo = new StringBuilder();
                particleInfo.append(1 + "\t");
                particleInfo.append(df.format(particle.getX()) + "\t");
                particleInfo.append(df.format(particle.getY()) + "\t");
                particleInfo.append(particle.getId() + "\t");
                particleInfo.append(t + "\t");
                particleInfo.append(particle.getSpeed() + "\n");


                bw.write(particleInfo.toString());
            }
            StringBuilder particleInfo = new StringBuilder();
            particleInfo.append(1 + "\t");
            particleInfo.append(df.format(respondingPedestrian.getX()) + "\t");
            particleInfo.append(df.format(respondingPedestrian.getY()) + "\t");
            particleInfo.append("25\t");
            particleInfo.append(t + "\t");
            particleInfo.append(respondingPedestrian.getSpeed() + "\n");
            bw.write(particleInfo.toString());

            bw.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void reloadDynamicOutput(){
        try (
//                FileReader reader = new FileReader("src/main/java/main/dynamicOutput_2.txt");
             FileWriter writer = new FileWriter("src/main/java/main/dynamic_2.txt")) {

//            int character;
//            while ((character = reader.read()) != -1) {
                writer.write("0.0 0.0");
//            }

        } catch (IOException e) {
            e.printStackTrace();
        }
     }


    public static void main( String[] args ) throws IOException {
        String jsonFilePathStatic = "src/main/java/main/static_2.txt";
        String jsonFilePathDynamic = "src/main/java/main/dynamicOutput_2.txt";
        String jsonFilePathUnaffiliated = "src/main/python/ej_2b/output_with_angles_order.txt";
        deleteOutput();

        reloadDynamicOutput();
        double dTEscritura = 0.0;
        double rMin = 0.0;
        double rMax = 0.0;
        double B = 0.0;
        double tauSalida = 0.0;
        double tauLlegada = 0.0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(jsonFilePathStatic));

            /*
            dT
            dTEscritura
            Rmin
            Rmax
            B
            tauSalida
            tauLlegada
             */
            // Leer las primeras 3 líneas y guardarlas en variables especiales
            dT = Double.parseDouble(br.readLine());
            dTEscritura = Double.parseDouble(br.readLine());
            rMin = Double.parseDouble(br.readLine());
            rMax = Double.parseDouble(br.readLine());
            B = Double.parseDouble(br.readLine());
            tauSalida = Double.parseDouble(br.readLine());
            tauLlegada = Double.parseDouble(br.readLine());
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Pedestrian testPedestrian = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(jsonFilePathDynamic));
            String linea;

            List<Position> targetList = new ArrayList<>();
            targetList.add(new Position(-9.75,-6.5));
            targetList.add(new Position(-3.25,6.6));
            targetList.add(new Position(3.25,6.6));
            targetList.add(new Position(9.75,-6.5));
            testPedestrian = new Pedestrian(1,rMin,1.0,targetList,rMin,rMax,tauSalida,tauLlegada,dT,B,d);
            testPedestrian.setPosition(9.75,6.6);
            testPedestrian.setLimitSpeed(vMax);
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Particle> unaffiliatedList = new ArrayList<>();
        BufferedReader unaffilatedBr = null;
        try {
            unaffilatedBr = new BufferedReader(new FileReader(jsonFilePathUnaffiliated));
            String linea;

            unaffilatedBr.readLine();
            String line;
            int i = 0;

            //PRIMERO LEO CADA PARTICULA, LA INICIALIZO Y CALCULO SU POSICION
            while (i < 25 && (line = unaffilatedBr.readLine()) != null ) {
                String[] columns = line.split("\t");
                System.out.println(columns);

                double x = Double.parseDouble(columns[1]);
                double y = Double.parseDouble(columns[2]);
                int id = Integer.parseInt(columns[3]);
                double speed = Double.parseDouble(columns[5]);
                double angle = Double.parseDouble(columns[6]);
                Particle particle = new Particle(id,rMax,1);
                particle.setPosition(x,y);
                particle.setAngle(angle);
                particle.setSpeed(speed);
                unaffiliatedList.add(particle);
                i++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        PedestrianSim sim = new PedestrianSim(testPedestrian,unaffiliatedList,dTEscritura,dT,unaffilatedBr);
        initiateOutput(0.00,unaffiliatedList,testPedestrian);
        sim.updateOutputPedestrian();

        double t = 0.00;
        int counter = 1;
        while(t < maxT){
            t += dT;
            if(!sim.advancePedestrian(t)){
                sim.updateDynamicAndOutput(t, id, tau,str);
                break;  //Se quedo sin targets
            }
            sim.updateDynamicAndOutput(t, id, tau,str);
            if( counter % 2 == 0){
                sim.updateUnaffilatedPedestrians(t);
            }
            counter++;
        }
        unaffilatedBr.close();

    }

}
