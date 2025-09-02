# 🧩 Proyecto Nonograma - Juego de Lógica

## ¿Qué es esto?

Mirá, es un **juego de Nonograma** hecho en Java. Es uno de esos juegos de lógica donde tenés que completar una grilla siguiendo las pistas de los números que aparecen en las filas y columnas.

## ¿Cómo se juega?

- **El objetivo**: Llenar la grilla correctamente siguiendo las pistas
- **Las pistas**: Son números que te dicen cuántas celdas consecutivas están llenas
- **Ejemplo práctico**: Si una fila dice "2 1", significa que hay un grupo de 2 celdas llenas juntas, seguido de un grupo de 1 celda llena
- **Dificultades**: Hay 4 niveles - Fácil (5x5), Medio (10x10), Difícil (15x15) y Experto (20x20)

## ¿Cómo está organizado el código?

El proyecto usa el patrón **MVC** (Modelo-Vista-Controlador). 

### Las carpetas que tenés:

```
src/main/java/com/nonogram/
├── model/          ← Acá está toda la lógica del juego
├── vista/          ← La interfaz gráfica que ves
├── controlador/    ← Maneja lo que pasa cuando hacés clic
└── AplicacionNonograma.java  ← Por acá empieza todo
```

## ¿Qué hace cada archivo?

### 1. **Modelo** (carpeta `model/`)
- **`ModeloNonograma.java`**: Este es el "cerebro" del juego
  - Se encarga de generar los puzzles automáticamente
  - Verifica si lo que hiciste está bien o mal
  - Controla si estás jugando, si ganaste o perdiste
  - Guarda la grilla del juego y la solución correcta

- **`NivelDificultad.java`**: Define los 4 niveles de dificultad
  - Fácil: grilla 5x5
  - Medio: grilla 10x10  
  - Difícil: grilla 15x15
  - Experto: grilla 20x20

- **`EstadoCelda.java`**: Solo dice si una celda está vacía o llena
- **`EstadoJuego.java`**: Dice si estás jugando, ganaste o perdiste

### 2. **Vista** (carpeta `vista/`)
- **`VistaNonograma.java`**: Es como un "contrato" que dice qué métodos debe tener la vista
- **`VistaNonogramaWindowBuilderNuevo.java`**: La interfaz gráfica real (hecha con WindowBuilder de Eclipse)
  - Te muestra la grilla del juego
  - Te muestra las pistas de las filas y columnas
  - Tiene botones para nuevo juego, verificar, pedir pistas, etc.

### 3. **Controlador** (carpeta `controlador/`)
- **`ControladorNonograma.java`**: Es como un "mensajero" entre la vista y el modelo
  - Recibe cuando hacés clic en alguna parte
  - Decide qué hacer cuando hacés clic en una celda
  - Conecta la vista con la lógica del juego

### 4. **Clase Principal**
- **`AplicacionNonograma.java`**: Por acá empieza todo
  - Crea el modelo, la vista y el controlador
  - Los conecta para que puedan hablar entre sí
  - Inicia el juego

## ¿Cómo funciona todo junto?

paso a paso:

1. **Empieza**: Se ejecuta `AplicacionNonograma.main()`
2. **Se crea todo**: Se crean el modelo, la vista y el controlador
3. **Se conectan**: Se conectan entre sí para que puedan comunicarse
4. **Empieza el juego**: El modelo genera un puzzle, la vista lo muestra
5. **Cuando hacés clic**:
   - La vista detecta que hiciste clic
   - El controlador recibe esa información
   - El controlador le dice al modelo que cambie la celda
   - El modelo actualiza el estado
   - La vista se actualiza para mostrar el cambio

## ¿Qué tiene de especial este juego?

- **Puzzles diferentes**: Cada vez que iniciás un nuevo juego, se genera uno diferente
- **Pistas**: Tenés 3 pistas disponibles por nivel para ayudarte
- **Verificar**: Podés verificar si lo que hiciste está bien en cualquier momento
- **Reiniciar**: Podés reiniciar el juego sin perder el puzzle
- **Ver solución**: Si te quedás atascado, podés ver la solución

## ¿Cómo lo ejecuto?

1. **Para ejecutar**: Buscá la clase `AplicacionNonograma` y ejecutá el método `main`

## ¿Qué conceptos de programación voy a aprender?

- **Patrón MVC**: Cómo separar las responsabilidades del código
- **Patrón Observer**: Cómo hacer que las partes se notifiquen entre sí
- **Interfaces**: Cómo definir "contratos" para las clases
- **Enums**: Cómo crear tipos de datos con valores fijos

