import java.awt.Color;
import java.awt.Graphics;

public class Linea extends Trazo {
    public Linea(int x1, int y1, int x2, int y2) {
        super(x1, y1, x2, y2);
    }

    @Override
    public void dibujar(Graphics g) {
        g.setColor(Color.WHITE);  // Color blanco para la línea
        g.drawLine(x1, y1, x2, y2);
    }
    
    @Override
    public boolean contains(int x, int y) {
        // Para simplificar, verificamos si el clic ocurrió cerca de la línea (en un rango tolerante)
        return Math.abs((x - x1) * (y - y2) - (x - x2) * (y - y1)) < 10;
    }
}
