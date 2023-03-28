/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package buscaminas;

/**
 *
 * @author User
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Buscaminas extends JFrame implements ActionListener {
     private static final long serialVersionUID = 1L;
    private int filas;
    private int columnas;
    private int minas;
    private JButton[][] botones;
    private int[][] valores;
    private boolean[][] revelado;
    private boolean[][] conBandera;


    public Buscaminas() {
        super("Buscaminas");
        String filasString = JOptionPane.showInputDialog(this, "Ingrese el número de filas:");
        String columnasString = JOptionPane.showInputDialog(this, "Ingrese el número de columnas:");
        String minasString = JOptionPane.showInputDialog(this, "Ingrese el número de minas:");
        try {
            filas = Integer.parseInt(filasString);
            columnas = Integer.parseInt(columnasString);
            minas = Integer.parseInt(minasString);
        } catch (NumberFormatException e) {
            filas = 10;
            columnas = 10;
            minas = 10;
        }
        botones = new JButton[filas][columnas];
        valores = new int[filas][columnas];
        revelado = new boolean[filas][columnas];
        setLayout(new GridLayout(filas, columnas));
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                botones[i][j] = new JButton();
                botones[i][j].addActionListener(this);
                add(botones[i][j]);
            }
        }
        colocarMinas();
        calcularValores();
        setSize(400, 400);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    public void colocarMinas() {
        for (int i = 0; i < minas; i++) {
            int fila = (int) (Math.random() * filas);
            int columna = (int) (Math.random() * columnas);
            if (valores[fila][columna] != -1) {
                valores[fila][columna] = -1;
            } else {
                i--;
            }
        }
    }

    public void calcularValores() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (valores[i][j] != -1) {
                    int minasAdyacentes = 0;
                    if (i > 0 && j > 0 && valores[i - 1][j - 1] == -1) minasAdyacentes++;
                    if (i > 0 && valores[i - 1][j] == -1) minasAdyacentes++;
                    if (i > 0 && j < columnas - 1 && valores[i - 1][j + 1] == -1) minasAdyacentes++;
                    if (j > 0 && valores[i][j - 1] == -1) minasAdyacentes++;
                    if (j < columnas - 1 && valores[i][j + 1] == -1) minasAdyacentes++;
                    if (i < filas - 1 && j > 0 && valores[i + 1][j - 1] == -1) minasAdyacentes++;
                    if (i < filas - 1 && valores[i + 1][j] == -1) minasAdyacentes++;
                    if (i < filas - 1 && j < columnas - 1 && valores[i + 1][j + 1] == -1) minasAdyacentes++;
                    valores[i][j] = minasAdyacentes;
                }
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
    for (int i = 0; i < filas; i++) {
        for (int j = 0; j < columnas; j++) {
            if (e.getSource() == botones[i][j]) {
                if (!revelado[i][j]) {
                    revelar(i, j);
                    if (valores[i][j] == -1) {
                        gameOver();
                    } else if (valores[i][j] == 0) {
                        revelarAdyacentes(i, j);
                    }
                    verificarVictoria();
                }
            }
        }
    }
    }
    public void verificarVictoria() {
    boolean victoria = true;
    for (int i = 0; i < filas; i++) {
        for (int j = 0; j < columnas; j++) {
            if (valores[i][j] != -1 && !revelado[i][j]) {
                victoria = false;
                break;
            }
        }
    }
    if (victoria) {
        JOptionPane.showMessageDialog(this, "¡Ganaste!");
        reiniciar();
    }
    }
    
    public void revelar(int fila, int columna) {
        botones[fila][columna].setEnabled(false);
        revelado[fila][columna] = true;
        if (valores[fila][columna] == -1) {
            botones[fila][columna].setText("X");
        } else if (valores[fila][columna] > 0) {
            botones[fila][columna].setText(valores[fila][columna] + "");
        }
    }
    
     public void revelarAdyacentes(int fila, int columna) {
         // Verifica si la celda de arriba a la izquierda está dentro del tablero y si no ha sido revelada todavía
    if (fila > 0 && columna > 0 && !revelado[fila - 1][columna - 1]) {
        // Revela la celda de arriba a la izquierda
        revelar(fila - 1, columna - 1);
        // Si la celda de arriba a la izquierda está vacía (su valor es 0), llama recursivamente a revelarAdyacentes para esa celda
        if (valores[fila - 1][columna - 1] == 0) revelarAdyacentes(fila - 1, columna - 1);
    }
    // Verifica si la celda de arriba está dentro del tablero y si no ha sido revelada todavía
    if (fila > 0 && !revelado[fila - 1][columna]) {
        // Revela la celda de arriba
        revelar(fila - 1, columna);
        // Si la celda de arriba está vacía (su valor es 0), llama recursivamente a revelarAdyacentes para esa celda
        if (valores[fila - 1][columna] == 0) revelarAdyacentes(fila - 1, columna);
    }
    // Verifica si la celda de arriba a la derecha está dentro del tablero y si no ha sido revelada todavía
    if (fila > 0 && columna < columnas - 1 && !revelado[fila - 1][columna + 1]) {
        // Revela la celda de arriba a la derecha
        revelar(fila - 1, columna + 1);
        // Si la celda de arriba a la derecha está vacía (su valor es 0), llama recursivamente a revelarAdyacentes para esa celda
        if (valores[fila - 1][columna + 1] == 0) revelarAdyacentes(fila - 1, columna + 1);
    }
    // Verifica si la celda de la izquierda está dentro del tablero y si no ha sido revelada todavía
    if (columna > 0 && !revelado[fila][columna - 1]) {
        // Revela la celda de la izquierda
        revelar(fila, columna - 1);
        // Si la celda de la izquierda está vacía (su valor es 0), llama recursivamente a revelarAdyacentes para esa celda
        if (valores[fila][columna - 1] == 0) revelarAdyacentes(fila, columna - 1);
    }
    // Verifica si la celda de la derecha está dentro del tablero y si no ha sido revelada todavía
    if (columna < columnas - 1 && !revelado[fila][columna + 1]) {
        // Revela la celda de la derecha
        revelar(fila, columna + 1);
        // Si la celda de la derecha está vacía (su valor es 0), llama recursivamente a revelarAdyacentes para esa celda
        if (valores[fila][columna + 1] == 0) revelarAdyacentes(fila, columna + 1);
    }
    
     // Verifica si la celda de abajo a la izquierda está dentro del tablero y si no ha sido revelada todavía
    if (fila < filas - 1 && columna > 0 && !revelado[fila + 1][columna - 1]) {
        // Revela la celda de abajo a la izquierda
        revelar(fila + 1, columna - 1);
        // Si la celda de abajo a la izquierda está vacía (su valor es 0), llama recursivamente a revelarAdyacentes para esa celda
        if (valores[fila + 1][columna - 1] == 0) revelarAdyacentes(fila + 1, columna - 1);
    }
    // Verifica si la celda de abajo está dentro del tablero y si no ha sido revelada todavía
    if (fila < filas - 1 && !revelado[fila + 1][columna]) {
        // Revela la celda de abajo
        revelar(fila + 1, columna);
        // Si la celda de abajo está vacía (su valor es 0), llama recursivamente a revelarAdyacentes para esa celda
        if (valores[fila + 1][columna] == 0) revelarAdyacentes(fila + 1, columna);
    }
    // Verifica si la celda de abajo a la derecha está dentro del tablero y si no ha sido revelada todavía
    if (fila < filas - 1 && columna < columnas - 1 && !revelado[fila + 1][columna + 1]) {
        // Revela la celda de abajo a la derecha
        revelar(fila + 1, columna + 1);
        // Si la celda de abajo a la derecha está vacía (su valor es 0), llama recursivamente a revelarAdyacentes para esa celda
        if (valores[fila + 1][columna + 1] == 0) revelarAdyacentes(fila + 1, columna + 1);
    }
}

    public void gameOver() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (valores[i][j] == -1) {
                    botones[i][j].setText("X");
                }
                botones[i][j].setEnabled(false);
            }
        }
        JOptionPane.showMessageDialog(this, "¡Perdiste!");
        reiniciar();
    }

    public void reiniciar() {
        this.dispose();
        new Buscaminas();

    }

    public static void main(String[] args) {
        new Buscaminas();
    }
}