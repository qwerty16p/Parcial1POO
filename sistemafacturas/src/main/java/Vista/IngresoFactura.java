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
import java.util.Date;
import Conexion.ConexionBD;
import DAO.FacturaDAO;
import Modelo.DetalleFactura;
import Modelo.EncabezadoFactura;

public class IngresoFactura extends JFrame {
    
    // Componentes del encabezado
    private JTextField txtIdFactura;
    private JTextField txtFechaEmision;
    private JTextField txtIdCliente;
    private JTextField txtIdVendedor;
    private JLabel lblTotalFactura;
    
    // Componentes para agregar detalles
    private JTextField txtIdProducto;
    private JTextField txtCantidad;
    private JTextField txtPrecioUnitario;
    private JButton btnAgregarDetalle;
    private JButton btnEliminarDetalle;
    
    // Tabla de detalles
    private JTable tablaDetalles;
    private DefaultTableModel modeloTabla;
    
    // Botones principales
    private JButton btnGuardarFactura;
    private JButton btnLimpiarTodo;
    private JButton btnCerrar;
    
    // Variables de control
    private float totalFactura = 0.0f;
    private FacturaDAO facturaDAO;
    
    public IngresoFactura() {
        facturaDAO = new FacturaDAO();
        initComponents();
        setupWindow();
    }
    
    private void initComponents() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel del encabezado
        JPanel panelEncabezado = crearPanelEncabezado();
        
        // Panel para agregar detalles
        JPanel panelAgregarDetalle = crearPanelAgregarDetalle();
        
        // Panel de la tabla
        JPanel panelTabla = crearPanelTabla();
        
        // Panel de botones
        JPanel panelBotones = crearPanelBotones();
        
        // Ensamblar la interfaz
        JPanel panelSuperior = new JPanel(new BorderLayout(10, 10));
        panelSuperior.add(panelEncabezado, BorderLayout.NORTH);
        panelSuperior.add(panelAgregarDetalle, BorderLayout.CENTER);
        
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipal.add(panelTabla, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        
        add(panelPrincipal);
        
        configurarEventos();
        inicializarDatos();
    }
    
