package co.edu.uniquindio.observer.strategy;

public class PagoEfectivo implements MetodoPago {
    @Override
    public String procesarPago(double monto) {
        return "Pago de $" + monto + " realizado en EFECTIVO.";
    }
}
