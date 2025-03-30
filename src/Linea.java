import java.awt.Color;
import java.awt.Graphics;

public class Linea extends Trazo {
    public Linea(int x1, int y1, int x2, int y2) {
        super(x1, y1, x2, y2);
    }

    @Override
    public void dibujar(Graphics g) {
        g.setColor(Color.WHITE);  
        g.drawLine(x1, y1, x2, y2);
    }
    
    @Override
    public boolean contains(int x, int y) {
        return Math.abs((x - x1) * (y - y2) - (x - x2) * (y - y1)) < 10;
    }
}