    private JPanel crearPanelEncabezado() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Datos del Encabezado"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // ID Factura
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("ID Factura:"), gbc);
        gbc.gridx = 1;
        txtIdFactura = new JTextField(10);
        panel.add(txtIdFactura, gbc);
        
        // Fecha de Emisión
        gbc.gridx = 2; gbc.gridy = 0;
        panel.add(new JLabel("Fecha Emisión:"), gbc);
        gbc.gridx = 3;
        txtFechaEmision = new JTextField(12);
        panel.add(txtFechaEmision, gbc);
        
        // ID Cliente
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("ID Cliente:"), gbc);
        gbc.gridx = 1;
        txtIdCliente = new JTextField(10);
        panel.add(txtIdCliente, gbc);
        
        // ID Vendedor
        gbc.gridx = 2; gbc.gridy = 1;
        panel.add(new JLabel("ID Vendedor:"), gbc);
        gbc.gridx = 3;
        txtIdVendedor = new JTextField(10);
        panel.add(txtIdVendedor, gbc);
        
        // Total Factura (solo lectura)
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Total Factura:"), gbc);
        gbc.gridx = 1;
        lblTotalFactura = new JLabel("$0.00");
        lblTotalFactura.setFont(new Font("Arial", Font.BOLD, 14));
        lblTotalFactura.setForeground(new Color(0, 128, 0));
        panel.add(lblTotalFactura, gbc);
        
        return panel;
    }
    
    private JPanel crearPanelAgregarDetalle() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Agregar Detalle"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // ID Producto
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("ID Producto:"), gbc);
        gbc.gridx = 1;
        txtIdProducto = new JTextField(10);
        panel.add(txtIdProducto, gbc);
        
        // Cantidad
        gbc.gridx = 2; gbc.gridy = 0;
        panel.add(new JLabel("Cantidad:"), gbc);
        gbc.gridx = 3;
        txtCantidad = new JTextField(8);
        panel.add(txtCantidad, gbc);
        
        // Precio Unitario
        gbc.gridx = 4; gbc.gridy = 0;
        panel.add(new JLabel("Precio Unit.:"), gbc);
        gbc.gridx = 5;
        txtPrecioUnitario = new JTextField(10);
        panel.add(txtPrecioUnitario, gbc);
        
        // Botones
        gbc.gridx = 6; gbc.gridy = 0;
        btnAgregarDetalle = new JButton("Agregar");
        btnAgregarDetalle.setBackground(new Color(76, 175, 80));
        btnAgregarDetalle.setForeground(Color.WHITE);
        btnAgregarDetalle.setFocusPainted(false);
        panel.add(btnAgregarDetalle, gbc);
        
        gbc.gridx = 7; gbc.gridy = 0;
        btnEliminarDetalle = new JButton("Eliminar");
        btnEliminarDetalle.setBackground(new Color(244, 67, 54));
        btnEliminarDetalle.setForeground(Color.WHITE);
        btnEliminarDetalle.setFocusPainted(false);
        panel.add(btnEliminarDetalle, gbc);
        
        return panel;
    }
    
    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Detalles de la Factura"));
        
        String[] columnas = {"#", "ID Producto", "Cantidad", "Precio Unitario", "Importe"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaDetalles = new JTable(modeloTabla);
        tablaDetalles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaDetalles.getTableHeader().setReorderingAllowed(false);
        
        // Configurar ancho de columnas
        tablaDetalles.getColumnModel().getColumn(0).setPreferredWidth(40);  // #
        tablaDetalles.getColumnModel().getColumn(1).setPreferredWidth(80);  // ID Producto
        tablaDetalles.getColumnModel().getColumn(2).setPreferredWidth(70);  // Cantidad
        tablaDetalles.getColumnModel().getColumn(3).setPreferredWidth(100); // Precio Unitario
        tablaDetalles.getColumnModel().getColumn(4).setPreferredWidth(100); // Importe
        
        JScrollPane scrollPane = new JScrollPane(tablaDetalles);
        scrollPane.setPreferredSize(new Dimension(500, 250));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        btnGuardarFactura = new JButton("Guardar Factura");
        btnGuardarFactura.setBackground(new Color(76, 175, 80));
        btnGuardarFactura.setForeground(Color.WHITE);
        btnGuardarFactura.setFocusPainted(false);
        btnGuardarFactura.setFont(new Font("Arial", Font.BOLD, 12));
        
        btnLimpiarTodo = new JButton("Limpiar Todo");
        btnLimpiarTodo.setBackground(new Color(255, 193, 7));
        btnLimpiarTodo.setForeground(Color.BLACK);
        btnLimpiarTodo.setFocusPainted(false);
        
        btnCerrar = new JButton("Cerrar");
        btnCerrar.setBackground(new Color(158, 158, 158));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFocusPainted(false);
        
        panel.add(btnGuardarFactura);
        panel.add(btnLimpiarTodo);
        panel.add(btnCerrar);
        
        return panel;
    }
    
    private void configurarEventos() {
        // Agregar detalle
        btnAgregarDetalle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarDetalle();
            }
        });
        
        // Eliminar detalle seleccionado
        btnEliminarDetalle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarDetalle();
            }
        });
        
        // Guardar factura
        btnGuardarFactura.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarFactura();
            }
        });
        
        // Limpiar todo
        btnLimpiarTodo.addActionListener(new ActionListener() {
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
        
        // Enter en campos de detalle para agregar automáticamente
        txtPrecioUnitario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarDetalle();
            }
        });
    }
    
    private void inicializarDatos() {
        // Establecer fecha actual
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        txtFechaEmision.setText(sdf.format(new Date()));
        
        // Generar ID de factura automático
        generarNuevoIdFactura();
        
        // Establecer foco inicial
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                txtIdCliente.requestFocus();
            }
        });
    }
    
    private void generarNuevoIdFactura() {
        try {
            Connection con = ConexionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "SELECT ISNULL(MAX(IdFactura), 0) + 1 FROM EncabezadoFactura");
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                int nuevoId = rs.getInt(1);
                txtIdFactura.setText(String.valueOf(nuevoId));
            }
            
            con.close();
        } catch (SQLException e) {
            txtIdFactura.setText("1");
            System.err.println("Error al generar ID: " + e.getMessage());
        }
    }
    
    private void agregarDetalle() {
        try {
            // Validar campos
            if (txtIdProducto.getText().trim().isEmpty() ||
                txtCantidad.getText().trim().isEmpty() ||
                txtPrecioUnitario.getText().trim().isEmpty()) {
                
                JOptionPane.showMessageDialog(this,
                    "Por favor complete todos los campos del detalle",
                    "Campos requeridos",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int idProducto = Integer.parseInt(txtIdProducto.getText().trim());
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());
            float precioUnitario = Float.parseFloat(txtPrecioUnitario.getText().trim());
            
            if (cantidad <= 0 || precioUnitario <= 0) {
                JOptionPane.showMessageDialog(this,
                    "La cantidad y el precio deben ser mayores que cero",
                    "Valores inválidos",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            float importe = cantidad * precioUnitario;
            
            // Agregar fila a la tabla
            Object[] fila = {
                modeloTabla.getRowCount() + 1, // Número consecutivo
                idProducto,
                cantidad,
                String.format("%.2f", precioUnitario),
                String.format("%.2f", importe)
            };
            
            modeloTabla.addRow(fila);
            
            // Actualizar total
            totalFactura += importe;
            lblTotalFactura.setText(String.format("$%.2f", totalFactura));
            
            // Limpiar campos de detalle
            txtIdProducto.setText("");
            txtCantidad.setText("");
            txtPrecioUnitario.setText("");
            txtIdProducto.requestFocus();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Por favor ingrese valores numéricos válidos",
                "Error de formato",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void eliminarDetalle() {
        int filaSeleccionada = tablaDetalles.getSelectedRow();
        
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                "Por favor seleccione una fila para eliminar",
                "Selección requerida",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Obtener importe de la fila a eliminar
        String importeStr = (String) modeloTabla.getValueAt(filaSeleccionada, 4);
        float importe = Float.parseFloat(importeStr);
        
        // Eliminar fila
        modeloTabla.removeRow(filaSeleccionada);
        
        // Actualizar total
        totalFactura -= importe;
        lblTotalFactura.setText(String.format("$%.2f", totalFactura));
        
        // Renumerar filas
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            modeloTabla.setValueAt(i + 1, i, 0);
        }
    }
    
    private void guardarFactura() {
        try {
            // Validar encabezado
            if (txtIdFactura.getText().trim().isEmpty() ||
                txtFechaEmision.getText().trim().isEmpty() ||
                txtIdCliente.getText().trim().isEmpty() ||
                txtIdVendedor.getText().trim().isEmpty()) {
                
                JOptionPane.showMessageDialog(this,
                    "Por favor complete todos los campos del encabezado",
                    "Campos requeridos",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Validar que haya detalles
            if (modeloTabla.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this,
                    "Debe agregar al menos un detalle a la factura",
                    "Sin detalles",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            Connection con = ConexionBD.getConnection();
            con.setAutoCommit(false);
            
            try {
                // Insertar encabezado
                PreparedStatement psEncabezado = con.prepareStatement(
                    "INSERT INTO EncabezadoFactura (IdFactura, FechaEmision, IdCliente, IdVendedor, TotalFactura) " +
                    "VALUES (?, ?, ?, ?, ?)");
                
                psEncabezado.setInt(1, Integer.parseInt(txtIdFactura.getText().trim()));
                psEncabezado.setString(2, txtFechaEmision.getText().trim());
                psEncabezado.setInt(3, Integer.parseInt(txtIdCliente.getText().trim()));
                psEncabezado.setInt(4, Integer.parseInt(txtIdVendedor.getText().trim()));
                psEncabezado.setFloat(5, totalFactura);
                
                psEncabezado.executeUpdate();
                
                // Insertar detalles
                PreparedStatement psDetalle = con.prepareStatement(
                    "INSERT INTO DetalleFactura (IdFactura, IdProducto, Cantidad, PrecioUnitario, Importe) " +
                    "VALUES (?, ?, ?, ?, ?)");
                
                int idFactura = Integer.parseInt(txtIdFactura.getText().trim());
                
                for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                    psDetalle.setInt(1, idFactura);
                    psDetalle.setInt(2, (Integer) modeloTabla.getValueAt(i, 1));
                    psDetalle.setInt(3, (Integer) modeloTabla.getValueAt(i, 2));
                    psDetalle.setFloat(4, Float.parseFloat((String) modeloTabla.getValueAt(i, 3)));
                    psDetalle.setFloat(5, Float.parseFloat((String) modeloTabla.getValueAt(i, 4)));
                    
                    psDetalle.executeUpdate();
                }
                
                con.commit();
                
                JOptionPane.showMessageDialog(this,
                    "Factura guardada exitosamente con ID: " + idFactura,
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
                
                limpiarTodo();
                
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
                con.close();
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Por favor ingrese valores numéricos válidos",
                "Error de formato",
                JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error al guardar la factura:\n" + e.getMessage(),
                "Error de base de datos",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limpiarTodo() {
        txtIdCliente.setText("");
        txtIdVendedor.setText("");
        txtIdProducto.setText("");
        txtCantidad.setText("");
        txtPrecioUnitario.setText("");
        
        modeloTabla.setRowCount(0);
        totalFactura = 0.0f;
        lblTotalFactura.setText("$0.00");
        
        generarNuevoIdFactura();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        txtFechaEmision.setText(sdf.format(new Date()));
        
        txtIdCliente.requestFocus();
    }
    
    private void setupWindow() {
        setTitle("Ingreso de Facturas - Sistema de Facturación");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 700);
        setLocationRelativeTo(null);
        setResizable(true);
    }
}