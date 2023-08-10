package main;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class App
{
    public static String method = "Cell Index Method";
    public static boolean sphere = true;


    public static void bruteGetOutput(List<Particle> particles, long time){

        try {
            String output = "src/main/python/brute_output.txt";
            File file = new File(output);
            // Si el archivo no existe es creado
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            //Escribo el titulo
            bw.write( "id de la partícula \"i\" \t-\t id's de las partículas cuya distancia borde-borde es menos de rc \n");
            //Escribo la información de las particulas
            for(Particle particle : particles) {
                StringBuilder particleInfo = new StringBuilder();
                particleInfo.append(particle.getId()).append("\t-\t").append(particle.getNeighbours()).append("\n");
                bw.write(particleInfo.toString());
            }
            String timeString = "Execution time in milliseconds\t-\t" + time;
            bw.write(timeString);
            bw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void bruteForceMethod(List<Particle> particles, double rc, boolean sphere, double length, int n, long startTime){
        int i=0;
        for(Particle particle1 : particles){
            for(int j=i+1; j < n; j++){
                Particle particle2= particles.get(j);
                if(!particle1.equals(particle2)){
                    if(sphere){
                        if(particle1.isNeighbour(particle2, rc) || particle1.isNeighbour(particle2, rc, length, length) || particle1.isNeighbour(particle2, rc, length, 0) || particle1.isNeighbour(particle2, rc, 0, length)){
                            particle1.addNeighbour(particle2);
                            particle2.addNeighbour(particle1);
                        }
                    }else{
                        if(particle1.isNeighbour(particle2, rc)){
                            particle1.addNeighbour(particle2);
                            particle2.addNeighbour(particle1);
                        }
                    }
                }
            }
            i++;
        }
        long endTime = System.currentTimeMillis() - startTime;
        bruteGetOutput(particles, endTime);
    }


    public static void main( String[] args )
    {
        long startTime = System.currentTimeMillis();
        String jsonFilePathStatic = "src/main/java/main/static.txt"; // Cambia esto con la ruta del archivo JSON
        String jsonFilePathDynamic = "src/main/java/main/dynamic.txt";
        Grid grid;
        List<Particle> particleList = new ArrayList<>();
        double maxR = 0.0;
        double L = 0.0;
        int N = 0;
        double Rc = 0.0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(archivo));
            String linea;

            // Leer las primeras 3 líneas y guardarlas en variables especiales
            double L = Double.parseDouble(br.readLine());
            int N = Integer.parseInt(br.readLine());
            double Rc = Double.parseDouble(br.readLine());

            // Leer y retornar solo el primer valor de cada par de valores
            int i = 0;
            while ((linea = br.readLine()) != null) {
                String[] valores = linea.split(" ");
                double r = Double.parseDouble(valores[0]);
                
                System.out.println("Primer Valor: " + primerValor);
                if (r > maxR){
                    maxR = r;
                }

                Particle particle = new Particle(id, r);
                particleList.add(particle);
                i++;
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader(archivo));
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



        if (method.equals("Cell Index Method")){
            int M = (int)Math.floor(L / (Rc + 2*maxR));
            grid = new Grid(L,M,sphere);
            for (Particle particle : particleList) {
                grid.addToCell(particle);
            }
            grid.setNeighbours(Rc);
            long endTime = System.currentTimeMillis() - startTime;
            grid.getOutput(endTime);
        }else{
            bruteForceMethod(particleList, Rc, sphere, L,N, startTime);
        }


    }
}
