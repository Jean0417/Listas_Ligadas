import java.awt.Color;
import java.awt.Graphics;

public class Rectangulo extends Trazo {
    public Rectangulo(int x1, int y1, int x2, int y2) {
        super(x1, y1, x2, y2);
    }

    @Override
    public void dibujar(Graphics g) {
        int ancho = Math.abs(x2 - x1);
        int alto = Math.abs(y2 - y1);
        int x = Math.min(x1, x2);
        int y = Math.min(y1, y2);

        g.setColor(Color.WHITE);  
        g.fillRect(x, y, ancho, alto);
    }

    @Override
    public boolean contains(int x, int y) {
        return x >= Math.min(x1, x2) && x <= Math.max(x1, x2)
                && y >= Math.min(y1, y2) && y <= Math.max(y1, y2);
    }
}
