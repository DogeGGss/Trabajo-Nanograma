package com.nonogram.vista;

import com.nonogram.controlador.ControladorNonograma;
import com.nonogram.model.EstadoCelda;
import com.nonogram.model.ModeloNonograma;
import com.nonogram.model.NivelDificultad;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class VistaNonogramaWindowBuilderNuevo extends JFrame implements VistaNonograma {
    
    private static final long serialVersionUID = 1L;
    private static final int ALTO_PISTA = 20;
    private static final int ANCHO_PISTA_MINIMO = 15; // Ancho mínimo para las pistas
    
    private ControladorNonograma controlador;
    private JPanel panelGrilla;
    private JButton[][] botonesCelda;
    private JLabel[][] etiquetasPistasFilas;
    private JLabel[][] etiquetasPistasColumnas;
    private JButton botonNuevoJuego;
    private JButton botonReiniciar;
    private JButton botonSolucion;
    private JButton botonInsertarSolucion;
    private JButton botonDarPista;
    private JLabel etiquetaEstado;
    private JLabel etiquetaPistas;
    private JComboBox<NivelDificultad> selectorNivel;
    
    public VistaNonogramaWindowBuilderNuevo() {
        inicializarComponentes();
        configurarDiseno();
        configurarManejadoresEventos();
        setVisible(true);
    }
    
    private void inicializarComponentes() {
        setTitle("Nonograma - TP1 - Programación III - UNGS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false); // Tamaño fijo para estabilidad
        // Tamaño fijo para el nivel más grande (Experto: 20x20)
        setSize(1600, 1000);
        
        panelGrilla = new JPanel();
        panelGrilla.setLayout(new GridBagLayout());
        
        inicializarComponentesDinamicos(NivelDificultad.FACIL);
        
        selectorNivel = new JComboBox<>(NivelDificultad.values());
        selectorNivel.setSelectedItem(NivelDificultad.FACIL);
        selectorNivel.setFocusable(false); // Deshabilitar focus para evitar subrayado
        
        botonNuevoJuego = new JButton("Nuevo Juego");
        botonReiniciar = new JButton("Reiniciar");
        botonSolucion = new JButton("Ver Solución");
        botonSolucion.setEnabled(false); // Deshabilitado inicialmente
        botonInsertarSolucion = new JButton("Insertar Solución");
        botonDarPista = new JButton("Dar Pista (3)");
        
        etiquetaEstado = new JLabel("Nonograma - Creado por Ulises Fonseca y Kevin Cordua - Programación III UNGS");
        etiquetaEstado.setHorizontalAlignment(SwingConstants.CENTER);
        etiquetaEstado.setFont(new Font("Arial", Font.BOLD, 16));
        
        etiquetaPistas = new JLabel("Pistas disponibles: 3");
        etiquetaPistas.setHorizontalAlignment(SwingConstants.CENTER);
        etiquetaPistas.setFont(new Font("Arial", Font.BOLD, 12));
    }
    
    private void configurarDiseno() {
        setLayout(new BorderLayout());
        
        JPanel panelBotones = new JPanel();
        panelBotones.add(new JLabel("Nivel: "));
        panelBotones.add(selectorNivel);
        panelBotones.add(botonNuevoJuego);
        panelBotones.add(botonReiniciar);
        panelBotones.add(botonSolucion);
        panelBotones.add(botonInsertarSolucion);
        panelBotones.add(botonDarPista);
        
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(etiquetaEstado, BorderLayout.CENTER);
        
        JPanel panelGrillaConPistas = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        NivelDificultad nivelActual = (NivelDificultad) selectorNivel.getSelectedItem();
        int tamañoActual = nivelActual.obtenerTamañoGrilla();
        int columnasPistas = etiquetasPistasColumnas.length; // Usar el número real de columnas
        
        // Agregar pistas de columnas (arriba de la grilla)
        for (int i = 0; i < columnasPistas; i++) {
            for (int columna = 0; columna < tamañoActual; columna++) {
                gbc.gridx = columna + columnasPistas; // Ajustar offset según columnas de pistas
                gbc.gridy = i;
                gbc.weightx = 0.0;
                gbc.weighty = 0.0;
                gbc.insets = new Insets(1, 1, 1, 1);
                panelGrillaConPistas.add(etiquetasPistasColumnas[i][columna], gbc);
            }
        }
        
        // Agregar pistas de filas (izquierda de la grilla)
        for (int fila = 0; fila < tamañoActual; fila++) {
            for (int i = 0; i < columnasPistas; i++) {
                gbc.gridx = i;
                gbc.gridy = fila + columnasPistas; // Ajustar offset según columnas de pistas
                gbc.weightx = 0.0;
                gbc.weighty = 0.0;
                gbc.insets = new Insets(1, 1, 1, 1);
                panelGrillaConPistas.add(etiquetasPistasFilas[fila][i], gbc);
            }
        }
        
        // Agregar botones de la grilla
        for (int fila = 0; fila < tamañoActual; fila++) {
            for (int columna = 0; columna < tamañoActual; columna++) {
                gbc.gridx = columna + columnasPistas; // Ajustar offset según columnas de pistas
                gbc.gridy = fila + columnasPistas; // Ajustar offset según columnas de pistas
                gbc.weightx = 0.0;
                gbc.weighty = 0.0;
                gbc.insets = new Insets(1, 1, 1, 1);
                panelGrillaConPistas.add(botonesCelda[fila][columna], gbc);
            }
        }
        
        JPanel panelCentral = new JPanel(new GridBagLayout());
        GridBagConstraints gbcCentral = new GridBagConstraints();
        
        // Calcular el offset para centrar la grilla según el nivel
        int tamañoMaximo = 20; // Tamaño del nivel Experto
        int offsetX = (tamañoMaximo - tamañoActual) / 2;
        int offsetY = (tamañoMaximo - tamañoActual) / 2;
        
        gbcCentral.gridx = offsetX;
        gbcCentral.gridy = offsetY;
        gbcCentral.weightx = 0.0;
        gbcCentral.weighty = 0.0;
        gbcCentral.anchor = GridBagConstraints.CENTER;
        gbcCentral.insets = new Insets(offsetY * 10, offsetX * 10, offsetY * 10, offsetX * 10);
        panelCentral.add(panelGrillaConPistas, gbcCentral);
        
        add(panelSuperior, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
        
        setLocationRelativeTo(null);
    }
    
    private void configurarManejadoresEventos() {
        int tamañoActual = botonesCelda != null ? botonesCelda.length : 5;
        
        for (int fila = 0; fila < tamañoActual; fila++) {
            for (int columna = 0; columna < tamañoActual; columna++) {
                if (botonesCelda[fila][columna].getMouseListeners().length > 0) {
                    botonesCelda[fila][columna].removeMouseListener(botonesCelda[fila][columna].getMouseListeners()[0]);
                }
            }
        }
        
        for (ActionListener listener : botonNuevoJuego.getActionListeners()) {
            botonNuevoJuego.removeActionListener(listener);
        }
        for (ActionListener listener : botonReiniciar.getActionListeners()) {
            botonReiniciar.removeActionListener(listener);
        }
        for (ActionListener listener : botonSolucion.getActionListeners()) {
            botonSolucion.removeActionListener(listener);
        }
        for (ActionListener listener : botonInsertarSolucion.getActionListeners()) {
            botonInsertarSolucion.removeActionListener(listener);
        }
        for (ActionListener listener : botonDarPista.getActionListeners()) {
            botonDarPista.removeActionListener(listener);
        }
        for (ActionListener listener : selectorNivel.getActionListeners()) {
            selectorNivel.removeActionListener(listener);
        }
        
        for (int fila = 0; fila < tamañoActual; fila++) {
            for (int columna = 0; columna < tamañoActual; columna++) {
                final int filaFinal = fila;
                final int columnaFinal = columna;
                
                                 botonesCelda[fila][columna].addMouseListener(new MouseAdapter() {
                     @Override
                     public void mouseClicked(MouseEvent e) {
                         if (controlador != null) {
                             controlador.manejarClicCelda(filaFinal, columnaFinal);
                         }
                     }
                 });
            }
        }
        
        botonNuevoJuego.addActionListener(event -> {
            if (controlador != null) {
                controlador.nuevoJuego();
            }
        });
        
        botonReiniciar.addActionListener(event -> {
            if (controlador != null) {
                controlador.reiniciarJuego();
            }
        });
        
        botonSolucion.addActionListener(event -> {
            if (controlador != null) {
                controlador.mostrarSolucion();
            }
        });
        
        botonInsertarSolucion.addActionListener(event -> {
            if (controlador != null) {
                controlador.verificarSolucion();
            }
        });
        
        botonDarPista.addActionListener(event -> {
            if (controlador != null) {
                boolean pistaRevelada = controlador.solicitarPista();
                if (pistaRevelada) {
                    actualizarContadorPistas();
                } else {
                    JOptionPane.showMessageDialog(this,
                        "No hay pistas disponibles o no se puede revelar más celdas.",
                        "Sin pistas",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        
        selectorNivel.addActionListener(e -> {
            NivelDificultad nivelSeleccionado = (NivelDificultad) selectorNivel.getSelectedItem();
            cambiarNivel(nivelSeleccionado);
        });
    }
    
    private int obtenerTamañoCelda() {
        // Tamaño fijo para todos los niveles, optimizado para la ventana de 1600x1000
        return 30; // Tamaño consistente para todos los niveles
    }
    
    private void ajustarTamañoVentana() {
        // No cambiar el tamaño de la ventana, solo centrarla
        setLocationRelativeTo(null);
    }
    
    private void cambiarNivel(NivelDificultad nuevoNivel) {
        // Limpiar la interfaz actual
        getContentPane().removeAll();
        
        // Reinicializar componentes con el nuevo nivel
        inicializarComponentesDinamicos(nuevoNivel);
        
        // Reconfigurar el diseño
        configurarDiseno();
        
        // Reconfigurar los manejadores de eventos
        configurarManejadoresEventos();
        
        // Ajustar la posición de la ventana
        ajustarTamañoVentana();
        
        // Notificar al controlador del cambio de nivel
        if (controlador != null) {
            controlador.cambiarNivel(nuevoNivel);
        }
        
        // Actualizar la interfaz
        revalidate();
        repaint();
    }
    
    @Override
    public void actualizarVisualizacion() {
        if (controlador == null) return;
        
        ModeloNonograma modelo = controlador.obtenerModelo();
        int tamañoActual = modelo.obtenerTamañoGrilla();
        
        if (botonesCelda == null || botonesCelda.length != tamañoActual) {
            getContentPane().removeAll();
            inicializarComponentesDinamicos(modelo.obtenerNivelActual());
            configurarDiseno();
            configurarManejadoresEventos();
            revalidate();
            repaint();
            SwingUtilities.invokeLater(() -> {
                revalidate();
                repaint();
            });
            return;
        }
        
        for (int fila = 0; fila < tamañoActual; fila++) {
            for (int columna = 0; columna < tamañoActual; columna++) {
                EstadoCelda estado = modelo.obtenerEstadoCelda(fila, columna);
                boolean esPista = modelo.esCeldaRevelada(fila, columna);
                actualizarBotonCelda(botonesCelda[fila][columna], estado, esPista);
            }
        }
        
        List<List<Integer>> pistasFilas = modelo.obtenerPistasFilas();
        for (int fila = 0; fila < tamañoActual; fila++) {
            List<Integer> pistas = pistasFilas.get(fila);
            for (int i = 0; i < etiquetasPistasFilas[fila].length; i++) {
                String textoPista = (i < pistas.size()) ? String.valueOf(pistas.get(i)) : "";
                etiquetasPistasFilas[fila][i].setText(textoPista);
            }
        }
        
        List<List<Integer>> pistasColumnas = modelo.obtenerPistasColumnas();
        for (int columna = 0; columna < tamañoActual; columna++) {
            List<Integer> pistas = pistasColumnas.get(columna);
            for (int i = 0; i < etiquetasPistasColumnas.length; i++) {
                String textoPista = (i < pistas.size()) ? String.valueOf(pistas.get(i)) : "";
                etiquetasPistasColumnas[i][columna].setText(textoPista);
            }
        }
        
        actualizarEstadoJuego();
        actualizarContadorPistas();
        revalidate();
        repaint();
    }
    
    private void actualizarBotonCelda(JButton boton, EstadoCelda estado) {
        switch (estado) {
            case VACIA:
                boton.setBackground(Color.WHITE);
                boton.setText("");
                break;
            case LLENA:
                boton.setBackground(Color.BLACK);
                boton.setText("");
                break;
            case MARCADA:
                boton.setBackground(Color.WHITE);
                boton.setText("X");
                boton.setForeground(Color.RED);
                break;
        }
    }
    
    private void actualizarBotonCelda(JButton boton, EstadoCelda estado, boolean esPista) {
        actualizarBotonCelda(boton, estado);
        
        if (esPista) {
            // Las pistas reveladas tienen borde azul
            boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLUE, 3),
                BorderFactory.createLineBorder(Color.BLACK, 1)
            ));
        } else {
            boton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        }
    }
    
    private void actualizarEstadoJuego() {
        if (controlador == null) return;
        
        ModeloNonograma modelo = controlador.obtenerModelo();
        switch (modelo.obtenerEstadoJuego()) {
            case JUGANDO:
                etiquetaEstado.setText("Programacion III - Universidad Nacional de General Sarmiento Trabajo Practico 1: Nonograma");
                break;
            case GANADO:
                etiquetaEstado.setText("¡Felicitaciones! ¡Ganaste!");
                break;
            case PERDIDO:
                etiquetaEstado.setText("¡Perdiste! Intenta de nuevo");
                break;
        }
    }
    
    private void actualizarContadorPistas() {
        if (controlador == null) return;
        
        ModeloNonograma modelo = controlador.obtenerModelo();
        int pistasDisponibles = modelo.obtenerPistasDisponibles();
        
        etiquetaPistas.setText("Pistas disponibles: " + pistasDisponibles);
        botonDarPista.setText("Dar Pista (" + pistasDisponibles + ")");
        botonDarPista.setEnabled(pistasDisponibles > 0);
    }
    
    @Override
    public void mostrarJuegoGanado() {
        JOptionPane.showMessageDialog(this,
            "¡Felicitaciones! Has completado el puzzle correctamente.",
            "¡Victoria!",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    @Override
    public void mostrarJuegoPerdido() {
        JOptionPane.showMessageDialog(this,
            "¡Has perdido! La solución no es correcta.",
            "Derrota",
            JOptionPane.WARNING_MESSAGE);
        
        // Habilitar el botón de ver solución después de perder
        botonSolucion.setEnabled(true);
    }
    
    @Override
    public void mostrarSolucion(ModeloNonograma modelo) {
        JDialog dialogoSolucion = new JDialog(this, "Solución del Puzzle", true);
        int tamañoActual = modelo.obtenerTamañoGrilla();
        dialogoSolucion.setLayout(new GridLayout(tamañoActual, tamañoActual, 2, 2));
        
        for (int fila = 0; fila < tamañoActual; fila++) {
            for (int columna = 0; columna < tamañoActual; columna++) {
                JLabel labelSolucion = new JLabel();
                int tamañoCelda = obtenerTamañoCelda();
                labelSolucion.setPreferredSize(new Dimension(tamañoCelda, tamañoCelda));
                labelSolucion.setMinimumSize(new Dimension(tamañoCelda, tamañoCelda));
                labelSolucion.setMaximumSize(new Dimension(tamañoCelda, tamañoCelda));
                labelSolucion.setFont(new Font("Arial", Font.BOLD, 20));
                labelSolucion.setHorizontalAlignment(SwingConstants.CENTER);
                labelSolucion.setVerticalAlignment(SwingConstants.CENTER);
                labelSolucion.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                
                EstadoCelda estadoSolucion = modelo.obtenerEstadoCeldaSolucion(fila, columna);
                if (estadoSolucion == EstadoCelda.LLENA) {
                    labelSolucion.setBackground(Color.BLACK);
                    labelSolucion.setOpaque(true);
                    labelSolucion.setText("");
                } else {
                    labelSolucion.setBackground(Color.WHITE);
                    labelSolucion.setOpaque(true);
                    labelSolucion.setText("X");
                    labelSolucion.setForeground(Color.RED);
                }
                
                dialogoSolucion.add(labelSolucion);
            }
        }
        
        dialogoSolucion.pack();
        dialogoSolucion.setLocationRelativeTo(this);
        dialogoSolucion.setVisible(true);
    }
    
    @Override
    public void establecerControlador(Object controlador) {
        this.controlador = (ControladorNonograma) controlador;
    }
    
    @Override
    public Object obtenerControlador() {
        return controlador;
    }
    
    @Override
    public void actualizarInformacionJuego() {
        if (controlador != null) {
            actualizarEstadoJuego();
        }
    }
    
    // Deshabilita el botón de ver solución (usado en nuevo juego)
    public void deshabilitarBotonSolucion() {
        botonSolucion.setEnabled(false);
    }
    
    // Habilita el botón de ver solución (usado después de perder)
    public void habilitarBotonSolucion() {
        botonSolucion.setEnabled(true);
    }
    
    private void inicializarComponentesDinamicos(NivelDificultad nivel) {
        int tamaño = nivel.obtenerTamañoGrilla();
        int tamañoCelda = obtenerTamañoCelda();
        
        // Calcular cuántas columnas de pistas se necesitan
        // Para grillas pequeñas (5x5) usamos 3, para más grandes usamos más
        int columnasPistas = Math.max(3, Math.min(tamaño / 2, 8)); // Máximo 8 columnas para evitar que se vea mal
        
        botonesCelda = new JButton[tamaño][tamaño];
        for (int fila = 0; fila < tamaño; fila++) {
            for (int columna = 0; columna < tamaño; columna++) {
                botonesCelda[fila][columna] = new JButton();
                botonesCelda[fila][columna].setPreferredSize(new Dimension(tamañoCelda, tamañoCelda));
                botonesCelda[fila][columna].setBackground(Color.WHITE);
                botonesCelda[fila][columna].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                botonesCelda[fila][columna].setFocusable(false); // Deshabilitar focus para evitar el cuadro azul
            }
        }
        
        etiquetasPistasFilas = new JLabel[tamaño][columnasPistas];
        for (int fila = 0; fila < tamaño; fila++) {
            for (int i = 0; i < columnasPistas; i++) {
                etiquetasPistasFilas[fila][i] = new JLabel("");
                // El ancho se ajustará automáticamente al contenido
                etiquetasPistasFilas[fila][i].setPreferredSize(new Dimension(ANCHO_PISTA_MINIMO, ALTO_PISTA));
                etiquetasPistasFilas[fila][i].setHorizontalAlignment(SwingConstants.CENTER);
                etiquetasPistasFilas[fila][i].setFont(new Font("Arial", Font.BOLD, 10));
            }
        }
        
        etiquetasPistasColumnas = new JLabel[columnasPistas][tamaño];
        for (int i = 0; i < columnasPistas; i++) {
            for (int columna = 0; columna < tamaño; columna++) {
                etiquetasPistasColumnas[i][columna] = new JLabel("");
                // El ancho se ajustará automáticamente al contenido
                etiquetasPistasColumnas[i][columna].setPreferredSize(new Dimension(tamañoCelda, ALTO_PISTA));
                etiquetasPistasColumnas[i][columna].setHorizontalAlignment(SwingConstants.CENTER);
                etiquetasPistasColumnas[i][columna].setFont(new Font("Arial", Font.BOLD, 10));
            }
        }
    }
}