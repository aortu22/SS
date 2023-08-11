package main;

public class AdjacentCell {
    Cell cell;
    double correctionX;
    double correctionY;

    public AdjacentCell(Cell cell, double correctionX, double correctionY) {
        this.cell = cell;
        this.correctionX = correctionX;
        this.correctionY = correctionY;
    }

    public Cell getCell() {
        return cell;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    public double getCorrectionX() {
        return correctionX;
    }

    public void setCorrectionX(double correctionX) {
        this.correctionX = correctionX;
    }

    public double getCorrectionY() {
        return correctionY;
    }

    public void setCorrectionY(double correctionY) {
        this.correctionY = correctionY;
    }
}
