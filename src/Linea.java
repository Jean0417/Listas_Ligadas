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
        double distancia = distanciaPuntoALinea(x, y, x1, y1, x2, y2);
        return distancia <= 5;  // Tenia problemas para seleccionar la linea, por eso le agregue este lumbral de tolerancia
    }

    private double distanciaPuntoALinea(int px, int py, int x1, int y1, int x2, int y2) {
        double numerador = Math.abs((x2 - x1) * (y1 - py) - (x1 - px) * (y2 - y1));
        double denominador = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        return numerador / denominador;
    }
}

