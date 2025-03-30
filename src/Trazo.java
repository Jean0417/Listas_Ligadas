import java.awt.Graphics;
import java.io.Serializable;

public abstract class Trazo implements Serializable {
    int x1, y1, x2, y2;

    public Trazo(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public abstract void dibujar(Graphics g);
    
    // Método para verificar si el clic ocurrió dentro de la figura
    public abstract boolean contains(int x, int y);
}
