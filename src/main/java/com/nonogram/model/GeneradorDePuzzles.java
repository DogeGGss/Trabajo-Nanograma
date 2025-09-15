package com.nonogram.model;

public class GeneradorDePuzzles {

	public EstadoCelda[][] generarSolucionAleatoria(int tamañoGrilla) {
		EstadoCelda[][] solucion = crearMatrizVacia(tamañoGrilla);
		int algoritmo = (int)(Math.random() * 5); // 0-4 algoritmos diferentes
		switch (algoritmo) {
			case 0: generarPuzzleConFormasGeometricas(solucion); break;
			case 1: generarPuzzleConPatronesSimetricos(solucion); break;
			case 2: generarPuzzleConSecuenciasLogicas(solucion); break;
			case 3: generarPuzzleConDensidadVariable(solucion); break;
			case 4: generarPuzzleConBordesYCentro(solucion); break;
		}
		asegurarValidezPuzzle(solucion);
		return solucion;
	}

	private EstadoCelda[][] crearMatrizVacia(int tamañoGrilla) {
		EstadoCelda[][] matriz = new EstadoCelda[tamañoGrilla][tamañoGrilla];
		for (int fila = 0; fila < tamañoGrilla; fila++) {
			for (int columna = 0; columna < tamañoGrilla; columna++) {
				matriz[fila][columna] = EstadoCelda.VACIA;
			}
		}
		return matriz;
	}

	// Algoritmo 1: Genera puzzles con formas geométricas básicas
	private void generarPuzzleConFormasGeometricas(EstadoCelda[][] solucion) {
		int n = solucion.length;
		int centro = n / 2;
		if (n <= 5) {
			for (int fila = centro - 1; fila <= centro + 1; fila++) {
				for (int col = centro - 1; col <= centro + 1; col++) {
					if (fila >= 0 && fila < n && col >= 0 && col < n) {
						solucion[fila][col] = EstadoCelda.LLENA;
					}
				}
			}
		} else if (n <= 10) {
			for (int i = 0; i < n; i++) {
				solucion[centro][i] = EstadoCelda.LLENA;
				solucion[i][centro] = EstadoCelda.LLENA;
			}
		} else {
			for (int fila = 0; fila < n; fila++) {
				for (int col = 0; col < n; col++) {
					int distancia = Math.abs(fila - centro) + Math.abs(col - centro);
					if (distancia <= n / 4) {
						solucion[fila][col] = EstadoCelda.LLENA;
					}
				}
			}
		}
	}

	// Algoritmo 2: Genera puzzles con patrones simétricos
	private void generarPuzzleConPatronesSimetricos(EstadoCelda[][] solucion) {
		int n = solucion.length;
		for (int fila = 0; fila < n; fila++) {
			for (int col = 0; col < n; col++) {
				if ((fila + col) % 2 == 0 && (fila < n / 2 || col < n / 2)) {
					solucion[fila][col] = EstadoCelda.LLENA;
				}
				if (fila == col || fila == n - 1 - col) {
					solucion[fila][col] = EstadoCelda.LLENA;
				}
			}
		}
	}

	// Algoritmo 3: Genera puzzles con secuencias lógicas
	private void generarPuzzleConSecuenciasLogicas(EstadoCelda[][] solucion) {
		int n = solucion.length;
		for (int fila = 0; fila < n; fila++) {
			for (int col = 0; col < n; col++) {
				if ((fila + col) % 3 == 0 && (fila * col) % 2 == 0) {
					solucion[fila][col] = EstadoCelda.LLENA;
				}
				if (esPrimo(fila + col) && (fila + col) > 1) {
					solucion[fila][col] = EstadoCelda.LLENA;
				}
			}
		}
	}

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
	private void generarPuzzleConDensidadVariable(EstadoCelda[][] solucion) {
		int n = solucion.length;
		for (int fila = 0; fila < n; fila++) {
			for (int col = 0; col < n; col++) {
				double distanciaAlCentro = Math.sqrt(Math.pow(fila - n/2.0, 2) + Math.pow(col - n/2.0, 2));
				double probabilidad = Math.max(0.1, 1.0 - distanciaAlCentro / (n/2.0));
				if (Math.random() < probabilidad) {
					solucion[fila][col] = EstadoCelda.LLENA;
				}
			}
		}
	}

	// Algoritmo 5: Genera puzzles con bordes y centro
	private void generarPuzzleConBordesYCentro(EstadoCelda[][] solucion) {
		int n = solucion.length;
		for (int i = 0; i < n; i++) {
			solucion[0][i] = EstadoCelda.LLENA;
			solucion[n-1][i] = EstadoCelda.LLENA;
			solucion[i][0] = EstadoCelda.LLENA;
			solucion[i][n-1] = EstadoCelda.LLENA;
		}
		int centro = n / 2;
		int radio = Math.max(1, n / 6);
		for (int fila = centro - radio; fila <= centro + radio; fila++) {
			for (int col = centro - radio; col <= centro + radio; col++) {
				if (fila >= 0 && fila < n && col >= 0 && col < n) {
					solucion[fila][col] = EstadoCelda.LLENA;
				}
			}
		}
	}

	private void asegurarValidezPuzzle(EstadoCelda[][] solucion) {
		int n = solucion.length;
		for (int fila = 0; fila < n; fila++) {
			int llenas = 0;
			int vacias = 0;
			for (int col = 0; col < n; col++) {
				if (solucion[fila][col] == EstadoCelda.LLENA) llenas++;
				else vacias++;
			}
			if (llenas == 0) solucion[fila][n/2] = EstadoCelda.LLENA;
			else if (vacias == 0) solucion[fila][n/2] = EstadoCelda.VACIA;
		}
		for (int col = 0; col < n; col++) {
			int llenas = 0;
			int vacias = 0;
			for (int fila = 0; fila < n; fila++) {
				if (solucion[fila][col] == EstadoCelda.LLENA) llenas++;
				else vacias++;
			}
			if (llenas == 0) solucion[n/2][col] = EstadoCelda.LLENA;
			else if (vacias == 0) solucion[n/2][col] = EstadoCelda.VACIA;
		}
	}
}
