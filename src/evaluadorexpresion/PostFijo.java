package evaluadorexpresion;

import java.util.*;

public class PostFijo {

    //Atributo que almacena la expresión en Infijo a ser convertida	
    public static String expresionInfijo = "";
    //Almacena la expresion Postfijo obtenida
    private static String expresionPostfijo = "";
    //Alamacena posible error al evaluar la expresion
    private static String errorExpresion = "";

    //Método que indica si un caracter es letra
    public static boolean esLetra(String dato) {
        if ((dato.compareTo("a") >= 0 && dato.compareTo("z") <= 0)
                || (dato.compareTo("A") >= 0 && dato.compareTo("Z") <= 0)) {
            return true;
        } else {
            return false;
        }
    }

    //Método que indica si un caracter es dígito
    public static boolean esDigito(String dato) {
        boolean d = false;
        if (dato.compareTo("0") >= 0 && dato.compareTo("9") <= 0) {
            d = true;
        }
        return d;
    }

    //Método que indica si un caracter es operador
    public static boolean esOperador(String dato) {
        boolean o = false;
        if (dato.equals("+") || dato.equals("-") || dato.equals("*") || dato.equals("/") || dato.equals("^")) {
            o = true;
        }
        return o;
    }

    //Método que indica si un operador es precedido por otro
    public static boolean esPredecesor(String operador1, String operador2) {
        boolean p = false;
        if (operador1.equals("^")) {
            p = true;
        } else if (operador1.equals("/") || operador1.equals("*")) {
            if (!operador2.equals("^")) {
                p = true;
            }
        } else if (operador1.equals("-") || operador1.equals("+")) {
            if (operador2.equals("+") || operador2.equals("-")) {
                p = true;
            }
        }
        return p;
    }

    //Método que permite obtener una expresión Potfijo a partir de la expresión Infijo
    public static String obtenerPostfijo() {
        Stack p = new Stack();
        expresionPostfijo = "";
        boolean error = false;
        int parentesis = 0;
        int i = 0;
        int noOperador = 0;
        errorExpresion = "";
        //Recorrer cada uno de los caracteres
        while (i < expresionInfijo.length() && !error) {
            String caracter = expresionInfijo.substring(i, i + 1);
            if (caracter.equals("(")) {
                noOperador = 1;
                p.push(caracter);
                parentesis++;
            } else if (caracter.equals(")")) {
                noOperador = 2;
                if (parentesis == 0) {
                    error = true;
                    errorExpresion = "Hace falta parentesis izquierdo";
                } else {
                    parentesis--;
                    caracter = (String) p.peek();
                    while (!p.empty() && !caracter.equals("(")) {
                        expresionPostfijo += " " + p.pop();
                        caracter = (String) p.peek();
                    }
                    p.pop();
                }
            } else if (esOperador(caracter)) {
                if (noOperador < 2) {
                    error = true;
                    errorExpresion = "Hace falta operando antes de " + caracter;
                } else {
                    noOperador = 0;
                    expresionPostfijo = expresionPostfijo + " ";
                    while (!p.empty() && esPredecesor((String) p.peek(), caracter)) {
                        expresionPostfijo = expresionPostfijo + p.pop();
                    }
                    p.push(caracter);
                }
            } else if (esLetra(caracter) || esDigito(caracter)) {
                noOperador = 3;
                expresionPostfijo = expresionPostfijo + caracter;
            } else {
                error = true;
                errorExpresion = "Simbolo '" + caracter + "' indefinido ";
            }
            i++;
        }
        //Verificar errores
        if (parentesis > 0) {
            errorExpresion = "Error convirtiendo: Hace falta parentesis derecho";
            expresionPostfijo = "";
        } else if (error || i == 0 || noOperador == 0) {
            errorExpresion = "Error convirtiendo: " + errorExpresion;
            expresionPostfijo = "";
        } else {
            //Construir la expresión POSTFIJO
            expresionPostfijo = expresionPostfijo + " ";
            while (!p.empty()) {
                expresionPostfijo = expresionPostfijo + (String) p.pop();
            }
        }
        return expresionPostfijo;
    }

