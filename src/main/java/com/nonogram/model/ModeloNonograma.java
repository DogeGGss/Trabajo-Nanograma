package com.nonogram.model;

import java.util.ArrayList;
import java.util.List;

// Modelo del Nonograma que implementa la lógica del juego.
// 
// Este modelo implementa:
// - La lógica del juego Nonograma
// - El patrón Observer para notificar cambios
// - El estado de la grilla y las reglas del juego
// - Generación de puzzles y validación de soluciones
// 
// Patrón Observer: El modelo notifica a los observadores (vista) cuando
// hay cambios en el estado del juego.
public class ModeloNonograma {
    
    private int tamañoGrilla;
    private EstadoCelda[][] grillaJuego;      // Grilla actual del jugador
    private EstadoCelda[][] grillaSolucion;   // Grilla con la solución
    private List<List<Integer>> pistasFilas;  // Pistas de las filas
    private List<List<Integer>> pistasColumnas; // Pistas de las columnas
    private EstadoJuego estadoJuego;
    private List<ObservadorModelo> observadores; // Lista de observadores (patrón Observer)
    
    // Nuevas funcionalidades
    private int pistasDisponibles;            // Número de pistas disponibles
    private boolean[][] celdasReveladas;      // Celdas que han sido reveladas como pista
    private NivelDificultad nivelActual;      // Nivel de dificultad actual
    
    // Constructor del modelo del Nonograma.
    // 
    // @param tamañoGrilla Tamaño de la grilla (por defecto 5x5)
    public ModeloNonograma(int tamañoGrilla) {
        this.tamañoGrilla = tamañoGrilla;
        this.estadoJuego = EstadoJuego.JUGANDO;
        this.observadores = new ArrayList<>();
        
        // Inicializar nuevas funcionalidades
        this.pistasDisponibles = 3; // 3 pistas disponibles por defecto
        this.celdasReveladas = new boolean[tamañoGrilla][tamañoGrilla];
        
        // Inicializar grillas
        inicializarGrillas();
    }
    
    // Constructor por defecto con grilla 5x5.
    public ModeloNonograma() {
        this(NivelDificultad.FACIL);
    }
    
    // Constructor con nivel de dificultad específico.
    // 
    // @param nivel Nivel de dificultad del juego
    public ModeloNonograma(NivelDificultad nivel) {
        this(nivel.obtenerTamañoGrilla());
        this.nivelActual = nivel;
        this.pistasDisponibles = nivel.obtenerPistasDisponibles();
    }
    
    // Interfaz para los observadores del modelo (patrón Observer).
    public interface ObservadorModelo {
        void alCambiarModelo();
    }
    
    // Agrega un observador al modelo (patrón Observer).
    // 
    // @param observador Observador que será notificado de cambios
    public void agregarObservador(ObservadorModelo observador) {
        if (!observadores.contains(observador)) {
            observadores.add(observador);
        }
    }
    
    // Remueve un observador del modelo.
    // 
    // @param observador Observador a remover
    public void removerObservador(ObservadorModelo observador) {
        observadores.remove(observador);
    }
    
    // Notifica a todos los observadores sobre cambios en el modelo.
    // Este método implementa el patrón Observer.
    private void notificarObservadores() {
        for (ObservadorModelo observador : observadores) {
            observador.alCambiarModelo();
        }
    }
    
    // Inicializa las grillas del juego y solución.
    private void inicializarGrillas() {
        grillaJuego = new EstadoCelda[tamañoGrilla][tamañoGrilla];
        grillaSolucion = new EstadoCelda[tamañoGrilla][tamañoGrilla];
        
        for (int fila = 0; fila < tamañoGrilla; fila++) {
            for (int columna = 0; columna < tamañoGrilla; columna++) {
                grillaJuego[fila][columna] = EstadoCelda.VACIA;
                grillaSolucion[fila][columna] = EstadoCelda.VACIA;
            }
        }
    }
    
    // Limpia solo la grilla del jugador, manteniendo la solución intacta.
    private void limpiarGrillaJugador() {
        for (int fila = 0; fila < tamañoGrilla; fila++) {
            for (int columna = 0; columna < tamañoGrilla; columna++) {
                grillaJuego[fila][columna] = EstadoCelda.VACIA;
            }
        }
    }
    
