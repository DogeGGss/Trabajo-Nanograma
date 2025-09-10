package com.nonogram.vista;

import com.nonogram.model.ModeloNonograma;

// Interfaz de la vista del Nonograma.
// 
// Esta interfaz define los métodos que debe implementar la vista gráfica.
// La implementación concreta se hará usando WindowBuilder en Eclipse.
// 
// Patrón MVC: La vista es responsable de mostrar la interfaz gráfica
// y notificar al controlador sobre las acciones del usuario.
public interface VistaNonograma {
    
    // Actualiza la visualización de la grilla y las pistas.
    // Este método es llamado cuando el modelo cambia.
    void actualizarVisualizacion();
    
    // Muestra un mensaje de victoria cuando el jugador gana.
    void mostrarJuegoGanado();
    
    // Muestra un mensaje de derrota cuando el jugador pierde.
    void mostrarJuegoPerdido();
    
    // Muestra la solución del puzzle.
    // 
    // @param modelo El modelo con la solución
    void mostrarSolucion(ModeloNonograma modelo);
    
    // Establece el controlador para manejar los eventos de la vista.
    // 
    // @param controlador El controlador de la vista
    void establecerControlador(Object controlador);
    
    // Obtiene el controlador de la vista.
    // 
    // @return El controlador de la vista
    Object obtenerControlador();
    
    // Actualiza la información del juego (movimientos, pistas, etc.).
    void actualizarInformacionJuego();
    
    // Deshabilita el botón de ver solución (usado en nuevo juego).
    void deshabilitarBotonSolucion();
    
    // Habilita el botón de ver solución (usado después de perder).
    void habilitarBotonSolucion();
}