    //Metodo que devuelve el ultimo mensaje de error
    public static String obtenerError() {
        return errorExpresion;
    }

    //Metodo para obtener los nombres de las variables que hay en la expresion
    public static List<String> obtenerVariables() {
        List<String> operandos = new ArrayList<>();
        int i = 0;
        int tipoOperando = 0;

        String texto = "";
        boolean error = false;
        errorExpresion = "";
        while (i < expresionPostfijo.length() && !error) {
            String caracter = expresionPostfijo.substring(i, i + 1);
            if (PostFijo.esLetra(caracter) && tipoOperando == 2) {
                error = true;
            } else if ((PostFijo.esLetra(caracter) && tipoOperando < 2) || (PostFijo.esDigito(caracter) && tipoOperando == 1)) {
                tipoOperando = 1;
                texto = texto + caracter;
            } else if (PostFijo.esDigito(caracter) && tipoOperando != 1) {
                tipoOperando = 2;
                texto = texto + caracter;
            } else if (caracter.equals(" ") && tipoOperando == 1) {
                if (!operandos.contains(texto)) {
                    operandos.add(texto);
                }
                texto = "";
                tipoOperando = 0;
            } else if (caracter.equals(" ") && tipoOperando == 2) {
                texto = "";
                tipoOperando = 0;
            }
            i++;
        }
        if (!error) {
            return operandos;
        } else {
            errorExpresion = "Error obteniendo variables";
            return null;
        }
    }

    public static ArbolBinario obtenerArbol() {

        Stack p = new Stack();

        TipoOperando tipo = TipoOperando.NINGUNO;
        errorExpresion = "";
        int i = 0;
        String texto = "";
        while (i < expresionPostfijo.length() && errorExpresion.equals("")) {
            String caracter = expresionPostfijo.substring(i, i + 1);
            if (esLetra(caracter) && tipo == TipoOperando.CONSTANTE) {
                errorExpresion = "Una constante numérica no puede tener letras";
            } else if ((esLetra(caracter) && tipo != TipoOperando.CONSTANTE)
                    || (esDigito(caracter) && tipo == TipoOperando.VARIABLE)) {
                tipo = TipoOperando.VARIABLE;
                texto += caracter;
            } else if (esDigito(caracter) && tipo != TipoOperando.VARIABLE) {
                tipo = TipoOperando.CONSTANTE;
                texto += caracter;
            } else if (caracter.equals(" ") && tipo != TipoOperando.NINGUNO) {
                //apilar el operando obtenido
                Nodo nOperando = new Nodo(texto, tipo);
                p.push(nOperando);
                texto = "";
                tipo = TipoOperando.NINGUNO;
            } else {
                caracter = expresionPostfijo.substring(i, i + 1);
                if (esOperador(caracter)) {
                    Nodo nOperador = new Nodo(caracter, TipoOperando.NINGUNO);
                    Nodo nIzquierdo = (Nodo) p.pop();
                    Nodo nDerecho = (Nodo) p.pop();
                    p.push(nOperador);
                }
            }
            i++;
        }
        return errorExpresion.equals("") ? new ArbolBinario((Nodo) p.pop()) : null;
    }

    /* Metodo que ejecuta la expresion con base en las variables encontradas
     * y los valores aportados de dichas variables
     */
    public static double evaluarExpresion(String[] variables, double valores[]) {
        return 0;
    }//evaluarExpresion

    //Metodo para obtener el valor de una de las variables aportadas
    private static double obtenerValor(String[] variables, double valores[], String variable) {
        double valor = 0;
        if (variables != null) {
            for (int i = 0; i < variables.length; i++) {
                if (variables[i].equals(variable)) {
                    return valores[i];
                }
            }
        }
        return valor;
    }//obtenerValor
}
