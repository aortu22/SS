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



    public static void bruteGetOutput(List<Particle> particles){

        try {
            String output = "output.txt";
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
            bw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void bruteForceMethod(List<Particle> particles, double rc, boolean sphere, double length, int n){
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
        bruteGetOutput(particles);
    }

    public static void main( String[] args )
    {
        String jsonFilePath = "src/main/java/main/static.json"; // Cambia esto con la ruta del archivo JSON
        Grid grid;
        List<Particle> particleList = new ArrayList<>();
        double maxR = 0.0;
        double L = 0.0;
        int N = 0;
        double Rc = 0.0;
        boolean sphere = false;
        String method = "";
        try {
            String jsonContent = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
            JSONObject jsonObject = new JSONObject(jsonContent);

            L = jsonObject.getDouble("L");
            N = jsonObject.getInt("N");
            Rc = jsonObject.getDouble("Rc");
            sphere = jsonObject.getBoolean("sphere");
            method = jsonObject.getString("method");

            JSONArray particlesArray = jsonObject.getJSONArray("particles");
            for (int i = 0; i < particlesArray.length(); i++) {
                JSONObject particleObj = particlesArray.getJSONObject(i);
                String id = particleObj.getString("id");
                double x = particleObj.getDouble("x");
                double y = particleObj.getDouble("y");
                double r = particleObj.getDouble("r");
                if (r > maxR){
                    maxR = r;
                }

                Particle particle = new Particle(id, x, y, r);
                particleList.add(particle);

            }
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
            grid.getOutput();
        }else{
            bruteForceMethod(particleList, Rc, sphere, L,N);

        }


    }
}
