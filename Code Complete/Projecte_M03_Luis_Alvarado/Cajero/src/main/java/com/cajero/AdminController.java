/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cajero;

import Clases.Usuario;
import Model.GestioAdmin;
import Model.GestioDades;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;

public class AdminController {

    @FXML
    private Button add_client;

    @FXML
    private Button add_accounts;

    @FXML
    private Button change;

    @FXML
    private Button delete_accounts;

    @FXML
    private Button delete_client;

    @FXML
    private Button exit;

    @FXML
    private ImageView logo;
    
    private Usuario user;
    
    private GestioAdmin gestioAdmin;
    
    private int opciones = 0; 
    @FXML
    private ComboBox<Integer> clienteComboBox;
    
      public void initData(Usuario user) {
        this.user = user;
        this.gestioAdmin = new GestioAdmin();
    }
   
      @FXML
    private void initialize() {
      
    }

@FXML
    private void crear_cliente() throws IOException {  
        opciones = 1;
        switchToAdmin_1(user,opciones);
    }

    @FXML
    private void crear_cuenta() throws IOException {
        opciones = 2;
        switchToAdmin_1(user,opciones);
    }      
      
      
@FXML
private void eliminarCliente() {
    // Obtener todos los IDs de cliente
    List<Integer> clienteIds = gestioAdmin.obtenerTodosLosClienteIds();
    
    // Crear un ComboBox para mostrar los IDs de cliente
    ComboBox<Integer> comboBoxClientes = new ComboBox<>(FXCollections.observableArrayList(clienteIds));

    // Crear un GridPane para organizar los elementos
    GridPane gridPane = new GridPane();
    gridPane.add(new Label("Seleccione un cliente:"), 0, 0);
    gridPane.add(comboBoxClientes, 1, 0);

    // Crear y mostrar el AlertDialog con el ComboBox
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Eliminar Cliente");
    alert.getDialogPane().setContent(gridPane);
    alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

    Optional<ButtonType> result = alert.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK) {
        // Obtener el ID del cliente seleccionado
        Integer clientId = comboBoxClientes.getValue();
        
        if (clientId != null) {
            if (gestioAdmin.eliminarUsuarioPorCliente(clientId)) {
                if(gestioAdmin.eliminarCliente(clientId)){
                mostrarAlerta("Éxito", "El cliente se ha eliminado correctamente.");
                }
            } else {
                mostrarAlerta("Error", "Ha ocurrido un error al intentar eliminar el cliente. Por favor, inténtelo nuevamente.");
            }
        } else {
            mostrarAlerta("Error", "Por favor, selecciona un cliente para eliminar.");
        }
    }
}

  @FXML
private void eliminarCuentaBancaria() {
    // Obtener todos los números de cuenta
    List<Integer> cuentaNumeros = gestioAdmin.obtenerTodosLosNumerosDeCuenta();
    
    // Crear un ComboBox para mostrar los números de cuenta
    ComboBox<Integer> comboBoxCuentas = new ComboBox<>(FXCollections.observableArrayList(cuentaNumeros));

    // Crear un GridPane para organizar los elementos
    GridPane gridPane = new GridPane();
    gridPane.add(new Label("Seleccione un número de cuenta:"), 0, 0);
    gridPane.add(comboBoxCuentas, 1, 0);

    // Crear y mostrar el AlertDialog con el ComboBox
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Eliminar Cuenta Bancaria");
    alert.getDialogPane().setContent(gridPane);
    alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

    Optional<ButtonType> result = alert.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK) {
        // Obtener el número de cuenta seleccionado
        Integer numeroCuenta = comboBoxCuentas.getValue();
        
        if (numeroCuenta != null) {
            if (gestioAdmin.eliminarCuentaBancaria(numeroCuenta)) {
                mostrarAlerta("Éxito", "La cuenta bancaria se ha eliminado correctamente.");
            } else {
                mostrarAlerta("Error", "Ha ocurrido un error al intentar eliminar la cuenta bancaria. Por favor, inténtelo nuevamente.");
            }
        } else {
            mostrarAlerta("Error", "Por favor, selecciona un número de cuenta para eliminar.");
        }
    }
}

   private void configurarTextFormatter(TextField editor) {
    // Configurar el TextFormatter para permitir solo 4 dígitos en el TextField
    editor.setTextFormatter(new TextFormatter<>(change -> {
        String newText = change.getControlNewText();
        if (newText.matches("\\d{0,4}")) { // Aceptar máximo 4 dígitos
            return change;
        }
        return null; // No permitir la edición
    }));
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
                        if (gestioAdmin.actualizarPinUsuario(user.getUsername(), nuevoPin)) {
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
        App.setRoot("primary");
    }

     private void switchToAdmin_1(Usuario user, int opciones) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("admin_1.fxml"));
        Parent admin_1_Root = loader.load();
        
        // Pasar el usuario y el pin al controlador secundario
        AdminController2 adminController2 = loader.getController();
        adminController2.setOpcion(opciones);
        adminController2.initData(user);
        App.setRoot(admin_1_Root);

    }
}
