package main;

import java.io.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public class EventDrivenSim {
    private final List<Bird> particleList;
    private final List<Vertix> vertixList;
    private final List<Wall> wallList;
    private Collision  lastCollision;
    private double time;

    public EventDrivenSim(List<Bird> particleList, List<Vertix> vertixList, List<Wall> wallList) {
        this.particleList = particleList;
        this.lastCollision = null;
        this.wallList = wallList;
        this.vertixList = vertixList;

        Bird[] birds= (Bird[]) getParticleList().toArray();
        for(int i=0;i< birds.length ;i++){
            Bird bird = birds[i];
            // Choque con otras particulas
            for(int j=i+1;j< birds.length;j++){
                Collision collision=new Collision(bird,birds[j], false);
                bird.getCollisionList().add(collision);
                birds[j].getCollisionList().add(collision);
            }
            // Choque con vertices
            for (Vertix vertix : vertixList) {
                Collision collision = new Collision(bird, vertix, true);
                bird.getCollisionList().add(collision);
            }
            // Choque con paredes
            for (Wall wall : wallList){
                Collision collision = new Collision(bird, wall);
                bird.getCollisionList().add(collision);
            }
        }

        this.time=0;
    }

    public List<Bird> getParticleList() {
        return particleList;
    }

    public List<Wall> getWalls() {
        return wallList;
    }

    //TENER EN CUENTA QUE LAS OTRAS COLICIONES TIENEN EL TIEMPO RELATIVO
    public Collision calculateNextStep(){
        double minStep = Double.MAX_VALUE-100000;
        Collision nextCollision = null;
        Bird[] birds= (Bird[]) getParticleList().toArray();
        for(int i=0;i< birds.length ;i++) {
            List<Collision> birdCollisionList = birds[i].getCollisionList();
            // Choque con otras particulas
            for (int j = i + 1; j < birds.length; j++) {
                double t = birds[i].birdColisionTime(birds[j])+time;
                Collision collision = birdCollisionList.get(birdCollisionList.indexOf(new Collision(birds[i],birds[j], false)));
                collision.setCollisionTime(t);
                if(t<minStep){
                    minStep = t;
                    nextCollision = collision;
                }
            }

            // Choque con vertices
            for (Vertix vertix : vertixList) {
                double t = birds[i].birdColisionTime(vertix)+time;
                Collision collision = birdCollisionList.get(birdCollisionList.indexOf(new Collision(birds[i],vertix, true)));
                collision.setCollisionTime(t);
                if(t<minStep){
                    minStep = t;
                    nextCollision = collision;
                }
            }

            // Choque con paredes
            for (Wall wall : wallList){
                Collision collision = birdCollisionList.get(birdCollisionList.indexOf(new Collision(birds[i],wall)));
                double t = getWallCollisionTime(collision)+time;
                collision.setCollisionTime(t);
                if(t<minStep || nextCollision == null){
                    minStep = t;
                    nextCollision = collision;
                }
            }
        }

        return  nextCollision;
    }

    public void moveAllParticles(Collision collision){
        double deltaT = collision.getCollisionTime() - time;
        for(Bird bird: particleList){
            bird.setNextPosition(deltaT);
        }
        time += deltaT;
    }

    public void recalculateDirections(Collision collision){
        Bird bird1 = collision.getBird1();
        if(collision.isWallCollision()){
            if(collision.getWall().isHorizontal()){
                bird1.setVy(-bird1.getVy());
            }else{
                bird1.setVx(-bird1.getVx());
            }
        }else if(collision.isVertixCollision()){
//            TODO: si conseguimos el angulo estaria bueno hacer esto:
//            Vertix vertix = collision.getVertix();
//            List<Double> vValues = vertix.vertixColision(angle,bird1.getVx(), bird1.getVy());
            bird1.setVx(-bird1.getVx());
            bird1.setVy(-bird1.getVy());
        }else{
            Bird bird2 = collision.getBird2();
            double deltaX=  bird2.getX() - bird1.getX();
            double deltaY=  bird2.getY() - bird1.getY();

            double deltaVx=  bird2.getVx() - bird1.getVx();
            double deltaVy=  bird2.getVy() - bird1.getVy();

            double deltaRx= bird2.getX() - bird1.getX();
            double deltaRy= bird2.getY() - bird1.getY();

            double deltaVdeltaR = (deltaVx*deltaRx)+ (deltaVy*deltaRy);

            double phi = bird1.getRadio() + bird2.getRadio();

            double J = 2 * bird1.getM() * bird2.getM() * deltaVdeltaR / (phi * (bird1.getM() + bird2.getM()));
            double Jx = J * deltaX/ phi;
            double Jy = J * deltaY/ phi;

            bird1.setVx(bird1.getVx() + Jx / bird1.getM());
            bird1.setVy(bird1.getVy() + Jy / bird1.getM());

            bird2.setVx(bird2.getVx() + Jx / bird2.getM());
            bird2.setVy(bird2.getVy() + Jy / bird2.getM());
        }
    }



    public double getWallCollisionTime(Collision collision){
//        Choque con paredes
            Wall wall=collision.getWall();
            Bird bird=collision.getBird1();
            if (bird.getVx() > 0 && !wall.isHorizontal() && wall.getFirstPoint().getX() > bird.getVx()) {
                return  (wall.getFirstPoint().getX() + bird.getRadio() - bird.getX()) / bird.getVx();
            } else if (bird.getVx() < 0 && !wall.isHorizontal() && wall.getFirstPoint().getX() < bird.getVx()) {
                return (wall.getFirstPoint().getX() + bird.getRadio() - bird.getX()) / bird.getVx();
            } else if (bird.getVy() < 0 && wall.isHorizontal() && wall.getFirstPoint().getY() < bird.getVy()) {
                return (wall.getFirstPoint().getY() + bird.getRadio() - bird.getY()) / bird.getVy();
            } else if (bird.getVy() > 0 && wall.isHorizontal() && wall.getFirstPoint().getY() > bird.getVy()) {
                return (wall.getFirstPoint().getY() + bird.getRadio() - bird.getY()) / bird.getVy();
            } else return Double.MAX_VALUE-100000;

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
    public void updateDynamicAndOutput(Double t, int N){
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

            // Writting particles information
            StringBuilder particleInfo = new StringBuilder();
            DecimalFormat df = new DecimalFormat("0.00", new DecimalFormatSymbols(locale));
            for (Wall wall: this.walls) {
                particleInfo.append(df.format(wall.getFirstPoint().getX())).append(' ').append(df.format(wall.getFirstPoint().getY()));
                particleInfo.append(' ').append(df.format(wall.getSecondPoint().getX())).append(' ').append(df.format(wall.getSecondPoint().getY()));
                particleInfo.append('\n');
            }
            for (Bird particle: this.particleList) {
                particleInfo.append(df.format(particle.getX())).append(' ').append(df.format(particle.getY()));
                particleInfo.append(' ').append(df.format(particle.getVx())).append(' ').append(df.format(particle.getVy()));
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