    // Inicia un nuevo juego generando un puzzle aleatorio.
    // Notifica a los observadores sobre el cambio.
    public void nuevoJuego() {
        generarPuzzle();
        estadoJuego = EstadoJuego.JUGANDO;
        
        // Reiniciar pistas disponibles según el nivel actual
        if (nivelActual != null) {
            pistasDisponibles = nivelActual.obtenerPistasDisponibles();
        } else {
            pistasDisponibles = 3; // Valor por defecto
        }
        
        // Limpiar celdas reveladas
        limpiarCeldasReveladas();
        
        notificarObservadores();
    }
    
    /**
     * Genera un puzzle aleatorio para el Nonograma.
     * Crea una solución válida y genera las pistas correspondientes.
     */
    private void generarPuzzle() {
        // Generar solución aleatoria
        generarSolucionAleatoria();
        
        // Generar pistas basadas en la solución
        generarPistas();
        
        // Limpiar SOLO la grilla del jugador (NO la solución)
        limpiarGrillaJugador();
    }
    
    /**
     * Genera una solución aleatoria válida para el Nonograma.
     * Utiliza diferentes algoritmos para crear puzzles variados y solucionables.
     */
    private void generarSolucionAleatoria() {
        // Limpiar solución
        for (int fila = 0; fila < tamañoGrilla; fila++) {
            for (int columna = 0; columna < tamañoGrilla; columna++) {
                grillaSolucion[fila][columna] = EstadoCelda.VACIA;
            }
        }
        
        // Elegir un algoritmo aleatorio según el tamaño
        int algoritmo = (int)(Math.random() * 10); // 0-9 algoritmos diferentes
        
        switch (algoritmo) {
            case 0:
                generarPuzzleConFormasGeometricas();
                break;
            case 1:
                generarPuzzleConPatronesSimetricos();
                break;
            case 2:
                generarPuzzleConSecuenciasLogicas();
                break;
            case 3:
                generarPuzzleConDensidadVariable();
                break;
            case 4:
                generarPuzzleConBordesYCentro();
                break;
            case 5:
                generarPuzzleConPatronesComplejos();
                break;
            case 6:
                generarPuzzleConLetrasYSímbolos();
                break;
            case 7:
                generarPuzzleConAnimalesYObjetos();
                break;
            case 8:
                generarPuzzleConLaberintos();
                break;
            case 9:
                generarPuzzleConFractales();
                break;
        }
        
        // Asegurar que el puzzle tenga al menos algunas celdas llenas
        if (contarCeldasLlenas() < tamañoGrilla) {
            generarPuzzleConFormasGeometricas(); // Fallback
        }
        
        // Asegurar que el puzzle sea solucionable (no demasiado denso ni demasiado vacío)
        int celdasLlenas = contarCeldasLlenas();
        int totalCeldas = tamañoGrilla * tamañoGrilla;
        double densidad = (double) celdasLlenas / totalCeldas;
        
        // Si la densidad es muy baja o muy alta, regenerar
        if (densidad < 0.1 || densidad > 0.8) {
            generarSolucionAleatoria(); // Recursión limitada
        }
    }
    

    
    /**
     * Cuenta el número de celdas llenas en la solución
     */
    private int contarCeldasLlenas() {
        int contador = 0;
        for (int fila = 0; fila < tamañoGrilla; fila++) {
            for (int col = 0; col < tamañoGrilla; col++) {
                if (grillaSolucion[fila][col] == EstadoCelda.LLENA) {
                    contador++;
                }
            }
        }
        return contador;
    }
    
