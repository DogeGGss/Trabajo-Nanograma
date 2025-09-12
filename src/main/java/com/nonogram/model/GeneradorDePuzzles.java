package com.nonogram.model;

public class GeneradorDePuzzles {


	public void generarSolucionAleatoria(ModeloNonograma modelo) {
        // Limpiar solución
    	limpiarSolucion(modelo);
        
        // Elegir un algoritmo aleatorio según el tamaño
        int algoritmo = (int)(Math.random() * 5); // 0-4 algoritmos diferentes
        
        switch (algoritmo) {
            case 0: generarPuzzleConFormasGeometricas(modelo); break;
            case 1: generarPuzzleConPatronesSimetricos(modelo); break;
            case 2: generarPuzzleConSecuenciasLogicas(modelo); break;
            case 3: generarPuzzleConDensidadVariable(modelo); break;
            case 4: generarPuzzleConBordesYCentro(modelo); break;
        }
    }
	
	private void limpiarSolucion(ModeloNonograma modelo) {
		// TODO Auto-generated method stub
    	for (int fila = 0; fila < modelo.obtenerTamañoGrilla(); fila++) {
            for (int columna = 0; columna < modelo.obtenerTamañoGrilla(); columna++) {
                modelo.setGrillaSolucion(fila, columna, EstadoCelda.VACIA);
            }
        }
		
	}
	
	// Algoritmo 1: Genera puzzles con formas geométricas básicas
    private void generarPuzzleConFormasGeometricas(ModeloNonograma modelo) {
        int centro = modelo.obtenerTamañoGrilla() / 2;
        
        // Crear diferentes formas según el tamaño
        if (modelo.obtenerTamañoGrilla() <= 5) {
            // Cuadrado en el centro
            for (int fila = centro - 1; fila <= centro + 1; fila++) {
                for (int col = centro - 1; col <= centro + 1; col++) {
                    if (fila >= 0 && fila < modelo.obtenerTamañoGrilla() && col >= 0 && col < modelo.obtenerTamañoGrilla()) {
                        modelo.setGrillaSolucion(fila, col, EstadoCelda.LLENA);
                    }
                }
            }
        } else if (modelo.obtenerTamañoGrilla() <= 10) {
            // Cruz
            for (int i = 0; i < modelo.obtenerTamañoGrilla(); i++) {
            	modelo.setGrillaSolucion(centro, i, EstadoCelda.LLENA);
            	modelo.setGrillaSolucion(i, centro, EstadoCelda.LLENA);
            }
        } else {
            // Diamante
            for (int fila = 0; fila < modelo.obtenerTamañoGrilla(); fila++) {
                for (int col = 0; col < modelo.obtenerTamañoGrilla(); col++) {
                    int distancia = Math.abs(fila - centro) + Math.abs(col - centro);
                    if (distancia <= modelo.obtenerTamañoGrilla() / 4) {
                    	modelo.setGrillaSolucion(fila, col, EstadoCelda.LLENA);                    }
                }
            }
        }
        
        // Asegurar que cada fila y columna tenga al menos una celda llena y una vacía
        asegurarValidezPuzzle(modelo);
    }
    
    // Algoritmo 2: Genera puzzles con patrones simétricos
    private void generarPuzzleConPatronesSimetricos(ModeloNonograma modelo) {
        // Patrón simétrico horizontal y vertical
        for (int fila = 0; fila < modelo.obtenerTamañoGrilla(); fila++) {
            for (int col = 0; col < modelo.obtenerTamañoGrilla(); col++) {
                // Simetría horizontal y vertical
                if ((fila + col) % 2 == 0 && (fila < modelo.obtenerTamañoGrilla() / 2 || col < modelo.obtenerTamañoGrilla() / 2)) {
                	modelo.setGrillaSolucion(fila, col, EstadoCelda.LLENA);
                }
                // Simetría diagonal
                if (fila == col || fila == modelo.obtenerTamañoGrilla() - 1 - col) {
                	modelo.setGrillaSolucion(fila, col, EstadoCelda.LLENA);
                }
            }
        }
        
        // Asegurar que cada fila y columna tenga al menos una celda llena y una vacía
        asegurarValidezPuzzle(modelo);
    }
    
