package com.nonogram.controlador;

import com.nonogram.model.EstadoCelda;
import com.nonogram.model.EstadoJuego;
import com.nonogram.model.ModeloNonograma;
import com.nonogram.model.NivelDificultad;
import com.nonogram.vista.VistaNonograma;

// Controlador del Nonograma que maneja la interacción entre el modelo y la vista.
// 
// Este controlador implementa:
// - Manejo de eventos del usuario
// - Comunicación entre modelo y vista
// - Lógica de control del juego
// 
// Patrón MVC: El controlador actúa como intermediario entre el modelo y la vista,
// procesando las acciones del usuario y actualizando ambos componentes.
public class ControladorNonograma {
    
    private ModeloNonograma modelo;
    private VistaNonograma vista;
   
    public ControladorNonograma(ModeloNonograma modelo, VistaNonograma vista) {
        this.modelo = modelo;
        this.vista = vista;
        
        // Registrar este controlador como observador del modelo
        modelo.agregarObservador(new ModeloNonograma.ObservadorModelo() {
            @Override
            public void alCambiarModelo() {
                actualizarVista();
            }
        });
    }
    
    // Maneja el clic en una celda de la grilla.
    public void manejarClicCelda(int fila, int columna) {
        if (modelo.obtenerEstadoJuego() != EstadoJuego.JUGANDO) {
            return; // No permitir cambios si el juego terminó
        }
        
        // Verificar si la celda ha sido revelada como pista
        if (modelo.esCeldaRevelada(fila, columna)) {
            return; // No permitir cambios en celdas reveladas como pista
        }
        
        EstadoCelda estadoActual = modelo.obtenerEstadoCelda(fila, columna);
        EstadoCelda nuevoEstado;
        
        // Clic: cicla entre VACIA -> LLENA -> VACIA
        switch (estadoActual) {
            case VACIA:
                nuevoEstado = EstadoCelda.LLENA;
                break;
            case LLENA:
                nuevoEstado = EstadoCelda.VACIA;
                break;
            default:
                nuevoEstado = EstadoCelda.LLENA;
        }
        
        modelo.establecerEstadoCelda(fila, columna, nuevoEstado);
    }
    
    // Inicia un nuevo juego.
    public void nuevoJuego() {
        modelo.nuevoJuego();
    }
    
    // Reinicia el juego actual.
    public void reiniciarJuego() {
        modelo.reiniciarJuego();
    }
    
    // Verifica la solución actual del jugador.
    public void verificarSolucion() {
        // Verificar si la solución es correcta
        if (modelo.verificarSolucion()) {
            vista.mostrarJuegoGanado();
        } else {
            vista.mostrarJuegoPerdido();
        }
    }
    
    // Muestra la solución del puzzle.
    public void mostrarSolucion() {
        vista.mostrarSolucion(modelo);
    }
    
    // Solicita una pista al modelo.
    // 
    // @return true si se pudo revelar una pista, false si no hay pistas disponibles
    public boolean solicitarPista() {
        return modelo.solicitarPista();
    }
    
    // Cambia el nivel de dificultad del juego.
    // 
    // @param nivel Nuevo nivel de dificultad
    public void cambiarNivel(NivelDificultad nivel) {
        modelo.cambiarNivel(nivel);
    }
    
    // Obtiene el número de pistas disponibles.
    // 
    // @return Número de pistas disponibles
    public int obtenerPistasDisponibles() {
        return modelo.obtenerPistasDisponibles();
    }
    

    
    // Obtiene el nivel de dificultad actual.
    // 
    // @return Nivel de dificultad actual
    public NivelDificultad obtenerNivelActual() {
        return modelo.obtenerNivelActual();
    }
    
    // Obtiene todos los niveles disponibles.
    // 
    // @return Array con todos los niveles
    public NivelDificultad[] obtenerNivelesDisponibles() {
        return ModeloNonograma.obtenerNivelesDisponibles();
    }
    

    
    // Actualiza la vista con los datos del modelo.
    private void actualizarVista() {
        vista.actualizarVisualizacion();
    }
    


    // Obtiene el modelo del juego.
    // 
    // @return El modelo del juego
    public ModeloNonograma obtenerModelo() {
        return modelo;
    }
}
