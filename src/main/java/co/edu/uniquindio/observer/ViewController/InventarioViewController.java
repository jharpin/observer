package co.edu.uniquindio.observer.ViewController;

import co.edu.uniquindio.observer.Producto;
import co.edu.uniquindio.observer.Observer;
import co.edu.uniquindio.observer.strategy.*;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class InventarioViewController {

    @FXML
    private TextField stockField;

    @FXML
    private Label stockLabelCliente1;

    @FXML
    private Label stockLabelCliente2;

    @FXML
    private ComboBox<String> comboMetodoPago;

    @FXML
    private TextField txtMonto;

    @FXML
    private ListView<String> listaHistorialPagos;

    private final Producto producto = new Producto();
    private final ProcesadorPago procesador = new ProcesadorPago();
    private final ObservableList<String> historial = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Setear observadores
        producto.agregarObserver(stock -> actualizarCliente(stockLabelCliente1, stock));
        producto.agregarObserver(stock -> actualizarCliente(stockLabelCliente2, stock));
        actualizarCliente(stockLabelCliente1, producto.getStock());
        actualizarCliente(stockLabelCliente2, producto.getStock());

        // Cargar métodos de pago
        comboMetodoPago.setItems(FXCollections.observableArrayList("Tarjeta", "Nequi", "Efectivo"));
        listaHistorialPagos.setItems(historial);
    }

    @FXML
    public void actualizarStock() {
        try {
            int nuevoStock = Integer.parseInt(stockField.getText());
            producto.setStock(nuevoStock);
        } catch (NumberFormatException e) {
            mostrarAlerta("Por favor ingrese un número válido.");
        }
    }

    private void actualizarCliente(Label label, int stock) {
        if (stock <= 0) {
            label.setText("Producto fuera de stock");
            label.setStyle("-fx-text-fill: red;");
        } else {
            label.setText("Stock actual: " + stock);
            label.setStyle("-fx-text-fill: black;");
        }
    }

    @FXML
    public void realizarPago() {
        String metodoSeleccionado = comboMetodoPago.getValue();
        String montoTexto = txtMonto.getText();

        if (metodoSeleccionado == null || montoTexto.isBlank()) {
            mostrarAlerta("Debe seleccionar un método de pago y escribir un monto.");
            return;
        }

        try {
            double monto = Double.parseDouble(montoTexto);

            // Seleccionar estrategia
            switch (metodoSeleccionado) {
                case "Tarjeta" -> procesador.setMetodoPago(new PagoTarjeta());
                case "Nequi" -> procesador.setMetodoPago(new PagoNequi());
                case "Efectivo" -> procesador.setMetodoPago(new PagoEfectivo());
                default -> throw new IllegalStateException("Método no reconocido");
            }

            String resultado = procesador.pagar(monto);
            historial.add(resultado);

            // Notificar a los observadores como si fuera un cambio de stock
            producto.setStock(producto.getStock()-1); // Fuerza la notificación con el mismo valor

            // Limpiar campos
            txtMonto.clear();
            comboMetodoPago.getSelectionModel().clearSelection();

        } catch (NumberFormatException e) {
            mostrarAlerta("El monto debe ser un número válido.");
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
