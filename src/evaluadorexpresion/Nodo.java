package evaluadorexpresion;

public class Nodo {

    String valor;
    TipoOperando tipo;
    Nodo izquierdo;
    Nodo derecho;

    public Nodo(String valor, TipoOperando tipo) {
        this.valor = valor;
        this.tipo = tipo;
        this.izquierdo = null;
        this.derecho = null;
    }

    public double obtenerValorNumerico() {
        try {
            if (tipo == TipoOperando.CONSTANTE) {
                return Double.parseDouble(valor);
            }
        } catch (Exception ex) {
        }
        return 0;
    }

}
