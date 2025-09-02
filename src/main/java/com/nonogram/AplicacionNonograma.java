package com.nonogram;

import com.nonogram.controlador.ControladorNonograma;
import com.nonogram.model.ModeloNonograma;
import com.nonogram.model.NivelDificultad;
import com.nonogram.vista.VistaNonograma;
import com.nonogram.vista.VistaNonogramaWindowBuilderNuevo;

import javax.swing.SwingUtilities;

public class AplicacionNonograma {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            inicializarAplicacion();
        });
    }
    
    private static void inicializarAplicacion() {

        
        // Crear el modelo (lógica del juego) - nivel fácil por defecto
        ModeloNonograma modelo = new ModeloNonograma(NivelDificultad.FACIL);
        
        // Crear la vista (interfaz gráfica)
        VistaNonograma vista = new VistaNonogramaWindowBuilderNuevo();
        
        // Crear el controlador (manejo de eventos)
        ControladorNonograma controlador = new ControladorNonograma(modelo, vista);
        
        // Conectar la vista con el controlador
        vista.establecerControlador(controlador);
        
        // Iniciar un nuevo juego
        modelo.nuevoJuego();
        
        // Actualizar la visualización inicial
        vista.actualizarVisualizacion();
        

    }
}