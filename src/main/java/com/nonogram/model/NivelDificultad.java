package com.nonogram.model;

// Enum que define los diferentes niveles de dificultad del juego.
// 
// Cada nivel tiene un tamaño de grilla específico y un número de pistas disponibles.
public enum NivelDificultad {
    
    FACIL(5, 3),      // 5x5 con 3 pistas
    MEDIO(10, 3),     // 10x10 con 3 pistas
    DIFICIL(15, 3),   // 15x15 con 3 pistas
    EXPERTO(20, 3);   // 20x20 con 3 pistas
    
    private final int tamañoGrilla;
    private final int pistasDisponibles;
    
    // Constructor del enum.
    // 
    // @param tamañoGrilla Tamaño de la grilla (NxN)
    // @param pistasDisponibles Número de pistas disponibles
    NivelDificultad(int tamañoGrilla, int pistasDisponibles) {
        this.tamañoGrilla = tamañoGrilla;
        this.pistasDisponibles = pistasDisponibles;
    }
    
    // Obtiene el tamaño de la grilla para este nivel.
    // 
    // @return Tamaño de la grilla
    public int obtenerTamañoGrilla() {
        return tamañoGrilla;
    }
    
    // Obtiene el número de pistas disponibles para este nivel.
    // 
    // @return Número de pistas disponibles
    public int obtenerPistasDisponibles() {
        return pistasDisponibles;
    }
    
    // Obtiene el nombre del nivel para mostrar en la interfaz.
    // 
    // @return Nombre del nivel
    public String obtenerNombre() {
        switch (this) {
            case FACIL:
                return "Fácil (5x5) - 3 pistas";
            case MEDIO:
                return "Medio (10x10) - 3 pistas";
            case DIFICIL:
                return "Difícil (15x15) - 3 pistas";
            case EXPERTO:
                return "Experto (20x20) - 3 pistas";
            default:
                return "Desconocido";
        }
    }
}
