package com.cajero;

import Model.GestioDades;
import Clases.Transaccion;
import static Clases.Transaccion.TipoTransaccion.*;
import Clases.Usuario;
import java.io.IOException;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class TertiaryController {
    @FXML
    private ImageView logo; 
    @FXML
    private TextField importeTextField;

    @FXML
    private TextField cuentaTextField;

    @FXML
    private Button confirmarButton;

    @FXML
    private Button cancelarButton;

    @FXML
    private ComboBox cuentasComboBox;
      
    @FXML
    private Label importe;
    
    @FXML
    private Label nombre;
    
     @FXML
    private Label cuenta;
    
    private int opcion;
    
    private Usuario user;
 
    private GestioDades gestioDades;

    public void initData(Usuario user ) {
        this.user = user;
        this.gestioDades = new GestioDades();
        cargarCuentasUsuario();
    }
    
    @FXML
    private void initialize() {
       // Cargar la imagen desde un archivo
        Image image = new Image(getClass().getResourceAsStream("BBVA_2019.png"));
        // Establecer el logo
        logo.setImage(image);
        
        // Configuración para permitir solo números en importeTextField
        importeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                importeTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        // Configuración para permitir solo números en cuentaTextField
        cuentaTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                cuentaTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }
     // Método para cargar las cuentas del usuario en el ComboBox
    private void cargarCuentasUsuario() {
        List<String> cuentas = gestioDades.obtenerCuentasUsuario(user.getId_cliente()); // Obtenemos las cuentas del usuario
        cuentasComboBox.getItems().addAll(cuentas); // Agregamos las cuentas al ComboBox
    }
    

@FXML
private void confirmarOperacion() throws IOException {
    String cuentaSeleccionada = (String) cuentasComboBox.getValue();
    String importe = importeTextField.getText();

    // Verificar si se ha seleccionado una cuenta y se ha ingresado un importe válido
    if (cuentaSeleccionada != null && !cuentaSeleccionada.isEmpty() && !importe.isEmpty()) {
        // Convertir el importe a un valor decimal
        double importeDecimal = Double.parseDouble(importe);

        // Obtener el saldo anterior de la cuenta seleccionada
        double saldoAnterior = gestioDades.obtenerSaldoCuenta(cuentaSeleccionada);

        String titular = gestioDades.obtenerTitularCuenta(cuentaSeleccionada);
       int cuenta = Integer.parseInt(cuentaSeleccionada);
        // Determinar la operación a realizar según el valor de la variable opcion
        switch (opcion) {
            case 1:
           Transaccion ingresar = new Transaccion(user.getId_cliente(), titular, cuenta, DEPOSITO, importeDecimal, saldoAnterior, saldoAnterior + importeDecimal);
                
                // Sumar el importe al saldo de la cuenta seleccionada
                gestioDades.actualizarSaldoCuenta(cuentaSeleccionada, importeDecimal, true);
                // Registrar la transacción de depósito
                gestioDades.registrarTransaccion(ingresar);
                mostrarAlerta("Éxito", "Se ha depositado el importe correctamente.");
                break;
            case 2:
            Transaccion retirar = new Transaccion(user.getId_cliente(),titular, cuenta, RETIRO, importeDecimal, saldoAnterior, saldoAnterior + importeDecimal);
                // Restar el importe al saldo de la cuenta seleccionada
                gestioDades.actualizarSaldoCuenta(cuentaSeleccionada, importeDecimal, false);
                // Registrar la transacción de retiro
                gestioDades.registrarTransaccion(retirar);
                mostrarAlerta("Éxito", "Se ha retirado el importe correctamente.");
                break;
            case 3:
               String cuentaDestino = cuentaTextField.getText();
                // Verificar si se ha ingresado una cuenta destino
                if (!cuentaDestino.isEmpty()) {
                    if (gestioDades.verificarExistenciaCuenta(cuentaDestino)) {
                        int destino = Integer.parseInt(cuentaDestino);
                        // La cuenta destino existe, proceder con la transferencia
                        Transaccion transferencia = new Transaccion(user.getId_cliente(),titular, cuenta, TRANSFERENCIA, destino, importeDecimal, saldoAnterior, saldoAnterior + importeDecimal);
                        // Restar el importe al saldo de la cuenta seleccionada
                        gestioDades.actualizarSaldoCuenta(cuentaSeleccionada, importeDecimal, false);
                        // Sumar el importe al saldo de la cuenta destino
                        gestioDades.actualizarSaldoCuenta(cuentaDestino, importeDecimal, true);
                        // Registrar la transacción de transferencia
                        gestioDades.registrarTransaccion(transferencia);
                        mostrarAlerta("Éxito", "Se ha transferido el importe correctamente.");
                    } else {
                        // La cuenta destino no existe, mostrar mensaje de error
                        mostrarAlerta1("Error", "La cuenta destino especificada no existe o no esta activa.");
                    }
                } else {
                    mostrarAlerta1("Error", "Por favor, especifique la cuenta destino.");
                }
                break;
            default:
                mostrarAlerta1("Error", "Operación no válida.");
                break;
        }
    } else {
        mostrarAlerta1("Error", "Por favor, seleccione una cuenta y especifique un importe.");
    }
    
}


private void mostrarAlerta(String titulo, String contenido) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(titulo);
    alert.setContentText(contenido);
    alert.showAndWait();
}
private void mostrarAlerta1(String titulo, String contenido) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle(titulo);
    alert.setContentText(contenido);
    alert.showAndWait();
}


    @FXML
    private void cancelarOperacion() throws IOException {
        // Implementa la lógica para cancelar la operación
          switchToSecondary(user);
    }

    private void switchToSecondary(Usuario user) throws IOException {
           FXMLLoader loader = new FXMLLoader(getClass().getResource("secondary.fxml"));
           Parent secondaryRoot = loader.load();
           
           // Pasar el usuario y el pin al controlador secundario
           SecondaryController secondaryController = loader.getController();
           secondaryController.initData(user);

           App.setRoot(secondaryRoot);
       }
    
     // Método para establecer el saldo
    public void setOpcion(int opciones) {
        this.opcion = opciones;
         if (opcion == 1) {
            importeTextField.setVisible(true);
            cuentaTextField.setVisible(false);
            confirmarButton.setVisible(true);
            importe.setVisible(true);
            nombre.setVisible(false);
            cancelarButton.setVisible(true);
          
        }   if (opcion == 2) {
            importeTextField.setVisible(true);
            cuentaTextField.setVisible(false);
            confirmarButton.setVisible(true);
            importe.setVisible(true);
            nombre.setVisible(false);
            cancelarButton.setVisible(true);
          
        }  if (opcion == 3) {
            importeTextField.setVisible(true);
            cuentaTextField.setVisible(true);
            confirmarButton.setVisible(true);
            importe.setVisible(true);
            nombre.setVisible(true);
            cancelarButton.setVisible(true);
        }
    }
}



