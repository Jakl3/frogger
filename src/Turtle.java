import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Turtle extends Canvas {
    public int x, y;
    public double scale;
    public String path;
    public final String[] filenames;
    private Rectangle collision;
    private int degrees;
    public int pic_idx;
    private boolean pics_left;
    private BufferedImage first_image;
    private int dir;
    private boolean sink;

    public Turtle(int _x, int _y, String _path, double _scale) {
        x = _x;
        y = _y;
        scale = _scale;
        path = _path;
        collision = new Rectangle(x, y, 1, 1);
        degrees = 180;
        pic_idx = 0;
        pics_left = true;
        sink = false;
        dir = Math.round(Math.random()) > 0.5 ? 1 : -1;


        File f = new File(_path);
        String[] fnames = f.list();
        assert fnames != null;

        filenames = fnames;

        first_image = null;
        try {
            first_image = ImageIO.read(new File(path + "/" + filenames[0]));
        } catch (IOException ignored) { }
        assert first_image != null;
    }

    public void movePos() {
        moveHoriz(dir);
        collision.setLocation(x,y);
        pic_idx += 1;
        if (pic_idx >= filenames.length*3) {
            pics_left = false;
            pic_idx = 0;
        }
    }

    public void moveHoriz(int dir) {
        // dir needs to be either 1 or -1, for r or l
        x = x + (dir * (62/filenames.length));
        if (dir != 0)
            degrees = dir == 1 ? 360 : 180;
        if (x > 60*16) x = 60*16;
        if (x < 0) x = 0;
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

    public void paint(Graphics window) {
        Graphics2D g2 = (Graphics2D) window;

        // Read in the image
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(path + "/" + filenames[pic_idx/3]));
        } catch (IOException ignored) { }
        assert image != null;

        if(pic_idx>=10){
            sink = false;
        }

        BufferedImage new_image = rotate(image);
        // Draw the image
        g2.drawImage(new_image, x, y, (int)(scale * new_image.getWidth(null)),
                (int)(scale * new_image.getHeight(null)),this);

        // Draw bounding box
//        g2.setColor(Color.RED);
//        BufferedImage new_first = rotate(first_image);
//        collision.setBounds(x, y, (int)(scale * new_first.getWidth(null)),
//                (int)(scale * new_first.getHeight(null)));
//        g2.draw(collision);
    }

    public Rectangle getRect(){

        return collision;

    }
}
