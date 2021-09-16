import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DrawBackground extends Canvas {
    // Canvas width and height
    private BufferedImage bush, grass, lilybush, road, water;

    public DrawBackground() {
        try {
            bush = ImageIO.read(new File("assets/others/bush.png"));
        } catch (IOException ignored) { }
        assert bush != null;

        try {
            grass = ImageIO.read(new File("assets/others/grass.png"));
        } catch (IOException ignored) { }
        assert grass != null;

        try {
            lilybush = ImageIO.read(new File("assets/others/lilybush.png"));
        } catch (IOException ignored) { }
        assert lilybush != null;

        try {
            road = ImageIO.read(new File("assets/others/road.png"));
        } catch (IOException ignored) { }
        assert road != null;

        try {
            water = ImageIO.read(new File("assets/others/water.png"));
        } catch (IOException ignored) { }
        assert water != null;
    }

    public void paint(Graphics window) {
//        System.out.println(width + " " + height);
        Graphics2D g2 = (Graphics2D) window;
        g2.setColor(Color.WHITE);

        int row = 0;
        for (int i = 0; i < Display.WIDTH/50; i++) {
            if (i % 2 == 0) {
                g2.drawImage(lilybush, i * 50, row * 50, 50, 51, null);
            }
            else {
                g2.drawImage(bush, i * 50, row * 50, 50, 51, null);
            }
        }
        for (row=1; row < 5; row++) {
            for (int i = 0; i < Display.WIDTH/50; i++) {
                g2.drawImage(water, i * 50, row * 50, 50, 51, null);
            }
        }
        for (int i = 0; i < Display.WIDTH/50; i++) {
            g2.drawImage(grass, i * 50, row * 50, 50, 51, null);
        }
        row++;
        for (; row < 12; row++) {
            for (int i = 0; i < Display.WIDTH/50; i++) {
                g2.drawImage(road, i * 50, row * 50, 50, 50, null);
            }
        }
        for (; row < 14; row++) {
            for (int i = 0; i < Display.WIDTH/50; i++) {
                g2.drawImage(grass, i * 50, row * 50, 50, 51, null);
            }
        }
    }
}
