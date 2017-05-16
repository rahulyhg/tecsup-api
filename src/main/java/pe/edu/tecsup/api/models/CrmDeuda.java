package pe.edu.tecsup.api.models;

/**
 * Created by ebenites on 28/03/2017.
 */
public class CrmDeuda {

    private String producto;

    private String monto;

    private String fecha;

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "CrmDeuda{" +
                "producto='" + producto + '\'' +
                ", monto='" + monto + '\'' +
                ", fecha='" + fecha + '\'' +
                '}';
    }
}