    /**
     * Algoritmo 1: Genera puzzles con formas geométricas básicas
     */
    private void generarPuzzleConFormasGeometricas() {
        int centro = tamañoGrilla / 2;
        
        // Crear diferentes formas según el tamaño
        if (tamañoGrilla <= 5) {
            // Cuadrado en el centro
            for (int fila = centro - 1; fila <= centro + 1; fila++) {
                for (int col = centro - 1; col <= centro + 1; col++) {
                    if (fila >= 0 && fila < tamañoGrilla && col >= 0 && col < tamañoGrilla) {
                        grillaSolucion[fila][col] = EstadoCelda.LLENA;
                    }
                }
            }
        } else if (tamañoGrilla <= 10) {
            // Cruz
            for (int i = 0; i < tamañoGrilla; i++) {
                grillaSolucion[centro][i] = EstadoCelda.LLENA;
                grillaSolucion[i][centro] = EstadoCelda.LLENA;
            }
        } else {
            // Diamante
            for (int fila = 0; fila < tamañoGrilla; fila++) {
                for (int col = 0; col < tamañoGrilla; col++) {
                    int distancia = Math.abs(fila - centro) + Math.abs(col - centro);
                    if (distancia <= tamañoGrilla / 4) {
                        grillaSolucion[fila][col] = EstadoCelda.LLENA;
                    }
                }
            }
        }
    }
    
    /**
     * Algoritmo 2: Genera puzzles con patrones simétricos
     */
    private void generarPuzzleConPatronesSimetricos() {
        // Patrón simétrico horizontal y vertical
        for (int fila = 0; fila < tamañoGrilla; fila++) {
            for (int col = 0; col < tamañoGrilla; col++) {
                // Simetría horizontal y vertical
                if ((fila + col) % 2 == 0 && (fila < tamañoGrilla / 2 || col < tamañoGrilla / 2)) {
                    grillaSolucion[fila][col] = EstadoCelda.LLENA;
                }
                // Simetría diagonal
                if (fila == col || fila == tamañoGrilla - 1 - col) {
                    grillaSolucion[fila][col] = EstadoCelda.LLENA;
                }
            }
        }
    }
    
    /**
     * Algoritmo 3: Genera puzzles con secuencias lógicas
     */
    private void generarPuzzleConSecuenciasLogicas() {
        // Secuencias basadas en patrones matemáticos
        for (int fila = 0; fila < tamañoGrilla; fila++) {
            for (int col = 0; col < tamañoGrilla; col++) {
                // Patrón de Fibonacci
                if ((fila + col) % 3 == 0 && (fila * col) % 2 == 0) {
                    grillaSolucion[fila][col] = EstadoCelda.LLENA;
                }
                // Patrón de números primos
                if (esPrimo(fila + col) && (fila + col) > 1) {
                    grillaSolucion[fila][col] = EstadoCelda.LLENA;
                }
            }
        }
    }
    
    /**
     * Algoritmo 4: Genera puzzles con densidad variable
     */
    private void generarPuzzleConDensidadVariable() {
        // Mayor densidad en el centro, menor en los bordes
        for (int fila = 0; fila < tamañoGrilla; fila++) {
            for (int col = 0; col < tamañoGrilla; col++) {
                double distanciaAlCentro = Math.sqrt(Math.pow(fila - tamañoGrilla/2.0, 2) + 
                                                   Math.pow(col - tamañoGrilla/2.0, 2));
                double probabilidad = Math.max(0.1, 1.0 - distanciaAlCentro / (tamañoGrilla/2.0));
                
                if (Math.random() < probabilidad) {
                    grillaSolucion[fila][col] = EstadoCelda.LLENA;
                }
            }
        }
    }
    
    /**
     * Algoritmo 5: Genera puzzles con bordes y centro
     */
    private void generarPuzzleConBordesYCentro() {
        // Bordes
        for (int i = 0; i < tamañoGrilla; i++) {
            grillaSolucion[0][i] = EstadoCelda.LLENA;
            grillaSolucion[tamañoGrilla-1][i] = EstadoCelda.LLENA;
            grillaSolucion[i][0] = EstadoCelda.LLENA;
            grillaSolucion[i][tamañoGrilla-1] = EstadoCelda.LLENA;
        }
        
        // Centro
        int centro = tamañoGrilla / 2;
        int radio = Math.max(1, tamañoGrilla / 6);
        for (int fila = centro - radio; fila <= centro + radio; fila++) {
            for (int col = centro - radio; col <= centro + radio; col++) {
                if (fila >= 0 && fila < tamañoGrilla && col >= 0 && col < tamañoGrilla) {
                    grillaSolucion[fila][col] = EstadoCelda.LLENA;
                }
            }
        }
    }
    
