package main;

import java.io.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public class PedestrianSim {

    private final Pedestrian respondingPedestrian;
    private final List<Particle> unaffiliatedPedestrian;
    private double nextT;
    private double dTEscritura;
    private double dT;
    private BufferedReader unaffilatedFIleReader;
    private int id = 25;
    private int frame;

    public PedestrianSim(Pedestrian respondingPedestrian, List<Particle> unaffiliatedPedestrian, double dTEscritura, double dT,BufferedReader unaffilatedFIleReader){
        this.respondingPedestrian = respondingPedestrian;
        this.unaffiliatedPedestrian = unaffiliatedPedestrian;
        this.nextT = dTEscritura;
        this.dTEscritura = dTEscritura;
        this.dT = dT;
        this.unaffilatedFIleReader = unaffilatedFIleReader;
        this.frame = 2;
    }

    public boolean advancePedestrian(double t){
        boolean isCollition = false;
        if(unaffiliatedPedestrian != null){
            if( respondingPedestrian.getPosition().calculateDistance(respondingPedestrian.getCurrentTarget()) < respondingPedestrian.getRadio()*2){
                if(respondingPedestrian.setNextTarget() == null){
                    return false;
                }
            }
            Particle closestImpactParticle = null;
            double smallestCollitionTime = 0;
            for(Particle particle : unaffiliatedPedestrian){
                //Esta en rango de choque, el radio de interaccion es r *5
                if(respondingPedestrian.calculateDistance(particle) < respondingPedestrian.getRadio() * 5){
                    double collitionTime = respondingPedestrian.calcularTiempoColision(particle);
                    if (collitionTime > smallestCollitionTime){
                        closestImpactParticle = particle;
                    }
                }
            }
            if(closestImpactParticle != null && respondingPedestrian.getPosition().calculateDistance(respondingPedestrian.getCurrentTarget()) > respondingPedestrian.getD() + respondingPedestrian.getRadio()){
                colitionHeuristic(closestImpactParticle);
                isCollition = respondingPedestrian.collidesWith(closestImpactParticle);
            }else{
                respondingPedestrian.setAngleToTarget();
            }
        }

        if(isCollition){
            respondingPedestrian.setRadio(respondingPedestrian.getrMin());
        }else if( respondingPedestrian.getPosition().calculateDistance(respondingPedestrian.getCurrentTarget()) <= respondingPedestrian.getD() && respondingPedestrian.getSpeed() != 0){
            respondingPedestrian.updateDecreseR();
        }else{
            respondingPedestrian.udapteIncreaseR();
        }
        respondingPedestrian.setSpeed(respondingPedestrian.calculateSpeedWithR());
        respondingPedestrian.updatePosition(dT);
        return true;
    }

    public void updateUnaffilatedPedestrians(double t){
        int i=0;
        String line;
        try{
            while (i < 25 && (line = unaffilatedFIleReader.readLine()) != null) {
                String[] columns = line.split("\t");

                double x = Double.parseDouble(columns[1]);
                double y = Double.parseDouble(columns[2]);
                double speed = Double.parseDouble(columns[5]);
                double angle = Double.parseDouble(columns[6]);
                unaffiliatedPedestrian.get(i).setPosition(x,y);
                unaffiliatedPedestrian.get(i).setAngle((angle + 360)%360);
                unaffiliatedPedestrian.get(i).setSpeed(speed);
                i++;
            }
        }catch (Exception ex){
            System.out.println(ex);
        }
    }


    public void colitionHeuristic(Particle particle) {
        //PRIMERO YA SE CHEQUEO QUE EL ANGULO LA PARTICULA Q LO CHOQUE NO VENGA DE A ATRAS
        double anguloA = ( respondingPedestrian.getAngle() + 360) % 360;
        double anguloB = ( particle.getAngle() + 360) % 360;

        // Calcular la diferencia entre los ángulos
        double diferencia = (anguloB - anguloA + 360) % 360;

        // Sumar 45 grados al ángulo A si es más cercano ir en sentido horario,
        // de lo contrario, restar 45 grados.
        double distance = respondingPedestrian.getPosition().calculateDistance(particle.getPosition());
        double angleToCorrect = 45 * ( ( 5 * respondingPedestrian.getRadio() - distance)/ (3* respondingPedestrian.getRadio()));

        if (diferencia <= 180) {
            anguloA = (anguloA + angleToCorrect) % 360;
        } else {
            anguloA = (anguloA - angleToCorrect + 360) % 360;
        }
        respondingPedestrian.setAngle(anguloA);
    }

    public void updateOutputPedestrian(){
        try {

            String output = "src/main/python/ej_2c/output_pedestrian.txt";
            String dynamic = "src/main/java/main/dynamic_2.txt";
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

    public void updateOutput(double t){
        try {

            String dynamic = "src/main/python/ej_2c/simulation_output.txt";
            Locale locale = new Locale("en", "US");

            File file = new File(dynamic);
            // Si el archivo no existe es creado

            // Parameter false make us write stepping in the information
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            DecimalFormat df = new DecimalFormat("0.0000", new DecimalFormatSymbols(locale));

            for(Particle particle : unaffiliatedPedestrian){
                StringBuilder particleInfo = new StringBuilder();
                particleInfo.append(frame + "\t");
                particleInfo.append(df.format(particle.getX()) + "\t");
                particleInfo.append(df.format(particle.getY()) + "\t");
                particleInfo.append(particle.getId() + "\t");
                particleInfo.append(t + "\t");
                particleInfo.append(particle.getSpeed() + "\n");


                bw.write(particleInfo.toString());
            }
            StringBuilder particleInfo = new StringBuilder();
            particleInfo.append(frame + "\t");
            particleInfo.append(df.format(respondingPedestrian.getX()) + "\t");
            particleInfo.append(df.format(respondingPedestrian.getY()) + "\t");
            particleInfo.append("25\t");
            particleInfo.append(t + "\t");
            particleInfo.append(respondingPedestrian.getSpeed() + "\n");
            bw.write(particleInfo.toString());

            bw.close();
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
            String dynamic = "src/main/java/main/dynamic_2.txt";
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
        updateOutputPedestrian();
        updateOutput(t);
        frame++;
    }
}
