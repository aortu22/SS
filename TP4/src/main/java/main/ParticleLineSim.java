package main;

import java.io.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public class ParticleLineSim {
    private final List<Particle> particleList;
    private double nextT;
    private double dTEscritura;
    private double dT;
    private double L;


    public ParticleLineSim(List<Particle> particleList, double dTEscritura, double dT,double L){
        this.particleList = particleList;
        this.nextT = dTEscritura;
        this.dTEscritura = dTEscritura;
        this.dT = dT;
        this.L = L;
    }

    //Move particles using gear 5
    public void moveParticlesAndRecalculateForces(double t, double dT,double L){
        for(Particle particle : particleList){
            particle.FiveGearPredictorTp2(dT,L,particleList);
        }
    }

    public void updateOutput(){
        try {
            String output = "src/main/python/output_2_"+dT+".txt";
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

    public void updateDynamicAndOutput(Double t){
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

            bw.write( t.toString() + '\n');

            // Ordering particles with id

            // Imprimo la informacion, este va a ser la posicion y el error respecto a la solucion analitca
            StringBuilder particleInfo = new StringBuilder();
            DecimalFormat df = new DecimalFormat("0.0000", new DecimalFormatSymbols(locale));
            for (Particle particle: this.particleList) {
                particleInfo.append(df.format(particle.getX()));
                particleInfo.append(' ').append(df.format(particle.getSpeed()));
                particleInfo.append('\n');
            }
            bw.write(particleInfo.toString());
            bw.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        updateOutput();
    }


}
