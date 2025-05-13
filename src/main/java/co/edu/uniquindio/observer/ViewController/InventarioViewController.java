package co.edu.uniquindio.observer.ViewController;

import co.edu.uniquindio.observer.Producto;
import co.edu.uniquindio.observer.Observer;
import co.edu.uniquindio.observer.strategy.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class InventarioViewController {

    // --- Administrador ---
    @FXML
    private TextField stockCliente1Field;
    @FXML
    private ComboBox<String> comboCliente1;
    @FXML
    private TextField stockCliente2Field;
    @FXML
    private ComboBox<String> comboCliente2;

    // --- Cliente 1 ---
    @FXML
    private Label stockLabelCliente1;
    @FXML
    private Label metodoLabelCliente1;

    // --- Cliente 2 ---
    @FXML
    private Label stockLabelCliente2;
    @FXML
    private Label metodoLabelCliente2;

    // --- Tab Pagos ---
    @FXML
    private ComboBox<String> comboMetodoPago;
    @FXML
    private TextField txtMonto;
    @FXML
    private ListView<String> listaHistorialPagos;

    // --- Lógica interna ---
    private final Producto productoCliente1 = new Producto();
    private final Producto productoCliente2 = new Producto();
    private final ObservableList<String> historial = FXCollections.observableArrayList();

    private MetodoPago metodoCliente1;
    private MetodoPago metodoCliente2;

    @FXML
    public void initialize() {
        // Combos del administrador
        comboCliente1.setItems(FXCollections.observableArrayList("Nequi", "Tarjeta", "Efectivo"));
        comboCliente2.setItems(FXCollections.observableArrayList("Nequi", "Tarjeta", "Efectivo"));

        // Combo de pagos (Strategy demostración)
        comboMetodoPago.setItems(FXCollections.observableArrayList("Nequi", "Tarjeta", "Efectivo"));
        listaHistorialPagos.setItems(historial);

        // Observers para Cliente 1 y Cliente 2
        productoCliente1.agregarObserver(stock -> {
            stockLabelCliente1.setText("Stock actual: " + stock);
            metodoLabelCliente1.setText("Método de pago: " +
                    (metodoCliente1 != null ? metodoCliente1.getClass().getSimpleName() : "Ninguno"));
        });

        productoCliente2.agregarObserver(stock -> {
            stockLabelCliente2.setText("Stock actual: " + stock);
            metodoLabelCliente2.setText("Método de pago: " +
                    (metodoCliente2 != null ? metodoCliente2.getClass().getSimpleName() : "Ninguno"));
        });
    }

    @FXML
    public void actualizarClientes() {
        try {
            // Cliente 1
            int stock1 = Integer.parseInt(stockCliente1Field.getText());
            metodoCliente1 = obtenerMetodo(comboCliente1.getValue());
            productoCliente1.setStock(stock1);

            // Cliente 2
            int stock2 = Integer.parseInt(stockCliente2Field.getText());
            metodoCliente2 = obtenerMetodo(comboCliente2.getValue());
            productoCliente2.setStock(stock2);

        } catch (NumberFormatException e) {
            mostrarAlerta("Los campos de stock deben ser números válidos.");
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
            MetodoPago metodo = obtenerMetodo(metodoSeleccionado);

            if (metodo == null) {
                mostrarAlerta("Método de pago no válido.");
                return;
            }

            String resultado = metodo.procesarPago(monto);
            historial.add(resultado);

            // Limpiar campos
            txtMonto.clear();
            comboMetodoPago.getSelectionModel().clearSelection();

        } catch (NumberFormatException e) {
            mostrarAlerta("El monto debe ser un número válido.");
        }
    }

    private MetodoPago obtenerMetodo(String seleccion) {
        return switch (seleccion) {
            case "Nequi" -> new PagoNequi();
            case "Tarjeta" -> new PagoTarjeta();
            case "Efectivo" -> new PagoEfectivo();
            default -> null;
        };
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
