import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TreeMap;

public class InterfazTablaVerdad extends JFrame {
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JTextField entradaExpresion;
    private boolean[] valoresVerdad;
    private String[][] matrizBinaria;

    public InterfazTablaVerdad() {
        setTitle("Generador de tablas de verdad");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelEntrada = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel etiquetaEntrada = new JLabel("Expresión lógica:");
        entradaExpresion = new JTextField(30);
        JButton botonGenerar = new JButton("Generar tabla");

        panelEntrada.add(etiquetaEntrada);
        panelEntrada.add(entradaExpresion);
        panelEntrada.add(botonGenerar);

        modeloTabla = new DefaultTableModel();
        tabla = new JTable(modeloTabla);
        JScrollPane panelDesplazamiento = new JScrollPane(tabla);

        panelPrincipal.add(panelEntrada, BorderLayout.NORTH);
        panelPrincipal.add(panelDesplazamiento, BorderLayout.CENTER);

        botonGenerar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generarTablaVerdad();
            }
        });

        add(panelPrincipal);
    }

    private void generarTablaVerdad() {
        String expresion = entradaExpresion.getText().replaceAll("\\s", "").toLowerCase();
        if (!validarExpresion(expresion)) {
            JOptionPane.showMessageDialog(this, "Expresión inválida", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            GeneradorValoresLogicos generador = new GeneradorValoresLogicos(expresion);
            valoresVerdad = generador.generarValoresVerdad();
            matrizBinaria = generador.obtenerMatrizBinaria();
            TreeMap<Character, Boolean> variables = generador.obtenerVariables();

            modeloTabla.setRowCount(0);
            modeloTabla.setColumnCount(0);

            for (Character var : variables.keySet()) {
                modeloTabla.addColumn(var.toString());
            }
            modeloTabla.addColumn(expresion);

            for (int i = 0; i < matrizBinaria.length; i++) {
                Object[] fila = new Object[variables.size() + 1];
                for (int j = 0; j < matrizBinaria[i].length; j++) {
                    fila[j] = matrizBinaria[i][j];
                }
                fila[variables.size()] = valoresVerdad[i] ? "true" : "false";
                modeloTabla.addRow(fila);
            }

        } catch (ExcepcionSimboloInvalido ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validarExpresion(String expresion) {
        if (expresion.isEmpty()) return false;
        char ultimoCaracter = expresion.charAt(expresion.length() - 1);
        return Character.isLetter(ultimoCaracter) || ultimoCaracter == ')' || ultimoCaracter == ']' || ultimoCaracter == '}';
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new InterfazTablaVerdad().setVisible(true);
        });
    }
}