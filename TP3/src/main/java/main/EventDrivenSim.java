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
    private double nextWritingTime;
    private final double writingPeriod;
    private final double L;
    private final double L_fixed;

    public EventDrivenSim(List<Bird> particleList, List<Vertix> vertixList, List<Wall> wallList,double writingPeriod, double L, double L_fixed) {
        this.particleList = particleList;
        this.lastCollision = null;
        this.wallList = wallList;
        this.vertixList = vertixList;
        this.L = L;
        this.L_fixed = L_fixed;

        Bird[] birds= (Bird[]) getParticleList().toArray(new Bird[particleList.size()]);
        for(int i=0;i< birds.length ;i++){
            Bird bird = birds[i];
            // Choque con otras particulas
            for(int j=i+1;j< birds.length;j++){
                Collision collision=new Collision(bird,birds[j]);
                bird.getCollisionList().add(collision);
                birds[j].getCollisionList().add(collision);
            }
            // Choque con vertices
            for (Vertix vertix : vertixList) {
                Collision collision = new Collision(bird, vertix);
                bird.getCollisionList().add(collision);
            }
            // Choque con paredes
            for (Wall wall : wallList){
                Collision collision = new Collision(bird, wall);
                bird.getCollisionList().add(collision);
            }
        }

        this.time=0;
        this.nextWritingTime = 0;
        this.writingPeriod = writingPeriod;
    }

    public List<Bird> getParticleList() {
        return particleList;
    }

    public List<Wall> getWalls() {
        return wallList;
    }

    public void recalculateCollisions(){
        if(lastCollision == null){
            Bird[] birds= getParticleList().toArray(new Bird[particleList.size()]);
            for(int i=0;i< birds.length ;i++) {
                List<Collision> birdCollisionList = birds[i].getCollisionList();
                // Choque con otras particulas
                for (int j = i + 1; j < birds.length; j++) {
                    double t = birds[i].birdColisionTime(birds[j])+time;
                    Collision collision = birdCollisionList.get(birdCollisionList.indexOf(new Collision(birds[i],birds[j])));
                    collision.setCollisionTime(t);
                }

                // Choque con vertices
                for (Vertix vertix : vertixList) {
                    double t = birds[i].birdColisionTime(vertix)+time;
                    Collision collision = birdCollisionList.get(birdCollisionList.indexOf(new Collision(birds[i],vertix)));
                    collision.setCollisionTime(t);
                }

                // Choque con paredes
                for (Wall wall : wallList){
                    Collision collision = birdCollisionList.get(birdCollisionList.indexOf(new Collision(birds[i],wall)));
                    double t = getWallCollisionTime(collision)+time;
                    collision.setCollisionTime(t);
                }
            }
        }else{
            Bird bird1 = lastCollision.getBird1();
            List<Collision> bird1collitionList = bird1.getCollisionList();
            for(Collision collision : bird1collitionList){
                double t = 0.0;
                if(collision.isVertixCollision()){
                    t = collision.getBird1().vertixColisionTime(collision.getVertix())+time;
                }else if(collision.isWallCollision()){
                    t = getWallCollisionTime(collision)+time;
                }else{
                    t = collision.getBird1().birdColisionTime(collision.getBird2())+time;
                }

                collision.setCollisionTime(t);

            }
            if(lastCollision.getBird2() != null){
                Bird bird2 = lastCollision.getBird2();
                List<Collision> bird2collitionList = bird2.getCollisionList();
                for(Collision collision : bird2collitionList){
                    double t = 0.0;
                    if(collision.isVertixCollision()){
                        t = collision.getBird1().birdColisionTime(collision.getVertix())+time;
                    }else if(collision.isWallCollision()){
                        t = getWallCollisionTime(collision)+time;
                    }else{
                        t = collision.getBird1().birdColisionTime(collision.getBird2())+time;
                        if(t < 0){
                            System.out.println("xd");
                        }
                    }

                    collision.setCollisionTime(t);

                }
            }
        }
    }


    //TENER EN CUENTA QUE LAS OTRAS COLICIONES TIENEN EL TIEMPO RELATIVO
    public Collision calculateNextStep(){
        double minStep = Double.MAX_VALUE-100000;
        Collision nextCollision = null;
        Bird[] birds= getParticleList().toArray(new Bird[particleList.size()]);
        for(int i=0;i< birds.length ;i++) {
            List<Collision> birdCollisionList = birds[i].getCollisionList();
            // Choque con otras particulas
            for(Collision collision : birdCollisionList){
                double t = collision.getCollisionTime();
                if(t<minStep){
                    minStep = t;
                    nextCollision = collision;
                }
            }
        }

        lastCollision = nextCollision;
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
                switch (wallList.indexOf(collision.getWall())) {
                    case 0, 2 -> addImpulse(bird1.getM() * Math.abs(2 * bird1.getVy()) , 0.00);
                    case 5, 7 -> addImpulse(0.0, bird1.getM() * Math.abs(2 * bird1.getVy()) );
                }
            }else{
                bird1.setVx(-bird1.getVx());
                switch (wallList.indexOf(collision.getWall())) {
                    case 1 -> addImpulse(bird1.getM() * Math.abs(2 * bird1.getVx()) , 0.00);
                    case 3, 4 -> addImpulse(bird1.getM() * Math.abs(2 * bird1.getVx()) , 0.0);
                    case 6 -> addImpulse(0.0, bird1.getM() * Math.abs(2 * bird1.getVx()) );
                }
            }
        }else{
            Bird bird2;
            if(collision.isVertixCollision()){
                bird2 = collision.getVertix();
            }else{
                bird2 = collision.getBird2();
            }
            double deltaX=  bird2.getX() - bird1.getX();
            double deltaY=  bird2.getY() - bird1.getY();

            double deltaVx=  bird2.getVx() - bird1.getVx();
            double deltaVy=  bird2.getVy() - bird1.getVy();

            double deltaRx= bird2.getX() - bird1.getX();
            double deltaRy= bird2.getY() - bird1.getY();

            double deltaVdeltaR = (deltaVx*deltaRx)+ (deltaVy*deltaRy);

            double phi = bird1.getRadio() + bird2.getRadio();

            double J = (2 * bird1.getM() * bird2.getM() * deltaVdeltaR) / (phi * ((bird1.getM() + bird2.getM())));
            double Jx = (J * deltaX)/ phi;
            double Jy = (J * deltaY)/ phi;
            double vx= bird1.getVx();
            double vy= bird1.getVy();
            bird1.setVx(bird1.getVx() + Jx / bird1.getM());
            bird1.setVy(bird1.getVy() + Jy / bird1.getM());
//            Si es colision con vertice, no updateo al vertice
            if(!collision.isVertixCollision()){
                bird2.setVx(bird2.getVx() - Jx / bird2.getM());
                bird2.setVy(bird2.getVy() - Jy / bird2.getM());
            }else{
                addImpulse(bird1.getM()*Math.abs(bird1.getVx()-vx),bird1.getM()*Math.abs(bird1.getVy())-vy);
            }
        }
    }



    public double getWallCollisionTime(Collision collision){
//        Choque con paredes
            Wall wall=collision.getWall();
            Bird bird=collision.getBird1();
            Double collisionTime;
            if (bird.getVx() > 0 && !wall.isHorizontal() && wall.getFirstPoint().getX() > (bird.getX() + bird.getRadio())) {
                collisionTime=(wall.getFirstPoint().getX() - bird.getRadio() - bird.getX()) / bird.getVx();
                double yPosition=bird.getY()+bird.getVy()*collisionTime;
                return wall.getFirstPoint().getY()< wall.getSecondPoint().getY()?(yPosition>wall.getFirstPoint().getY() && yPosition<wall.getSecondPoint().getY()?collisionTime:Double.MAX_VALUE-100000):(yPosition<wall.getFirstPoint().getY() && yPosition>wall.getSecondPoint().getY()?collisionTime:Double.MAX_VALUE-100000);
            } else if (bird.getVx() < 0 && !wall.isHorizontal() && wall.getFirstPoint().getX() < (bird.getX() - bird.getRadio())) {
                collisionTime=(wall.getFirstPoint().getX() + bird.getRadio() - bird.getX()) / bird.getVx();
                double yPosition=bird.getY()+bird.getVy()*collisionTime;
                return wall.getFirstPoint().getY()< wall.getSecondPoint().getY()?(yPosition>wall.getFirstPoint().getY() && yPosition<wall.getSecondPoint().getY()?collisionTime:Double.MAX_VALUE-100000):(yPosition<wall.getFirstPoint().getY() && yPosition>wall.getSecondPoint().getY()?collisionTime:Double.MAX_VALUE-100000);
            } else if (bird.getVy() < 0 && wall.isHorizontal() && wall.getFirstPoint().getY() < (bird.getY() - bird.getRadio())) {
                collisionTime=(wall.getFirstPoint().getY() + bird.getRadio() - bird.getY()) / bird.getVy();
                double xPosition=bird.getX()+bird.getVx()*collisionTime;
                return wall.getFirstPoint().getX()< wall.getSecondPoint().getX()?(xPosition>wall.getFirstPoint().getX() && xPosition<wall.getSecondPoint().getX()?collisionTime:Double.MAX_VALUE-100000):(xPosition<wall.getFirstPoint().getX() && xPosition>wall.getSecondPoint().getX()?collisionTime:Double.MAX_VALUE-100000);
            } else if (bird.getVy() > 0 && wall.isHorizontal() && wall.getFirstPoint().getY() > (bird.getY() + bird.getRadio())) {
                collisionTime=(wall.getFirstPoint().getY() - bird.getRadio() - bird.getY()) / bird.getVy();
                double xPosition=bird.getX()+bird.getVx()*collisionTime;
                return wall.getFirstPoint().getX()< wall.getSecondPoint().getX()?(xPosition>wall.getFirstPoint().getX() && xPosition<wall.getSecondPoint().getX()?collisionTime:Double.MAX_VALUE-100000):(xPosition<wall.getFirstPoint().getX() && xPosition>wall.getSecondPoint().getX()?collisionTime:Double.MAX_VALUE-100000);
            } else return Double.MAX_VALUE-100000;

    }

    public void updateDCMOutput(){
        try{
            String output = "src/main/python/outputDCM" + L + ".txt";
            String dynamic = "src/main/java/main/dynamic.txt";
            String dynamicOutput = "src/main/java/main/dynamicOutput.txt";

            File fileOutput = new File(output);
            if (!fileOutput.exists()) {
                fileOutput.createNewFile();
            }

            // Parameter true to append to what the file already has
            FileWriter fwOutput = new FileWriter(fileOutput, true);
            BufferedWriter bwOutput = new BufferedWriter(fwOutput);

            BufferedReader reader = new BufferedReader(new FileReader(dynamic));
            BufferedReader readerInit = new BufferedReader(new FileReader(dynamicOutput));
            String linea;
            String lineaInit;

            double dcm=0;
            int count=0;
            String toWrite = "";
            while ((linea = reader.readLine()) != null && (lineaInit = readerInit.readLine()) != null) {
                // Dividir la cadena por espacios en blanco
                String[] partes = linea.split(" ");
                String[] partesInit = lineaInit.split(" ");

                if (partes.length >= 2) {
                    String r_x = partes[0];
                    String r_y = partes[1];
                    String r_x_init = partesInit[0];
                    String r_y_init = partesInit[1];

                    // Convertir las cadenas en números de punto flotante (double)
                    try {
                        double r_x_double = Double.parseDouble(r_x);
                        double r_y_double = Double.parseDouble(r_y);
                        double r_x_init_double = Double.parseDouble(r_x_init);
                        double r_y_init_double = Double.parseDouble(r_y_init);
                        double d_x = Math.pow(r_x_double-r_x_init_double, 2);
                        double d_y = Math.pow(r_y_double-r_y_init_double, 2);
                        dcm+=Math.sqrt(d_x + d_y);
                        count++;
                    } catch (NumberFormatException e) {
                        throw new RuntimeException(e);
                    }
                }else{
                    toWrite = linea;
                }
            }
            bwOutput.write(toWrite + " " + Double.toString(dcm/count) + "\n");

            // Cerrar archivos
            reader.close();
            readerInit.close();
            bwOutput.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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
        if(t < nextWritingTime){
            return;
        }
        nextWritingTime = t + writingPeriod;
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
            DecimalFormat df = new DecimalFormat("0.0000000", new DecimalFormatSymbols(locale));
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
        updateDCMOutput();
    }

    void addImpulseForL(){
        try {
            String dynamic = "src/main/python/impulse"+ L +".txt";
            File file = new File(dynamic);
            // Si el archivo no existe es creado
            if (!file.exists()) {
                file.createNewFile();
            }
            // Parameter false make us write stepping in the information
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("\nL = "+ L);
            bw.newLine(); // Agrega una nueva línea después de cada escritura
            bw.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    void addDCMForL(){
        try {
            String output = "src/main/python/outputDCM" + L + ".txt";
            File fileOutput = new File(output);
            if (!fileOutput.exists()) {
                fileOutput.createNewFile();
            }
            // Parameter false make us write stepping in the information
            FileWriter fw = new FileWriter(fileOutput, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("\nL = "+ L);
            bw.newLine(); // Agrega una nueva línea después de cada escritura
            bw.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    void addImpulse(double impulse_left, double impulse_right){
        Locale locale = new Locale("en", "US");
        try {
            String dynamic = "src/main/python/impulse"+ L +".txt";
            File file = new File(dynamic);
            // Si el archivo no existe es creado
            if (!file.exists()) {
                file.createNewFile();

            }
            // Parameter false make us write stepping in the information
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            StringBuilder info = new StringBuilder();
            DecimalFormat df = new DecimalFormat("0.0000", new DecimalFormatSymbols(locale));
            info.append(df.format(time)).append(" ").append(df.format(impulse_left)).append(" ").append(df.format(impulse_right));
            bw.write(info.toString());
            bw.newLine(); // Agrega una nueva línea después de cada escritura
            bw.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
