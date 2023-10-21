package main;

import java.util.List;

public class PeatonalSim {

    private final List<Pedestrian> respondingPedestrian;
    private final List<Pedestrian> unaffiliatedPedestrian;
    private double nextT;
    private double dTEscritura;
    private double dT;
    private double L;
    private int N;


    public PeatonalSim(List<Pedestrian> respondingPedestrian, List<Pedestrian> unaffiliatedPedestrian, double dTEscritura, double dT, int N){
        this.respondingPedestrian = respondingPedestrian;
        this.unaffiliatedPedestrian = unaffiliatedPedestrian;
        this.nextT = dTEscritura;
        this.dTEscritura = dTEscritura;
        this.dT = dT;
        this.L = L;
        this.N = N;
    }
}
