package com.cajero;

import Clases.Cliente;
import Clases.CuentaBancaria;
import static Clases.CuentaBancaria.EstadoCuenta.ACTIVA;
import Clases.Usuario;
import Model.GestioAdmin;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.converter.IntegerStringConverter;

public class AdminController2 {

     @FXML
    private ImageView logo;

    @FXML
    private Button exit;

    @FXML
    private Button confirmarButton;

    @FXML
    private Label nombre;

    @FXML
    private Label apellido;

    @FXML
    private Label teléfono;

    @FXML
    private Label correo;

    @FXML
    private TextField name;

    @FXML
    private TextField last_name;

    @FXML
    private TextField phone;

    @FXML
    private TextField email;
    
    @FXML
    private TextField password;
    
    @FXML
    private Label pin;

    @FXML
    private Label id_cliente;

    @FXML
    private Label tipo;

    @FXML
    private Label saldo_client;

     @FXML
    private ComboBox Id;

    @FXML
    private ComboBox<String> type;

    @FXML
    private TextField saldo;

    
    private int opcion;
    private Usuario user;
    private GestioAdmin gestioAdmin;

    
     public void initData(Usuario user ) {
        this.user = user;
        this.gestioAdmin = new GestioAdmin();
         cargarClientes();
    }
     
    @FXML
  private void initialize() {
      // Cargar la imagen desde un archivo
      Image image = new Image(getClass().getResourceAsStream("BBVA_2019.png"));
      // Establecer el logo
      logo.setImage(image);

      // Configurar el TextFormatter para permitir solo 4 dígitos en el TextField password
      TextFormatter<Integer> formatter1 = new TextFormatter<>(new IntegerStringConverter(), null, change -> {
          String newText = change.getControlNewText();
          if (newText.matches("\\d{0,15}")) { // Aceptar máximo 4 dígitos
              return change;
          }
          return null; // No permitir la edición
      });

      // Configurar el TextFormatter para permitir solo 4 dígitos en el TextField password
      TextFormatter<Integer> formatter = new TextFormatter<>(new IntegerStringConverter(), null, change -> {
          String newText = change.getControlNewText();
          if (newText.matches("\\d{0,4}")) { // Aceptar máximo 4 dígitos
              return change;
          }
          return null; // No permitir la edición
      });
      phone.setTextFormatter(formatter1);
      password.setTextFormatter(formatter);

      // Configurar opciones del ComboBox type
      type.getItems().addAll("Corrientes", "Ahorros");

  }


    
     // Método para cargar las cuentas del usuario en el ComboBox
        private void cargarClientes() {
            List<Integer> clientes = gestioAdmin.obtenerTodosLosClienteIds(); // Obtenemos las cuentas del usuario
            Id.getItems().addAll(clientes); // Agregamos las cuentas al ComboBox
        }

  
    
