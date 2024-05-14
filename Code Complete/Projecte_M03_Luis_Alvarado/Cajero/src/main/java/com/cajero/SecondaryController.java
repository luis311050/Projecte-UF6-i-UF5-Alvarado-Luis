package com.cajero;

import Model.GestioDades;
import Clases.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;

import java.io.IOException;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class SecondaryController {
    @FXML
    private ImageView logo; 
    @FXML
    private Button add;

    @FXML
    private Button consultar;

    @FXML
    private Button change;

    @FXML
    private Button transferir;

    @FXML
    private Button retirar;

    @FXML
    private Button exit;
   
    private int opciones = 0; 

    private Usuario user;
 
    private GestioDades gestioDades;

    public void initData(Usuario user) {
        this.user = user;
        this.gestioDades = new GestioDades();
    }
    
    @FXML
    private void initialize() {
         // Cargar la imagen desde un archivo
        Image image = new Image(getClass().getResourceAsStream("BBVA_2019.png"));
        // Establecer el logo
        logo.setImage(image);
        initData(user);
    }
    
    
    @FXML
    private void ingresarSaldo() throws IOException {
        opciones = 1;
        switchToTertiary(user,opciones);
    }

    @FXML
    private void retirarSaldo() throws IOException {
        opciones = 2;
        switchToTertiary(user,opciones);
    }

    @FXML
    private void transferirSaldo() throws IOException {
        opciones = 3;
        switchToTertiary(user,opciones);
    }
    
   private void switchToTertiary(Usuario user, int opciones) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("tertiary.fxml"));
        Parent tertiaryRoot = loader.load();
        
        // Pasar el usuario y el pin al controlador secundario
        TertiaryController tertiaryController = loader.getController();
        tertiaryController.setOpcion(opciones);
        tertiaryController.initData(user);
        App.setRoot(tertiaryRoot);

    }
    
   @FXML
    private void consultarSaldo() {
        ObservableList<String> cuentas = FXCollections.observableArrayList();

        // Obtener la lista de cuentas del usuario
        cuentas.addAll(gestioDades.obtenerCuentasUsuario(user.getId_cliente()));

        // Crear un ComboBox para mostrar las cuentas
        ComboBox<String> comboBoxCuentas = new ComboBox<>(cuentas);

        // Crear un GridPane para organizar los elementos
        GridPane gridPane = new GridPane();
        gridPane.add(new Label("Seleccione una cuenta:"), 0, 0);
        gridPane.add(comboBoxCuentas, 1, 0);

        // Agregar un ChangeListener para manejar la selección del usuario
        comboBoxCuentas.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Obtener y mostrar el saldo de la cuenta seleccionada
                double saldo = gestioDades.obtenerSaldoCuenta(newValue);
                mostrarSaldo(newValue, saldo);
            }
        });
  // Crear y mostrar el AlertDialog con el ComboBox
    Alert alert = new Alert(Alert.AlertType.NONE);
    alert.setTitle("Consultar Saldo");
    alert.getDialogPane().setContent(gridPane);
    alert.getButtonTypes().add(ButtonType.CANCEL);
    alert.showAndWait();
}

private void mostrarSaldo(String cuenta, double saldo) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Saldo de la cuenta " + cuenta);
    alert.setHeaderText(null);
    alert.setContentText("El saldo de la cuenta " + cuenta + " es: " + saldo);
    alert.showAndWait();
}

   private void configurarTextFormatter(TextField textField) {
    TextFormatter<String> formatter = new TextFormatter<>(change -> {
        String newText = change.getControlNewText();
        if (newText.matches("\\d{0,4}")) { // Aceptar máximo 4 dígitos
            return change;
        }
        return null; // No permitir la edición
    });
    textField.setTextFormatter(formatter);
}

@FXML
private void cambiarPin() {
    TextInputDialog dialog = new TextInputDialog();
    dialog.setTitle(String.valueOf(user.getId_cliente()));
    dialog.setHeaderText(null);
    dialog.setContentText("Ingrese su PIN actual:");
    configurarTextFormatter(dialog.getEditor());

    Optional<String> result = dialog.showAndWait();

    result.ifPresent(pinActualIngresado -> {
        if (!pinActualIngresado.equals(user.getPassword())) {
            mostrarAlerta("Error", "El PIN actual ingresado es incorrecto.");
        } else {
            TextInputDialog nuevoPinDialog = new TextInputDialog();
            nuevoPinDialog.setTitle("Cambiar PIN");
            nuevoPinDialog.setHeaderText(null);
            nuevoPinDialog.setContentText("Ingrese su nuevo PIN:");
            configurarTextFormatter(nuevoPinDialog.getEditor());

            Optional<String> nuevoPinResult = nuevoPinDialog.showAndWait();

            nuevoPinResult.ifPresent(nuevoPin -> {
                TextInputDialog confirmarPinDialog = new TextInputDialog();
                confirmarPinDialog.setTitle("Cambiar PIN");
                confirmarPinDialog.setHeaderText(null);
                confirmarPinDialog.setContentText("Confirme su nuevo PIN:");
                configurarTextFormatter(confirmarPinDialog.getEditor());

                Optional<String> confirmarPinResult = confirmarPinDialog.showAndWait();

                confirmarPinResult.ifPresent(confirmarPin -> {
                    if (nuevoPin.equals(confirmarPin)) {
                        // Actualizar el PIN del usuario en la base de datos
                        if (gestioDades.actualizarPinUsuario(user.getUsername(), nuevoPin)) {
                            mostrarAlerta("Éxito", "El PIN se ha actualizado correctamente.");
                        } else {
                            mostrarAlerta("Error", "Ha ocurrido un error al intentar actualizar el PIN. Por favor, inténtelo nuevamente.");
                        }
                    } else {
                        mostrarAlerta("Error", "Los PIN no coinciden. Por favor, inténtelo nuevamente.");
                    }
                });
            });
        }
    });
}

private void mostrarAlerta(String titulo, String contenido) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(titulo);
    alert.setHeaderText(null);
    alert.setContentText(contenido);
    alert.showAndWait();
}

    
     @FXML
    private void switchToPrimary() throws IOException {
        // Implementa la lógica para cambiar a la pantalla primaria
        App.setRoot("primary");
    }
}
