package com.nonogram.model;

// Enum que representa los posibles estados de una celda en el Nonograma.
public enum EstadoCelda {
    VACIA(0),    // Celda vacía (blanca)
    LLENA(1),    // Celda llena (negra)
    MARCADA(2);  // Celda marcada con X
    
    private final int valor;
    
    // Constructor del enum.
    // 
    // @param valor Valor numérico del estado
    EstadoCelda(int valor) {
        this.valor = valor;
    }
    
    // Obtiene el valor numérico del estado.
    // 
    // @return Valor numérico del estado
    public int obtenerValor() {
        return valor;
    }
}
