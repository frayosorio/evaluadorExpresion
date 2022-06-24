package evaluadorexpresion;

import java.util.List;

public class ArbolBinario {

    Nodo raiz;

    private List<String> variables;
    private List<Double> valores;

    public ArbolBinario(Nodo raiz) {
        this.raiz = raiz;
    }

    private String mostrarInorden(Nodo n) {
        if (n != null) {
            return mostrarInorden(n.izquierdo) + " "
                    + n.valor + " "
                    + mostrarInorden(n.derecho);
        }
        return "";
    }

    public String mostrarInorden() {
        return mostrarInorden(this.raiz);
    }

    private String mostrarPostorden(Nodo n) {
        if (n != null) {
            return mostrarPostorden(n.izquierdo) + " "
                    + mostrarPostorden(n.derecho) + " "
                    + n.valor;
        }
        return "";
    }

    public String mostrarPostorden() {
        return mostrarPostorden(this.raiz);
    }

    private double obtenerValorVariable(String variable) {
        double valor = 0;
        if (this.variables.size() > 0) {
            int p = this.variables.indexOf(variable);
            if (p >= 0) {
                valor = this.valores.get(p);
            }
        }
        return valor;
    }

    private double obtenerValorNodo(Nodo n) {
        return n.tipo == TipoOperando.CONSTANTE ? n.obtenerValorNumerico() : obtenerValorVariable(n.valor);
    }

    private double evaluarNodo(Nodo n) {
        if (n.izquierdo == null && n.derecho == null) {
            return obtenerValorNodo(n);
        } else {
            double operando1 = evaluarNodo(n.izquierdo);
            double operando2 = evaluarNodo(n.derecho);
            switch (n.valor) {
                case "+":
                    return operando1 + operando2;
                case "-":
                    return operando1 - operando2;
                case "*":
                    return operando1 + operando2;
                case "/":
                    return operando2 != 0 ? operando1 / operando2 : 0;
                case "%":
                    return operando2 != 0 ? operando1 % operando2 : 0;
                case "^":
                    return Math.pow(operando1, operando2);
            }
        }
        return 0;
    }

    public double evaluar(List<String> variables, List<Double> valores) {
        this.variables = variables;
        this.valores = valores;
        return evaluarNodo(raiz);
    }

}
