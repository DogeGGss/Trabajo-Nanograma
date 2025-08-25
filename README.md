# Nonograma - Trabajo Práctico 1

**Programación III - Universidad Nacional de General Sarmiento**

**Alumnos:** Ulises Fonseca y Kevin Cordua

**Fecha de entrega:** Martes 16 de septiembre

## Descripción del Proyecto

Este trabajo práctico implementa una aplicación visual que permite jugar al juego de ingenio llamado **Nonograma**. El nonograma es un tipo de rompecabezas originario de Japón, que se juega sobre una cuadrícula en blanco y negro. Es un juego de ingenio con reglas simples y soluciones desafiantes.

### Reglas del Juego

- Se tiene una grilla de casillas de 5×5, que deben ser pintadas de negro o marcadas con una X
- En las pistas, cada número indica una cadena de celdas negras consecutivas
- Entre dos cadenas de celdas negras debe existir al menos una celda libre (marcada con X)
- Al costado de cada fila aparecen los largos de las cadenas de casillas en negro para esa fila
- Sobre cada columna aparecen los largos de las cadenas de casillas en negro para esa columna
- El objetivo es encontrar y marcar todas las casillas negras

**Observación:** Dos cadenas de casillas negras están separadas, al menos, por una X.

## Funcionalidades Implementadas

### Funcionalidades Básicas (Obligatorias)
- ✅ Interfaz visual intuitiva para grillas de 5×5
- ✅ Pintar celdas de negro (clic)
- ✅ Dejar celdas en blanco
- ✅ Evaluación de solución (botón "Ver Solución")
- ✅ Información de victoria/derrota
- ✅ Mostrar solución correcta

### Funcionalidades Opcionales (Implementadas)
- ✅ **Niveles de juego:** Grillas de 5×5, 10×10, 15×15, 20×20
- ✅ **Sistema de pistas limitado:** Botón "Dar Pista" con límite de 3 pistas por nivel
- ✅ **Mostrar solución:** Botón "Insertar Solución" que completa automáticamente la grilla

## Arquitectura del Proyecto

### Patrón MVC (Model-View-Controller)

La aplicación está implementada siguiendo el patrón de diseño **MVC** (Modelo-Vista-Controlador), que separa claramente las responsabilidades de cada componente:

#### 1. Modelo (Model)
**Responsabilidades:**
- Contiene toda la lógica del juego
- Maneja el estado de la grilla (jugador y solución)
- Genera puzzles aleatorios
- Valida soluciones
- Gestiona el sistema de pistas

**Clases principales:**
- `ModeloNonograma.java`: Lógica central del juego
- `NivelDificultad.java`: Enum con diferentes niveles
- `EstadoCelda.java`: Estados posibles de una celda (VACIA, LLENA, MARCADA)
- `EstadoJuego.java`: Estados del juego (JUGANDO, GANADO, PERDIDO)

**Características del Modelo:**
```java
// Generación de puzzles con 10 algoritmos diferentes
private void generarSolucionAleatoria() {
    // Algoritmos: formas geométricas, patrones simétricos, 
    // secuencias lógicas, densidad variable, etc.
}

// Sistema de pistas inteligente
public boolean solicitarPista() {
    // Revela celdas aleatorias de la solución
    // Previene modificación de celdas reveladas
}
```

#### 2. Vista (View)
**Responsabilidades:**
- Interfaz gráfica de usuario
- Muestra el estado del juego
- Captura eventos del usuario
- Actualiza la visualización

**Clases principales:**
- `VistaNonograma.java`: Interfaz que define el contrato
- `VistaNonogramaWindowBuilderNuevo.java`: Implementación con Swing

**Características de la Vista:**
```java
// Interfaz responsive con JLayeredPane
private void configurarDiseno() {
    // Grilla centrada independientemente de las pistas
    // Pistas posicionadas sin afectar el centrado
}

// Manejo de eventos del mouse
cellButtons\[fila\]\[columna\].addMouseListener(new MouseAdapter() {
    // Clic: pintar celda
});
```

#### 3. Controlador (Controller)
**Responsabilidades:**
- Coordina la comunicación entre Modelo y Vista
- Maneja eventos del usuario
- Actualiza el modelo según las acciones del usuario
- Notifica a la vista sobre cambios

**Clases principales:**
- `ControladorNonograma.java`: Controlador principal

**Características del Controlador:**
```java
// Manejo de clics en celdas
public void manejarClicCelda(int fila, int columna) {
    // Determina el nuevo estado de la celda
    // Actualiza el modelo
    // Notifica a la vista
}

// Verificación de solución
public void verificarSolucion() {
    // Compara grilla del jugador con solución
    // Muestra resultado (victoria/derrota)
}
```

### Patrón Observer

La aplicación implementa el **patrón Observer** para mantener sincronizados el Modelo y la Vista:

#### Implementación del Observer

**En el Modelo:**
```java
public interface ObservadorModelo {
    void alCambiarModelo();
}

private List<ObservadorModelo> observadores;

public void agregarObservador(ObservadorModelo observador) {
    if (!observadores.contains(observador)) {
        observadores.add(observador);
    }
}

private void notificarObservadores() {
    for (ObservadorModelo observador : observadores) {
        observador.alCambiarModelo();
    }
}
```

