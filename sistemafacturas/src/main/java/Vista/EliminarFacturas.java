package Vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import Conexion.ConexionBD;

public class EliminarFacturas extends JFrame {
    
    // Componentes de búsqueda
    private JTextField txtBuscarFactura;
    private JButton btnBuscar;
    private JButton btnMostrarTodas;
    
    // Componentes de información de la factura
    private JTextField txtIdFactura;
    private JTextField txtFechaEmision;
    private JTextField txtIdCliente;
    private JTextField txtIdVendedor;
    private JLabel lblTotalFactura;
    
    // Tabla de facturas
    private JTable tablaFacturas;
    private DefaultTableModel modeloTabla;
    
    // Tabla de detalles de la factura seleccionada
    private JTable tablaDetalles;
    private DefaultTableModel modeloDetalles;
    
    // Botones principales
    private JButton btnEliminarFactura;
    private JButton btnLimpiar;
    private JButton btnCerrar;
    
    public EliminarFacturas() {
        initComponents();
        setupWindow();
        cargarTodasLasFacturas();
    }
    
    private void initComponents() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel de búsqueda
        JPanel panelBusqueda = crearPanelBusqueda();
        
        // Panel de información de la factura
        JPanel panelInfoFactura = crearPanelInfoFactura();
        
        // Panel de tablas
        JPanel panelTablas = crearPanelTablas();
        
        // Panel de botones
        JPanel panelBotones = crearPanelBotones();
        
        // Ensamblar la interfaz
        JPanel panelSuperior = new JPanel(new BorderLayout(10, 10));
        panelSuperior.add(panelBusqueda, BorderLayout.NORTH);
        panelSuperior.add(panelInfoFactura, BorderLayout.CENTER);
        
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipal.add(panelTablas, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        
        add(panelPrincipal);
        
        configurarEventos();
    }
    
    private JPanel crearPanelBusqueda() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Buscar Factura"));
        
        panel.add(new JLabel("ID Factura:"));
        txtBuscarFactura = new JTextField(15);
        panel.add(txtBuscarFactura);
        
        btnBuscar = new JButton("Buscar");
        btnBuscar.setBackground(new Color(33, 150, 243));
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFocusPainted(false);
        panel.add(btnBuscar);
        
        btnMostrarTodas = new JButton("Mostrar Todas");
        btnMostrarTodas.setBackground(new Color(76, 175, 80));
        btnMostrarTodas.setForeground(Color.WHITE);
        btnMostrarTodas.setFocusPainted(false);
        panel.add(btnMostrarTodas);
        
