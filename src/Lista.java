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
            Nodo apuntador = cabeza;
            while (apuntador.siguiente != null) {
                apuntador = apuntador.siguiente;
            }
            apuntador.siguiente = nuevoNodo;
        }
    }

    public void eliminarNodo(Nodo nodoAEliminar) {
        if (cabeza == null) return;

        if (cabeza == nodoAEliminar) {
            cabeza = cabeza.siguiente;
            return;
        }

        Nodo apuntador = cabeza;
        while (apuntador.siguiente != null) {
            if (apuntador.siguiente == nodoAEliminar) {
                apuntador.siguiente = apuntador.siguiente.siguiente;
                return;
            }
            apuntador = apuntador.siguiente;
        }
    }

    public Nodo seleccionarNodo(int x, int y) {
        Nodo apuntador = cabeza;
        while (apuntador != null) {
            if (x >= Math.min(apuntador.x1, apuntador.x2) && x <= Math.max(apuntador.x1, apuntador.x2) &&
                    y >= Math.min(apuntador.y1, apuntador.y2) && y <= Math.max(apuntador.y1, apuntador.y2)) {
                return apuntador;
            }
            apuntador = apuntador.siguiente;
        }
        return null;
    }

    public void dibujarTrazos(Graphics g, Nodo nodoSeleccionado) {
        Nodo apuntador = cabeza;
        while (apuntador != null) {
            g.setColor(apuntador == nodoSeleccionado ? Color.RED : Color.WHITE);
            switch (apuntador.tipoTrazo) {
                case "Linea" -> g.drawLine(apuntador.x1, apuntador.y1, apuntador.x2, apuntador.y2);
                case "Rectangulo" -> g.drawRect(Math.min(apuntador.x1, apuntador.x2), Math.min(apuntador.y1, apuntador.y2),
                        Math.abs(apuntador.x2 - apuntador.x1), Math.abs(apuntador.y2 - apuntador.y1));
                case "Circulo" -> g.drawOval(Math.min(apuntador.x1, apuntador.x2), Math.min(apuntador.y1, apuntador.y2),
                        Math.abs(apuntador.x2 - apuntador.x1), Math.abs(apuntador.y2 - apuntador.y1));
            }
            apuntador = apuntador.siguiente;
        }
    }

    public String[] obtenerDatos() {
        int tamaño = contarNodos();
        String[] datos = new String[tamaño];
        Nodo apuntador = cabeza;
        int i = 0;
        while (apuntador != null) {
            datos[i++] = apuntador.tipoTrazo + ";" + apuntador.x1 + ";" + apuntador.y1 + ";" + apuntador.x2 + ";" + apuntador.y2;
            apuntador = apuntador.siguiente;
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
        Nodo apuntador = cabeza;
        while (apuntador != null) {
            contador++;
            apuntador = apuntador.siguiente;
        }
        return contador;
    }
}