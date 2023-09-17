package main;


import java.util.*;

public class Vertix extends Bird{
    double tangC;
    double normC;

//        Por choque elastico
    public Vertix(int id, double x, double y) {
//        Es una particula quieta con masa infinita y radio 0
        super(id, 0, 1000000 ,0.0);
        tangC = 1.0;
        normC = 1.0;
        this.setPosition(x,y);
    }

// va = velocidad con al que viene la particula que choca
//    public List<Double> vertixColision(double alpha, double va_x, double va_y){
//        double vf_x =(-normC*Math.pow(Math.cos(alpha),2.0) + tangC*Math.pow(Math.sin(alpha),2.0))*va_x
//                + (-(normC + tangC)*Math.sin(alpha)*Math.cos(alpha))*va_y;
//
//        double vf_y =-(normC + tangC)*Math.sin(alpha)*Math.cos(alpha)*va_x
//                + ((-normC*Math.pow(Math.sin(alpha),2.0) + tangC*Math.pow(Math.cos(alpha),2.0)))*va_y;
//
//        List<Double> result = new ArrayList<>();
//        result.add(vf_x);
//        result.add(vf_y);
//        return result;
//    }
}
