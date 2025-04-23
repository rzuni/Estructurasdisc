public class AnalizadorSimbolosLogicos {

    public static TipoSimboloLogicoEnum analizarSimbolo(char simbolo) throws ExcepcionSimboloInvalido {
        if (simbolo == '\u21d2' || simbolo == '\u2192') {
            return TipoSimboloLogicoEnum.IMPLICACION;
        } else if (simbolo == '\u21d4' || simbolo == '\u2261' || simbolo == '\u2194') {
            return TipoSimboloLogicoEnum.BICONDICIONAL;
        } else if (simbolo == '\u00ac' || simbolo == '\u02dc' || simbolo == '\u0021') {
            return TipoSimboloLogicoEnum.NEGACION;
        } else if (simbolo == '\u2227' || simbolo == '\u0026' || simbolo == '\u00b7') {
            return TipoSimboloLogicoEnum.CONJUNCION;
        } else if (simbolo == '\u2228' || simbolo == '\u002b' || simbolo == '\u2225') {
            return TipoSimboloLogicoEnum.DISYUNCION;
        } else if (simbolo == '\u2295' || simbolo == '\u22bb') {
            return TipoSimboloLogicoEnum.XOR;
        } else if (simbolo == '(' || simbolo == '[' || simbolo == '{') {
            return TipoSimboloLogicoEnum.PARENTESIS;
        } else {
            throw new ExcepcionSimboloInvalido("Símbolo no reconocido");
        }
    }

    public static boolean esSimboloLogico(char simbolo) {
        return simbolo == '\u21d2' || simbolo == '\u2192' || simbolo == '\u21d4' ||
                simbolo == '\u2261' || simbolo == '\u2194' ||
                simbolo == '\u00ac' || simbolo == '\u02dc' || simbolo == '\u0021' ||
                simbolo == '\u2227' || simbolo == '\u0026' || simbolo == '\u00b7' ||
                simbolo == '\u2228' || simbolo == '\u002b' || simbolo == '\u2225' ||
                simbolo == '\u2295' || simbolo == '\u22bb';
    }
}