package guiSimulacija;

public class Vector {

    private double x;
    private double y;

    public Vector(double x, double y){
        this.x = x;
        this.y = y;

    }
    public double getX() { return x;}
    public double getY() { return y;}
    public void setX(double x) { this.x = x;}
    public void setY(double y) { this.y = y;}

    public Vector unitVector(){
        double magnitude = Math.sqrt(Math.pow(x,2) + Math.pow(y,2));
        if(magnitude == 0) return null;
        return new Vector(x/magnitude, y/magnitude);
    }

    public static Vector generateRandomVector(){
        double newX, newY;
        double max = 1, min = -1;
        do {
            newX = Math.random() * (max - min) + min;
            newY = Math.random() * (max - min) + min;
        } while (newX == 0 && newY == 0);

        return new Vector(newX,newY);
    }

}
