package Modelo;
public class EncabezadoFactura {
    private int idFactura;
    private String fechaEmision;
    private int idCliente;
    private int idVendedor;
    private float totalFactura;

    public int getIdFactura() { return idFactura; }
    public void setIdFactura(int idFactura) { this.idFactura = idFactura; }

    public String getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(String fechaEmision) { this.fechaEmision = fechaEmision; }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public int getIdVendedor() { return idVendedor; }
    public void setIdVendedor(int idVendedor) { this.idVendedor = idVendedor; }

    public float getTotalFactura() { return totalFactura; }
    public void setTotalFactura(float totalFactura) { this.totalFactura = totalFactura; }
}