package main;

import java.io.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Grid {
    private final int rows;
    private final int columns;
    private final Cell[][] grid;
    private final double cellLength;
    private final double length;
    private final boolean spherical;


    public Grid(double length, int  m,  boolean spherical){
        this.rows = m;
        this.columns = m;
        this.length =length;
        this.cellLength = length/m;
        this.spherical = spherical;
        grid = new Cell[m][m];
        for (int row = 0; row < m; row++) {
            for (int col = 0; col < m; col++) {
                grid[row][col] = new Cell(row, col, m, length);
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public Cell getCell(int row, int column){
        return grid[row][column];
    }

    public void addToCell(Particle particle){
        int i = (int) Math.floor(particle.getY() / cellLength);
        int j = (int) Math.floor(particle.getX() / cellLength);
        if(i==grid.length){
            i=0;
        }
        if(j==grid[0].length){
            j=0;
        }
        grid[i][j].addParticle(particle);
    }


    public Set<Particle> getParticles(int row, int col){
        return grid[row][col].getParticles();
    }

    public void setNeighbours(double rc){
        List<AdjacentCell> adjacent;
        Set<Particle> gridParticles;
        List<Particle> particlesArray;
        for(int i=0;i< rows;i++){
            for (int j=0;j<columns;j++) {
                adjacent=getAdjacentCells(i,j);
                gridParticles=grid[i][j].getParticles();
                //checking if particles in different cells are neighbours
                for(AdjacentCell cell : adjacent){
                    Set<Particle> particles = cell.cell.getParticles();
                    for (Particle particle2 : particles){
                        for (Particle particle:gridParticles) {
                            if(!particle.equals(particle2) && particle.isNeighbour(particle2, rc, cell.correctionX, cell.correctionY)){
                                particle.addNeighbour(particle2);
                                particle2.addNeighbour(particle);
                            }
                        }
                    }
                }
                //checking if particles in same cell are neighbours
                particlesArray = new ArrayList<>();
                particlesArray.addAll(gridParticles);
                for(int k=0;k<gridParticles.size();k++){
                    for(int t=k+1;t<gridParticles.size();t++){
                        if(particlesArray.get(k).isNeighbour(particlesArray.get(t), rc)){
                            particlesArray.get(k).addNeighbour(particlesArray.get(t));
                            particlesArray.get(t).addNeighbour(particlesArray.get(k));
                        }
                    }
                }
            }
        }
    }
    public List<AdjacentCell> getAdjacentCells(int row, int col){
        List<AdjacentCell> ret =new ArrayList<>();
        if(row < rows - 1 && col < columns - 1 && row > 0){
            ret.add(new AdjacentCell(grid[row - 1][col], 0,0));
            ret.add(new AdjacentCell(grid[row - 1][col+1], 0,0));
            ret.add(new AdjacentCell(grid[row][col+1], 0,0));
            ret.add(new AdjacentCell(grid[row + 1][col+1], 0,0));
        }
        else if(col == columns - 1){
            if(row < rows - 1 && row > 0){
                ret.add(new AdjacentCell(grid[row - 1][col], 0,0));
                if(spherical){
                    ret.add(new AdjacentCell(grid[row - 1][0], length,0));
                    ret.add(new AdjacentCell(grid[row][0], length,0));
                    ret.add(new AdjacentCell(grid[row + 1][0], length,0));
                }
            }else if (row == rows - 1){
                ret.add(new AdjacentCell(grid[row - 1][col], 0,0));
                if(spherical){
                    ret.add(new AdjacentCell(grid[row - 1][0], length,0));
                    ret.add(new AdjacentCell(grid[row][0], length,0));
                    ret.add(new AdjacentCell(grid[0][0], length,length));
                }
            }else if (row == 0 && spherical){
                ret.add(new AdjacentCell(grid[rows - 1][col], 0,-length));
                ret.add(new AdjacentCell(grid[rows - 1][0], length,-length));
                ret.add(new AdjacentCell(grid[row][0], length,0));
                ret.add(new AdjacentCell(grid[row + 1][0], length,0));
            }
        } else if (row == rows - 1){
            if(col < columns - 1){
                ret.add(new AdjacentCell(grid[row - 1][col], 0,0));
                ret.add(new AdjacentCell(grid[row - 1][col+1], 0,0));
                ret.add(new AdjacentCell(grid[row][col+1], 0,0));
                if(spherical){
                    ret.add(new AdjacentCell(grid[0][col+1], 0,length));
                }
            }
        } else if (row == 0){
            if(col < columns - 1) {
                ret.add(new AdjacentCell(grid[row][col+1], 0,0));
                ret.add(new AdjacentCell(grid[row + 1][col+1], 0,0));
                if(spherical){
                    ret.add(new AdjacentCell(grid[rows - 1][col], 0,-length));
                    ret.add(new AdjacentCell(grid[rows - 1][col + 1], 0,-length));
                }
            }
        }
        return ret;
    }

    public void updateOutput(){
        try {
            String output = "src/main/python/output.txt";
            String dynamic = "src/main/java/main/dynamic.txt";
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
    public void updateDynamicAndOutput(Integer t, int N){
        Locale locale = new Locale("en", "US");
        try {
            String dynamic = "src/main/java/main/dynamic.txt";
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
            List<Particle> allParticles = new ArrayList<>();
            for(int i=0;i< rows;i++){
                for (int j=0;j<columns;j++) {
                    allParticles.addAll(grid[i][j].getParticles());
                }
            }
            Collections.sort(allParticles);

            // Writting particles information
            StringBuilder particleInfo = new StringBuilder();
            DecimalFormat df = new DecimalFormat("0.00", new DecimalFormatSymbols(locale));
            for (Particle particle: allParticles) {
                particleInfo.append(df.format(particle.getX())).append(' ').append(df.format(particle.getY()));
                if(particle instanceof Bird bird){
                    particleInfo.append(' ').append(df.format(bird.getTheta()));
                }
                particleInfo.append('\n');
            }
            bw.write(particleInfo.toString());
            bw.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        updateOutput();
    }
    public void getOutput(long time){
        try {
            String output = "src/main/python/output.txt";
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
            for(int i=0;i< rows;i++){
                for (int j=0;j<columns;j++) {
                    StringBuilder particleInfo = new StringBuilder();
                    Set<Particle> particles = grid[i][j].getParticles();
                    for (Particle particle : particles){
                        particleInfo.append(particle.getId()).append("\t-\t").append(particle.getNeighbours()).append("\n");
                    }
                    bw.write(particleInfo.toString());
                }
            }
            String timeString = "Execution time in milliseconds\t-\t" + time;
            bw.write(timeString);
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double evolveBirdsT(double error,double delta_time,int N){
        Set<Particle> cellParticules;
        Double[] orderSumatory = new Double[2];
        orderSumatory[0] = 0.0;
        orderSumatory[1] = 0.0;
        for (int i=0; i < rows;i++){
            for(int j=0; j < columns;j++){
                cellParticules = grid[i][j].getParticles();
                for (Particle particle: cellParticules) {
                    ((Bird) particle).updateAngles(error);
                    ((Bird) particle).setNextPosition(this.length, delta_time);
                    orderSumatory[0] += ((Bird) particle).getV() * Math.sin(Math.toRadians(((Bird) particle).getTheta()));
                    orderSumatory[1] += ((Bird) particle).getV() * Math.cos(Math.toRadians(((Bird) particle).getTheta()));
                }
            }
        }
        for (int i=0; i < rows;i++){
            for(int j=0; j < columns;j++){
                cellParticules = grid[i][j].getParticles();
                for (Particle particle: cellParticules) {
                    ((Bird) particle).setFutureAngle();
                }
            }
        }
        orderSumatory[0] = orderSumatory[0]/N;
        orderSumatory[1] = orderSumatory[1]/N;
        return Math.sqrt(Math.pow(orderSumatory[0],2) + Math.pow(orderSumatory[1],2));
    }

}

