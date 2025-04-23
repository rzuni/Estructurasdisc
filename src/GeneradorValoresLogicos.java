import java.util.*;

public class GeneradorValoresLogicos {
    private TreeMap<Character, Boolean> variablesLogicas = new TreeMap<>();
    private String expresion;
    private String[][] matrizBinaria;

    public GeneradorValoresLogicos(String expresion) {
        this.expresion = expresion;
    }

    public String[][] obtenerMatrizBinaria() {
        return matrizBinaria;
    }

    public boolean[] generarValoresVerdad() throws ExcepcionSimboloInvalido {
        Queue<Object> colaPostfija = convertirPostfijo(expresion);
        matrizBinaria = generarMatrizBinaria();
        boolean[] valoresVerdad = new boolean[(int) Math.pow(2, variablesLogicas.size())];

        for (int i = 0; i < matrizBinaria.length; i++) {
            int iterador = 0;
            for (Character var : variablesLogicas.keySet()) {
                variablesLogicas.replace(var, matrizBinaria[i][iterador].equals("True"));
                iterador++;
            }
            Queue<Object> copiaCola = new LinkedList<>(colaPostfija);
            valoresVerdad[i] = evaluarExpresion(copiaCola, variablesLogicas);
        }

        return valoresVerdad;
    }

    public TreeMap<Character, Boolean> obtenerVariables() {
        return variablesLogicas;
    }

    private String[][] generarMatrizBinaria() {
        String[][] matriz = new String[(int) Math.pow(2, variablesLogicas.size())][variablesLogicas.size()];
        for (int i = 0; i < matriz.length; i++) {
            String fila = convertirBinario(i);
            matriz[i] = fila.split(" ");
        }
        return matriz;
    }

    private String convertirBinario(int numero) {
        StringBuilder binario = new StringBuilder();
        int digitos = 0;
        while (numero > 0) {
            int residuo = numero % 2;
            numero /= 2;
            binario.insert(0, residuo == 0 ? "False " : "True ");
            digitos++;
        }

        while (digitos < variablesLogicas.size()) {
            binario.insert(0, "False ");
            digitos++;
        }
        return binario.toString().trim();
    }

    private boolean evaluarExpresion(Queue<Object> expresionPostfija, TreeMap<Character, Boolean> valores) throws ExcepcionSimboloInvalido {
        Stack<Boolean> pila = new Stack<>();

        try {
            while (!expresionPostfija.isEmpty()) {
                Object elemento = expresionPostfija.remove();
                if (elemento instanceof Character && Character.isLetter((Character) elemento)) {
                    pila.add(valores.get(elemento));
                } else {
                    TipoSimboloLogicoEnum tipo = AnalizadorSimbolosLogicos.analizarSimbolo((Character) elemento);
                    SimboloLogico simbolo = new SimboloLogico(tipo);
                    if (tipo != TipoSimboloLogicoEnum.NEGACION) {
                        boolean operando2 = pila.pop();
                        boolean operando1 = pila.pop();
                        pila.add(simbolo.aplicarOperacion(operando1, operando2));
                    } else {
                        boolean operando = pila.pop();
                        pila.add(simbolo.aplicarOperacion(operando, false));
                    }
                }
            }
            return pila.pop();
        } catch (EmptyStackException e) {
            throw new ExcepcionSimboloInvalido("Expresión inválida");
        }
    }

    private Queue<Object> convertirPostfijo(String expresionOriginal) throws ExcepcionSimboloInvalido {
        Queue<Object> colaPostfija = new LinkedList<>();
        Stack<SimboloLogico> pilaOperadores = new Stack<>();
        Stack<Character> pilaParentesis = new Stack<>();

        try {
            for (int i = 0; i < expresionOriginal.length(); i++) {
                char caracter = expresionOriginal.charAt(i);
                if (Character.isLetter(caracter)) {
                    colaPostfija.add(caracter);
                    variablesLogicas.put(caracter, false);
                } else if (caracter == ')' || caracter == ']' || caracter == '}') {
                    if (pilaParentesis.isEmpty() || pilaParentesis.pop() != caracter) {
                        throw new ExcepcionSimboloInvalido("Paréntesis no coinciden");
                    }
                    while (!pilaOperadores.isEmpty() && pilaOperadores.peek().getTipo() != TipoSimboloLogicoEnum.PARENTESIS) {
                        colaPostfija.add(pilaOperadores.pop().getSimbolo());
                    }
                    pilaOperadores.pop();
                } else {
                    TipoSimboloLogicoEnum tipo = AnalizadorSimbolosLogicos.analizarSimbolo(caracter);
                    SimboloLogico simbolo = new SimboloLogico(tipo);
                    if (simbolo.getTipo() == TipoSimboloLogicoEnum.PARENTESIS) {
                        pilaOperadores.push(simbolo);
                        pilaParentesis.push(caracter == '(' ? ')' : (caracter == '[' ? ']' : '}'));
                    } else {
                        while (!pilaOperadores.isEmpty() &&
                                pilaOperadores.peek().getPrecedencia() > simbolo.getPrecedencia() &&
                                pilaOperadores.peek().getTipo() != TipoSimboloLogicoEnum.PARENTESIS) {
                            colaPostfija.add(pilaOperadores.pop().getSimbolo());
                        }
                        pilaOperadores.push(simbolo);
                    }
                }
            }
            while (!pilaOperadores.isEmpty()) {
                colaPostfija.add(pilaOperadores.pop().getSimbolo());
            }
            return colaPostfija;
        } catch (EmptyStackException e) {
            throw new ExcepcionSimboloInvalido("Expresión mal formada");
        }
    }
}