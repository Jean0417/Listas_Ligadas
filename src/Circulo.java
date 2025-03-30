import java.awt.Color;
import java.awt.Graphics;

public class Circulo extends Trazo {
    public Circulo(int x1, int y1, int x2, int y2) {
        super(x1, y1, x2, y2);
    }

    @Override
    public void dibujar(Graphics g) {
        int radio = (int) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        g.setColor(Color.WHITE); 
        g.fillOval(x1 - radio, y1 - radio, radio * 2, radio * 2);
    }

    @Override
    public boolean contains(int x, int y) {
        int radio = (int) Math.hypot(x2 - x1, y2 - y1);
        return Math.pow(x - x1, 2) + Math.pow(y - y1, 2) <= Math.pow(radio, 2);
    }
}