    @FXML
    private void exit() throws IOException {
        switchToAdmin(user);
    }

    
@FXML
private void confirmarOperacion() {
    switch (opcion) {
        case 1:
            // Verificar que todos los campos estén completos
            if (name.getText().isEmpty() || last_name.getText().isEmpty() || phone.getText().isEmpty() || email.getText().isEmpty() || password.getText().isEmpty()) {
                System.out.println("Por favor complete todos los campos.");
                return; // Salir del método si algún campo está vacío
            }

            // Verificar si el campo de teléfono contiene solo números
            String telefono = phone.getText();
            if (!telefono.matches("[0-9]+")) {
                System.out.println("El campo de teléfono solo debe contener números.");
                return; // Salir del método si el teléfono no es válido
            }

            // Obtener el nombre completo del cliente
            String nombreCompleto = name.getText() + " " + last_name.getText();

            // Obtener el correo del cliente
            String correoCliente = email.getText();

            // Obtener el PIN del cliente
            String pinCliente = password.getText();

            // Crear un objeto Cliente con los datos obtenidos
            Cliente nuevoCliente = new Cliente(nombreCompleto, telefono, correoCliente, pinCliente);

            // Llamar al método agregarCliente y verificar si se ha agregado correctamente
            if (gestioAdmin.agregarCliente(nuevoCliente)) {
                System.out.println("Cliente agregado correctamente.");
                
                // Generar el nombre de usuario
                String username = gestioAdmin.generarUsername(nuevoCliente);
                
                // Crear el objeto Usuario con el nombre de usuario y el PIN del cliente
                Usuario nuevoUsuario = new Usuario(username, pinCliente, nuevoCliente.getIdCliente());
                
                // Llamar al método agregarUsuario y verificar si se ha agregado correctamente
                if (gestioAdmin.agregarUsuario(nuevoUsuario)) {
                    System.out.println("Usuario agregado correctamente.");
                } else {
                    System.out.println("Error al agregar el usuario.");
                }
            } else {
                System.out.println("Error al agregar el cliente.");
            }
            break;
       case 2:
        // Verificar que se haya seleccionado un cliente y que todos los campos estén completos
        if (Id.getValue() == null || type.getValue() == null || saldo.getText().isEmpty()) {
            System.out.println("Por favor complete todos los campos.");
            return; // Salir del método si algún campo está vacío
        }

        // Obtener los valores seleccionados de los ComboBox
        int idCliente = (int) Id.getValue();
        String tipoCuenta = type.getValue();
        double saldoCuenta = Double.parseDouble(saldo.getText());
        String fechaApertura = obtenerFechaActual(); // Suponiendo que tienes un método para obtener la fecha actual

        // Obtener el nombre del titular utilizando el método obtenerNombreClientePorId
        String titular = gestioAdmin.obtenerNombreClientePorId(idCliente);

        // Crear un objeto CuentaBancaria con los datos obtenidos
        CuentaBancaria nuevaCuenta = new CuentaBancaria(titular,idCliente, tipoCuenta, saldoCuenta, fechaApertura, ACTIVA);

        // Llamar al método agregarCuentaBancaria y verificar si se ha agregado correctamente
        if (gestioAdmin.agregarCuentaBancaria(nuevaCuenta)) {
            System.out.println("Cuenta bancaria agregada correctamente.");
        } else {
            System.out.println("Error al agregar la cuenta bancaria.");
        }
        break;


        default:
            // Agregar cualquier otra lógica para opciones no reconocidas
            break;
    }
}


public String obtenerFechaActual() {
    // Obtener la fecha actual
    LocalDate fechaActual = LocalDate.now();
    
    // Formatear la fecha en el formato deseado (por ejemplo, "yyyy-MM-dd")
    String fechaFormateada = fechaActual.toString(); // Formato por defecto "yyyy-MM-dd"
    
    return fechaFormateada;
}

 
     private void switchToAdmin(Usuario user) throws IOException {
         FXMLLoader loader = new FXMLLoader(getClass().getResource("admin.fxml"));
           Parent adminRoot = loader.load();
           
           // Pasar el usuario y el pin al controlador secundario
           AdminController adminController = loader.getController();
           adminController.initData(user);

           App.setRoot(adminRoot);
       }
     
    public void setOpcion(int opciones) {
        this.opcion = opciones;
         if (opcion == 1) {
            nombre.setVisible(true);
            apellido.setVisible(true);
            teléfono.setVisible(true);
            correo.setVisible(true);  
            name.setVisible(true);
            last_name.setVisible(true);
            phone.setVisible(true);
            email.setVisible(true);
            password.setVisible(true);
            pin.setVisible(true); 
            exit.setVisible(true);
            confirmarButton.setVisible(true);
           
            id_cliente.setVisible(false); 
            tipo.setVisible(false);
            saldo_client.setVisible(false);
          

            Id.setVisible(false);
            type.setVisible(false);
            saldo.setVisible(false);
          
        }   if (opcion == 2) {
            nombre.setVisible(false);
            apellido.setVisible(false);
            teléfono.setVisible(false);
            correo.setVisible(false);
            
            name.setVisible(false);
            last_name.setVisible(false);
            phone.setVisible(false);
            email.setVisible(false);
            password.setVisible(false);
            pin.setVisible(false); 
            exit.setVisible(true);
            confirmarButton.setVisible(true);
            
            id_cliente.setVisible(true); 
            tipo.setVisible(true);
            saldo_client.setVisible(true);

            Id.setVisible(true);
            type.setVisible(true);
            saldo.setVisible(true);  
          }  
    }

   
}