    /**
     * Verifica si un número es primo
     */
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
    
    /**
     * Algoritmo 6: Genera puzzles con patrones complejos y variados
     */
    private void generarPuzzleConPatronesComplejos() {
        // Combinación de múltiples patrones
        for (int fila = 0; fila < tamañoGrilla; fila++) {
            for (int col = 0; col < tamañoGrilla; col++) {
                boolean llenar = false;
                
                // Patrón de ondas
                if (Math.sin(fila * 0.5) > 0.3 && Math.cos(col * 0.5) > 0.3) {
                    llenar = true;
                }
                
                // Patrón de espiral
                int distanciaAlCentro = Math.abs(fila - tamañoGrilla/2) + Math.abs(col - tamañoGrilla/2);
                if (distanciaAlCentro % 3 == 0 && (fila + col) % 2 == 0) {
                    llenar = true;
                }
                
                // Patrón de cuadrados anidados
                if ((fila % 4 == 0 || fila % 4 == 3) && (col % 4 == 0 || col % 4 == 3)) {
                    llenar = true;
                }
                
                // Patrón de números de Fibonacci
                if (esFibonacci(fila + col)) {
                    llenar = true;
                }
                
                if (llenar) {
                    grillaSolucion[fila][col] = EstadoCelda.LLENA;
                }
            }
        }
    }
    
    /**
     * Verifica si un número está en la secuencia de Fibonacci
     */
    private boolean esFibonacci(int numero) {
        if (numero <= 1) return true;
        
        int a = 0, b = 1;
        while (b <= numero) {
            if (b == numero) return true;
            int temp = a + b;
            a = b;
            b = temp;
        }
        return false;
    }
    
    /**
     * Algoritmo 7: Genera puzzles con letras y símbolos
     */
    private void generarPuzzleConLetrasYSímbolos() {
        // Generar diferentes letras según el tamaño
        if (tamañoGrilla <= 5) {
            generarLetraX();
        } else if (tamañoGrilla <= 10) {
            generarLetraT();
        } else if (tamañoGrilla <= 15) {
            generarLetraH();
        } else {
            generarLetraE();
        }
    }
    
    /**
     * Genera la letra X
     */
    private void generarLetraX() {
        for (int i = 0; i < tamañoGrilla; i++) {
            grillaSolucion[i][i] = EstadoCelda.LLENA; // Diagonal principal
            grillaSolucion[i][tamañoGrilla - 1 - i] = EstadoCelda.LLENA; // Diagonal secundaria
        }
    }
    
    /**
     * Genera la letra T
     */
    private void generarLetraT() {
        // Línea horizontal superior
        for (int col = 0; col < tamañoGrilla; col++) {
            grillaSolucion[0][col] = EstadoCelda.LLENA;
        }
        // Línea vertical central
        for (int fila = 0; fila < tamañoGrilla; fila++) {
            grillaSolucion[fila][tamañoGrilla / 2] = EstadoCelda.LLENA;
        }
    }
    
    /**
     * Genera la letra H
     */
    private void generarLetraH() {
        // Líneas verticales
        for (int fila = 0; fila < tamañoGrilla; fila++) {
            grillaSolucion[fila][0] = EstadoCelda.LLENA;
            grillaSolucion[fila][tamañoGrilla - 1] = EstadoCelda.LLENA;
        }
        // Línea horizontal central
        for (int col = 0; col < tamañoGrilla; col++) {
            grillaSolucion[tamañoGrilla / 2][col] = EstadoCelda.LLENA;
        }
    }
    
    /**
     * Genera la letra E
     */
    private void generarLetraE() {
        // Línea vertical izquierda
        for (int fila = 0; fila < tamañoGrilla; fila++) {
            grillaSolucion[fila][0] = EstadoCelda.LLENA;
        }
        // Líneas horizontales
        for (int col = 0; col < tamañoGrilla; col++) {
            grillaSolucion[0][col] = EstadoCelda.LLENA; // Superior
            grillaSolucion[tamañoGrilla / 2][col] = EstadoCelda.LLENA; // Central
            grillaSolucion[tamañoGrilla - 1][col] = EstadoCelda.LLENA; // Inferior
        }
    }
    
