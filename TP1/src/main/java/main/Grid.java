package main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

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
                int id = col + row * m;
                grid[row][col] = new Cell(row, col, id, m, length);
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
        grid[(int) Math.floor(particle.getY() / cellLength)][(int) Math.floor(particle.getX() / cellLength)].addParticle(particle);
    }


    public Set<Particle> getParticles(int row, int col){
        return grid[row][col].getParticles();
    }

    public void setNeighbours(double rc){
        List<Cell> adjacent;
        Set<Particle> gridParticles;
        List<Particle> particlesArray = new ArrayList<>();
        for(int i=0;i< rows;i++){
            for (int j=0;j<columns;j++) {
                adjacent=getAdjacentCells(i,j);
                gridParticles=grid[i][j].getParticles();
                //checking if particles in different cells are neighbours
                for (Particle particle:gridParticles
                     ) {
                    for(Cell cell : adjacent){
                        Set<Particle> particles = cell.getParticles();
                        if(cell.getRow()<i && cell.getCol()<j){
                            for (Particle particle2 : particles){
                                if(!particle.equals(particle2) && particle.isNeighbour(particle2, rc, length, length)){
                                    particle.addNeighbour(particle2);
                                    particle2.addNeighbour(particle);
                                }
                            }
                        }else if(cell.getRow() <i){
                            for (Particle particle2 :particles
                            ) {
                                if(!particle.equals(particle2) && particle.isNeighbour(particle2,rc,0,length)){
                                    particle.addNeighbour(particle2);
                                    particle2.addNeighbour(particle);
                                }
                            }
                        }else if(cell.getCol() >j){
                            for (Particle particle2 :particles
                            ) {
                                if(!particle.equals(particle2) && particle.isNeighbour(particle2,rc, length, 0)){
                                    particle.addNeighbour(particle2);
                                    particle2.addNeighbour(particle);
                                }
                            }
                        }
                        else{
                            for (Particle particle2 :particles
                            ) {
                                if(!particle.equals(particle2) && particle.isNeighbour(particle2,rc)){
                                    particle.addNeighbour(particle2);
                                    particle2.addNeighbour(particle);
                                }
                            }
                        }
                        // si lo agarre por sphericla hago el isNeighbour 2, sino el comun

                    }

                }
                //checking if particles in same cell are neighbours
                if(rc>Math.sqrt(Math.pow(cellLength,2)*2)){
                    for(Particle particle: gridParticles){
                        for (Particle particle2: gridParticles){
                            if(!particle.equals(particle2)){
                                particle.addNeighbour(particle2);
                                particle2.addNeighbour(particle);
                            }
                        }
                    }
                }else{
                    particlesArray.addAll(gridParticles);
                    for(int k=0;k<gridParticles.size();k++){
                        for(int t=k;t<gridParticles.size();t++){
                            if(!particlesArray.get(k).equals(particlesArray.get(t))){
                                if(spherical){
                                    if(particlesArray.get(k).isNeighbour(particlesArray.get(t), rc) || particlesArray.get(k).isNeighbour(particlesArray.get(t), rc, length, length) || particlesArray.get(k).isNeighbour(particlesArray.get(t), rc, length, 0) || particlesArray.get(k).isNeighbour(particlesArray.get(t), rc, 0, length)){
                                        particlesArray.get(k).addNeighbour(particlesArray.get(t));
                                        particlesArray.get(t).addNeighbour(particlesArray.get(k));
                                    }
                                }else{
                                    if(particlesArray.get(k).isNeighbour(particlesArray.get(t), rc)){
                                        particlesArray.get(k).addNeighbour(particlesArray.get(t));
                                        particlesArray.get(t).addNeighbour(particlesArray.get(k));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    public List<Cell> getAdjacentCells(int row, int col){
        List<Cell> ret =new ArrayList<>();
        if (row==rows-1 && col==columns-1){
            if(spherical){
                ret.add(grid[0][0]);
                ret.add(grid[row][0]);
                ret.add(grid[0][col]);
            }
        }
        else if(row==rows-1){
            if(spherical){
                ret.add(grid[0][col]);
                ret.add(grid[0][col+1]);
            }
            ret.add(grid[row][col+1]);
        } else if (col==columns-1) {
            if(spherical){
                ret.add(grid[row][0]);
                ret.add(grid[row+1][0]);
            }
            ret.add(grid[row+1][col]);
        }
        else{
            ret.add(grid[row+1][col]);
            ret.add(grid[row][col+1]);
            ret.add(grid[row+1][col+1]);
        }
        return ret;
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

}

