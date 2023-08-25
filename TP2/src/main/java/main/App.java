package main;

import java.text.DecimalFormat;
import java.util.*;
import java.io.*;

public class App
{

    public static final int iterations = 501;
    public static final int tries = 100;
    public static double error = 0.0;

    public static int N = 0;

    public static void saveOrderStat(Map<Integer, List<Double>> orderStatList){
        
        try {
            String output = "src/main/python/order"+ N +".txt";
            File file = new File(output);
            // Si el archivo no existe es creado
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file,true);
            BufferedWriter bw = new BufferedWriter(fw);
            //Escribo el titulo
            bw.write( "n = " + error + "\n");
            //Escribo la información de las particulas
            DecimalFormat df = new DecimalFormat("#.####");
            for (Map.Entry<Integer, List<Double>> order : orderStatList.entrySet()) {
                double sum = 0;
                for(Double d : order.getValue())
                    sum += d;
                double prom = sum/tries;
                bw.write(df.format(prom) + "\n");
            }
            bw.write("\n");
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteOutput(){
        String output = "src/main/python/output.txt";
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
    public static void main( String[] args )
    {
        List<Double> eta = new ArrayList<>(Arrays.asList(0.0,
//                0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9,
            1.0,
//                1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 1.8, 1.9,
            2.0,
//                2.1, 2.2, 2.3, 2.4, 2.5, 2.6, 2.7, 2.8, 2.9,
            3.0,
//                3.1, 3.2, 3.3, 3.4, 3.5, 3.6, 3.7, 3.8, 3.9,
            4.0,
//                4.1, 4.2, 4.3, 4.4, 4.5, 4.6, 4.7, 4.8, 4.9,
                5.0));
        for (Double n: eta) {
            System.out.println("For eta: " + n);
            String jsonFilePathStatic = "src/main/java/main/static.txt";
            String jsonFilePathDynamic = "src/main/java/main/dynamic.txt";
            Map<Integer, List<Double>> orderStatList = new HashMap<>();
            for (int i = 0; i < iterations - 1; i++) {
                orderStatList.put(i, new ArrayList<>());
            }
            Grid grid;
            List<Bird> birdList = new ArrayList<>();
            double maxR = 0.0;
            double L = 0.0;
            int N = 0;
            double Rc = 0.0;
//            double n = 0.0;
            double dT = 0.0;
            try {
                BufferedReader br = new BufferedReader(new FileReader(jsonFilePathStatic));

                // Leer las primeras 3 líneas y guardarlas en variables especiales
                L = Double.parseDouble(br.readLine());
                N = Integer.parseInt(br.readLine());
                Rc = Double.parseDouble(br.readLine());
//                n = Double.parseDouble(br.readLine());
                App.error = n;
                App.N = N;
                dT = Double.parseDouble(br.readLine());
                // Leer y retornar solo el primer valor de cada par de valores
                int i = 0;
                while (br.readLine() != null) {
                    double v = 0.03; //desp lo pedimos por input o algo
                    Bird bird = new Bird(i, v);
                    birdList.add(bird);
                    i++;
                }

                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            int j = 0;
            while (j < tries){
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
                        double t = Double.parseDouble(valores[2]);
                        birdList.get(i).setPosition(x,y);
                        birdList.get(i).setTheta(t);
                        i++;
                    }

                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println("Try " + j);
                int M = (int)Math.floor(L / (Rc + 2*maxR));
                // Always spherical because i want the particle to move from top to back (traspass)
                boolean spherical = true;
                grid = new Grid(L,M,spherical);
                deleteOutput();
                reloadDynamicOutput();
                grid.updateOutput();
                for (Bird bird : birdList) {
                    grid.addToCell(bird);
                }
                int i = 1;
                while(i < iterations){
                    grid.setNeighbours(Rc);
                    orderStatList.get(i-1).add(grid.evolveBirdsT(n,dT,N));
                    grid.updateDynamicAndOutput(i,N);
                    i++;
                }
                j++;
            }
            saveOrderStat(orderStatList);



        }

    }
}
