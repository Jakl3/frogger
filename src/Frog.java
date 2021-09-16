import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Frog extends JPanel implements KeyListener {
    private int degrees;
    public int pic_idx;
    private final String[] filenames;
    private String path;
    private Rectangle frog;
    private BufferedImage image;
    public boolean picsLeft;
    public boolean floating;

    private long[] pressed; // long[0] = keycode, long[1] = time
    private final HashMap<Integer, int[]> directions;

    private int origX, origY;

    public Frog(int _x, int _y, int _w, int _h, String _path) {

        // create direction map: {keycode, [dirx, diry]}
        directions = new HashMap<>();
        directions.put(37, new int[] {-1, 0});
        directions.put(39, new int[] {1, 0});
        directions.put(38, new int[] {0, -1});
        directions.put(40, new int[] {0, 1});

        origX = _x;
        origY = _y;

        path = _path;
        degrees = 180;
        pic_idx = 0;
        picsLeft = true;

        frog = new Rectangle(_x, _y, _w, _h);

        File f = new File(_path);
        filenames = f.list();
    }

    public int getX() {
        return frog.x;
    }

    public int getY() {
        return frog.y;
    }


    public void render(Graphics window) {
        Graphics2D g2 = (Graphics2D) window;
//        System.out.println(floating);

        if (picsLeft && pic_idx == 0 && !floating) {
            int curr_x = (int)(Math.round(frog.x / 50.0) * 50);
            int curr_y = (int)(Math.round(frog.y / 50.0) * 50);
            frog.x = curr_x;
            frog.y = curr_y;
        }

        // paint frogs
        for (int key : directions.keySet()) {
            int[] dir = directions.get(key);
            if (pressed != null && pressed[0] == key) {
                if (picsLeft) {
                    movePos(dir[0], dir[1]);
                }
                else {
                    pic_idx = 0;
                    picsLeft = true;
                    pressed = null;
                }
            }
        }

        image = read();
        g2.drawImage(image, frog.x, frog.y, null);
//        g2.setColor(Color.RED);
//        g2.draw(frog);
    }

    public void movePos(int dirX, int dirY) {
        moveHoriz(dirX);
        moveVert(dirY);
        pic_idx += 1;
        if (pic_idx >= filenames.length) {
            pic_idx = 0;
            picsLeft = false;
        }
    }

    public void moveHoriz(int dir) {
        // dir needs to be either 1 or -1, for r or l
        frog.x = frog.x + (dir * (Display.GRID/filenames.length));
        if (dir != 0)
            degrees = -1 * dir * 90;
    }

    public void moveVert(int dir) {
        // dir needs to be either -1 or 1, for u or d
        frog.y = frog.y + (dir * (Display.GRID/filenames.length));
        if (dir != 0)
            degrees = dir == 1 ? 360 : 180;
    }

    public BufferedImage read() {
        BufferedImage readImg = null;
        try {
            readImg = ImageIO.read(new File(path + "/" + filenames[pic_idx]));
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert readImg != null;
        return rotate(resize(readImg, frog.width, frog.height));
    }

    private BufferedImage rotate(BufferedImage image) {
        // Rotate the image to the right direction
        final double rads = Math.toRadians(degrees);
        final double sin = Math.abs(Math.sin(rads));
        final double cos = Math.abs(Math.cos(rads));
        final int w = (int) Math.floor(image.getWidth(null) * cos + image.getHeight(null) * sin);
        final int h = (int) Math.floor(image.getHeight(null) * cos + image.getWidth(null) * sin);
        final BufferedImage rotatedImage = new BufferedImage(w, h, image.getType());
        final AffineTransform at = new AffineTransform();
        at.translate(w / 2.0, h / 2.0);
        at.rotate(rads,0, 0);
        at.translate(-image.getWidth(null) / 2.0, -image.getHeight(null) / 2.0);
        final AffineTransformOp rotateOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        return rotateOp.filter(image, rotatedImage);
    }

    public BufferedImage resize(BufferedImage inputImage, int scaledWidth, int scaledHeight) {
        BufferedImage outputImage = new BufferedImage(scaledWidth,
                scaledHeight, inputImage.getType());
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();
        return outputImage;
    }

    public Rectangle getRect() {
        return frog;
    }
    public void moveFrog( int _x, int _y){
        frog.x = _x;
        frog.y = _y;
    }

    @Override
    public void keyTyped(KeyEvent e) {
//        keyPressed(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keycode = e.getKeyCode();
        if (pressed == null) {
            if (directions.containsKey(keycode))
                pressed = new long[] {keycode, System.currentTimeMillis()};
        }
    }

    @Override
    public void keyReleased(KeyEvent e) { }
}
