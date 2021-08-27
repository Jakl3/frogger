import java.awt.*;
import java.io.*;
import java.util.*;

public class Frog extends Canvas {
    public int x, y;
    public double scale;
    public String path;
    private final int speed;
    private final String[] filenames;

    private int prevDir;

    public Frog(int _x, int _y, String _path, double _scale) {
        x = _x;
        y = _y;
        scale = _scale;
        speed = 30;
        path = _path;
        prevDir = 0;

        File f = new File(_path);
        String[] fnames = f.list();
        assert fnames != null;

        filenames = fnames;
    }

    public void moveHoriz(int dir) {
        // dir needs to be either 1 or -1, for r or l
        x = x + (dir * speed);
        prevDir = dir == 1 ? 2 : 1;
    }

    public void moveVert(int dir) {
        // dir needs to be either 1 or -1, for u or d
        y = y + (dir * speed);
        prevDir = dir == 1 ? 3 : 0;
    }

    public void paint(Graphics window) {
        Graphics2D g2 = (Graphics2D) window;

        Image img = Toolkit.getDefaultToolkit().getImage(path + "/" + filenames[prevDir]);
        g2.drawImage(img, x, y, (int)(scale * img.getWidth(null)), (int)(scale * img.getHeight(null)),this);
    }

}
