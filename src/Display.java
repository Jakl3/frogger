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

    public static int GRID = 50;
    public static int xERROR = 10;
    public static int yERROR = 16;
    public static int WIDTH;
    public static int HEIGHT;

    private boolean gameOver;

    private Frog frog;
    private List<Car> vehicles;
    private List<Turtle> turtles;
    private List<Log> logs;
    private BufferedImage img;
    private static final String backpath = "assets/others/background.png";
    private static final int numObs = 10;

    private DrawGrid grid;
    private DrawBackground bg;


    public Display(int _WIDTH, int _HEIGHT) {
        WIDTH = _WIDTH + xERROR;
        HEIGHT = _HEIGHT + yERROR;
        setBackground(Color.BLACK);

        img = null;
        try {
            img = ImageIO.read(new File(backpath));
        } catch (IOException ignored) {
        }
        assert img != null;

        grid = new DrawGrid(WIDTH, HEIGHT, 14, 20);
        bg = new DrawBackground();

        frog = new Frog(450, 600, 50, 50, "assets/frog");

        //logs
        logs = new ArrayList<>();
        int[] xcor = new int[]{800, 800};
        for (int x = 0; x < numObs; x++) {
            int ycor = (int) (Math.round(Math.random()));
            Log l = new Log(xcor[ycor], (ycor + 1) * 100, "assets/log", 2, -1);
            logs.add(l);

            int gap = (Math.random() > 0.5 ? 100 : 150);
//            System.out.println(xcor);
            xcor[ycor] += l.getWidth() + gap;
        }

        //turtles
        turtles = new ArrayList<>();
        xcor = new int[]{0, 0};
//        int dir = Math.round(Math.random()) > 0.5 ? 1 : -1;
        int dir = 1;
        for (int x = 0; x < numObs; x++) {
            int ycor = (int) (Math.round(Math.random()));
            Turtle t = new Turtle(xcor[ycor], -50 + (ycor + 1) * 100, "assets/turtle", 1, dir);
            turtles.add(t);
            int gap = (Math.random() > 0.5 ? 50 : 100);
//            System.out.println(xcor);
            xcor[ycor] += (t.getWidth() + gap) * dir;
        }

        //vehicles
        vehicles = new ArrayList<>();
        xcor = new int[]{800, 800, 800, 800, 800, 800};
        for (int x = 0; x < 18; x++) {
            int ycor = (int) (Math.random()*6);
            Car v = new Car(xcor[ycor], 250 + (ycor + 1) * 50, "assets/vehicle", 5, 1, 1);
            vehicles.add(v);
            int gap = (int) (Math.random() * 100 + 50);
//            System.out.println(v.fnamea + " " + v.getWidth() + " " + v.getHeight());
            xcor[ycor] -= (v.getWidth() + gap);
//            System.out.println(xcor);
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
        Graphics2D g2 = (Graphics2D) window;
//        System.out.println("" + frog.x + " " + frog.y);
        window.setColor(Color.BLACK);
        window.fillRect(0, 0, WIDTH, HEIGHT);

//        window.drawImage(img, 0, 0, WIDTH, HEIGHT, null);
        bg.paint(window);


        Rectangle water = new Rectangle(0, 50, WIDTH, 200);
//        g2.draw(water);

        //paint logs
        boolean good = false;
        for (Log log : logs) {
            log.paint(window);
            if (frog.getRect().intersects(log.getRect())) {
                frog.floating = true;
                frog.moveFrog(frog.getRect().x += log.getSpeed(), frog.getRect().y);
                good = true;
            }
            if (log.getX() < (-(log.getWidth() + 10))) {
                log.respawn(800 + log.getWidth(), log.getY());
            }
        }


        // paint turtles
        for (int x = 0; x < turtles.size(); x++) {
            Turtle turtle = turtles.get(x);
            turtle.movePos();
            turtle.paint(window);
            //System.out.println(turtle.getX());
            if (turtle.x > WIDTH) {
                //System.out.println("PASS");
                turtles.get(x).respawn(-10, turtle.y);
            }
            if (frog.getRect().intersects(turtle.getRect()) && !turtle.sink) {
                frog.floating = true;
                frog.moveFrog(frog.getRect().x += turtle.speed, frog.getRect().y);
                good = true;
            }
        }
        if (!good) {
            frog.floating = false;
            if (frog.getRect().intersects(water)) {
//                frog.moveFrog(SpawnX,SpawnY);
                gameOver = true;
            }
        }

        // paint vehicles
        for (Car vehicle : vehicles) {
            vehicle.paint(window);

            if (frog.getRect().intersects(vehicle.getRect())) {
//                frog.moveFrog(SpawnX, SpawnY);
                gameOver = true;
            }
            if (vehicle.getX() > WIDTH + vehicle.getWidth()) {
                vehicle.respawn(-(vehicle.getWidth() + 10), vehicle.getY());
            }
        }

        frog.render(window);

//        System.out.println(gameOver);

        if (gameOver) {
            window.setColor(Color.BLACK);
            window.fillRect(0, 0, WIDTH, HEIGHT);
        }



//        System.out.println(frog.getX() + " " + frog.getY());

        // Draw grid
//        grid.paint(window);
//
    }
}
