import java.awt.*;
import java.io.File;

public class Obstacle {
private int x,y,w,h,speed;
public Image[] imgs;

    public Obstacle(String _path, int speed){
        File f = new File(_path);
        String[] filenames = f.list();
        assert filenames != null;
        imgs = new Image[filenames.length];
        this.speed = speed;
        for(int x = 0;x<filenames.length;x++){
        Image img  = Toolkit.getDefaultToolkit().getImage(filenames[x]);
        imgs[x] = img;
        }
    }

    public void paint(Graphics window){

//        window.draw(x,y,w,h);

    }

    public void changeX(){
        x-=speed;
    }
}
