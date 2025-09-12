package com.nonogram.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    private boolean[][] celdasReveladas;      // Celdas que han sido reveladas como/por pista
    private NivelDificultad nivelActual;      // Nivel de dificultad actual
    GeneradorDePuzzles generador;
    
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
        
        //inicializo el generador
        this.generador = new GeneradorDePuzzles();
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
                grillaJuego[fila][columna] = EstadoCelda.MARCADA; // Las celdas vacías muestran X
                grillaSolucion[fila][columna] = EstadoCelda.VACIA;
            }
        }
    }
    
    // Limpia solo la grilla del jugador, manteniendo la solución intacta.
    private void limpiarGrillaJugador() {
        for (int fila = 0; fila < tamañoGrilla; fila++) {
            for (int columna = 0; columna < tamañoGrilla; columna++) {
                grillaJuego[fila][columna] = EstadoCelda.MARCADA; // Las celdas vacías muestran X
            }
        }
    }
    
    // Genera un nuevo puzzle automáticamente.
    public void nuevoJuego() {
    	 // Generar solución del puzzle
        //generarSolucionAleatoria();
    	if (generador == null) {
            generador = new GeneradorDePuzzles();
        }
        generador.generarSolucionAleatoria(this);
        
        // Generar pistas basadas en la solución
        generarPistas();
        
        // Reiniciar grilla del jugador
        limpiarGrillaJugador();
        
        // Reiniciar estado del juego
        estadoJuego = EstadoJuego.JUGANDO;
        
        // Reiniciar pistas disponibles
        pistasDisponibles = nivelActual != null ? nivelActual.obtenerPistasDisponibles() : 3;
        
        // Limpiar celdas reveladas
        limpiarCeldasReveladas();
        
        // Notificar cambios
        notificarObservadores();
    }
    
    // ... existing code ...
    
   
    
    // Genera las pistas (hints) basadas en la solución.
    // Las pistas indican las longitudes de las secuencias de celdas llenas.
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
    
    // Obtiene las pistas para una fila específica.
    // 
    // @param fila Índice de la fila
    // @return Lista con las longitudes de las secuencias de celdas llenas
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
    
    // Obtiene las pistas para una columna específica.
    // 
    // @param columna Índice de la columna
    // @return Lista con las longitudes de las secuencias de celdas llenas
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
    
    
    // Verifica si la solución actual del jugador es correcta.
    public boolean verificarSolucion() {
        for (int fila = 0; fila < tamañoGrilla; fila++) {
            for (int columna = 0; columna < tamañoGrilla; columna++) {
                if (!esEstadoCorrecto(fila, columna)) {
                    return false;
                }
            }
        }
        
        // Si llegamos aquí, la solución es correcta
        estadoJuego = EstadoJuego.GANADO;
        notificarObservadores();
        return true;
    }
    
    // Solicita una pista al modelo.
    // 
    // @return true si se pudo revelar una pista, false si no hay pistas disponibles
    public boolean solicitarPista() {
        if (pistasDisponibles <= 0) {
            return false; // No hay pistas disponibles
        }
        else {
        
        	// Buscar una celda no revelada para mostrar como pista
        	boolean pista = false;
        	Random rand = new Random();

        	
        	while(!pista) {
        		int n = tamañoGrilla;           // cantidad de filas
            	int fila = rand.nextInt(n);      // índice aleatorio de fila
            	int columna = rand.nextInt(n);   // índice aleatorio de columna
            	if (!celdasReveladas[fila][columna] && 
                        !esEstadoCorrecto(fila, columna)) {
                	// Revelar esta celda como pista
                		grillaJuego[fila][columna] = grillaSolucion[fila][columna];
                		celdasReveladas[fila][columna] = true;
                		pistasDisponibles--;
                		pista = true;
                    
                		// Notificar cambios
                		notificarObservadores();
                		return true;
                	}
            	else if(celdasEnJuegoIgualGrillaSolucion()) {
            		return false;
            	}
        	}
        	
        }
        
        
        return false; // No se pudo revelar ninguna pista
    }
    
 // Devuelve true si **todas** las celdas del jugador coinciden con la solución
    private boolean celdasEnJuegoIgualGrillaSolucion() {
        for (int fila = 0; fila < tamañoGrilla; fila++) {
            for (int columna = 0; columna < tamañoGrilla; columna++) {
                if (!esEstadoCorrecto(fila, columna)) {
                    return false; // hay al menos una celda incorrecta
                }
            }
        }
        return true; // todas las celdas están correctas
    }

	// Verifica si el estado actual de una celda coincide con la solución
    private boolean esEstadoCorrecto(int fila, int columna) {
        EstadoCelda estadoJuego = grillaJuego[fila][columna];
        EstadoCelda estadoSolucion = grillaSolucion[fila][columna];
        
        // Si la solución es VACIA, el juego debe ser MARCADA (X)
        if (estadoSolucion == EstadoCelda.VACIA) {
            return estadoJuego == EstadoCelda.MARCADA;
        }
        // Si la solución es LLENA, el juego debe ser LLENA
        else if (estadoSolucion == EstadoCelda.LLENA) {
            return estadoJuego == EstadoCelda.LLENA;
        }
        
        return false;
    }
    
    // Obtiene el estado actual de una celda.
    // 
    // @param fila Fila de la celda
    // @param columna Columna de la celda
    // @return Estado actual de la celda
    public EstadoCelda obtenerEstadoCelda(int fila, int columna) {
        if (fila >= 0 && fila < tamañoGrilla && columna >= 0 && columna < tamañoGrilla) {
            return grillaJuego[fila][columna];
        }
        return EstadoCelda.VACIA;
    }
    
    // Establece el estado de una celda en la grilla del jugador.
    public void establecerEstadoCelda(int fila, int columna, EstadoCelda estado) {
        if (fila >= 0 && fila < tamañoGrilla && columna >= 0 && columna < tamañoGrilla) {
            grillaJuego[fila][columna] = estado;
            notificarObservadores();
        }
    }
    
    // Obtiene el estado de una celda en la solución.
    // 
    // @param fila Fila de la celda
    // @param columna Columna de la celda
    // @return Estado de la celda en la solución
    public EstadoCelda obtenerEstadoCeldaSolucion(int fila, int columna) {
        if (fila >= 0 && fila < tamañoGrilla && columna >= 0 && columna < tamañoGrilla) {
            return grillaSolucion[fila][columna];
        }
        return EstadoCelda.VACIA;
    }
    
    //creo que esto esta repetido los metodos get son los mismos que obtenerEstado...
    ///////////////////////////////////////////////////////////////////////////////
    
    public void setGrillaJuego(int fila,int columna,EstadoCelda celda) {
		this.grillaJuego[fila][columna] =celda ;
	}
    
    public void setGrillaSolucion(int fila,int columna,EstadoCelda celda) {
		this.grillaSolucion[fila][columna] = celda ;
	}
    

	public EstadoCelda getGrillaJuego(int fila,int col) {
		return grillaJuego[fila][col];
	}

	public EstadoCelda getGrillaSolucion(int fila, int col) {
		return grillaSolucion[fila][col];
	}
    ///////////////////////////////////////////////////////////////////////////////////

    

    
    // Obtiene las pistas de todas las filas.
    // 
    // @return Lista de pistas por fila
    public List<List<Integer>> obtenerPistasFilas() {
        return pistasFilas;
    }
    
    // Obtiene las pistas de todas las columnas.
    // 
    // @return Lista de pistas por columna
    public List<List<Integer>> obtenerPistasColumnas() {
        return pistasColumnas;
    }
    
    // Obtiene el estado actual del juego.
    // 
    // @return Estado actual del juego
    public EstadoJuego obtenerEstadoJuego() {
        return estadoJuego;
    }
    
    // Obtiene el tamaño de la grilla.
    // 
    // @return Tamaño de la grilla
    public int obtenerTamañoGrilla() {
        return tamañoGrilla;
    }
    
    // Reinicia el juego actual sin generar un nuevo puzzle.
    // Notifica a los observadores sobre el cambio.
    public void reiniciarJuego() {
        limpiarGrillaJugador();
        estadoJuego = EstadoJuego.JUGANDO;
        pistasDisponibles = 3;
        limpiarCeldasReveladas();
        notificarObservadores();
    }
    
    // Limpia las celdas reveladas.
    private void limpiarCeldasReveladas() {
        for (int fila = 0; fila < tamañoGrilla; fila++) {
            for (int columna = 0; columna < tamañoGrilla; columna++) {
                celdasReveladas[fila][columna] = false;
            }
        }
    }
    
    // Obtiene el número de pistas disponibles.
    // 
    // @return Número de pistas disponibles
    public int obtenerPistasDisponibles() {
        return pistasDisponibles;
    }
    

    
    // Verifica si una celda ha sido revelada como pista.
    // 
    // @param fila Fila de la celda
    // @param columna Columna de la celda
    // @return true si la celda fue revelada como pista
    public boolean esCeldaRevelada(int fila, int columna) {
        if (fila >= 0 && fila < tamañoGrilla && columna >= 0 && columna < tamañoGrilla) {
            return celdasReveladas[fila][columna];
        }
        return false;
    }
    
    // Cambia el nivel de dificultad del juego.
    // 
    // @param nuevoNivel Nuevo nivel de dificultad
    public void cambiarNivel(NivelDificultad nuevoNivel) {
        this.nivelActual = nuevoNivel;
        this.tamañoGrilla = nuevoNivel.obtenerTamañoGrilla();
        this.pistasDisponibles = nuevoNivel.obtenerPistasDisponibles();
        
        // Redimensionar el array de celdas reveladas
        this.celdasReveladas = new boolean[tamañoGrilla][tamañoGrilla];
        
        // Asegurar que todas las celdas estén marcadas como no reveladas
        for (int fila = 0; fila < tamañoGrilla; fila++) {
            for (int columna = 0; columna < tamañoGrilla; columna++) {
                celdasReveladas[fila][columna] = false;
            }
        }
        
        // Reinicializar las grillas con el nuevo tamaño
        this.grillaJuego = new EstadoCelda[tamañoGrilla][tamañoGrilla];
        this.grillaSolucion = new EstadoCelda[tamañoGrilla][tamañoGrilla];
        
        // Inicializar grillas vacías
        for (int fila = 0; fila < tamañoGrilla; fila++) {
            for (int columna = 0; columna < tamañoGrilla; columna++) {
                grillaJuego[fila][columna] = EstadoCelda.MARCADA; // Las celdas vacías muestran X
                grillaSolucion[fila][columna] = EstadoCelda.VACIA;
            }
        }
        
        // Generar la solución primero
        generador.generarSolucionAleatoria(this);
        
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
    
    // Obtiene el nivel de dificultad actual.
    // 
    // @return Nivel de dificultad actual
    public NivelDificultad obtenerNivelActual() {
        return nivelActual;
    }
    
    // Obtiene todos los niveles de dificultad disponibles.
    // 
    // @return Array con todos los niveles
    public static NivelDificultad[] obtenerNivelesDisponibles() {
        return NivelDificultad.values();
    }
}
