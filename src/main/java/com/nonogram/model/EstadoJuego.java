package com.nonogram.model;

// Enum que representa los posibles estados del juego Nonograma.
public enum EstadoJuego {
    JUGANDO(0),  // El juego está en curso
    GANADO(1),   // El jugador ha ganado
    PERDIDO(2);  // El jugador ha perdido
    
    private final int valor;
    
    // Constructor del enum.
    // 
    // @param valor Valor numérico del estado
    EstadoJuego(int valor) {
        this.valor = valor;
    }
    
    // Obtiene el valor numérico del estado.
    // 
    // @return Valor numérico del estado
    public int obtenerValor() {
        return valor;
    }
}
