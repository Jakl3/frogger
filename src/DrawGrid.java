import javax.swing.*;
import java.awt.*;

public class DrawGrid extends Canvas {
    // Canvas width and height
    private int width, height;
    private int rows, cols;
    private int gridSize;

    public DrawGrid(int w, int h, int r, int c) {
        this.width = w;
        this.height = h;
        this.rows = r;
        this.cols = c;
        this.gridSize = 50;
    }

    public void paint(Graphics window) {
//        System.out.println(width + " " + height);
        Graphics2D g2 = (Graphics2D) window;
        g2.setColor(Color.WHITE);

        int i;

        // draw the rows
//        int rowHt = height / (rows);
        for (i = 0; i < rows; i++)
            g2.drawLine(0, i * gridSize, width, i * gridSize);

        // draw the columns
//        int rowWid = width / (cols);
        for (i = 0; i < cols; i++)
            g2.drawLine(i * gridSize, 0, i * gridSize, height);
    }
}
