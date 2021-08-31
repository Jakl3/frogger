import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class FroggerGame extends JPanel implements Runnable, KeyListener {

    private Frog frog;
    private List<Obstacle> vehicles;
    private List<Obstacle> turtles;
    private List<Obstacle> logs;
    private HashMap<Integer, Boolean> keys;
    private HashMap<Integer, Boolean> released;

    public FroggerGame(int speed) {
        setBackground(Color.BLACK);

        keys = new HashMap<>();
        released = new HashMap<>();

        frog = new Frog(200, 200, "assets/frog", 0.5);
        logs = new ArrayList<>();
//        vehicles = new Obstacle("assets/vehicles", speed);
//        turtles = new Obstacle("assets/turtles", speed);
//        logs = new Obstacle("assets/logs", speed);

        addKeyListener(this);
        this.setFocusable(true);

        Thread t = new Thread(this);
        t.setPriority(Thread.MAX_PRIORITY);
        t.start();
    }

    @Override
    public void run() {
        System.out.println("Running");
        try {
            while (true) {
                Thread.sleep(3);
                repaint();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
//        keyPressed(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("Key code press: " + e.getKeyCode());
        int keycode = e.getKeyCode();
        if (!keys.containsKey(keycode)) {
            keys.put(keycode, false);
        }
        keys.put(keycode, true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println("Key code release: " + e.getKeyCode());
        int keycode = e.getKeyCode();
        keys.put(keycode, false);
        released.put(keycode, true);
    }

    public void paint(Graphics window) {
        window.setColor(Color.BLACK);
        window.fillRect(0, 0, 800, 600);
        window.setColor(Color.WHITE);

        if (check(37)) {
            frog.moveHoriz(-1);
            released.put(37, false);
        }
        else if (check(39)) {
            frog.moveHoriz(1);
            released.put(39, false);
        }
        else if (check(38)) {
            frog.moveVert(-1);
            released.put(38, false);
        }
        else if (check(40)) {
            frog.moveVert(1);
            released.put(40, false);
        }

        frog.paint(window);
        for (Obstacle log : logs) {
            log.paint(window);
        }

    }

    private boolean check(int kc) {
        return keys.get(kc) != null && keys.get(kc) &&
                released.get(kc) != null && released.get(kc);
    }
}