        return panel;
    }
    
    private JPanel crearPanelInfoFactura() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Información de la Factura Seleccionada"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // ID Factura
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("ID Factura:"), gbc);
        gbc.gridx = 1;
        txtIdFactura = new JTextField(10);
        txtIdFactura.setEditable(false);
        txtIdFactura.setBackground(Color.LIGHT_GRAY);
        panel.add(txtIdFactura, gbc);
        
        // Fecha de Emisión
        gbc.gridx = 2; gbc.gridy = 0;
        panel.add(new JLabel("Fecha Emisión:"), gbc);
        gbc.gridx = 3;
        txtFechaEmision = new JTextField(12);
        txtFechaEmision.setEditable(false);
        txtFechaEmision.setBackground(Color.LIGHT_GRAY);
        panel.add(txtFechaEmision, gbc);
        
        // ID Cliente
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("ID Cliente:"), gbc);
        gbc.gridx = 1;
        txtIdCliente = new JTextField(10);
        txtIdCliente.setEditable(false);
        txtIdCliente.setBackground(Color.LIGHT_GRAY);
        panel.add(txtIdCliente, gbc);
        
        // ID Vendedor
        gbc.gridx = 2; gbc.gridy = 1;
        panel.add(new JLabel("ID Vendedor:"), gbc);
        gbc.gridx = 3;
        txtIdVendedor = new JTextField(10);
        txtIdVendedor.setEditable(false);
        txtIdVendedor.setBackground(Color.LIGHT_GRAY);
        panel.add(txtIdVendedor, gbc);
        
        // Total Factura
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Total Factura:"), gbc);
        gbc.gridx = 1;
        lblTotalFactura = new JLabel("$0.00");
        lblTotalFactura.setFont(new Font("Arial", Font.BOLD, 14));
        lblTotalFactura.setForeground(new Color(0, 128, 0));
        panel.add(lblTotalFactura, gbc);
        
        return panel;
    }
    
    private JPanel crearPanelTablas() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 0));
        
        // Panel de tabla de facturas
        JPanel panelTablaFacturas = new JPanel(new BorderLayout());
        panelTablaFacturas.setBorder(BorderFactory.createTitledBorder("Lista de Facturas"));
        
        String[] columnasFacturas = {"ID Factura", "Fecha Emisión", "ID Cliente", "ID Vendedor", "Total"};
        modeloTabla = new DefaultTableModel(columnasFacturas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaFacturas = new JTable(modeloTabla);
        tablaFacturas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaFacturas.getTableHeader().setReorderingAllowed(false);
        
        JScrollPane scrollFacturas = new JScrollPane(tablaFacturas);
        scrollFacturas.setPreferredSize(new Dimension(400, 300));
        panelTablaFacturas.add(scrollFacturas, BorderLayout.CENTER);
        
        // Panel de tabla de detalles
        JPanel panelTablaDetalles = new JPanel(new BorderLayout());
        panelTablaDetalles.setBorder(BorderFactory.createTitledBorder("Detalles de la Factura"));
        
        String[] columnasDetalles = {"ID Producto", "Cantidad", "Precio Unit.", "Importe"};
        modeloDetalles = new DefaultTableModel(columnasDetalles, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaDetalles = new JTable(modeloDetalles);
        tablaDetalles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaDetalles.getTableHeader().setReorderingAllowed(false);
        
        JScrollPane scrollDetalles = new JScrollPane(tablaDetalles);
        scrollDetalles.setPreferredSize(new Dimension(400, 300));
        panelTablaDetalles.add(scrollDetalles, BorderLayout.CENTER);
        
        panel.add(panelTablaFacturas);
        panel.add(panelTablaDetalles);
        
        return panel;
    }
    
    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        btnEliminarFactura = new JButton("Eliminar Factura");
        btnEliminarFactura.setBackground(new Color(244, 67, 54));
        btnEliminarFactura.setForeground(Color.WHITE);
        btnEliminarFactura.setFocusPainted(false);
        btnEliminarFactura.setFont(new Font("Arial", Font.BOLD, 12));
        btnEliminarFactura.setEnabled(false);
        
        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setBackground(new Color(255, 193, 7));
        btnLimpiar.setForeground(Color.BLACK);
        btnLimpiar.setFocusPainted(false);
        
        btnCerrar = new JButton("Cerrar");
        btnCerrar.setBackground(new Color(158, 158, 158));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFocusPainted(false);
        
        panel.add(btnEliminarFactura);
        panel.add(btnLimpiar);
        panel.add(btnCerrar);
        
        return panel;
    }
    
    private void configurarEventos() {
        // Buscar factura
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarFactura();
            }
        });
        
        // Mostrar todas las facturas
        btnMostrarTodas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarTodasLasFacturas();
            }
        });
        
        // Selección en tabla de facturas
        tablaFacturas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarDetallesFactura();
            }
        });
        
        // Eliminar factura
        btnEliminarFactura.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarFactura();
            }
        });
        
        // Limpiar
        btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarTodo();
            }
        });
        
        // Cerrar
        btnCerrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        // Enter en campo de búsqueda
        txtBuscarFactura.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarFactura();
            }
        });
    }
    
    private void cargarTodasLasFacturas() {
        try {
            Connection con = ConexionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "SELECT IdFactura, FechaEmision, IdCliente, IdVendedor, TotalFactura " +
                "FROM EncabezadoFactura ORDER BY IdFactura DESC");
            ResultSet rs = ps.executeQuery();
            
            modeloTabla.setRowCount(0);
            
            while (rs.next()) {
                Object[] fila = {
                    rs.getInt("IdFactura"),
                    rs.getString("FechaEmision"),
                    rs.getInt("IdCliente"),
                    rs.getInt("IdVendedor"),
                    String.format("$%.2f", rs.getFloat("TotalFactura"))
                };
                modeloTabla.addRow(fila);
            }
            
            con.close();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar las facturas:\n" + e.getMessage(),
                "Error de base de datos",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void buscarFactura() {
        String idFactura = txtBuscarFactura.getText().trim();
        
        if (idFactura.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Por favor ingrese un ID de factura para buscar",
                "Campo requerido",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            Connection con = ConexionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "SELECT IdFactura, FechaEmision, IdCliente, IdVendedor, TotalFactura " +
                "FROM EncabezadoFactura WHERE IdFactura = ?");
            ps.setInt(1, Integer.parseInt(idFactura));
            ResultSet rs = ps.executeQuery();
            
            modeloTabla.setRowCount(0);
            
            if (rs.next()) {
                Object[] fila = {
                    rs.getInt("IdFactura"),
                    rs.getString("FechaEmision"),
                    rs.getInt("IdCliente"),
                    rs.getInt("IdVendedor"),
                    String.format("$%.2f", rs.getFloat("TotalFactura"))
                };
                modeloTabla.addRow(fila);
            } else {
                JOptionPane.showMessageDialog(this,
                    "No se encontró ninguna factura con el ID: " + idFactura,
                    "Factura no encontrada",
                    JOptionPane.INFORMATION_MESSAGE);
            }
            
            con.close();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Por favor ingrese un ID de factura válido (número entero)",
                "Formato inválido",
                JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error al buscar la factura:\n" + e.getMessage(),
                "Error de base de datos",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void cargarDetallesFactura() {
        int filaSeleccionada = tablaFacturas.getSelectedRow();
        
        if (filaSeleccionada == -1) {
            limpiarCamposFactura();
            btnEliminarFactura.setEnabled(false);
            return;
        }
        
        // Cargar información del encabezado
        txtIdFactura.setText(modeloTabla.getValueAt(filaSeleccionada, 0).toString());
        txtFechaEmision.setText(modeloTabla.getValueAt(filaSeleccionada, 1).toString());
        txtIdCliente.setText(modeloTabla.getValueAt(filaSeleccionada, 2).toString());
        txtIdVendedor.setText(modeloTabla.getValueAt(filaSeleccionada, 3).toString());
        lblTotalFactura.setText(modeloTabla.getValueAt(filaSeleccionada, 4).toString());
        
        // Cargar detalles
        try {
            int idFactura = Integer.parseInt(txtIdFactura.getText());
            Connection con = ConexionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "SELECT IdProducto, Cantidad, PrecioUnitario, Importe " +
                "FROM DetalleFactura WHERE IdFactura = ?");
            ps.setInt(1, idFactura);
            ResultSet rs = ps.executeQuery();
            
            modeloDetalles.setRowCount(0);
            
            while (rs.next()) {
                Object[] fila = {
                    rs.getInt("IdProducto"),
                    rs.getInt("Cantidad"),
                    String.format("%.2f", rs.getFloat("PrecioUnitario")),
                    String.format("%.2f", rs.getFloat("Importe"))
                };
                modeloDetalles.addRow(fila);
            }
            
            con.close();
            btnEliminarFactura.setEnabled(true);
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar los detalles de la factura:\n" + e.getMessage(),
                "Error de base de datos",
                JOptionPane.ERROR_MESSAGE);
            btnEliminarFactura.setEnabled(false);
        }
    }
    
    private void eliminarFactura() {
        int filaSeleccionada = tablaFacturas.getSelectedRow();
        
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                "Por favor seleccione una factura para eliminar",
                "Selección requerida",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String idFactura = txtIdFactura.getText();
        
        int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de que desea eliminar la factura " + idFactura + "?\n" +
            "Esta acción no se puede deshacer y eliminará tanto el encabezado como todos los detalles.",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }
        
        try {
            Connection con = ConexionBD.getConnection();
            con.setAutoCommit(false);
            
            try {
                // Eliminar detalles primero (por integridad referencial)
                PreparedStatement psDetalles = con.prepareStatement(
                    "DELETE FROM DetalleFactura WHERE IdFactura = ?");
                psDetalles.setInt(1, Integer.parseInt(idFactura));
                int detallesEliminados = psDetalles.executeUpdate();
                
                // Eliminar encabezado
                PreparedStatement psEncabezado = con.prepareStatement(
                    "DELETE FROM EncabezadoFactura WHERE IdFactura = ?");
                psEncabezado.setInt(1, Integer.parseInt(idFactura));
                int encabezadoEliminado = psEncabezado.executeUpdate();
                
                if (encabezadoEliminado > 0) {
                    con.commit();
                    
                    JOptionPane.showMessageDialog(this,
                        "Factura " + idFactura + " eliminada exitosamente.\n" +
                        "Detalles eliminados: " + detallesEliminados,
                        "Eliminación exitosa",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    // Actualizar la vista
                    cargarTodasLasFacturas();
                    limpiarCamposFactura();
                    
                } else {
                    con.rollback();
                    JOptionPane.showMessageDialog(this,
                        "No se pudo eliminar la factura. Verifique que existe.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
                
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
                con.close();
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error al eliminar la factura:\n" + e.getMessage(),
                "Error de base de datos",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limpiarCamposFactura() {
        txtIdFactura.setText("");
        txtFechaEmision.setText("");
        txtIdCliente.setText("");
        txtIdVendedor.setText("");
        lblTotalFactura.setText("$0.00");
        modeloDetalles.setRowCount(0);
        btnEliminarFactura.setEnabled(false);
    }
    
    private void limpiarTodo() {
        txtBuscarFactura.setText("");
        limpiarCamposFactura();
        cargarTodasLasFacturas();
        tablaFacturas.clearSelection();
    }
    
    private void setupWindow() {
        setTitle("Eliminar Facturas - Sistema de Facturación");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 750);
        setLocationRelativeTo(null);
        setResizable(true);
    }
}
