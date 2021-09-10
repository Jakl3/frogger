import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.List;
import java.io.*;
import javax.imageio.*;
import java.awt.image.BufferedImage;

public class Display extends JPanel implements Runnable {

    public static int GRID=50;
    public static int xERROR=10;
    public static int yERROR=16;
    public static int WIDTH;
    public static int HEIGHT;

    public enum STATE {
        MENU,
        GAME,
        INSTRUCTIONS
    };
    public static STATE state=STATE.MENU;

    private Frog frog;
    private List<Car> vehicles;
    private List<Turtle> turtles;
    private List<Log> logs;
    private BufferedImage img;
    private static final String backpath = "assets/background.png";
    private static final int numObs = 100;

    private DrawGrid grid;


    public Display(int _WIDTH, int _HEIGHT) {
        WIDTH = _WIDTH + xERROR;
        HEIGHT = _HEIGHT + yERROR;
        setBackground(Color.BLACK);
        img = null;
        try {
            img = ImageIO.read(new File(backpath));
        } catch (IOException ignored) { }
        assert img != null;

        grid = new DrawGrid(WIDTH, HEIGHT, 14, 20, 50);

        frog = new Frog(0, 0, 50, 50, "assets/frog");

        //logs
        logs = new ArrayList<>();
        int xcor = 800;
        for(int x = 0;x<numObs;x++){
            Log l = new Log(xcor,200,"assets/log",2,-1,1);
            logs.add(l);
            int gap = (int)(Math.random()*100+30);
            xcor+= l.scale * l.getWidth()+gap;
        }

        //turtles
        turtles = new ArrayList<>();
        xcor = 0;
        for(int x = 0;x<20;x++){
            Turtle t = new Turtle(xcor,400,"assets/turtle", 1);
            turtles.add(t);
            xcor-= 100;
        }

        //vehicles
        vehicles = new ArrayList<>();
        xcor = 0;
        for(int x = 0;x<numObs;x++){
            Car v = new Car(xcor,600,1, "assets/vehicle",5,1,1);
            vehicles.add(v);
            int gap = (int)(Math.random()*100+50);
            System.out.println(v.fnamea + " " + v.getWidth() + " " + v.getHeight());
            xcor-=(v.getWidth()+gap);
            System.out.println(xcor);
        }


        addKeyListener(frog);
        this.setFocusable(true);

        Thread t = new Thread(this);
        t.setPriority(Thread.MAX_PRIORITY);
        t.start();
    }

    @Override
    public void run() {
        System.out.println("Running");
        while (true) {
            try {
                Thread.sleep(25);
                repaint();
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
    }

    public void paint(Graphics window) {
//        System.out.println("" + frog.x + " " + frog.y);
        window.setColor(Color.BLACK);
        window.fillRect(0, 0, WIDTH, HEIGHT);

        //paint logs
        for (Log log : logs) {
            log.paint(window);

        }
        // paint turtles
        for (int x = 0;x<turtles.size();x++) {
            Turtle turtle = turtles.get(x);
            turtle.movePos();
            turtle.paint(window);
//            System.out.println(turtle.getX());
//            if(turtle.getX() > FRAME_WIDTH){
//                turtles.remove(x);
//                x--;
//            }
            if(frog.getRect().intersects(turtle.getRect())){
                frog.move(turtle.getX(),turtle.getY());
            }
        }
        // paint vehicles
        for (Car vehicle : vehicles) {
            vehicle.paint(window);

            if(frog.getRect().intersects(vehicle.getRect())){
                frog.move(400, 400);
            }
        }

        frog.render(window);

        System.out.println(frog.getX() + " " + frog.getY());

        // Draw grid
        grid.paint(window);
//
    }
}
