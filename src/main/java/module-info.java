module co.edu.uniquindio.observer {
    requires javafx.controls;
    requires javafx.fxml;

    // Abre los paquetes que usan reflexión (controladores y vista)
    opens co.edu.uniquindio.observer to javafx.fxml;
    opens co.edu.uniquindio.observer.ViewController to javafx.fxml;

    // Exporta los paquetes públicos
    exports co.edu.uniquindio.observer;
    exports co.edu.uniquindio.observer.strategy;

}
