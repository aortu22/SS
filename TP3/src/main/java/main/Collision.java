package main;

public class Collision {
    private Bird bird1;
    private Bird bird2;
    private Wall wall;

    private Vertix vertix;
    private double collisionTime;
    private boolean wallCollision = false;
    private boolean vertixCollision = false;


    public Bird getBird1() {
        return bird1;
    }

    public Bird getBird2() {
        return bird2;
    }

    public Wall getWall() {
        return wall;
    }

    public Vertix getVertix() {
        return vertix;
    }

    public double getCollisionTime() {
        return collisionTime;
    }

    public void setCollisionTime(double collisionTime) {
        this.collisionTime = collisionTime;
    }

    public boolean isWallCollision() {
        return wallCollision;
    }
    public boolean isVertixCollision() {
        return vertixCollision;
    }


    public Collision(Bird bird1, Wall wall) {
        this.bird1 = bird1;
        this.wall = wall;
        this.bird2=null;
        this.wallCollision=true;
        this.collisionTime=Double.MAX_VALUE-100000;
    }
    public Collision(Bird bird1, Bird bird2, boolean vertixCollision) {
        this.bird1 = bird1;
        this.bird2=bird2;
        this.wall=null;
        this.wallCollision=false;
        this.vertixCollision = vertixCollision;
        this.collisionTime=Double.MAX_VALUE-100000;
    }
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Collision)){
            return false;
        }
        Collision other=(Collision) obj;
        return this.wall==other.getWall() && other.getBird1()==this.getBird1() && this.getBird2()==other.getBird2();
    }
}
