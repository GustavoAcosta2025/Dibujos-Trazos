import java.awt.Color;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.IOException;

public class Lista {
    private Nodo cabeza;

    public void agregar(Nodo nuevoNodo) {
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

    public void eliminarNodo(Nodo nodoAEliminar) {
        if (cabeza == null) return;

        if (cabeza == nodoAEliminar) {
            cabeza = cabeza.siguiente;
            return;
        }

        Nodo actual = cabeza;
        while (actual.siguiente != null) {
            if (actual.siguiente == nodoAEliminar) {
                actual.siguiente = actual.siguiente.siguiente;
                return;
            }
            actual = actual.siguiente;
        }
    }

    public Nodo seleccionarNodo(int x, int y) {
        Nodo actual = cabeza;
        while (actual != null) {
            if (x >= Math.min(actual.x1, actual.x2) && x <= Math.max(actual.x1, actual.x2) &&
                    y >= Math.min(actual.y1, actual.y2) && y <= Math.max(actual.y1, actual.y2)) {
                return actual;
            }
            actual = actual.siguiente;
        }
        return null;
    }

    public void dibujarTrazos(Graphics g, Nodo nodoSeleccionado) {
        Nodo actual = cabeza;
        while (actual != null) {
            g.setColor(actual == nodoSeleccionado ? Color.RED : Color.WHITE);
            switch (actual.tipoTrazo) {
                case "Linea" -> g.drawLine(actual.x1, actual.y1, actual.x2, actual.y2);
                case "Rectangulo" -> g.drawRect(Math.min(actual.x1, actual.x2), Math.min(actual.y1, actual.y2),
                        Math.abs(actual.x2 - actual.x1), Math.abs(actual.y2 - actual.y1));
                case "Circulo" -> g.drawOval(Math.min(actual.x1, actual.x2), Math.min(actual.y1, actual.y2),
                        Math.abs(actual.x2 - actual.x1), Math.abs(actual.y2 - actual.y1));
            }
            actual = actual.siguiente;
        }
    }

    public String[] obtenerDatos() {
        int tamaño = contarNodos();
        String[] datos = new String[tamaño];
        Nodo actual = cabeza;
        int i = 0;
        while (actual != null) {
            datos[i++] = actual.tipoTrazo + ";" + actual.x1 + ";" + actual.y1 + ";" + actual.x2 + ";" + actual.y2;
            actual = actual.siguiente;
        }
        return datos;
    }

    public void cargarDesdeArchivo(String ruta) {
        cabeza = null;
        BufferedReader br = Archivo.abrirArchivo(ruta);
        if (br != null) {
            try {
                String linea;
                while ((linea = br.readLine()) != null) {
                    String[] datos = linea.split(";");
                    if (datos.length == 5) {
                        agregar(new Nodo(datos[0], Integer.parseInt(datos[1]), Integer.parseInt(datos[2]),
                                Integer.parseInt(datos[3]), Integer.parseInt(datos[4])));
                    }
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private int contarNodos() {
        int contador = 0;
        Nodo actual = cabeza;
        while (actual != null) {
            contador++;
            actual = actual.siguiente;
        }
        return contador;
    }
}