    /**
     * Algoritmo 8: Genera puzzles con animales y objetos
     */
    private void generarPuzzleConAnimalesYObjetos() {
        // Generar diferentes objetos según el tamaño
        if (tamañoGrilla <= 5) {
            generarCorazon();
        } else if (tamañoGrilla <= 10) {
            generarEstrella();
        } else if (tamañoGrilla <= 15) {
            generarCasa();
        } else {
            generarArbol();
        }
    }
    
    /**
     * Genera un corazón
     */
    private void generarCorazon() {
        int centro = tamañoGrilla / 2;
        // Lados del corazón
        for (int i = 0; i < 3; i++) {
            if (centro - i >= 0 && centro + i < tamañoGrilla) {
                grillaSolucion[centro - i][centro - i] = EstadoCelda.LLENA;
                grillaSolucion[centro - i][centro + i] = EstadoCelda.LLENA;
            }
        }
        // Base del corazón
        for (int i = 0; i < 3; i++) {
            if (centro + i < tamañoGrilla) {
                grillaSolucion[centro + i][centro] = EstadoCelda.LLENA;
            }
        }
    }
    
    /**
     * Genera una estrella
     */
    private void generarEstrella() {
        int centro = tamañoGrilla / 2;
        // Puntas de la estrella
        for (int i = 0; i < tamañoGrilla; i++) {
            if (i % 2 == 0) {
                grillaSolucion[0][i] = EstadoCelda.LLENA; // Superior
                grillaSolucion[tamañoGrilla - 1][i] = EstadoCelda.LLENA; // Inferior
                grillaSolucion[i][0] = EstadoCelda.LLENA; // Izquierda
                grillaSolucion[i][tamañoGrilla - 1] = EstadoCelda.LLENA; // Derecha
            }
        }
        // Centro de la estrella
        grillaSolucion[centro][centro] = EstadoCelda.LLENA;
    }
    
    /**
     * Genera una casa
     */
    private void generarCasa() {
        // Techo triangular
        int alturaTecho = tamañoGrilla / 3;
        for (int fila = 0; fila < alturaTecho; fila++) {
            for (int col = fila; col < tamañoGrilla - fila; col++) {
                grillaSolucion[fila][col] = EstadoCelda.LLENA;
            }
        }
        // Cuerpo de la casa
        for (int fila = alturaTecho; fila < tamañoGrilla; fila++) {
            grillaSolucion[fila][0] = EstadoCelda.LLENA;
            grillaSolucion[fila][tamañoGrilla - 1] = EstadoCelda.LLENA;
        }
        // Base
        for (int col = 0; col < tamañoGrilla; col++) {
            grillaSolucion[tamañoGrilla - 1][col] = EstadoCelda.LLENA;
        }
    }
    
    /**
     * Genera un árbol
     */
    private void generarArbol() {
        int centro = tamañoGrilla / 2;
        // Tronco
        for (int fila = tamañoGrilla / 2; fila < tamañoGrilla; fila++) {
            grillaSolucion[fila][centro] = EstadoCelda.LLENA;
            if (centro + 1 < tamañoGrilla) {
                grillaSolucion[fila][centro + 1] = EstadoCelda.LLENA;
            }
        }
        // Copa triangular
        int alturaCopa = tamañoGrilla / 2;
        for (int fila = 0; fila < alturaCopa; fila++) {
            int ancho = alturaCopa - fila;
            for (int col = centro - ancho; col <= centro + ancho; col++) {
                if (col >= 0 && col < tamañoGrilla) {
                    grillaSolucion[fila][col] = EstadoCelda.LLENA;
                }
            }
        }
    }
    