## ¿Cómo funciona el Observer en este proyecto?

El patrón Observer es como un sistema de notificaciones automáticas:

**Sin Observer** (lío total):
- La vista tendría que preguntar constantemente al modelo "¿cambió algo?"
- El controlador tendría que recordar actualizar la vista cada vez que pasa algo
- Todo estaría muy acoplado y sería difícil de mantener

**Con Observer** (todo automático):
- El modelo "avisa" a todos los que estén escuchando cuando algo cambia
- La vista se registra como "oyente" del modelo
- Cuando hacés un cambio (por ejemplo, llenás una celda):
  1. El modelo actualiza el estado
  2. El modelo automáticamente avisa a todos los oyentes
  3. La vista recibe la notificación y se actualiza sola

**En el código**:
- El modelo tiene una lista de `observadores`
- La vista se agrega como observador con `modelo.agregarObservador(this)`
- Cuando algo cambia, el modelo llama a `notificarObservadores()`
- Todos los observadores reciben el aviso y se actualizan

## ¿Cómo funciona el controlador?

El controlador es como el "cerebro" que conecta todo. Te explico qué hace:

### **¿Qué hace el controlador?**

- **Recibe los clics**: Cuando hacés clic en una celda, la vista le avisa al controlador
- **Decide qué hacer**: El controlador analiza la acción y decide cómo responder
- **Conecta vista y modelo**: Es el puente entre lo que ves y la lógica del juego
- **Maneja el estado**: Controla si podés hacer cambios o no

### **Ejemplo práctico del flujo:**

1. **Hacés clic** en una celda de la grilla
2. **La vista detecta** el clic y le avisa al controlador
3. **El controlador verifica** si podés cambiar esa celda:
   - ¿El juego terminó? → No permitir cambios
   - ¿Es una celda revelada como pista? → No permitir cambios
4. **Si está permitido**, le dice al modelo que cambie la celda
5. **El modelo actualiza** el estado y avisa a todos los observadores
6. **La vista se actualiza** automáticamente para mostrar el cambio

## ¿Cómo se generan los mapas?

Cada vez que iniciás un nuevo juego, el sistema elige **aleatoriamente** qué tipo de patrón generar. No es completamente aleatorio, sino que elige entre 5 algoritmos diferentes.

### **El proceso de generación:**

1. **Se elige un número aleatorio** del 0 al 4
2. **Cada número corresponde** a un algoritmo específico
3. **El algoritmo elegido** genera un patrón predecible
4. **Se verifica que sea solucionable** (no demasiado fácil ni difícil)

### **Los 5 tipos de mapas:**

- **0 - Formas Geométricas**: Cuadrados, cruces, diamantes
- **1 - Patrones Simétricos**: Simetría horizontal, vertical y diagonal
- **2 - Secuencias Lógicas**: Patrones de Fibonacci y números primos
- **3 - Densidad Variable**: Más celdas llenas en el centro
- **4 - Bordes y Centro**: Bordes completos con centro lleno

## ¿Cómo cambian los mapas según el nivel?

Cada algoritmo se adapta al tamaño de la grilla, creando patrones más simples o complejos:

### **Nivel Fácil (5x5):**
- **Formas Geométricas**: Cuadrado simple en el centro
- **Letras**: Solo letra X (diagonales)
- **Objetos**: Corazón pequeño y simple

### **Nivel Medio (10x10):**
- **Formas Geométricas**: Cruz completa
- **Letras**: Letra T (línea horizontal + vertical)
- **Objetos**: Estrella con puntas

### **Nivel Difícil (15x15):**
- **Formas Geométricas**: Diamante más complejo
- **Letras**: Letra H (dos líneas verticales + horizontal)
- **Objetos**: Casa con techo triangular

### **Nivel Experto (20x20):**
- **Formas Geométricas**: Diamante muy complejo
- **Letras**: Letra E (línea vertical + tres horizontales)
- **Objetos**: Árbol con tronco y copa triangular

### **Ejemplo concreto:**

Si el algoritmo "Formas Geométricas" (número 0) es elegido:
- **En 5x5**: Crea un cuadrado de 3x3 en el centro
- **En 10x10**: Crea una cruz que va de borde a borde
- **En 15x15**: Crea un diamante que ocupa 1/4 de la grilla
- **En 20x20**: Crea un diamante muy grande y detallado

Esto significa que **cada nivel tiene acceso a los mismos 5 tipos de mapas**, pero la complejidad y el detalle varía según el tamaño de la grilla.
