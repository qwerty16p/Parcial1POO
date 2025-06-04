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

public class ModificarFactura extends JFrame {
    
    // Componentes de búsqueda
    private JTextField txtBuscarFactura;
    private JButton btnBuscar;
    private JButton btnCargarFactura;
    
    // Componentes del encabezado (editables)
    private JTextField txtIdFactura;
    private JTextField txtFechaEmision;
    private JTextField txtIdCliente;
    private JTextField txtIdVendedor;
    private JLabel lblTotalFactura;
    
    // Componentes para modificar detalles
    private JTextField txtIdProducto;
    private JTextField txtCantidad;
    private JTextField txtPrecioUnitario;
    private JButton btnAgregarDetalle;
    private JButton btnModificarDetalle;
    private JButton btnEliminarDetalle;
    
    // Tabla de detalles
    private JTable tablaDetalles;
    private DefaultTableModel modeloTabla;
    
    // Botones principales
    private JButton btnGuardarCambios;
    private JButton btnLimpiarTodo;
    private JButton btnCerrar;
    
    // Variables de control
    private float totalFactura = 0.0f;
    private boolean facturaSeleccionada = false;
    
    public ModificarFactura() {
        initComponents();
        setupWindow();
    }
    
    private void initComponents() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel de búsqueda
        JPanel panelBusqueda = crearPanelBusqueda();
        
        // Panel del encabezado
        JPanel panelEncabezado = crearPanelEncabezado();
        
        // Panel para modificar detalles
        JPanel panelModificarDetalle = crearPanelModificarDetalle();
        
        // Panel de la tabla
        JPanel panelTabla = crearPanelTabla();
        
        // Panel de botones
        JPanel panelBotones = crearPanelBotones();
        
        // Ensamblar la interfaz
        JPanel panelSuperior = new JPanel(new BorderLayout(10, 10));
        panelSuperior.add(panelBusqueda, BorderLayout.NORTH);
        panelSuperior.add(panelEncabezado, BorderLayout.CENTER);
        panelSuperior.add(panelModificarDetalle, BorderLayout.SOUTH);
        
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipal.add(panelTabla, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        
        add(panelPrincipal);
        
        configurarEventos();
        deshabilitarEdicion();
    }
    
    private JPanel crearPanelBusqueda() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Buscar Factura para Modificar"));
        
        panel.add(new JLabel("ID Factura:"));
        txtBuscarFactura = new JTextField(15);
        panel.add(txtBuscarFactura);
        
        btnBuscar = new JButton("Buscar");
        btnBuscar.setBackground(new Color(33, 150, 243));
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFocusPainted(false);
        panel.add(btnBuscar);
        
        btnCargarFactura = new JButton("Cargar para Editar");
        btnCargarFactura.setBackground(new Color(76, 175, 80));
        btnCargarFactura.setForeground(Color.WHITE);
        btnCargarFactura.setFocusPainted(false);
        btnCargarFactura.setEnabled(false);
        panel.add(btnCargarFactura);
        
        return panel;
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
        txtIdFactura.setEditable(false);
        txtIdFactura.setBackground(Color.LIGHT_GRAY);
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
    
    private JPanel crearPanelModificarDetalle() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Modificar Detalles"));
        
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
        gbc.gridx = 0; gbc.gridy = 1;
        btnAgregarDetalle = new JButton("Agregar");
        btnAgregarDetalle.setBackground(new Color(76, 175, 80));
        btnAgregarDetalle.setForeground(Color.WHITE);
        btnAgregarDetalle.setFocusPainted(false);
        panel.add(btnAgregarDetalle, gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        btnModificarDetalle = new JButton("Modificar");
        btnModificarDetalle.setBackground(new Color(255, 193, 7));
        btnModificarDetalle.setForeground(Color.BLACK);
        btnModificarDetalle.setFocusPainted(false);
        panel.add(btnModificarDetalle, gbc);
        
        gbc.gridx = 2; gbc.gridy = 1;
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
        
        btnGuardarCambios = new JButton("Guardar Cambios");
        btnGuardarCambios.setBackground(new Color(76, 175, 80));
        btnGuardarCambios.setForeground(Color.WHITE);
        btnGuardarCambios.setFocusPainted(false);
        btnGuardarCambios.setFont(new Font("Arial", Font.BOLD, 12));
        
        btnLimpiarTodo = new JButton("Limpiar Todo");
        btnLimpiarTodo.setBackground(new Color(255, 193, 7));
        btnLimpiarTodo.setForeground(Color.BLACK);
        btnLimpiarTodo.setFocusPainted(false);
        
        btnCerrar = new JButton("Cerrar");
        btnCerrar.setBackground(new Color(158, 158, 158));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFocusPainted(false);
        
        panel.add(btnGuardarCambios);
        panel.add(btnLimpiarTodo);
        panel.add(btnCerrar);
        
        return panel;
    }
    
