import java.awt.Graphics;

public class ListaLigada {
    Nodo cabeza;

    public ListaLigada() {
        cabeza = null;
    }

    public void agregar(Trazo trazo) {
        Nodo nuevoNodo = new Nodo(trazo);
        if (cabeza == null) {
            cabeza = nuevoNodo;
        } else {
            Nodo actual = cabeza;
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            actual.siguiente = nuevoNodo;
        }
    }

    public void eliminar(Trazo trazo) {
        if (cabeza == null) return;

        if (cabeza.trazo.equals(trazo)) {
            cabeza = cabeza.siguiente;
            return;
        }

        Nodo actual = cabeza;
        while (actual.siguiente != null) {
            if (actual.siguiente.trazo.equals(trazo)) {
                actual.siguiente = actual.siguiente.siguiente;
                return;
            }
            actual = actual.siguiente;
        }
    }

    public void dibujar(Graphics g) {
        Nodo actual = cabeza;
        while (actual != null) {
            actual.trazo.dibujar(g);
            actual = actual.siguiente;
        }
    }
}
