import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.List;
import java.io.*;
import javax.imageio.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicBoolean;

public class Display extends JPanel implements Runnable {

    public static int GRID = 50;
    public static int xERROR = 10;
    public static int yERROR = 16;
    public static int WIDTH;
    public static int HEIGHT;

    private final AtomicBoolean running = new AtomicBoolean(false);

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
        int[] x_cor = new int[]{1000, 800};
        for (int x = 0; x < 20; x++) {
            int y_cor = (int) (Math.round(Math.random()));
            Log l = new Log(x_cor[y_cor], (y_cor + 1) * 100, "assets/log", 2, -1);
            logs.add(l);

            int gap = (Math.random() > 0.5 ? 150 : 250);
//            System.out.println(x_cor);
            x_cor[y_cor] += l.getWidth() + gap;
        }

        //turtles
        turtles = new ArrayList<>();
        x_cor = new int[]{-200, 0};
//        int dir = Math.round(Math.random()) > 0.5 ? 1 : -1;
        int dir = 1;
        for (int x = 0; x < numObs; x++) {
            int y_cor = (int) (Math.round(Math.random()));
            Turtle t = new Turtle(x_cor[y_cor], -50 + (y_cor + 1) * 100, "assets/turtle", 1, dir);
            turtles.add(t);
            int gap = (Math.random() > 0.5 ? 50 : 100);
            x_cor[y_cor] += (t.width + gap) * dir;
        }

        //vehicles
        vehicles = new ArrayList<>();
        x_cor = new int[]{800, 800, 800, 800, 800, 800};
        for (int x = 0; x < 11; x++) {
            int y_cor = (int) (Math.random()*6);
            Car v = new Car(x_cor[y_cor], 250 + (y_cor + 1) * 50, "assets/vehicle", 4, 1, 1);
            vehicles.add(v);
            int gap = (int) (Math.random() * 150 + 100);
            x_cor[y_cor] -= (v.getWidth() + gap);
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
        running.set(true);
        while (running.get()) {
            try {
                Thread.sleep(25);
                repaint();
            } catch (Exception e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }

    public void stop() {
        running.set(false);
    }

    public void paint(Graphics window) {
        Graphics2D g2 = (Graphics2D) window;
        window.setColor(Color.BLACK);
        window.fillRect(0, 0, WIDTH, HEIGHT);

//        window.drawImage(img, 0, 0, WIDTH, HEIGHT, null);
        bg.paint(window);


        Rectangle water = new Rectangle(0, 55, WIDTH, 250-55);
//        g2.setColor(Color.RED);
//        g2.draw(water);

        //paint logs
        boolean good = false;
        for (int x = 0;x<logs.size();x++) {
            Log log = logs.get(x);
            for(int y = 0;y< logs.size();y++){
                if( y!=x&&log.getRect().intersects(logs.get(y).getRect())){
                    log.respawn(800+(log.getWidth()*(int)(Math.random()*12+2)), log.getY());
                    System.out.println(log.getX());
                }
            }
            log.paint(window);
            if (frog.getRect().intersects(log.getRect())) {
                frog.floating = true;
                frog.moveFrog(frog.getRect().x += log.getSpeed(), frog.getRect().y);
                good = true;
            }
            if (log.getX() < (-(log.getWidth() + 20))) {
                log.respawn(800+(log.getWidth()*(int)(Math.random()*12+2)), log.getY());
            }
        }


        // paint turtles
        boolean movin = false;
        for (Turtle turtle : turtles) {
            turtle.movePos();
            turtle.paint(window);
            //System.out.println(turtle.getX());
            if (turtle.x > WIDTH) {
                //System.out.println("PASS");
                turtle.respawn(-(turtle.getWidth() + 50), turtle.y);
            }
            if (frog.getRect().intersects(turtle.getRect()) && !turtle.sink) {
                frog.floating = true;
                if (!movin) {
                    frog.moveFrog(frog.getRect().x += turtle.speed, frog.getRect().y);
                    movin = true;
                }
                good = true;
            } else {
                movin = false;
            }
        }
        if (!good) {
            frog.floating = false;
            if (frog.getRect().intersects(water)) {
//                frog.moveFrog(SpawnX,SpawnY);
                gameOver = true;
            }
        }
//        System.out.println(turtles.get(0).pic_idx);

        // paint vehicles
        for (int x = 0;x<vehicles.size();x++) {
            Car vehicle = vehicles.get(x);
            for(int y = 0;y< vehicles.size();y++){
                if( y!=x&&vehicle.getRect().intersects(vehicles.get(y).getRect())){
                    vehicle.respawn(-(vehicle.getWidth()*(int)(Math.random()*4+2)), vehicle.getY());
                }
            }
            vehicle.paint(window);
            if (frog.getRect().intersects(vehicle.getRect())) {
//                frog.moveFrog(SpawnX, SpawnY);
                gameOver = true;
            }
            if (vehicle.getX() > WIDTH + vehicle.getWidth()) {
                vehicle.respawn(-(vehicle.getWidth()*(int)(Math.random()*4+2)), vehicle.getY());
            }
        }

        frog.render(window);

//        System.out.println(gameOver);


        if(frog.getY()<20){
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            window.setColor(Color.BLACK);
            window.fillRect(0, 0, WIDTH, HEIGHT);
            window.setColor(Color.GREEN);
            window.setFont(new Font("Monospaced",Font.BOLD,100));
            window.drawString("YOU WIN!",WIDTH/4,HEIGHT/2);
            stop();
        }
        else if (gameOver) {
            window.setColor(Color.BLACK);
            window.fillRect(0, 0, WIDTH, HEIGHT);
            window.setColor(Color.RED);
            window.setFont(new Font("Monospaced",Font.BOLD,100));
            window.drawString("GAME OVER!",WIDTH/5,HEIGHT/2);
            stop();
        }



//        System.out.println(frog.getX() + " " + frog.getY());

        // Draw grid
//        grid.paint(window);
//
    }
}
