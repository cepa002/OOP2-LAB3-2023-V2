package guiSimulacija;

import java.awt.Color;
import java.awt.Graphics;


public abstract class Figure {

    private Vector positionVector, displacementVector;

    private double radius;

    public Figure (Vector positionVector, Vector displacementVector){
        this(positionVector, displacementVector, 20);
    }

    public Figure (Vector positionVector, Vector displacementVector, double r){
        this.positionVector = positionVector;
        this.displacementVector = displacementVector;
        this.radius = r;
    }

    private double distance(Vector v){
        return Math.sqrt(Math.pow(v.getX()-positionVector.getX(), 2) + Math.pow(v.getY()-positionVector.getY(), 2));
    }

    public boolean isVectorInside(Vector vector) {
        return distance(vector) <= radius;
    }

    public boolean isOverlapping(Figure figure){
        return distance(figure.positionVector) <= radius + figure.radius;
    }

    public abstract Color getColor();

    public abstract void draw(Graphics g);

    public Vector getPositionVector() {
        return positionVector;
    }

    public Vector getDisplacementVector() {
        return displacementVector;
    }

    public double getRadius() {
        return radius;
    }
}
