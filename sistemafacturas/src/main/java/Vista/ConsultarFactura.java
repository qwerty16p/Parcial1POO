package Vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import DAO.FacturaDAO;
import Modelo.DetalleFactura;
import Modelo.EncabezadoFactura;

public class ConsultarFactura extends JFrame {
    
    // Componentes de la interfaz
    private JTextField txtIdFactura;
    private JButton btnBuscar;
    private JButton btnLimpiar;
    private JButton btnCerrar;
    
    // Campos para mostrar datos del encabezado
    private JTextField txtFechaEmision;
    private JTextField txtIdCliente;
    private JTextField txtIdVendedor;
    private JTextField txtTotalFactura;
    
    // Tabla para mostrar detalles
    private JTable tablaDetalles;
    private DefaultTableModel modeloTabla;
    
    // DAO para operaciones
    private FacturaDAO facturaDAO;
    
    public ConsultarFactura() {
        facturaDAO = new FacturaDAO();
        initComponents();
        setupWindow();
    }
    
    private void initComponents() {
        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel superior - Búsqueda
        JPanel panelBusqueda = crearPanelBusqueda();
        
        // Panel central - Datos del encabezado
        JPanel panelEncabezado = crearPanelEncabezado();
        
        // Panel inferior - Tabla de detalles
        JPanel panelDetalles = crearPanelDetalles();
        
        // Panel de botones
        JPanel panelBotones = crearPanelBotones();
        
        // Agregar paneles al principal
        panelPrincipal.add(panelBusqueda, BorderLayout.NORTH);
        
        JPanel panelCentro = new JPanel(new BorderLayout(10, 10));
        panelCentro.add(panelEncabezado, BorderLayout.NORTH);
        panelCentro.add(panelDetalles, BorderLayout.CENTER);
        
        panelPrincipal.add(panelCentro, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        
        add(panelPrincipal);
        
        // Configurar eventos
        configurarEventos();
    }
    
    private JPanel crearPanelBusqueda() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Buscar Factura"));
        
        panel.add(new JLabel("ID Factura:"));
        txtIdFactura = new JTextField(10);
        panel.add(txtIdFactura);
        
        btnBuscar = new JButton("Buscar");
        btnBuscar.setBackground(new Color(33, 150, 243));
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFocusPainted(false);
        panel.add(btnBuscar);
        
        return panel;
    }
    
    private JPanel crearPanelEncabezado() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Datos de la Factura"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Fecha de Emisión
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Fecha de Emisión:"), gbc);
        gbc.gridx = 1;
        txtFechaEmision = new JTextField(15);
        txtFechaEmision.setEditable(false);
        txtFechaEmision.setBackground(Color.LIGHT_GRAY);
        panel.add(txtFechaEmision, gbc);
        
        // ID Cliente
        gbc.gridx = 2; gbc.gridy = 0;
        panel.add(new JLabel("ID Cliente:"), gbc);
        gbc.gridx = 3;
        txtIdCliente = new JTextField(10);
        txtIdCliente.setEditable(false);
        txtIdCliente.setBackground(Color.LIGHT_GRAY);
        panel.add(txtIdCliente, gbc);
        
