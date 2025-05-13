package co.edu.uniquindio.observer.strategy;

public class ProcesadorPago {
    private MetodoPago metodo;

    public void setMetodoPago(MetodoPago metodo) {
        this.metodo = metodo;
    }

    public String pagar(double monto) {
        if (metodo == null) {
            return "Debe seleccionar un método de pago.";
        }
        return metodo.procesarPago(monto);
    }
}
