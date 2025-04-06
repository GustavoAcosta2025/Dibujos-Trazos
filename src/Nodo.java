public class Nodo {

    String tipoTrazo;
    int x1, y1, x2, y2;
    Nodo siguiente;

    public Nodo(String tipoTrazo, int x1, int y1, int x2, int y2) {
        this.tipoTrazo = tipoTrazo;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        siguiente = null;
    }
}