    private void setupWindow() {
        setTitle("Modificar Facturas - Sistema de Facturación");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(true);
    }
    
    private void deshabilitarEdicion() {
        txtFechaEmision.setEnabled(false);
        txtIdCliente.setEnabled(false);
        txtIdVendedor.setEnabled(false);
        txtIdProducto.setEnabled(false);
        txtCantidad.setEnabled(false);
        txtPrecioUnitario.setEnabled(false);
        btnAgregarDetalle.setEnabled(false);
        btnModificarDetalle.setEnabled(false);
        btnEliminarDetalle.setEnabled(false);
        btnGuardarCambios.setEnabled(false);
        btnLimpiarTodo.setEnabled(false);
    }
    
    private void habilitarEdicion() {
        txtFechaEmision.setEnabled(true);
        txtIdCliente.setEnabled(true);
        txtIdVendedor.setEnabled(true);
        txtIdProducto.setEnabled(true);
        txtCantidad.setEnabled(true);
        txtPrecioUnitario.setEnabled(true);
        btnAgregarDetalle.setEnabled(true);
        btnModificarDetalle.setEnabled(true);
        btnEliminarDetalle.setEnabled(true);
        btnGuardarCambios.setEnabled(true);
        btnLimpiarTodo.setEnabled(true);
    }
    
    private void limpiarCamposDetalle() {
        txtIdProducto.setText("");
        txtCantidad.setText("");
        txtPrecioUnitario.setText("");
    }
    
    private void limpiarTodo() {
        txtBuscarFactura.setText("");
        txtIdFactura.setText("");
        txtFechaEmision.setText("");
        txtIdCliente.setText("");
        txtIdVendedor.setText("");
        lblTotalFactura.setText("$0.00");
        limpiarCamposDetalle();
        modeloTabla.setRowCount(0);
        totalFactura = 0.0f;
        facturaSeleccionada = false;
        btnCargarFactura.setEnabled(false);
        deshabilitarEdicion();
    }
    
