package com.cajero;

import Model.GestioDades;
import Model.GestioAdmin;
import Clases.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;

public class PrimaryController {

    @FXML
    private ImageView logo; 
    
    @FXML
    private TextField username;

    @FXML
    private TextField password;

    @FXML
    private Button validar;

    private final GestioDades gestioDades = new GestioDades();
    
    private final GestioAdmin gestioAdmin = new GestioAdmin();

    @FXML
    private void initialize() {
        // Cargar la imagen desde un archivo
        Image image = new Image(getClass().getResourceAsStream("BBVA_2019.png"));
        // Establecer el logo
        logo.setImage(image);
        
        // Configurar el TextFormatter para permitir solo 4 dígitos en el TextField password
        TextFormatter<Integer> formatter = new TextFormatter<>(new IntegerStringConverter(), null, change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d{0,4}")) { // Aceptar máximo 4 dígitos
                return change;
            }
            return null; // No permitir la edición
        });

        password.setTextFormatter(formatter);
    }

    @FXML
    private void validation() throws IOException {
        // Crear el usuario con los datos de los TextField
        Usuario user = new Usuario(username.getText(), password.getText());
        // Validar los datos
        if (gestioDades.validarUsuario(user)) {
            int idCliente = gestioDades.obtenerId_Cliente(user);
            user.setId_cliente(idCliente);
            switchToSecondary(user);
            
        } else if(gestioAdmin.validarAdministrador(user)){
            switchToAdmin(user);
         
        }else{mostrarAlerta1("Error de validación", "Usuario o contraseña incorrectos.");
            }
        }

    private void switchToSecondary(Usuario user) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("secondary.fxml"));
        Parent secondaryRoot = loader.load();

        // Pasar el usuario al controlador secundario
        SecondaryController secondaryController = loader.getController();
        secondaryController.initData(user);

        App.setRoot(secondaryRoot);
    }
    
     private void switchToAdmin(Usuario user) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("admin.fxml"));
        Parent adminRoot = loader.load();

        // Pasar el usuario al controlador secundario
        AdminController adminController = loader.getController();
        adminController.initData(user);

        App.setRoot(adminRoot);
    }

    private void mostrarAlerta1(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}