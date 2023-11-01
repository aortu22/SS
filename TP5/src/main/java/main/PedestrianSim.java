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

    public boolean advancePedestrian(double t){
        // EJERCICIO C
        boolean isCollition = false;
        if(unaffiliatedPedestrian != null){
            if( respondingPedestrian.getPosition().calculateDistance(respondingPedestrian.getCurrentTarget()) < respondingPedestrian.getRadio()){
                if(respondingPedestrian.setNextTarget() == null){
                    return false;
                }
            }

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
                isCollition = respondingPedestrian.collidesWith(closestImpactParticle);
            }else{
                respondingPedestrian.setAngleToTarget();
            }
        }
        if(isCollition){
            respondingPedestrian.setRadio(respondingPedestrian.getrMin());
        }else if(respondingPedestrian.getCurrentTarget().getX() - respondingPedestrian.getPosition().getX() < respondingPedestrian.getD()){
            respondingPedestrian.updateDecreseR();
        }else{
            respondingPedestrian.udapteIncreaseR();
        }
        respondingPedestrian.setSpeed(respondingPedestrian.calculateSpeedWithR());
        respondingPedestrian.updatePosition(dT);
        return true;

    }

    public void updateUnaffilatedPedestrians(double t){
        String unaffilatedFile = "src/main/java/.....txt";
        //actualizar las posiciones y direcciones de los unaffilated
    }


    public void colitionHeuristic(Particle particle) {
        //PRIMERO YA SE CHEQUEO QUE EL ANGULO LA PARTICULA Q LO CHOQUE NO VENGA DE A ATRAS
        double anguloA = ( respondingPedestrian.getAngle() + 360) % 360;
        double anguloB = ( particle.getAngle() + 360) % 360;

        // Calcular la diferencia entre los ángulos
        double diferencia = (anguloB - anguloA + 360) % 360;

        // Sumar 45 grados al ángulo A si es más cercano ir en sentido horario,
        // de lo contrario, restar 45 grados.
        if (diferencia <= 180) {
            anguloA = (anguloA + 45) % 360;
        } else {
            anguloA = (anguloA - 45 + 360) % 360;
        }
        respondingPedestrian.setAngle(anguloA);
    }

    public void updateOutput(int id, double tau, String str){
        try {

            DecimalFormat df_time = new DecimalFormat("#.#");
            String t = df_time.format(tau);

            String output = "src/main/python/output_"+id+"_"+t+"_"+str+".txt";
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

    public void updateDynamicAndOutput(Double t, int id, double tau, String str){
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


            // Imprimo la informacion, este va a ser la posicion y el error respecto a la solucion analitca
            StringBuilder particleInfo = new StringBuilder();
            DecimalFormat df = new DecimalFormat("0.0000", new DecimalFormatSymbols(locale));
            particleInfo.append(df.format(respondingPedestrian.getSpeed()));
            bw.write(t.toString() + ' ' + particleInfo);
            bw.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        updateOutput(id,tau, str);
    }
}
