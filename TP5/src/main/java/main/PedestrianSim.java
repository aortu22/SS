package main;

import java.io.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public class PedestrianSim {

    private final Pedestrian respondingPedestrian;
    private final List<Pedestrian> unaffiliatedPedestrian;
    private double nextT;
    private double dTEscritura;
    private double dT;


    public PedestrianSim(Pedestrian respondingPedestrian, List<Pedestrian> unaffiliatedPedestrian, double dTEscritura, double dT){
        this.respondingPedestrian = respondingPedestrian;
        this.unaffiliatedPedestrian = unaffiliatedPedestrian;
        this.nextT = dTEscritura;
        this.dTEscritura = dTEscritura;
        this.dT = dT;
    }

    public void advancePedestrian(double t){
        // EJERCICIO C
        if(unaffiliatedPedestrian != null){
            updateUnaffilatedPedestrians(t);
            Particle closestImpactParticle = null;
            double smallestCollitionTime = 0;
            for(Particle particle : unaffiliatedPedestrian){
                //Esta en rango de choque
                if(respondingPedestrian.calculateDistance(particle) < 2 * respondingPedestrian.getrInteraction()){
                    double collitionTime = respondingPedestrian.calcularTiempoColision(particle);
                    if (collitionTime > smallestCollitionTime){
                        closestImpactParticle = particle;
                    }
                }
            }
            if(closestImpactParticle != null){
                colitionHeuristic(closestImpactParticle);
            }else{
                respondingPedestrian.setAngleToTarget();
            }
        }else{  //EJERCICIO B
            if(respondingPedestrian.getNextTarget().getX() - respondingPedestrian.getPosition().getX() < respondingPedestrian.getD()){
                respondingPedestrian.updateDecreseR();
            }else{
                respondingPedestrian.udapteIncreaseR();
            }
            respondingPedestrian.setSpeed(respondingPedestrian.calculateSpeedWithR());
            respondingPedestrian.updatePosition(dT);
        }

    }

    public void updateUnaffilatedPedestrians(double t){
        String unaffilatedFile = "src/main/java/.....txt";
        //actualizar las posiciones y direcciones de los unaffilated
    }

    public void colitionHeuristic(Particle particle){
        
    }

    public void updateOutput(double tau, String str){
        try {
            String output = "src/main/python/output_1_"+tau+"_"+str+".txt";
            String dynamic = "src/main/java/main/dynamic_1.txt";
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

    public void updateDynamicAndOutput(Double t, double tau, String str){
        if(t < nextT){
            return;
        }
        nextT += dTEscritura;
        Locale locale = new Locale("en", "US");
        try {
            String dynamic = "src/main/java/main/dynamic_1.txt";
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

            // Imprimo la informacion, este va a ser la posicion y el error respecto a la solucion analitca
            StringBuilder particleInfo = new StringBuilder();
            DecimalFormat df = new DecimalFormat("0.0000", new DecimalFormatSymbols(locale));
            particleInfo.append(df.format(respondingPedestrian.getSpeed()));
            particleInfo.append('\n');
            bw.write(particleInfo.toString());
            bw.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        updateOutput(tau, str);
    }
}
