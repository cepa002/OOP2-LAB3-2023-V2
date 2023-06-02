package guiSimulacija;

import java.awt.Graphics;
import java.awt.Color;

public class Disk extends Figure{
    public Disk(Vector positionVector, Vector displacementVector) { super(positionVector, displacementVector);}

    public Disk(Vector positionVector, Vector displacementVector, double r) {
        super(positionVector, displacementVector, r);
    }
    @Override
    public Color getColor() {
        return Color.BLUE;
    }

    // x koordinata oblika cos(2kpi / 8) * radius + xCord ke[0,8)
    // y koordinata oblika sin(2kpi / 8) * radius + yCord ke[0,8)
    @Override
    public void draw(Graphics g) {
        g.setColor(getColor());
        int[] xCords = new int[8];
        int[] yCords = new int[8];
        double startX = getPositionVector().getX();
        double startY = getPositionVector().getY();
        double radius = getRadius();
        for(int k = 0; k < 8; k++){
            xCords[k] = (int)(Math.cos(k*Math.PI/4)*radius + startX);
            yCords[k] = (int)(Math.sin(k*Math.PI/4)*radius + startY);
        }
        g.fillPolygon(xCords, yCords, 8);
    }
}

//ZVEZDA

//    @Override
//    public void draw(Graphics g) {
//        g.setColor(Color.RED);
//        int[] xCords = new int[10];
//        int[] yCords = new int[10];
//        for(int k = 0; k < 10; k++){
//            double multiplier = k % 2 == 0 ? 1 : 0.4;
//            xCords[k] = (int)(Math.cos(Math.PI/10*3+k*Math.PI/5)*getRadius()*multiplier + getPositionVector().getX());
//            yCords[k] = (int)(Math.sin(Math.PI/10*3+k*Math.PI/5)*getRadius()*multiplier + getPositionVector().getY());
//        }
//        g.fillPolygon(xCords, yCords, 10);
//    }
