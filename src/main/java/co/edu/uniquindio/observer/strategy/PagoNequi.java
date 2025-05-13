package co.edu.uniquindio.observer.strategy;

public class PagoNequi implements MetodoPago {
    @Override
    public String procesarPago(double monto) {
        return "Pago de $" + monto + " realizado con NEQUI.";
    }
}