    private void configurarEventos() {
        // Buscar factura
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarFactura();
            }
        });
        
        // Cargar factura para editar
        btnCargarFactura.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarFacturaParaEditar();
            }
        });
        
        // Selección en tabla para cargar detalle
        tablaDetalles.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarDetalleSeleccionado();
            }
        });
        
        // Agregar detalle
        btnAgregarDetalle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarDetalle();
            }
        });
        
        // Modificar detalle seleccionado
        btnModificarDetalle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modificarDetalle();
            }
        });
        
        // Eliminar detalle seleccionado
        btnEliminarDetalle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarDetalle();
            }
        });
        
        // Guardar cambios
        btnGuardarCambios.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarCambios();
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
        
        // Enter en campo de búsqueda
        txtBuscarFactura.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarFactura();
            }
        });
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
            
            if (rs.next()) {
                // Mostrar información encontrada
                txtIdFactura.setText(rs.getString("IdFactura"));
                txtFechaEmision.setText(rs.getString("FechaEmision"));
                txtIdCliente.setText(rs.getString("IdCliente"));
                txtIdVendedor.setText(rs.getString("IdVendedor"));
                lblTotalFactura.setText(String.format("$%.2f", rs.getFloat("TotalFactura")));
                
                btnCargarFactura.setEnabled(true);
                
                JOptionPane.showMessageDialog(this,
                    "Factura encontrada. Presione 'Cargar para Editar' para modificarla.",
                    "Factura encontrada",
                    JOptionPane.INFORMATION_MESSAGE);
                
            } else {
                JOptionPane.showMessageDialog(this,
                    "No se encontró ninguna factura con el ID: " + idFactura,
                    "Factura no encontrada",
                    JOptionPane.INFORMATION_MESSAGE);
                btnCargarFactura.setEnabled(false);
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
    
    private void cargarFacturaParaEditar() {
        try {
            int idFactura = Integer.parseInt(txtIdFactura.getText());
            Connection con = ConexionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "SELECT IdProducto, Cantidad, PrecioUnitario, Importe " +
                "FROM DetalleFactura WHERE IdFactura = ?");
            ps.setInt(1, idFactura);
            ResultSet rs = ps.executeQuery();
            
            modeloTabla.setRowCount(0);
            totalFactura = 0.0f;
            
            while (rs.next()) {
                Object[] fila = {
                    modeloTabla.getRowCount() + 1,
                    rs.getInt("IdProducto"),
                    rs.getInt("Cantidad"),
                    String.format("%.2f", rs.getFloat("PrecioUnitario")),
                    String.format("%.2f", rs.getFloat("Importe"))
                };
                modeloTabla.addRow(fila);
                totalFactura += rs.getFloat("Importe");
            }
            
            lblTotalFactura.setText(String.format("$%.2f", totalFactura));
            con.close();
            
            // Habilitar edición
            habilitarEdicion();
            facturaSeleccionada = true;
            
            JOptionPane.showMessageDialog(this,
                "Factura cargada exitosamente. Ahora puede modificarla.",
                "Factura cargada",
                JOptionPane.INFORMATION_MESSAGE);
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar los detalles de la factura:\n" + e.getMessage(),
                "Error de base de datos",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void cargarDetalleSeleccionado() {
        int filaSeleccionada = tablaDetalles.getSelectedRow();
        
        if (filaSeleccionada != -1) {
            txtIdProducto.setText(modeloTabla.getValueAt(filaSeleccionada, 1).toString());
            txtCantidad.setText(modeloTabla.getValueAt(filaSeleccionada, 2).toString());
            txtPrecioUnitario.setText(modeloTabla.getValueAt(filaSeleccionada, 3).toString());
        }
    }
    
    private void agregarDetalle() {
        if (!facturaSeleccionada) {
            JOptionPane.showMessageDialog(this,
                "Primero debe cargar una factura para editar",
                "Factura no cargada",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
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
                modeloTabla.getRowCount() + 1,
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
            limpiarCamposDetalle();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Por favor ingrese valores numéricos válidos",
                "Error de formato",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void modificarDetalle() {
        int filaSeleccionada = tablaDetalles.getSelectedRow();
        
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                "Por favor seleccione una fila para modificar",
                "Selección requerida",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
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
            
            // Restar el importe anterior del total
            String importeAnterior = (String) modeloTabla.getValueAt(filaSeleccionada, 4);
            totalFactura -= Float.parseFloat(importeAnterior);
            
            // Calcular nuevo importe
            float importe = cantidad * precioUnitario;
            
            // Actualizar fila
            modeloTabla.setValueAt(idProducto, filaSeleccionada, 1);
            modeloTabla.setValueAt(cantidad, filaSeleccionada, 2);
            modeloTabla.setValueAt(String.format("%.2f", precioUnitario), filaSeleccionada, 3);
            modeloTabla.setValueAt(String.format("%.2f", importe), filaSeleccionada, 4);
            
            // Actualizar total
            totalFactura += importe;
            lblTotalFactura.setText(String.format("$%.2f", totalFactura));
            
            // Limpiar campos de detalle
            limpiarCamposDetalle();
            
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
        
        // Limpiar campos de detalle
        limpiarCamposDetalle();
    }
    
    private void guardarCambios() {
        if (!facturaSeleccionada) {
            JOptionPane.showMessageDialog(this,
                "Primero debe cargar una factura para editar",
                "Factura no cargada",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
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
                    "Debe tener al menos un detalle en la factura",
                    "Sin detalles",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de que desea guardar los cambios en la factura?",
                "Confirmar modificación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
            
            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
            }
            
            Connection con = ConexionBD.getConnection();
            con.setAutoCommit(false);
            
            try {
                int idFactura = Integer.parseInt(txtIdFactura.getText().trim());
                
                // Actualizar encabezado
                PreparedStatement psEncabezado = con.prepareStatement(
                    "UPDATE EncabezadoFactura SET FechaEmision = ?, IdCliente = ?, " +
                    "IdVendedor = ?, TotalFactura = ? WHERE IdFactura = ?");
                
                psEncabezado.setString(1, txtFechaEmision.getText().trim());
                psEncabezado.setInt(2, Integer.parseInt(txtIdCliente.getText().trim()));
                psEncabezado.setInt(3, Integer.parseInt(txtIdVendedor.getText().trim()));
                psEncabezado.setFloat(4, totalFactura);
                psEncabezado.setInt(5, idFactura);
                
                psEncabezado.executeUpdate();
                
                // Eliminar detalles existentes
                PreparedStatement psEliminar = con.prepareStatement(
                    "DELETE FROM DetalleFactura WHERE IdFactura = ?");
                psEliminar.setInt(1, idFactura);
                psEliminar.executeUpdate();
                
                // Insertar detalles nuevos
                PreparedStatement psDetalle = con.prepareStatement(
                    "INSERT INTO DetalleFactura (IdFactura, IdProducto, Cantidad, PrecioUnitario, Importe) " +
                    "VALUES (?, ?, ?, ?, ?)");
                
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
                    "Factura modificada exitosamente con ID: " + idFactura,
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
                "Por favor ingrese valores numéricos válidos en los campos de ID y valores",
                "Error de formato",
                JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error al guardar los cambios:\n" + e.getMessage(),
                "Error de base de datos",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}