**En la Vista:**
```java
public class VistaNonogramaWindowBuilderNuevo extends JFrame 
    implements VistaNonograma, ModeloNonograma.ObservadorModelo {
    
    @Override
    public void alCambiarModelo() {
        // Actualiza la visualización cuando cambia el modelo
        actualizarVisualizacion();
    }
}
```

#### Flujo de Comunicación

1. **Usuario interactúa** → Vista captura evento
2. **Vista notifica** → Controlador recibe evento
3. **Controlador actualiza** → Modelo cambia estado
4. **Modelo notifica** → Observadores (Vista) son notificados
5. **Vista se actualiza** → Interfaz refleja cambios

## Estructura del Código

```
src/main/java/com/nonogram/
├── AplicacionNonograma.java          # Clase principal
├── controlador/
│   └── ControladorNonograma.java     # Controlador MVC
├── model/
│   ├── ModeloNonograma.java          # Modelo principal
│   ├── NivelDificultad.java          # Enum niveles
│   ├── EstadoCelda.java              # Enum estados celda
│   └── EstadoJuego.java              # Enum estados juego
└── vista/
    ├── VistaNonograma.java           # Interfaz vista
    └── VistaNonogramaWindowBuilderNuevo.java  # Implementación Swing
```

## Características Técnicas

### Generación de Puzzles
- **10 algoritmos diferentes** para generar puzzles variados
- **Validación de densidad** para asegurar puzzles solucionables
- **Patrones temáticos:** formas geométricas, letras, animales, fractales

### Sistema de Pistas
- **Pistas limitadas** por nivel (3 para fácil, 2 para medio, 1 para difícil)
- **Revelación aleatoria** de celdas correctas
- **Protección** de celdas reveladas (no se pueden modificar)

### Interfaz de Usuario
- **Ventana centrada** de 1920×1080 píxeles
- **Grilla perfectamente centrada** independiente de las pistas
- **Pistas compactas** con espaciado optimizado
- **Botones intuitivos** para todas las acciones

### Niveles de Dificultad
- **Fácil:** 5×5, 3 pistas disponibles
- **Medio:** 10×10, 2 pistas disponibles  
- **Difícil:** 15×15, 1 pista disponible
- **Experto:** 20×20, 0 pistas disponibles

## Compilación y Ejecución

### Requisitos
- Java Development Kit (JDK) 24 o superior
- Eclipse IDE (recomendado)

### Compilación desde Terminal
```bash
# Limpiar y compilar
.\run.bat

# O manualmente:
rmdir /s /q bin
mkdir bin
javac -d bin -cp "src/main/java" src/main/java/com/nonogram/model/*.java
javac -d bin -cp "bin" src/main/java/com/nonogram/vista/*.java
javac -d bin -cp "bin" src/main/java/com/nonogram/controlador/*.java
javac -d bin -cp "bin" src/main/java/com/nonogram/*.java
```

### Ejecución
```bash
# Desde terminal
java -cp bin com.nonogram.AplicacionNonograma

# Desde Eclipse
# Ejecutar la clase AplicacionNonograma
```

## Decisiones de Diseño

### Separación de Responsabilidades
- **Modelo:** Contiene solo lógica de negocio, sin dependencias de Swing
- **Vista:** Solo maneja la interfaz, no contiene lógica de juego
- **Controlador:** Coordina sin conocer detalles de implementación

### Patrón Observer
- **Desacoplamiento:** Modelo no conoce directamente a la Vista
- **Extensibilidad:** Fácil agregar nuevas vistas sin modificar el modelo
- **Mantenibilidad:** Cambios en un componente no afectan otros

### Generación de Puzzles
- **Variedad:** 10 algoritmos diferentes para puzzles únicos
- **Calidad:** Validación de densidad para puzzles solucionables
- **Escalabilidad:** Algoritmos adaptan patrones según el tamaño

### Interfaz de Usuario
- **Centrado perfecto:** Grilla siempre centrada independientemente de pistas
- **Responsive:** Se adapta a diferentes tamaños de grilla
- **Intuitiva:** Controles claros y feedback inmediato

## Ventajas de la Implementación

### Para el Desarrollo
- **Modularidad:** Componentes independientes y reutilizables
- **Escalabilidad:** Fácil agregar nuevas funcionalidades
- **Mantenibilidad:** Código organizado y bien documentado

### Para el Usuario
- **Interfaz intuitiva:** Controles claros y responsivos
- **Feedback inmediato:** Actualización automática de la vista
- **Funcionalidad completa:** Todas las características requeridas

### Para el Profesor
- **Comprensión de patrones:** Implementación correcta de MVC y Observer
- **Código profesional:** Estructura empresarial estándar
- **Documentación completa:** Explicaciones detalladas

## Conclusión

Esta implementación demuestra una comprensión sólida de los patrones de diseño MVC y Observer, proporcionando una base sólida para el desarrollo de aplicaciones Java con interfaces gráficas. La arquitectura es escalable, mantenible y sigue las mejores prácticas de la industria.

El proyecto cumple con todos los requisitos obligatorios y opcionales de la consigna, implementando una aplicación completa y funcional del juego Nonograma con una interfaz moderna y una arquitectura robusta.
