public class SimboloLogico {
    private TipoSimboloLogicoEnum tipo;
    private int precedencia;
    private char simbolo;

    public SimboloLogico(TipoSimboloLogicoEnum tipo) {
        this.tipo = tipo;

        if (tipo == TipoSimboloLogicoEnum.NEGACION) {
            this.precedencia = 6;
            this.simbolo = '\u00ac';
        } else if (tipo == TipoSimboloLogicoEnum.CONJUNCION) {
            this.precedencia = 5;
            this.simbolo = '\u2227';
        } else if (tipo == TipoSimboloLogicoEnum.XOR) {
            this.precedencia = 4;
            this.simbolo = '\u2295';
        } else if (tipo == TipoSimboloLogicoEnum.DISYUNCION) {
            this.precedencia = 3;
            this.simbolo = '\u2228';
        } else if (tipo == TipoSimboloLogicoEnum.IMPLICACION) {
            this.precedencia = 2;
            this.simbolo = '\u21d2';
        } else if (tipo == TipoSimboloLogicoEnum.BICONDICIONAL) {
            this.precedencia = 1;
            this.simbolo = '\u21d4';
        } else if (tipo == TipoSimboloLogicoEnum.PARENTESIS) {
            this.precedencia = 7;
            this.simbolo = '(';
        }
    }

    public TipoSimboloLogicoEnum getTipo() {
        return tipo;
    }

    public int getPrecedencia() {
        return precedencia;
    }

    public char getSimbolo() {
        return simbolo;
    }

    public boolean aplicarOperacion(boolean operando1, boolean operando2) {
        switch (tipo) {
            case NEGACION:
                return !operando1;
            case CONJUNCION:
                return operando1 && operando2;
            case DISYUNCION:
                return operando1 || operando2;
            case IMPLICACION:
                return !operando1 || operando2;
            case BICONDICIONAL:
                return operando1 == operando2;
            case XOR:
                return operando1 ^ operando2;
            default:
                return false;
        }
    }
}