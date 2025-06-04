package sistemaFacturacion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Conexion.ConexionBD;
import Modelo.DetalleFactura;
import Modelo.EncabezadoFactura;
import Vista.ConsultarFactura;
import Vista.IngresoFactura;
import Vista.EliminarFacturas;
import Vista.ModificarFactura;
import java.sql.Connection;
import java.sql.SQLException;

public class Main extends JFrame {
    
    // Componentes de la interfaz
    private JButton btnIngresoFactura;
    private JButton btnModificarFactura;
    private JButton btnEliminarFactura;
    private JButton btnConsultarFactura;
    private JButton btnSalir;
    private JLabel lblTitulo;
    
    public Main() {
        initComponents();
        setupWindow();
    }
    
    private void initComponents() {
        // Configuración del título
        lblTitulo = new JLabel("SISTEMA DE FACTURACIÓN");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setForeground(new Color(51, 102, 153));
        
        // Inicialización de botones
        btnIngresoFactura = new JButton("Ingreso de Facturas");
        btnModificarFactura = new JButton("Modificar Facturas");
        btnEliminarFactura = new JButton("Eliminar Facturas");
        btnConsultarFactura = new JButton("Consultar Facturas");
        btnSalir = new JButton("Salir");
        
        // Configuración de botones
        configurarBotones();
        
        // Configuración de eventos
        configurarEventos();
    }
    
    private void configurarBotones() {
        // Configurar tamaño y estilo de botones
        Dimension btnSize = new Dimension(200, 40);
        Font btnFont = new Font("Arial", Font.PLAIN, 14);
        
        JButton[] botones = {btnIngresoFactura, btnModificarFactura, 
                           btnEliminarFactura, btnConsultarFactura, btnSalir};
        
        for (JButton btn : botones) {
            btn.setPreferredSize(btnSize);
            btn.setFont(btnFont);
            btn.setFocusPainted(false);
        }
        
        // Colores específicos
        btnIngresoFactura.setBackground(new Color(76, 175, 80));
        btnIngresoFactura.setForeground(Color.WHITE);
        
        btnModificarFactura.setBackground(new Color(255, 193, 7));
        btnModificarFactura.setForeground(Color.BLACK);
        
        btnEliminarFactura.setBackground(new Color(244, 67, 54));
        btnEliminarFactura.setForeground(Color.WHITE);
        
        btnConsultarFactura.setBackground(new Color(33, 150, 243));
        btnConsultarFactura.setForeground(Color.WHITE);
        
        btnSalir.setBackground(new Color(158, 158, 158));
        btnSalir.setForeground(Color.WHITE);
    }
    
    private void configurarEventos() {
        // Evento para Ingreso de Facturas
        btnIngresoFactura.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirVentanaIngreso();
            }
        });
        
        // Evento para Modificar Facturas
        btnModificarFactura.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirVentanaModificacion();
            }
        });
        
        // Evento para Eliminar Facturas
        btnEliminarFactura.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirVentanaEliminacion();
            }
        });
        
        // Evento para Consultar Facturas
        btnConsultarFactura.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirVentanaConsulta();
            }
        });
        
        // Evento para Salir
        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salirAplicacion();
            }
        });
    }
    
    private void setupWindow() {
        // Configuración del layout
        setLayout(new BorderLayout());
        
        // Panel principal
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new GridBagLayout());
        panelPrincipal.setBackground(Color.WHITE);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Agregar título
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelPrincipal.add(lblTitulo, gbc);
        
        // Agregar separador
        gbc.gridy = 1;
        gbc.insets = new Insets(20, 10, 20, 10);
        panelPrincipal.add(new JSeparator(), gbc);
        
        // Agregar botones
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridy = 2;
        panelPrincipal.add(btnIngresoFactura, gbc);
        
        gbc.gridy = 3;
        panelPrincipal.add(btnModificarFactura, gbc);
        
        gbc.gridy = 4;
        panelPrincipal.add(btnEliminarFactura, gbc);
        
        gbc.gridy = 5;
        panelPrincipal.add(btnConsultarFactura, gbc);
        
        // Separador antes del botón salir
        gbc.gridy = 6;
        gbc.insets = new Insets(20, 10, 10, 10);
        panelPrincipal.add(new JSeparator(), gbc);
        
        gbc.gridy = 7;
        gbc.insets = new Insets(10, 10, 10, 10);
        panelPrincipal.add(btnSalir, gbc);
        
        add(panelPrincipal, BorderLayout.CENTER);
        
        // Configuración de la ventana
        setTitle("Sistema de Facturación - Ferretería");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Icono de la aplicación (opcional)
        try {
            setIconImage(new ImageIcon(getClass().getResource("/icons/factura.png")).getImage());
        } catch (Exception e) {
            // Si no encuentra el icono, continúa sin él
        }
    }
    
    // Métodos para abrir las diferentes ventanas
   private void abrirVentanaIngreso() {
    try {
        new IngresoFactura().setVisible(true);
    } catch (Exception e) {
        mostrarError("Error al abrir ventana de ingreso", e);
    }
}
    
    private void abrirVentanaModificacion() {
        try {
         new ModificarFactura().setVisible(true);
    } catch (Exception e) {
        mostrarError("Error al abrir ventana de modificacion", e);
    }
    }
    
    private void abrirVentanaEliminacion() {
        try {
            new EliminarFacturas().setVisible(true);
        } catch (Exception e) {
            mostrarError("Error al abrir ventana de eliminar facturas", e);
        }
    }
    
    private void abrirVentanaConsulta() {
    try {
        new ConsultarFactura().setVisible(true);
    } catch (Exception e) {
        mostrarError("Error al abrir ventana de consulta", e);
    }
}
    
    private void salirAplicacion() {
        int respuesta = JOptionPane.showConfirmDialog(this,
            "¿Está seguro que desea salir del sistema?",
            "Confirmar salida",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (respuesta == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
    
    private void mostrarError(String mensaje, Exception e) {
        JOptionPane.showMessageDialog(this,
            mensaje + "\n" + e.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
    
    // Método main para ejecutar la aplicación
    public static void main(String[] args) {
        // Probar conexión a la base de datos al iniciar
        probarConexionBD();
        
        // Ejecutar en el Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Main().setVisible(true);
            }
        });
    }
    
    // Método para probar la conexión a la base de datos
    private static void probarConexionBD() {
        try {
            Connection conn = ConexionBD.getConnection();
            System.out.println("Conexión a la base de datos exitosa");
            ConexionBD.closeConnection(conn);
        } catch (SQLException e) {
            System.err.println("Error de conexión a la base de datos: " + e.getMessage());
            JOptionPane.showMessageDialog(null, 
                "Error de conexión a la base de datos:\n" + e.getMessage() +
                "\n\nVerifica que SQL Server esté ejecutándose en el puerto correcto",
                "Error de Conexión", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}