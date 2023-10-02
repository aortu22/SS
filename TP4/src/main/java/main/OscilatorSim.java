package main;

import java.io.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class OscilatorSim {
    private final Particle particle;
    private final double K;
    private final double Y;
    private double nextT;
    private double dTEscritura;
    private double dT;
    private final String method;

    public OscilatorSim(Particle particle,double K,double Y,String method,double dTEscritura, double dT) {
        this.particle = particle;
        this.K = K;
        this.Y = Y;
        this.method = method;
        this.nextT = this.dTEscritura = dTEscritura;
        this.dT = dT;
    }

    public void recalculatePosition(double t, double dT){
        switch (method) {
            case "beeman" -> {
                particle.setBeemanPosition(dT);
                particle.beemanVelocity(dT);
                particle.setAcceleration((-K*particle.getX() - Y*particle.getSpeed())/particle.getM());
            }
            case "gear" -> particle.FiveGearPredictor(dT);
            case "verlet" -> {
                // Verlet - Original
                particle.setVerletPosition(dT);
                particle.verletVelocity(dT);
                particle.setAcceleration((-K*particle.getX() - Y*particle.getSpeed())/particle.getM());
            }
        }
        particle.setError(Math.pow(particle.analyticSolutionComparison(t) - particle.getX(), 2));
    }


    public void updateOutput(){
        try {
            String output = "src/main/python/output_"+method+"_1_"+dT+".txt";
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

    public void updateDynamicAndOutput(Double t){
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
//            DecimalFormat df_1 = new DecimalFormat("0.0000", new DecimalFormatSymbols(locale));
//            DecimalFormat df_2 = new DecimalFormat("0.000000000000000000000000000000000000000000000000", new DecimalFormatSymbols(locale));

//            particleInfo.append(df_1.format(particle.getX())).append(' ').append(df_2.format(particle.getError()));
            particleInfo.append(particle.getX()).append(' ').append(particle.getError());
            particleInfo.append('\n');
            bw.write(particleInfo.toString());
            bw.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        updateOutput();
    }
}
