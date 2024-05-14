/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;


import Clases.Cliente;
import Clases.CuentaBancaria;
import Clases.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GestioAdmin {
    
    // Método para comprobar que el usuario y la contraseña están dentro de la base de datos
    public boolean validarAdministrador(Usuario user) {
        boolean valido = false;
        String sql = "SELECT * FROM administradores WHERE Username = ? AND Contraseña = ?";
        
        try (Connection connection = new Connexio().connecta();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            ResultSet resultSet = statement.executeQuery();   
           valido = resultSet.next(); // Establecer válido como true si se encuentra una coincidencia  
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return valido;
    }
    
    //Metodo para actualizar el pin
        public boolean actualizarPinUsuario(String usuario, String nuevoPin) {
            boolean exito = false;
            String sql = "UPDATE administradores SET Contraseña = ? WHERE Username = ?";

            try (Connection connection = new Connexio().connecta();
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, nuevoPin);
                statement.setString(2, usuario);
                int filasAfectadas = statement.executeUpdate();
                exito = filasAfectadas > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return exito;
        }

    // Método para obtener todos los IDs de cliente
    public List<Integer> obtenerTodosLosClienteIds() {
        List<Integer> clienteIds = new ArrayList<>();
        String sql = "SELECT ID_Cliente FROM clientes";
        
        try (Connection connection = new Connexio().connecta();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                int clientId = resultSet.getInt("ID_Cliente");
                clienteIds.add(clientId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return clienteIds;
    }
    
    // Método para obtener todos los numeros de cuenta
    public List<Integer> obtenerTodosLosNumerosDeCuenta() {
        List<Integer> numerosDeCuenta = new ArrayList<>();
        String sql = "SELECT NumeroCuenta FROM cuentas_bancarias";

        try (Connection connection = new Connexio().connecta();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int numeroCuenta = resultSet.getInt("NumeroCuenta");
                numerosDeCuenta.add(numeroCuenta);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return numerosDeCuenta;
    }
    

    // Método para eliminar un cliente de la tabla clientes importante que no este con una cuenta vinculada
    public boolean eliminarCliente(int clientId) {
        boolean exito = false;
        String sql = "DELETE FROM clientes WHERE ID_Cliente = ?";

        try (Connection connection = new Connexio().connecta();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, clientId);
            int filasAfectadas = statement.executeUpdate();
            exito = filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exito;
    }
    
     // Método para eliminar un usuario de la tabla usuarios por su ID_Cliente
    public boolean eliminarUsuarioPorCliente(int clienteId) {
        boolean exito = false;
        String sql = "DELETE FROM usuarios WHERE ID_Cliente = ?";
        
        try (Connection connection = new Connexio().connecta();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, clienteId);
            int filasAfectadas = statement.executeUpdate();
            exito = filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exito;
    }
    
     // Método para eliminar cuenta bancaria por su numero de cuenta
    public boolean eliminarCuentaBancaria(int numeroCuenta) {
        boolean exito = false;
        String sql = "DELETE FROM cuentas_bancarias WHERE NumeroCuenta = ?";
        
        try (Connection connection = new Connexio().connecta();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, numeroCuenta);
            int filasAfectadas = statement.executeUpdate();
            exito = filasAfectadas > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return exito;
    }
    
    
   public boolean agregarCliente(Cliente cliente) {
    boolean exito = false;
    String sql = "INSERT INTO clientes (Nombre, Teléfono, Correo) VALUES (?, ?, ?)";
    
    try (Connection connection = new Connexio().connecta();
         PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        
        statement.setString(1, cliente.getNombre());
        statement.setString(2, cliente.getTelefono());
        statement.setString(3, cliente.getCorreo());
        
        int filasAfectadas = statement.executeUpdate();
        if (filasAfectadas > 0) {
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                cliente.setIdCliente(rs.getInt(1)); // Asignar el ID generado al objeto cliente
            }
            exito = true;
        }
        
    } catch (SQLException e) {
        e.printStackTrace();
    }
    
    return exito;
}
   
  // Método para agregar un nuevo usuario a la base de datos
public boolean agregarUsuario(Usuario user) {
    boolean exito = false;
    String sql = "INSERT INTO usuarios (Username, Password, ID_Cliente) VALUES (?, ?, ?)";

    try (Connection connection = new Connexio().connecta();
         PreparedStatement statement = connection.prepareStatement(sql)) {

        // Establecer los valores de los parámetros en la consulta SQL
        statement.setString(1, user.getUsername());
        statement.setString(2, user.getPassword());
        statement.setInt(3, user.getId_cliente());

        // Ejecutar la consulta SQL para agregar un nuevo usuario
        int filasAfectadas = statement.executeUpdate();
        if (filasAfectadas > 0) {
            exito = true;
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return exito;
}

// Método para generar un nombre de usuario basado en el nombre y apellido del cliente
public String generarUsername(Cliente cliente) {
    String nombreCompleto = cliente.getNombre();

    // Obtener el nombre y el apellido del cliente
    String[] partesNombre = nombreCompleto.split(" ");
    String nombre = partesNombre[0]; // Primer nombre
    String apellido = partesNombre[1]; // Apellido

    // Obtener la primera letra del nombre y convertirla a minúscula
    char primeraLetra = nombre.charAt(0);
    String primerCaracter = String.valueOf(primeraLetra).toLowerCase();

    // Concatenar la primera letra del nombre, el apellido y el ID del cliente para formar el nombre de usuario
    String username = primerCaracter + apellido + cliente.getIdCliente();

    return username;
}

// Método para agregar una nueva cuenta bancaria a la base de datos
public boolean agregarCuentaBancaria(CuentaBancaria cuenta) {
    boolean exito = false;
    String sql = "INSERT INTO cuentas_bancarias (ID_Cliente, Titular, TipoCuenta, Saldo, FechaApertura, Estado) VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection connection = new Connexio().connecta();
         PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

        // Establecer los valores de los parámetros en la consulta SQL
        statement.setInt(1, cuenta.getNumeroCuenta());
        statement.setString(2, cuenta.getTitular());
        statement.setString(3, cuenta.getTipoCuenta());
        statement.setDouble(4, cuenta.getSaldo());
        statement.setString(5, cuenta.getFechaApertura());
        statement.setString(6, cuenta.getEstado().name()); // Convertir el estado a su representación de cadena

        // Ejecutar la consulta SQL para agregar una nueva cuenta bancaria
        int filasAfectadas = statement.executeUpdate();
        if (filasAfectadas > 0) {
            exito = true;
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return exito;
}

// Método para obtener el nombre de un cliente por su ID de cliente
public String obtenerNombreClientePorId(int idCliente) {
    String nombre = null;
    String sql = "SELECT Nombre FROM clientes WHERE ID_Cliente = ?";

    try (Connection connection = new Connexio().connecta();
         PreparedStatement statement = connection.prepareStatement(sql)) {

        // Establecer el valor del parámetro en la consulta SQL
        statement.setInt(1, idCliente);

        // Ejecutar la consulta SQL para obtener el nombre del cliente por su ID de cliente
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            nombre = resultSet.getString("Nombre");
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return nombre;
}
    
}

