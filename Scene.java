package guiSimulacija;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Scene extends Canvas implements Runnable {

    private final Simulation window;
    private final ArrayList<Figure> figures = new ArrayList<>();
    private boolean working = false;
    private final Thread thread;
    private int offset = 3;
    private final int sleepTime = 3;

    public Scene(Simulation simulation){
        window = simulation;
        setBackground(Color.GRAY);
        setPreferredSize(window.getSize());
        setFont(new Font("Comic Sans MS", Font.BOLD, 35));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                getParent().requestFocus();
                if(e.getButton() == MouseEvent.BUTTON1){
                    if(getWorking()) return;
                    addFigure(new Disk((new Vector(e.getX(), e.getY())), Vector.generateRandomVector()));
                }
            }
        });
        thread = new Thread(this);
        thread.start();
    }
    public void addFigure(Figure newFigure){
        int frameWidth = getWidth(), frameHeight = getHeight();
        boolean state = newFigure.getPositionVector().getX() + newFigure.getRadius() >= frameWidth ||
                        newFigure.getPositionVector().getX() - newFigure.getRadius() <= 0 ||
                        newFigure.getPositionVector().getY() + newFigure.getRadius() >= frameHeight ||
                        newFigure.getPositionVector().getY() - newFigure.getRadius() <= 0;
        if(state) return;

        for(Figure figure : figures){
            if(newFigure.isOverlapping(figure)) return;
        }
        figures.add(newFigure);
        Graphics g = getGraphics();
        if(g != null) newFigure.draw(getGraphics());
    }

    @Override
    public void paint(Graphics g) {
        if(g == null) return;
        drawFigures(g);
        if(!getWorking()) writePause(g);
        //setVisible(true);
    }

    private void writePause(Graphics g){
        g.setColor(Color.black);
        FontMetrics metrics = g.getFontMetrics();
        int xCoord = (getWidth() - metrics.stringWidth("PAUZA"))/2;
        int yCoord = (getHeight() - metrics.getHeight())/2 + metrics.getAscent();
        g.drawString("PAUZA", xCoord, yCoord);
    }

    private void drawFigures(Graphics g){ for(Figure f : figures) f.draw(g); }

    private void drawScene(){
        Graphics graphics = getGraphics();
        if(graphics == null) return;
        getGraphics().dispose();
        repaint();
    }

    private void calculateCollision(Figure f, Figure figure, Vector newDisplacement){
        double x = f.getDisplacementVector().getX() - figure.getDisplacementVector().getX();
        double y = f.getDisplacementVector().getY() - figure.getDisplacementVector().getY();
        Vector v1_v2 = new Vector(x, y);
        x = f.getPositionVector().getX() - figure.getPositionVector().getX();
        y = f.getPositionVector().getY() - figure.getPositionVector().getY();
        Vector x1_x2 = new Vector(x,y);
        double scalarProduct = v1_v2.getX()*x1_x2.getX() + v1_v2.getY()*x1_x2.getY();
        double magnitude = Math.pow(x1_x2.getX(), 2) + Math.pow(x1_x2.getY(), 2);
        scalarProduct = scalarProduct/magnitude;
        x *= scalarProduct;
        y *= scalarProduct;
        x1_x2.setX(x); x1_x2.setY(y);
        x = f.getDisplacementVector().getX();
        y = f.getDisplacementVector().getY();
        newDisplacement.setX(x- x1_x2.getX());
        newDisplacement.setY(y- x1_x2.getY());
    }

    private void moveFigures(){
        for(Figure figure : figures){
            // check collision with other figures
            for(Figure f : figures){
                if (f != figure && f.isOverlapping(figure)){
                    Vector newDisplacement = Vector.generateRandomVector();
                    Vector newDisplacement1 = Vector.generateRandomVector();
                    calculateCollision(f, figure, newDisplacement);
                    calculateCollision(figure, f, newDisplacement1);
                    f.getDisplacementVector().setX(newDisplacement.getX());
                    f.getDisplacementVector().setY(newDisplacement.getY());
                    figure.getDisplacementVector().setX(newDisplacement1.getX());
                    figure.getDisplacementVector().setY(newDisplacement1.getY());
                    break;
                }
            }
            // calculate new coordinates and check the wall collision
            double xCoord = figure.getPositionVector().getX();
            double yCoord = figure.getPositionVector().getY();
            Vector displacementVector = figure.getDisplacementVector();
            xCoord += offset * (displacementVector.unitVector().getX());
            yCoord += offset * (displacementVector.unitVector().getY());

            Vector position = new Vector(xCoord, yCoord);

            if(position.getX() + figure.getRadius() >= getWidth() || position.getX() - figure.getRadius() <=0){
                displacementVector.setX(-displacementVector.getX());
                return;
            }
            if(position.getY() + figure.getRadius() >= getHeight() || position.getY() - figure.getRadius() <=0){
                displacementVector.setY(-displacementVector.getY());
                return;
            }
            figure.getPositionVector().setX(xCoord);
            figure.getPositionVector().setY(yCoord);
        }
    }

    @Override
    public void run() {
        try{
            while(!Thread.interrupted()){
                synchronized (this) {
                    while (!getWorking()) {wait(); }
                }
                drawScene();
                Thread.sleep(sleepTime);
                moveFigures();
            }
        }catch (InterruptedException e){}
    }

    public synchronized void continueWorking() { working = true; notify();}
    public synchronized boolean getWorking() { return working;}
    public synchronized void pause() { working = false; drawScene();}
    public void interrupt() {thread.interrupt();}

}
