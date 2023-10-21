package main;

import java.util.List;

public class Pedestrian extends Particle{
    private final List<Position> targetList;
    private Position nextTarget;

    public Pedestrian(int id, double radio, double M, List<Position> targetList) {
        super(id, radio, M);
        this.targetList = targetList;
        this.nextTarget = targetList.get(0);
    }



}
