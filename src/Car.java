import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.*;
import java.io.File;
import java.util.Arrays;

public class Car extends Canvas {

    private int x,y,dir,type;
    private double scale,speed;
    private String path;
    private Rectangle collision;
    private BufferedImage img;

    public Car(int _x, int _y, String _path, double _speed, int _dir, int _type){
        //type  -> -1 means touching is bad 1 means touching is good
        x = _x;
        y = _y;
        path = _path;
        speed = _speed;
        dir = _dir;
        type = _type;

        File f = new File(_path);
        String[] filenames = f.list();
        assert filenames != null;

        int rand = (int) (Math.random()*filenames.length);
        img = null;
        try {
            img = ImageIO.read(new File(path + "/" + filenames[rand]));
        } catch (IOException ignored) {
        }
        assert img != null;

        scale = 50.0 / img.getHeight();

        collision =  new Rectangle(x, y, (int)(scale * img.getWidth(null)), (int)(scale * img.getHeight(null)));
    }

    public int getType(){
        return type;
    }

    public void paint(Graphics window){
        Graphics2D g2 = (Graphics2D) window;
//        System.out.println("" + path + " " + )

//        System.out.println("" + img.getWidth(null) + " " + img.getHeight(null));
        collision.setBounds(x, y, (int)(scale * img.getWidth(null)), (int)(scale * img.getHeight(null)));
        g2.drawImage(img, x, y, (int)(scale * img.getWidth(null)),
                (int)(scale * img.getHeight(null)),this);
        g2.setColor(Color.GREEN);
        //g2.draw(collision);
        this.changeX();

    }
    public int getHeight(){
        return (int)(scale*img.getHeight(null));
    }
    public int getWidth(){
        return (int)(scale*img.getWidth(null));
    }
    public Rectangle getRect(){
        return collision;
    }
    public int getX(){ return x;}
    public int getY(){ return y;}
    public void changeX(){
        x+=dir*speed;
    }
    public void respawn(int _x, int _y){ x = _x; y = _y; }
}
