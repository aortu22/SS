package main;

public class Position {
    private double x;
    private double y;
    public Position(double x,double y){
        this.x = x;
        this.y = y;
    }
    public void setPosition(double x,double y){
        this.x = x;
        this.y = y;
    }
    public void setX(double x){
        this.x=x;
    }
    public void setY(double y){
        this.y=y;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public double calculateDistance(Position otherPosition){
        return Math.sqrt(Math.pow(x - otherPosition.x,2) + Math.pow(y - otherPosition.y,2));
    }


}
