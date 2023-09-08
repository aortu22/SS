package main;

import java.util.ArrayList;
import java.util.List;

public class Wall {
    private final Position firstPoint;
    private final Position secondPoint;
    private final boolean horizontal;
    private final List<Collision> collisionList;

    public Wall(double x1, double y1,double x2,double y2) {
        this.firstPoint = new Position(x1,y1);
        this.secondPoint = new Position(x2,y2);
        if(y1==y2){
            this.horizontal=true;
        }else if(x1==x2){
            this.horizontal=false;
        }else throw new IllegalArgumentException();
        this.collisionList=new ArrayList<>();
    }

    public Position getFirstPoint() {
        return firstPoint;
    }

    public Position getSecondPoint() {
        return secondPoint;
    }

    public List<Collision> getCollisionList() {
        return collisionList;
    }

    public boolean isHorizontal() {
        return horizontal;
    }
}
