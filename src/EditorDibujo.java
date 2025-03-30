import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;

public class EditorDibujo extends JFrame {
    private ListaLigada trazos = new ListaLigada();  
    private String figuraSeleccionada = "Linea"; 
    private JPanel panelDibujo;
    private int startX, startY, endX, endY;  
    private boolean isDragging = false;  
    private Trazo figuraEnProceso = null; 
    private Trazo figuraSeleccionadaPanel = null; 
    private boolean modoSeleccion = false; 

    public EditorDibujo() {
        setTitle("Editor de Dibujos Vectoriales");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(new Color(250, 250, 250));  
        panelDibujo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                trazos.dibujar(g);

                if (figuraEnProceso != null) {
                    figuraEnProceso.dibujar(g);
                }
                
                if (figuraSeleccionadaPanel != null) {
                    g.setColor(Color.RED); 
                    figuraSeleccionadaPanel.dibujar(g);
                }
            }
        };
        panelDibujo.setBackground(new Color(191, 218, 255));  
        panelDibujo.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));  
        panelDibujo.setPreferredSize(new Dimension(700, 500));
        panelDibujo.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (modoSeleccion) {
                    startX = e.getX();
                    startY = e.getY();
        
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
                        trazos.agregar(trazoFinal);  
                        panelDibujo.repaint();  
                    }
                    isDragging = false;
                    figuraEnProceso = null;  
                }
            }
        });

        panelDibujo.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                endX = e.getX();
                endY = e.getY();
                if (!modoSeleccion) {
                    figuraEnProceso = crearFigura(startX, startY, endX, endY);
                    panelDibujo.repaint();  
                }
            }
        });

        add(panelDibujo, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(new Color(191, 218, 255));  
        panelBotones.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));  
        panelBotones.add(crearBotonConImagen("figuras.png", "Figura", e -> seleccionarFigura()));
        panelBotones.add(crearBotonConImagen("seleccionar.png", "Seleccionar", e -> activarModoSeleccion()));
        panelBotones.add(crearBotonConImagen("eliminar.png", "Eliminar", e -> eliminarFiguraSeleccionada()));
        panelBotones.add(crearBotonConImagen("guardar.png", "Guardar", e -> guardarDibujo()));
        panelBotones.add(crearBotonConImagen("cargar.png", "Cargar", e -> cargarDibujo()));

        add(panelBotones, BorderLayout.NORTH);
    }

    private JButton crearBotonConImagen(String rutaImagen, String texto, ActionListener accion) {
        JButton boton = new JButton(texto);
        boton.addActionListener(accion);

        ImageIcon icono = new ImageIcon(getClass().getResource("/imagenes/" + rutaImagen));
        Image imagen = icono.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH); 
        boton.setIcon(new ImageIcon(imagen));

        boton.setVerticalTextPosition(SwingConstants.BOTTOM);  
        boton.setHorizontalTextPosition(SwingConstants.CENTER);  
        boton.setText(texto); 

        return boton;
    }

    private void seleccionarFigura() {
        String[] opciones = {"Linea", "Rectangulo", "Circulo"};
        figuraSeleccionada = (String) JOptionPane.showInputDialog(this, 
            "Selecciona una figura para dibujar:",
            "Seleccionar Figura", JOptionPane.QUESTION_MESSAGE, null, opciones, figuraSeleccionada);
    }

    private void activarModoSeleccion() {
        // Activar o desactivar el modo de selección
        modoSeleccion = !modoSeleccion;
        String mensaje = modoSeleccion ? "Modo selección activado." : "Modo selección desactivado."; //Dar cick nuevamente para desabilitarlo, de lo contrario no podre dibujar mas figuras
        JOptionPane.showMessageDialog(panelDibujo, mensaje);
    }

    private void eliminarFiguraSeleccionada() {
        if (figuraSeleccionadaPanel != null) {
            trazos.eliminar(figuraSeleccionadaPanel);  
            figuraSeleccionadaPanel = null;  
            panelDibujo.repaint();  
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
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Dibujo");  
        int resultado = fileChooser.showSaveDialog(this);
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
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Cargar Dibujo");
        int resultado = fileChooser.showOpenDialog(this);
        
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
                trazos = new ListaLigada();  
                while (true) {
                    try {
                        Trazo trazo = (Trazo) ois.readObject();
                        trazos.agregar(trazo);
                    } catch (EOFException e) {
                        break;
                    }
                }
                panelDibujo.repaint();  
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
