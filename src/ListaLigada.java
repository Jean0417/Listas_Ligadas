import java.awt.Graphics;

public class ListaLigada {
    Nodo cabeza;

    public ListaLigada() {
        cabeza = null;
    }

    // Método para agregar un trazo al final de la lista
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

    // Método para eliminar un trazo de la lista
    public void eliminar(Trazo trazo) {
        if (cabeza == null) return;

        // Si el trazo a eliminar es el primero
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

    // Método para recorrer la lista y dibujar los trazos
    public void dibujar(Graphics g) {
        Nodo actual = cabeza;
        while (actual != null) {
            actual.trazo.dibujar(g);
            actual = actual.siguiente;
        }
    }
}
