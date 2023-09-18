package main;

public class Vertix extends Bird{
    double tangC;
    double normC;

//        Por choque elastico
    public Vertix(int id, double x, double y) {
//        Es una particula quieta con masa infinita y radio 0
        super(id, 0, 1000000 ,0.00001);
        tangC = 1.0;
        normC = 1.0;
        this.setPosition(x,y);
    }

}