    // Algoritmo 3: Genera puzzles con secuencias lógicas
    private void generarPuzzleConSecuenciasLogicas(ModeloNonograma modelo) {
        // Secuencias basadas en patrones matemáticos
        for (int fila = 0; fila < modelo.obtenerTamañoGrilla(); fila++) {
            for (int col = 0; col < modelo.obtenerTamañoGrilla(); col++) {
                // Patrón de Fibonacci
                if ((fila + col) % 3 == 0 && (fila * col) % 2 == 0) {
                	modelo.setGrillaSolucion(fila, col, EstadoCelda.LLENA);
                }
                // Patrón de números primos
                if (esPrimo(fila + col) && (fila + col) > 1) {
                	modelo.setGrillaSolucion(fila, col, EstadoCelda.LLENA);
                }
            }
        }
        
        // Asegurar que cada fila y columna tenga al menos una celda llena y una vacía
        asegurarValidezPuzzle(modelo);
    }
 // Verifica si un número es primo
    private boolean esPrimo(int numero) {
        if (numero <= 1) return false;
        if (numero <= 3) return true;
        if (numero % 2 == 0 || numero % 3 == 0) return false;
        
        for (int i = 5; i * i <= numero; i += 6) {
            if (numero % i == 0 || numero % (i + 2) == 0) {
                return false;
            }
        }
        return true;
    }
    
    // Algoritmo 4: Genera puzzles con densidad variable
    private void generarPuzzleConDensidadVariable(ModeloNonograma modelo) {
        // Mayor densidad en el centro, menor en los bordes
        for (int fila = 0; fila < modelo.obtenerTamañoGrilla(); fila++) {
            for (int col = 0; col < modelo.obtenerTamañoGrilla(); col++) {
                double distanciaAlCentro = Math.sqrt(Math.pow(fila - modelo.obtenerTamañoGrilla()/2.0, 2) + 
                                                   Math.pow(col - modelo.obtenerTamañoGrilla()/2.0, 2));
                double probabilidad = Math.max(0.1, 1.0 - distanciaAlCentro / (modelo.obtenerTamañoGrilla()/2.0));
                
                if (Math.random() < probabilidad) {
                	modelo.setGrillaSolucion(fila, col, EstadoCelda.LLENA);                }
            }
        }
        
        // Asegurar que cada fila y columna tenga al menos una celda llena y una vacía
        asegurarValidezPuzzle(modelo);
    }
    
 // Algoritmo 5: Genera puzzles con bordes y centro
    private void generarPuzzleConBordesYCentro(ModeloNonograma modelo) {
        // Bordes
        for (int i = 0; i < modelo.obtenerTamañoGrilla(); i++) {
            modelo.setGrillaSolucion(0, i, EstadoCelda.LLENA);
            modelo.setGrillaSolucion(modelo.obtenerTamañoGrilla()-1, i, EstadoCelda.LLENA);
            modelo.setGrillaSolucion(i, 0, EstadoCelda.LLENA);
            modelo.setGrillaSolucion(i, modelo.obtenerTamañoGrilla()-1, EstadoCelda.LLENA);
        }
        
        // Centro
        int centro = modelo.obtenerTamañoGrilla() / 2;
        int radio = Math.max(1, modelo.obtenerTamañoGrilla() / 6);
        for (int fila = centro - radio; fila <= centro + radio; fila++) {
            for (int col = centro - radio; col <= centro + radio; col++) {
                if (fila >= 0 && fila < modelo.obtenerTamañoGrilla() && col >= 0 && col < modelo.obtenerTamañoGrilla()) {
                    modelo.setGrillaSolucion(fila, col, EstadoCelda.LLENA);
                }
            }
        }
        
        // Asegurar que cada fila y columna tenga al menos una celda llena y una vacía
        asegurarValidezPuzzle(modelo);
    }
    
 // Asegura que cada fila y columna tenga al menos una celda llena y una vacía
    private void asegurarValidezPuzzle(ModeloNonograma modelo) {
        int n = modelo.obtenerTamañoGrilla();
        // filas
        for (int fila = 0; fila < n; fila++) {
            int llenas = 0;
            int vacias = 0;
            for (int col = 0; col < n; col++) {
                if (modelo.getGrillaSolucion(fila, col) == EstadoCelda.LLENA) llenas++;
                else vacias++;
            }
            if (llenas == 0) modelo.setGrillaSolucion(fila, n/2, EstadoCelda.LLENA);
            else if (vacias == 0) modelo.setGrillaSolucion(fila, n/2, EstadoCelda.VACIA);
        }
        // columnas (una sola vez)
        for (int col = 0; col < n; col++) {
            int llenas = 0;
            int vacias = 0;
            for (int fila = 0; fila < n; fila++) {
                if (modelo.getGrillaSolucion(fila, col) == EstadoCelda.LLENA) llenas++;
                else vacias++;
            }
            if (llenas == 0) modelo.setGrillaSolucion(n/2, col, EstadoCelda.LLENA);
            else if (vacias == 0) modelo.setGrillaSolucion(n/2, col, EstadoCelda.VACIA);
        }
    }
}
