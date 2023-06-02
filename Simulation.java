package guiSimulacija;

import java.awt.Frame;
import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Simulation extends Frame {
    private Scene scene;

    private void exitProgram(){ dispose(); scene.interrupt();}

    private void addListenersAndPopulate(){

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_SPACE){
                    if(!scene.getWorking()) scene.continueWorking();
                    else scene.pause();
                }
                else if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
                    exitProgram();
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                scene.dispatchEvent(e);
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitProgram();
            }
        });
        add(scene, BorderLayout.CENTER);
    }

    public Simulation(){
        setBounds(700, 200,500 ,400);
        setResizable(false);
        setTitle("Simulacija");
        scene = new Scene(this);
        addListenersAndPopulate();
        setVisible(true);
    }

    public static void main(String[] args) {
        new Simulation();
    }
}
