package DAO;

import Conexion.ConexionBD;
import Modelo.DetalleFactura;
import Modelo.EncabezadoFactura;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FacturaDAO {

    public EncabezadoFactura obtenerEncabezado(int idFactura) throws SQLException {
        Connection con = ConexionBD.getConnection();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM EncabezadoFactura WHERE IdFactura = ?");
        ps.setInt(1, idFactura);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            EncabezadoFactura f = new EncabezadoFactura();
            f.setIdFactura(idFactura);
            f.setFechaEmision(rs.getString("FechaEmision"));
            f.setIdCliente(rs.getInt("IdCliente"));
            f.setIdVendedor(rs.getInt("IdVendedor"));
            f.setTotalFactura(rs.getFloat("TotalFactura"));
            return f;
        }
        return null;
    }

    public List<DetalleFactura> obtenerDetalles(int idFactura) throws SQLException {
        Connection con = ConexionBD.getConnection();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM DetalleFactura WHERE IdFactura = ?");
        ps.setInt(1, idFactura);
        ResultSet rs = ps.executeQuery();
        List<DetalleFactura> lista = new ArrayList<>();
        while (rs.next()) {
            DetalleFactura d = new DetalleFactura();
            d.setIdDetalle(rs.getInt("IdDetalle"));
            d.setIdFactura(idFactura);
            d.setIdProducto(rs.getInt("IdProducto"));
            d.setCantidad(rs.getInt("Cantidad"));
            d.setPrecioUnitario(rs.getFloat("PrecioUnitario"));
            d.setImporte(rs.getFloat("Importe"));
            lista.add(d);
        }
        return lista;
    }

    public void actualizarEncabezado(EncabezadoFactura f) throws SQLException {
        Connection con = ConexionBD.getConnection();
        PreparedStatement ps = con.prepareStatement(
                "UPDATE EncabezadoFactura SET FechaEmision=?, IdCliente=?, IdVendedor=?, TotalFactura=? WHERE IdFactura=?");
        ps.setString(1, f.getFechaEmision());
        ps.setInt(2, f.getIdCliente());
        ps.setInt(3, f.getIdVendedor());
        ps.setFloat(4, f.getTotalFactura());
        ps.setInt(5, f.getIdFactura());
        ps.executeUpdate();
    }

    public void actualizarDetalle(DetalleFactura d) throws SQLException {
        Connection con = ConexionBD.getConnection();
        PreparedStatement ps = con.prepareStatement(
                "UPDATE DetalleFactura SET IdProducto=?, Cantidad=?, PrecioUnitario=?, Importe=? WHERE IdDetalle=?");
        ps.setInt(1, d.getIdProducto());
        ps.setInt(2, d.getCantidad());
        ps.setFloat(3, d.getPrecioUnitario());
        ps.setFloat(4, d.getImporte());
        ps.setInt(5, d.getIdDetalle());
        ps.executeUpdate();
    }

public void eliminarFactura(int idFactura) throws SQLException {
    Connection con = null;
    PreparedStatement psDetalles = null;
    PreparedStatement psEncabezado = null;

    try {
        con = ConexionBD.getConnection();
        con.setAutoCommit(false); // Iniciar transacción

        // Eliminar detalles
        String sqlDetalles = "DELETE FROM DetalleFactura WHERE IdFactura = ?";
        psDetalles = con.prepareStatement(sqlDetalles);
        psDetalles.setInt(1, idFactura);
        psDetalles.executeUpdate();

        // Eliminar encabezado
        String sqlEncabezado = "DELETE FROM EncabezadoFactura WHERE IdFactura = ?";
        psEncabezado = con.prepareStatement(sqlEncabezado);
        psEncabezado.setInt(1, idFactura);
        int filas = psEncabezado.executeUpdate();

        if (filas > 0) {
            con.commit(); // Confirmar eliminación
            System.out.println("Factura eliminada exitosamente.");
        } else {
            con.rollback(); // Si no existía, deshacer
            System.out.println("No se encontró la factura.");
        }

    } catch (SQLException e) {
        if (con != null) {
            con.rollback();
        }
        throw e;
    } finally {
        if (psDetalles != null) psDetalles.close();
        if (psEncabezado != null) psEncabezado.close();
        if (con != null) con.setAutoCommit(true); // Restaurar estado original
        if (con != null) con.close();
    }
}
}