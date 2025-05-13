package co.edu.uniquindio.observer.strategy;

public class PagoTarjeta implements MetodoPago {
    @Override
    public String procesarPago(double monto) {
        return "Pago de $" + monto + " realizado con TARJETA.";
    }
}