    /**
     * Algoritmo 9: Genera puzzles con laberintos
     */
    private void generarPuzzleConLaberintos() {
        // Generar un laberinto simple
        for (int fila = 0; fila < tamañoGrilla; fila++) {
            for (int col = 0; col < tamañoGrilla; col++) {
                // Bordes del laberinto
                if (fila == 0 || fila == tamañoGrilla - 1 || col == 0 || col == tamañoGrilla - 1) {
                    grillaSolucion[fila][col] = EstadoCelda.LLENA;
                }
                // Paredes internas en patrón de laberinto
                else if ((fila % 2 == 0 && col % 3 == 0) || (col % 2 == 0 && fila % 3 == 0)) {
                    grillaSolucion[fila][col] = EstadoCelda.LLENA;
                }
                // Pasillos aleatorios
                else if (Math.random() < 0.3) {
                    grillaSolucion[fila][col] = EstadoCelda.LLENA;
                }
            }
        }
    }
    
    /**
     * Algoritmo 10: Genera puzzles con fractales
     */
    private void generarPuzzleConFractales() {
        // Generar un patrón fractal simple (Sierpinski-like)
        for (int fila = 0; fila < tamañoGrilla; fila++) {
            for (int col = 0; col < tamañoGrilla; col++) {
                // Patrón de Sierpinski modificado
                if ((fila & col) == 0) {
                    grillaSolucion[fila][col] = EstadoCelda.LLENA;
                }
                // Patrón de Mandelbrot simplificado
                else if (Math.abs(Math.sin(fila * 0.5) * Math.cos(col * 0.5)) > 0.5) {
                    grillaSolucion[fila][col] = EstadoCelda.LLENA;
                }
            }
        }
    }
    
    /**
     * Genera las pistas (hints) basadas en la solución.
     * Las pistas indican las longitudes de las secuencias de celdas llenas.
     */
    private void generarPistas() {
        // Generar pistas de filas
        pistasFilas = new ArrayList<>();
        for (int fila = 0; fila < tamañoGrilla; fila++) {
            List<Integer> pistas = obtenerPistasFila(fila);
            pistasFilas.add(pistas);
        }
        
        // Generar pistas de columnas
        pistasColumnas = new ArrayList<>();
        for (int columna = 0; columna < tamañoGrilla; columna++) {
            List<Integer> pistas = obtenerPistasColumna(columna);
            pistasColumnas.add(pistas);
        }
    }
    
    /**
     * Obtiene las pistas para una fila específica.
     * 
     * @param fila Índice de la fila
     * @return Lista con las longitudes de las secuencias de celdas llenas
     */
    private List<Integer> obtenerPistasFila(int fila) {
        List<Integer> pistas = new ArrayList<>();
        int contadorActual = 0;
        
        for (int columna = 0; columna < tamañoGrilla; columna++) {
            if (grillaSolucion[fila][columna] == EstadoCelda.LLENA) {
                contadorActual++;
            } else if (contadorActual > 0) {
                pistas.add(contadorActual);
                contadorActual = 0;
            }
        }
        
        // Agregar el último grupo si existe
        if (contadorActual > 0) {
            pistas.add(contadorActual);
        }
        
        return pistas.isEmpty() ? java.util.Arrays.asList(0) : pistas;
    }
    
    /**
     * Obtiene las pistas para una columna específica.
     * 
     * @param columna Índice de la columna
     * @return Lista con las longitudes de las secuencias de celdas llenas
     */
    private List<Integer> obtenerPistasColumna(int columna) {
        List<Integer> pistas = new ArrayList<>();
        int contadorActual = 0;
        
        for (int fila = 0; fila < tamañoGrilla; fila++) {
            if (grillaSolucion[fila][columna] == EstadoCelda.LLENA) {
                contadorActual++;
            } else if (contadorActual > 0) {
                pistas.add(contadorActual);
                contadorActual = 0;
            }
        }
        
        // Agregar el último grupo si existe
        if (contadorActual > 0) {
            pistas.add(contadorActual);
        }
        
        return pistas.isEmpty() ? java.util.Arrays.asList(0) : pistas;
    }
    