        // ID Vendedor
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("ID Vendedor:"), gbc);
        gbc.gridx = 1;
        txtIdVendedor = new JTextField(10);
        txtIdVendedor.setEditable(false);
        txtIdVendedor.setBackground(Color.LIGHT_GRAY);
        panel.add(txtIdVendedor, gbc);
        
        // Total Factura
        gbc.gridx = 2; gbc.gridy = 1;
        panel.add(new JLabel("Total Factura:"), gbc);
        gbc.gridx = 3;
        txtTotalFactura = new JTextField(10);
        txtTotalFactura.setEditable(false);
        txtTotalFactura.setBackground(Color.LIGHT_GRAY);
        panel.add(txtTotalFactura, gbc);
        
        return panel;
    }
    
    private JPanel crearPanelDetalles() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Detalles de la Factura"));
        
        // Crear modelo de tabla
        String[] columnas = {"ID Detalle", "ID Producto", "Cantidad", "Precio Unitario", "Importe"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Solo lectura
            }
        };
        
        tablaDetalles = new JTable(modeloTabla);
        tablaDetalles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaDetalles.getTableHeader().setReorderingAllowed(false);
        
        // Configurar ancho de columnas
        tablaDetalles.getColumnModel().getColumn(0).setPreferredWidth(80);  // ID Detalle
        tablaDetalles.getColumnModel().getColumn(1).setPreferredWidth(80);  // ID Producto
        tablaDetalles.getColumnModel().getColumn(2).setPreferredWidth(70);  // Cantidad
        tablaDetalles.getColumnModel().getColumn(3).setPreferredWidth(100); // Precio Unitario
        tablaDetalles.getColumnModel().getColumn(4).setPreferredWidth(100); // Importe
        
        JScrollPane scrollPane = new JScrollPane(tablaDetalles);
        scrollPane.setPreferredSize(new Dimension(500, 200));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setBackground(new Color(255, 193, 7));
        btnLimpiar.setForeground(Color.BLACK);
        btnLimpiar.setFocusPainted(false);
        
        btnCerrar = new JButton("Cerrar");
        btnCerrar.setBackground(new Color(158, 158, 158));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFocusPainted(false);
        
        panel.add(btnLimpiar);
        panel.add(btnCerrar);
        
        return panel;
    }
    
    private void configurarEventos() {
        // Evento para buscar
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarFactura();
            }
        });
        
        // También buscar al presionar Enter en el campo ID
        txtIdFactura.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarFactura();
            }
        });
        
        // Evento para limpiar
        btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarCampos();
            }
        });
        
        // Evento para cerrar
        btnCerrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
    
    private void buscarFactura() {
        String idTexto = txtIdFactura.getText().trim();
        
        if (idTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor ingrese el ID de la factura a buscar", 
                "Campo requerido", 
                JOptionPane.WARNING_MESSAGE);
            txtIdFactura.requestFocus();
            return;
        }
        
        try {
            int idFactura = Integer.parseInt(idTexto);
            
            // Buscar encabezado
            EncabezadoFactura encabezado = facturaDAO.obtenerEncabezado(idFactura);
            
            if (encabezado == null) {
                JOptionPane.showMessageDialog(this, 
                    "No se encontró ninguna factura con el ID: " + idFactura, 
                    "Factura no encontrada", 
                    JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                return;
            }
            
            // Mostrar datos del encabezado
            mostrarEncabezado(encabezado);
            
            // Buscar y mostrar detalles
            List<DetalleFactura> detalles = facturaDAO.obtenerDetalles(idFactura);
            mostrarDetalles(detalles);
            
            JOptionPane.showMessageDialog(this, 
                "Factura encontrada exitosamente", 
                "Búsqueda exitosa", 
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "El ID de la factura debe ser un número válido", 
                "Error de formato", 
                JOptionPane.ERROR_MESSAGE);
            txtIdFactura.requestFocus();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error al consultar la base de datos:\n" + e.getMessage(), 
                "Error de base de datos", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void mostrarEncabezado(EncabezadoFactura encabezado) {
        txtFechaEmision.setText(encabezado.getFechaEmision());
        txtIdCliente.setText(String.valueOf(encabezado.getIdCliente()));
        txtIdVendedor.setText(String.valueOf(encabezado.getIdVendedor()));
        txtTotalFactura.setText(String.format("%.2f", encabezado.getTotalFactura()));
    }
    
    private void mostrarDetalles(List<DetalleFactura> detalles) {
        // Limpiar tabla
        modeloTabla.setRowCount(0);
        
        // Agregar filas
        for (DetalleFactura detalle : detalles) {
            Object[] fila = {
                detalle.getIdDetalle(),
                detalle.getIdProducto(),
                detalle.getCantidad(),
                String.format("%.2f", detalle.getPrecioUnitario()),
                String.format("%.2f", detalle.getImporte())
            };
            modeloTabla.addRow(fila);
        }
    }
    
    private void limpiarCampos() {
        txtIdFactura.setText("");
        txtFechaEmision.setText("");
        txtIdCliente.setText("");
        txtIdVendedor.setText("");
        txtTotalFactura.setText("");
        modeloTabla.setRowCount(0);
        txtIdFactura.requestFocus();
    }
    
    private void setupWindow() {
        setTitle("Consultar Facturas - Sistema de Facturación");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 600);
        setLocationRelativeTo(null);
        setResizable(true);
        
        // Establecer foco inicial
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                txtIdFactura.requestFocus();
            }
        });
    }
}
