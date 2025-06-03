package Modelo;
public class DetalleFactura {
    private int idDetalle;
    private int idFactura;
    private int idProducto;
    private int cantidad;
    private float precioUnitario;
    private float importe;

    public int getIdDetalle() { return idDetalle; }
    public void setIdDetalle(int idDetalle) { this.idDetalle = idDetalle; }

    public int getIdFactura() { return idFactura; }
    public void setIdFactura(int idFactura) { this.idFactura = idFactura; }

    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public float getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(float precioUnitario) { this.precioUnitario = precioUnitario; }

    public float getImporte() { return importe; }
    public void setImporte(float importe) { this.importe = importe; }
}