    /**
     * Establece el estado de una celda en la grilla del jugador.
     * 
     * @param fila Fila de la celda
     * @param columna Columna de la celda
     * @param estado Nuevo estado de la celda
     */
    public void establecerEstadoCelda(int fila, int columna, EstadoCelda estado) {
        if (fila >= 0 && fila < tamañoGrilla && columna >= 0 && columna < tamañoGrilla &&
            estadoJuego == EstadoJuego.JUGANDO) {
            
            grillaJuego[fila][columna] = estado;
            // NO verificar automáticamente - solo cuando se presione el botón
            notificarObservadores();
        }
    }
    

    
    /**
     * Verifica si la solución actual es correcta.
     * Solo es correcta si TODAS las celdas están correctamente marcadas.
     * 
     * @return true si la solución es correcta
     */
    private boolean esSolucionCorrecta() {
        for (int fila = 0; fila < tamañoGrilla; fila++) {
            for (int columna = 0; columna < tamañoGrilla; columna++) {
                EstadoCelda estadoJugador = grillaJuego[fila][columna];
                EstadoCelda estadoSolucion = grillaSolucion[fila][columna];
                
                // Si la celda debe estar llena en la solución
                if (estadoSolucion == EstadoCelda.LLENA) {
                    // El jugador debe haberla marcado como llena
                    if (estadoJugador != EstadoCelda.LLENA) {
                        return false;
                    }
                } else {
                    // Si la celda debe estar vacía en la solución
                    // El jugador puede tenerla vacía o marcada con X
                    if (estadoJugador == EstadoCelda.LLENA) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    /**
     * Verifica si la solución actual es correcta (método público).
     * 
     * @return true si la solución es correcta
     */
    public boolean verificarSolucion() {
        return esSolucionCorrecta();
    }
    

    
    /**
     * Obtiene el estado actual de una celda.
     * 
     * @param fila Fila de la celda
     * @param columna Columna de la celda
     * @return Estado actual de la celda
     */
    public EstadoCelda obtenerEstadoCelda(int fila, int columna) {
        if (fila >= 0 && fila < tamañoGrilla && columna >= 0 && columna < tamañoGrilla) {
            return grillaJuego[fila][columna];
        }
        return EstadoCelda.VACIA;
    }
    
    /**
     * Obtiene el estado de una celda en la solución.
     * 
     * @param fila Fila de la celda
     * @param columna Columna de la celda
     * @return Estado de la celda en la solución
     */
    public EstadoCelda obtenerEstadoCeldaSolucion(int fila, int columna) {
        if (fila >= 0 && fila < tamañoGrilla && columna >= 0 && columna < tamañoGrilla) {
            return grillaSolucion[fila][columna];
        }
        return EstadoCelda.VACIA;
    }
    

    

    
    /**
     * Obtiene las pistas de todas las filas.
     * 
     * @return Lista de pistas por fila
     */
    public List<List<Integer>> obtenerPistasFilas() {
        return pistasFilas;
    }
    
    /**
     * Obtiene las pistas de todas las columnas.
     * 
     * @return Lista de pistas por columna
     */
    public List<List<Integer>> obtenerPistasColumnas() {
        return pistasColumnas;
    }
    
    /**
     * Obtiene el estado actual del juego.
     * 
     * @return Estado actual del juego
     */
    public EstadoJuego obtenerEstadoJuego() {
        return estadoJuego;
    }
    
    /**
     * Obtiene el tamaño de la grilla.
     * 
     * @return Tamaño de la grilla
     */
    public int obtenerTamañoGrilla() {
        return tamañoGrilla;
    }
    
    /**
     * Reinicia el juego actual sin generar un nuevo puzzle.
     * Notifica a los observadores sobre el cambio.
     */
    public void reiniciarJuego() {
        limpiarGrillaJugador();
        estadoJuego = EstadoJuego.JUGANDO;
        pistasDisponibles = 3;
        limpiarCeldasReveladas();
        notificarObservadores();
    }
    
    /**
     * Solicita una pista revelando una celda correcta.
     * Solo funciona si hay pistas disponibles.
     * 
     * @return true si se pudo revelar una pista, false si no hay pistas disponibles
     */
    public boolean solicitarPista() {
        if (pistasDisponibles <= 0) {
            return false; // No hay pistas disponibles
        }
        
        // Crear una lista de todas las celdas que pueden ser reveladas
        java.util.List<int[]> celdasDisponibles = new ArrayList<>();
        
        for (int fila = 0; fila < tamañoGrilla; fila++) {
            for (int columna = 0; columna < tamañoGrilla; columna++) {
                if (grillaSolucion[fila][columna] == EstadoCelda.LLENA && 
                    !celdasReveladas[fila][columna] &&
                    grillaJuego[fila][columna] != EstadoCelda.LLENA) {
                    
                    celdasDisponibles.add(new int[]{fila, columna});
                }
            }
        }
        
        // Si no hay celdas disponibles, no se puede revelar nada
        if (celdasDisponibles.isEmpty()) {
            return false;
        }
        
        // Seleccionar una celda aleatoria de las disponibles
        int indiceAleatorio = (int)(Math.random() * celdasDisponibles.size());
        int[] celdaSeleccionada = celdasDisponibles.get(indiceAleatorio);
        int fila = celdaSeleccionada[0];
        int columna = celdaSeleccionada[1];
        
        // Revelar la celda seleccionada
        grillaJuego[fila][columna] = EstadoCelda.LLENA;
        celdasReveladas[fila][columna] = true;
        pistasDisponibles--;
        

        
        notificarObservadores();
        return true;
    }
    
    /**
     * Limpia las celdas reveladas.
     */
    private void limpiarCeldasReveladas() {
        for (int fila = 0; fila < tamañoGrilla; fila++) {
            for (int columna = 0; columna < tamañoGrilla; columna++) {
                celdasReveladas[fila][columna] = false;
            }
        }
    }
    
    /**
     * Obtiene el número de pistas disponibles.
     * 
     * @return Número de pistas disponibles
     */
    public int obtenerPistasDisponibles() {
        return pistasDisponibles;
    }
    

    
    /**
     * Verifica si una celda ha sido revelada como pista.
     * 
     * @param fila Fila de la celda
     * @param columna Columna de la celda
     * @return true si la celda fue revelada como pista
     */
    public boolean esCeldaRevelada(int fila, int columna) {
        if (fila >= 0 && fila < tamañoGrilla && columna >= 0 && columna < tamañoGrilla) {
            return celdasReveladas[fila][columna];
        }
        return false;
    }
    
    /**
     * Cambia el nivel de dificultad del juego.
     * 
     * @param nuevoNivel Nuevo nivel de dificultad
     */
    public void cambiarNivel(NivelDificultad nuevoNivel) {
        this.nivelActual = nuevoNivel;
        this.tamañoGrilla = nuevoNivel.obtenerTamañoGrilla();
        this.pistasDisponibles = nuevoNivel.obtenerPistasDisponibles();
        
        // Redimensionar el array de celdas reveladas
        this.celdasReveladas = new boolean[tamañoGrilla][tamañoGrilla];
        
        // Reinicializar las grillas con el nuevo tamaño
        this.grillaJuego = new EstadoCelda[tamañoGrilla][tamañoGrilla];
        this.grillaSolucion = new EstadoCelda[tamañoGrilla][tamañoGrilla];
        
        // Inicializar grillas vacías
        for (int fila = 0; fila < tamañoGrilla; fila++) {
            for (int columna = 0; columna < tamañoGrilla; columna++) {
                grillaJuego[fila][columna] = EstadoCelda.VACIA;
                grillaSolucion[fila][columna] = EstadoCelda.VACIA;
            }
        }
        
        // Generar la solución primero
        generarSolucionAleatoria();
        
        // Luego generar las pistas basadas en la solución
        generarPistas();
        

        
        // Limpiar solo la grilla del jugador
        limpiarGrillaJugador();
        limpiarCeldasReveladas();
        
        estadoJuego = EstadoJuego.JUGANDO;
        
        // Notificar observadores dos veces para asegurar que se actualice la vista
        notificarObservadores();
        
        // Pequeño delay para asegurar que la vista se actualice completamente
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        notificarObservadores();
    }
    
    /**
     * Obtiene el nivel de dificultad actual.
     * 
     * @return Nivel de dificultad actual
     */
    public NivelDificultad obtenerNivelActual() {
        return nivelActual;
    }
    
    /**
     * Obtiene todos los niveles de dificultad disponibles.
     * 
     * @return Array con todos los niveles
     */
    public static NivelDificultad[] obtenerNivelesDisponibles() {
        return NivelDificultad.values();
    }
}
