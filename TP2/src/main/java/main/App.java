package main;

import java.util.*;
import java.io.*;

public class App
{

    public static void main( String[] args )
    {

        String jsonFilePathStatic = "src/main/java/main/static.txt"; // Cambia esto con la ruta del archivo JSON
        String jsonFilePathDynamic = "src/main/java/main/dynamic.txt";
        Grid grid;
        List<Particle> particleList = new ArrayList<>();
        double maxR = 0.0;
        double L = 0.0;
        int N = 0;
        double Rc = 0.0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(jsonFilePathStatic));
            String linea;

            // Leer las primeras 3 líneas y guardarlas en variables especiales
            L = Double.parseDouble(br.readLine());
            N = Integer.parseInt(br.readLine());
            Rc = Double.parseDouble(br.readLine());

            // Leer y retornar solo el primer valor de cada par de valores
            int i = 0;
            while ((linea = br.readLine()) != null) {
                String[] valores = linea.split(" ");
                double r = Double.parseDouble(valores[0]);

                if (r > maxR){
                    maxR = r;
                }

                Particle particle = new Particle(i, r);
                particleList.add(particle);
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
                particleList.get(i).setPosition(x,y);
                i++;
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        long startTime = System.currentTimeMillis();
        int M = (int)Math.floor(L / (Rc + 2*maxR));
        grid = new Grid(L,M,sphere);
        for (Particle particle : particleList) {
            grid.addToCell(particle);
        }
        grid.setNeighbours(Rc);
        long endTime = System.currentTimeMillis() - startTime;
        grid.getOutput(endTime);

    }
}
