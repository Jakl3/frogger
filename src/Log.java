import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.*;
import java.io.File;
import java.util.Arrays;

public class Log extends Canvas {

    private int x,y,dir,speed;
    public double scale;
    private String path;
    private Rectangle collision;
    private BufferedImage img;
    private int width, height;


    public Log(int _x, int _y, String _path, int _speed, int _dir){
        //type  -> -1 means touching is bad 1 means touching is good
        x = _x;
        y = _y;
        path = _path;
        speed = _speed;
        dir = _dir;

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

        width = (int)(scale * img.getWidth(null));
        height = (int)(scale * img.getHeight(null));

        collision =  new Rectangle(x, y, width, height);
    }

    public void paint(Graphics window){
        Graphics2D g2 = (Graphics2D) window;
//        System.out.println("" + path + " " + )
//        System.out.println("" + img.getWidth(null) + " " + img.getHeight(null));
        g2.drawImage(img, x, y, width, height,this);
        g2.setColor(Color.GREEN);

        collision.setBounds(x, y, width, height);
        g2.draw(collision);
        this.changeX();

    }
    public void respawn(int _x , int _y){x = _x; y = _y; }
    public int getHeight(){
        return height;
    }
    public int getWidth(){ return width; }
    public Rectangle getRect(){
        return collision;
    }
    public int getX(){ return x; }
    public int getY(){ return y; }
    public void changeX(){
        x+=dir*speed;
    }
    public int getSpeed(){ return dir*speed; }

}
