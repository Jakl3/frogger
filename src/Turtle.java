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
    private int dir;
    public boolean sink;
    private int width, height;
    public int speed;

    public Turtle(int _x, int _y, String _path, double _scale, int _dir) {
        x = _x;
        y = _y;
        scale = _scale;
        path = _path;
        collision = new Rectangle(x, y, 1, 1);
        degrees = 180;
        pic_idx = 0;
        pics_left = true;
        sink = false;
        dir = _dir;



        File f = new File(_path);
        String[] fnames = f.list();
        assert fnames != null;

        filenames = fnames;

        speed = (dir * (50/filenames.length));

        int max_height = -1, max_width = -1;


        for (String fname : filenames) {
            BufferedImage temp_img = null;
            try {
                temp_img = ImageIO.read(new File(path + "/" + fname));
            } catch (IOException ignored) { }
            assert temp_img != null;

            max_height = Math.max(max_height, temp_img.getHeight());
            max_width = Math.max(max_width, temp_img.getWidth());
        }

        scale = 50.0 / max_height;
        width = (int)(scale * max_width);
        height = (int)(scale * max_height);

        collision =  new Rectangle(x, y, width, height);
    }

    public void movePos() {
        moveHoriz();
        collision.setLocation(x,y);
        pic_idx += 1;
        if (pic_idx >= filenames.length*7) {
            pics_left = false;
            pic_idx = 0;
        }
    }

    public void moveHoriz() {
        // dir needs to be either 1 or -1, for r or l
        x = x + (dir * (50/filenames.length));
        if (dir != 0)
            degrees = dir == 1 ? 360 : 180;
//        if (x > 60*16) x = 60*16;
//        if (x < 0) x = 0;
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
            image = ImageIO.read(new File(path + "/" + filenames[pic_idx/7]));
        } catch (IOException ignored) { }
        assert image != null;

        if(pic_idx>=10*7){
            sink = true;
        }
        else {
            sink = false;
        }

        BufferedImage new_image = rotate(image);
        int n_width = (int)(scale * new_image.getWidth(null));
        int n_height = (int)(scale * new_image.getHeight(null));
        // Draw the image
        g2.drawImage(new_image, x, y+25-n_height/2, n_width, n_height,this);
//        g2.drawImage(new_image, x, y, width, height,this);

        // Draw bounding box
        g2.setColor(Color.RED);
        collision.setBounds(x, y+25-n_height/2, n_width, n_height);
//        g2.draw(collision);
    }

    public Rectangle getRect(){

        return collision;

    }
    public void respawn(int _x, int _y){
        x = _x;
        y = _y;
    }


}
