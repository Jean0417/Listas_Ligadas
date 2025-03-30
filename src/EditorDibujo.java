import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;

public class EditorDibujo extends JFrame {
    private ListaLigada trazos = new ListaLigada();  
    private String figuraSeleccionada; 
    private JPanel panelDibujo;
    private int startX, startY, endX, endY;  // Coordenadas de inicio y fin de la figura
    private boolean isDragging = false;  // Indica si el usuario está arrastrando para definir el tamaño
    private Trazo figuraEnProceso = null; // Figura que está siendo dibujada
    private Trazo figuraSeleccionadaPanel = null; // Figura que se selecciona al hacer clic en el panel
    private boolean modoSeleccion = false; // Estado para saber si estamos en modo selección

    public EditorDibujo() {
        setTitle("Editor de Dibujos Vectoriales");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Estilo para la ventana
        getContentPane().setBackground(new Color(250, 250, 250));  // Fondo gris claro

        // Panel de dibujo con bordes y fondo azul cielo
        panelDibujo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Dibujar todos los trazos en el panel usando la lista ligada
                trazos.dibujar(g);
                // Si está arrastrando, dibuja la figura en proceso
                if (figuraEnProceso != null) {
                    figuraEnProceso.dibujar(g);
                }
                // Si una figura está seleccionada, dibujarla de alguna forma destacada
                if (figuraSeleccionadaPanel != null) {
                    g.setColor(Color.RED); // Cambiar color a rojo para la figura seleccionada
                    figuraSeleccionadaPanel.dibujar(g);
                }
            }
        };
        panelDibujo.setBackground(new Color(191, 218, 255));  // Fondo azul cielo suave
        panelDibujo.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));  // Borde negro
        panelDibujo.setPreferredSize(new Dimension(700, 500));
        panelDibujo.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (modoSeleccion) {
                    startX = e.getX();
                    startY = e.getY();
                    // Verificar si la figura que se hizo clic está seleccionada
                    Nodo actual = trazos.cabeza;
                    while (actual != null) {
                        if (actual.trazo.contains(startX, startY)) {
                            figuraSeleccionadaPanel = actual.trazo;
                            JOptionPane.showMessageDialog(panelDibujo, "Figura seleccionada: " + actual.trazo.getClass().getSimpleName());
                            return;
                        }
                        actual = actual.siguiente;
                    }
                } else {
                    startX = e.getX();
                    startY = e.getY();
                    isDragging = true;
                }
            }

            public void mouseReleased(MouseEvent e) {
                endX = e.getX();
                endY = e.getY();
                if (!modoSeleccion && isDragging) {
                    Trazo trazoFinal = crearFigura(startX, startY, endX, endY);
                    if (trazoFinal != null) {
                        trazos.agregar(trazoFinal);  // Usamos el método agregar de la lista ligada
                        panelDibujo.repaint();  // Redibujar el panel
                    }
                    isDragging = false;
                    figuraEnProceso = null;  // Finalizar el dibujo de la figura
                }
            }
        });

        panelDibujo.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                endX = e.getX();
                endY = e.getY();
                if (!modoSeleccion) {
                    figuraEnProceso = crearFigura(startX, startY, endX, endY);
                    panelDibujo.repaint();  // Redibujar el panel mientras se arrastra
                }
            }
        });

        add(panelDibujo, BorderLayout.CENTER);

        // Panel de botones con fondo azul cielo suave
        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(new Color(191, 218, 255));  // Fondo azul cielo
        panelBotones.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));  // Alineación a la izquierda

        // Agregar los botones con imágenes y texto
        panelBotones.add(crearBotonConImagen("figuras.png", "Figura", e -> seleccionarFigura()));
        panelBotones.add(crearBotonConImagen("seleccionar.png", "Seleccionar", e -> activarModoSeleccion()));
        panelBotones.add(crearBotonConImagen("eliminar.png", "Eliminar", e -> eliminarFiguraSeleccionada()));
        panelBotones.add(crearBotonConImagen("guardar.png", "Guardar", e -> guardarDibujo()));
        panelBotones.add(crearBotonConImagen("cargar.png", "Cargar", e -> cargarDibujo()));

        add(panelBotones, BorderLayout.NORTH);
    }

    // Método para crear un botón con una imagen y un texto
    private JButton crearBotonConImagen(String rutaImagen, String texto, ActionListener accion) {
        JButton boton = new JButton(texto);
        boton.addActionListener(accion);

        // Cargar la imagen desde el archivo en src/imagenes
        ImageIcon icono = new ImageIcon(getClass().getResource("/imagenes/" + rutaImagen));
        Image imagen = icono.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);  // Redimensionamos la imagen
        boton.setIcon(new ImageIcon(imagen));

        // Establecer el texto debajo de la imagen
        boton.setVerticalTextPosition(SwingConstants.BOTTOM);  // Poner el texto debajo de la imagen
        boton.setHorizontalTextPosition(SwingConstants.CENTER);  // Centrar el texto horizontalmente
        boton.setText(texto);  // Asignar el texto

        return boton;
    }

    private void seleccionarFigura() {
        // Selecciona la figura para dibujar (puedes cambiar la figura aquí si deseas)
        String[] opciones = {"Linea", "Rectangulo", "Circulo"};
        figuraSeleccionada = (String) JOptionPane.showInputDialog(this, 
            "Selecciona una figura para dibujar:",
            "Seleccionar Figura", JOptionPane.QUESTION_MESSAGE, null, opciones, figuraSeleccionada);
    }

    private void activarModoSeleccion() {
        // Activar o desactivar el modo de selección
        modoSeleccion = !modoSeleccion;
        String mensaje = modoSeleccion ? "Modo selección activado." : "Modo selección desactivado.";
        JOptionPane.showMessageDialog(panelDibujo, mensaje);
    }

    private void eliminarFiguraSeleccionada() {
        if (figuraSeleccionadaPanel != null) {
            trazos.eliminar(figuraSeleccionadaPanel);  // Usamos el método eliminar de la lista ligada
            figuraSeleccionadaPanel = null;  // Deseleccionar la figura
            panelDibujo.repaint();  // Redibujar el panel de inmediato
            JOptionPane.showMessageDialog(panelDibujo, "Figura eliminada.");
        } else {
            JOptionPane.showMessageDialog(panelDibujo, "No hay figura seleccionada.");
        }
    }

    private Trazo crearFigura(int x1, int y1, int x2, int y2) {
        Trazo trazo = null;
        switch (figuraSeleccionada) {
            case "Linea":
                trazo = new Linea(x1, y1, x2, y2);
                break;
            case "Rectangulo":
                trazo = new Rectangulo(x1, y1, x2, y2);
                break;
            case "Circulo":
                trazo = new Circulo(x1, y1, x2, y2);
                break;
        }
        return trazo;
    }

    private void guardarDibujo() {
        // Crear un JFileChooser para seleccionar la ubicación y el nombre del archivo
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Dibujo");
        
        // Mostrar el cuadro de diálogo de guardar
        int resultado = fileChooser.showSaveDialog(this);
        
        // Si el usuario selecciona "Guardar"
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
                Nodo actual = trazos.cabeza;
                while (actual != null) {
                    oos.writeObject(actual.trazo);
                    actual = actual.siguiente;
                }
                JOptionPane.showMessageDialog(this, "Dibujo guardado exitosamente.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar el archivo.");
            }
        }
    }

    private void cargarDibujo() {
        // Crear un JFileChooser para seleccionar el archivo a cargar
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Cargar Dibujo");
        
        // Mostrar el cuadro de diálogo de abrir
        int resultado = fileChooser.showOpenDialog(this);
        
        // Si el usuario selecciona "Abrir"
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
                trazos = new ListaLigada();  // Limpiar la lista de trazos
                while (true) {
                    try {
                        Trazo trazo = (Trazo) ois.readObject();
                        trazos.agregar(trazo);
                    } catch (EOFException e) {
                        break;
                    }
                }
                panelDibujo.repaint();  // Redibujar el panel con las figuras cargadas
                JOptionPane.showMessageDialog(this, "Dibujo cargado exitosamente.");
            } catch (IOException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar el archivo.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new EditorDibujo().setVisible(true);
        });
    }
}
