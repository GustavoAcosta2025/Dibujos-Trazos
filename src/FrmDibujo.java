import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;


public class FrmDibujo extends JFrame {
    Lista lista = new Lista();
    Nodo nodoSeleccionado = null;
    BufferedImage imagen;
    String[] opciones = {"Linea", "Rectangulo", "Circulo"};
    JComboBox<String> ComboBox;

    private JButton btnSeleccionar;
    private JButton btnEliminar;
    private JButton btnGuardar;
    private JButton btnCargar;
    private JToolBar tbDibujo;
    private JPanel panel;

    int x, y, xTemp, yTemp;
    boolean trazando = false;

    public FrmDibujo() {
        tbDibujo = new JToolBar();
        ComboBox = new JComboBox<>(opciones);
        btnSeleccionar = new JButton();
        btnEliminar = new JButton();
        btnGuardar = new JButton();
        btnCargar = new JButton();

        tbDibujo.add(ComboBox);
        setSize(700, 500);
        setTitle("Dibujos Vectoriales");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                lista.dibujarTrazos(g, nodoSeleccionado);

                if (trazando) {
                    g.setColor(Color.BLUE);
                    switch (opciones[ComboBox.getSelectedIndex()]) {
                        case "Linea":
                            g.drawLine(x, y, xTemp, yTemp);
                            break;
                        case "Rectangulo":
                            g.drawRect(Math.min(x, xTemp), Math.min(y, yTemp),
                                    Math.abs(xTemp - x), Math.abs(yTemp - y));
                            break;
                        case "Circulo":
                            g.drawOval(Math.min(x, xTemp), Math.min(y, yTemp),
                                    Math.abs(xTemp - x), Math.abs(yTemp - y));
                            break;
                    }
                }
            }
        };

        btnSeleccionar.setIcon(new ImageIcon(getClass().getResource("/imagenes/seleccionar.png")));
        btnSeleccionar.setToolTipText("Seleccionar");
        btnSeleccionar.addActionListener(e -> btnSeleccionarClick());
        tbDibujo.add(btnSeleccionar);

        panel.setBackground(Color.BLACK);
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                x = e.getX();
                y = e.getY();
                xTemp = x;
                yTemp = y;
                trazando = true;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (trazando) {
                    int x2 = e.getX();
                    int y2 = e.getY();
                    String tipo = opciones[ComboBox.getSelectedIndex()];
                    lista.agregar(new Nodo(tipo, x, y, x2, y2));
                    trazando = false;
                    panel.repaint();
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                nodoSeleccionado = lista.seleccionarNodo(e.getX(), e.getY());
                panel.repaint();
            }
        });

        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (trazando) {
                    xTemp = e.getX();
                    yTemp = e.getY();
                    panel.repaint();
                }
            }
        });

        btnEliminar.setIcon(new ImageIcon(getClass().getResource("/imagenes/eliminar.png")));
        btnEliminar.setToolTipText("Eliminar");
        btnEliminar.addActionListener(e -> btnEliminarClick());
        tbDibujo.add(btnEliminar);

        btnGuardar.setIcon(new ImageIcon(getClass().getResource("/imagenes/guardar.png")));
        btnGuardar.setToolTipText("Guardar");
        btnGuardar.addActionListener(e -> btnGuardarClick());
        tbDibujo.add(btnGuardar);

        btnCargar.setIcon(new ImageIcon(getClass().getResource("/imagenes/cargar.png")));
        btnCargar.setToolTipText("Cargar");
        btnCargar.addActionListener(e -> btnCargarClick());
        tbDibujo.add(btnCargar);

        getContentPane().add(tbDibujo, BorderLayout.NORTH);
        getContentPane().add(panel, BorderLayout.CENTER);

        imagen = new BufferedImage(700, 500, BufferedImage.TYPE_INT_ARGB);
    }

    private void btnSeleccionarClick() {
        nodoSeleccionado = null;
        panel.repaint();
    }

    private void btnEliminarClick() {
        if (nodoSeleccionado != null) {
            lista.eliminarNodo(nodoSeleccionado);
            nodoSeleccionado = null;
            panel.repaint();
        }
    }

    private void btnGuardarClick() {
        String rutaArchivo = Archivo.elegirArchivo();
        if (!rutaArchivo.isEmpty()) {
            if (!rutaArchivo.endsWith(".dbj")) {
                rutaArchivo += ".dbj";
            }
            if (Archivo.guardarArchivo(rutaArchivo, lista.obtenerDatos())) {
                JOptionPane.showMessageDialog(this, "El dibujo fue guardado sin problemas.");
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo guardar el Archivo.");
            }
        }
    }

    private void btnCargarClick() {
        String rutaArchivo = Archivo.elegirArchivo();
        if (!rutaArchivo.isEmpty()) {
            lista.cargarDesdeArchivo(rutaArchivo);
            panel.repaint();
            JOptionPane.showMessageDialog(this, "El dibujo fue cargado con exito.");
        }
    }
}
