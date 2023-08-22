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
            bw.write( "\nn = " + error + "\n");
            //Escribo la información de las particulas
            DecimalFormat df = new DecimalFormat("#.####");
            for (Map.Entry<Integer, List<Double>> order : orderStatList.entrySet()) {
                double sum = 0;
                for(Double d : order.getValue())
                    sum += d;
                double prom = sum/tries;
                bw.write(df.format(prom) + "\n");
            }
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
    public static void main( String[] args )
    {

        String jsonFilePathStatic = "src/main/java/main/static.txt";
        String jsonFilePathDynamic = "src/main/java/main/dynamic.txt";
        Map<Integer, List<Double>> orderStatList = new HashMap<>();
        for (int i = 0; i < iterations - 1; i++) {
            orderStatList.put(i, new ArrayList<>());
        }
//        List<Double> orderStatList = new ArrayList<>(iterations);
        Grid grid;
        List<Bird> birdList = new ArrayList<>();
        double maxR = 0.0;
        double L = 0.0;
        int N = 0;
        double Rc = 0.0;
        double n = 0.0;
        double dT = 0.0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(jsonFilePathStatic));

            // Leer las primeras 3 líneas y guardarlas en variables especiales
            L = Double.parseDouble(br.readLine());
            N = Integer.parseInt(br.readLine());
            Rc = Double.parseDouble(br.readLine());
            n = Double.parseDouble(br.readLine());
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

//        long startTime = System.currentTimeMillis();
        int j =0;
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
            grid.updateOutput();
            for (Bird bird : birdList) {
                grid.addToCell(bird);
            }
            int i = 1;
            while( i < iterations){
//                System.out.println("Iteration " + i);
                grid.setNeighbours(Rc);
                orderStatList.get(i-1).add(grid.evolveBirdsT(n,dT,N));
                if(j==0){
                // Me parece que no hace falta tener promedio de los outputs,
                // por ahora lo dejo asi pero desp cuando tenga un rato mas lo hago promediado
                    grid.updateDynamicAndOutput(i,N);
                }
                i++;
            }
            j++;
        }
        saveOrderStat(orderStatList);


    }
}
