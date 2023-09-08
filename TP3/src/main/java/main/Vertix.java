package main;


import java.util.*;

public class Vertix extends Wall{
    double tangC;
    double normC;

//        Por choque elastico
    public Vertix(double x1, double y1,double x2,double y2) {
        super(x1,y1,x2,y2);
        tangC = 1.0;
        normC = 1.0;
    }
//        Por choque NO elastico (disipación de energía)
    public Vertix(double x1, double y1,double x2,double y2, double normC, double tangC) {
        super(x1,y1,x2,y2);
        this.tangC = tangC;
        this.normC = normC;
    }
// va = velocidad con al que viene la particula que choca
    public List<Double> vertixColision(double alpha, double va_x, double va_y){
        double vf_x =(-normC*Math.pow(Math.cos(alpha),2.0) + tangC*Math.pow(Math.sin(alpha),2.0))*va_x
                + (-(normC + tangC)*Math.sin(alpha)*Math.cos(alpha))*va_y;

        double vf_y =-(normC + tangC)*Math.sin(alpha)*Math.cos(alpha)*va_x
                + ((-normC*Math.pow(Math.sin(alpha),2.0) + tangC*Math.pow(Math.cos(alpha),2.0)))*va_y;

        List<Double> result = new ArrayList<>();
        result.add(vf_x);
        result.add(vf_y);
        return result;
    }
}
