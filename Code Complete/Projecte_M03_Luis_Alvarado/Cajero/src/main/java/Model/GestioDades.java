/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Clases.Transaccion;
import Clases.Transaccion.TipoTransaccion;
import Clases.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class GestioDades {
    
    // Método para comprobar que el usuario y la contraseña estén dentro de la base de datos
    public boolean validarUsuario(Usuario user) {
        boolean valido = false;
        String sql = "SELECT * FROM usuarios WHERE Username = ? AND Password = ?";
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

    
    
   // Método para obtener el ID_Cliente asociado a un usuario
    public int obtenerId_Cliente(Usuario user) {
        int idCliente = -1; // Valor predeterminado en caso de que no se encuentre ningún usuario válido
        String sql = "SELECT ID_Cliente FROM usuarios WHERE Username = ? AND Password = ?";
        try (Connection connection = new Connexio().connecta();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                // Si se encuentra un usuario válido, se asigna el ID_Cliente correspondiente
                idCliente = resultSet.getInt("ID_Cliente");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idCliente;
    }

    
    //Metodo para actualizar el pin
    public boolean actualizarPinUsuario(String usuario, String nuevoPin) {
        boolean exito = false;
        String sql = "UPDATE usuarios SET Password = ? WHERE Username = ?";

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


    // Método para obtener una lista de todas las cuentas activas del cliente
    public List<String> obtenerCuentasUsuario(int id_cliente) {
        List<String> cuentas = new ArrayList<>();
        String sql = "SELECT NumeroCuenta FROM cuentas_bancarias WHERE ID_Cliente = ? AND Estado = 'ACTIVA'";

        try (Connection connection = new Connexio().connecta();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id_cliente);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                cuentas.add(resultSet.getString("NumeroCuenta"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cuentas;
    }

    
    
     // Método para obtener el saldo de una cuenta bancaria
    public double obtenerSaldoCuenta(String numeroCuenta) {
        double saldo = 0.0;
        String sql = "SELECT Saldo FROM cuentas_bancarias WHERE NumeroCuenta = ?";
        
        try (Connection connection = new Connexio().connecta();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, numeroCuenta);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                saldo = resultSet.getDouble("Saldo");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return saldo;
    }     
    
    
    // Método para obtener el tirular de una cuenta bancaria
    public String obtenerTitularCuenta(String numeroCuenta) {
        String titular = " ";
        String sql = "SELECT Titular FROM cuentas_bancarias WHERE NumeroCuenta = ?";
        
        try (Connection connection = new Connexio().connecta();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, numeroCuenta);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                titular = resultSet.getString("Titular");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return titular;
    }
    
    
  // Método para actualizar el saldo de una cuenta bancaria
    public boolean actualizarSaldoCuenta(String numeroCuenta, double importe, boolean sumar) {
        boolean exito = false;
        String sql;

        // Determinar la operación a realizar según el parámetro sumar
        if (sumar) {
            // Sumar el importe al saldo actual
            sql = "UPDATE cuentas_bancarias SET Saldo = Saldo + ? WHERE NumeroCuenta = ?";
        } else {
            // Restar el importe al saldo actual
            sql = "UPDATE cuentas_bancarias SET Saldo = Saldo - ? WHERE NumeroCuenta = ?";
        }

        try (Connection connection = new Connexio().connecta();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, importe);
            statement.setString(2, numeroCuenta);

            int filasAfectadas = statement.executeUpdate();
            exito = filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return exito;
    }

     // Método para registrar una transacción en la base de datos
    public boolean registrarTransaccion(Transaccion transaccion) {

    String sql = "INSERT INTO transacciones (ID_Cliente, Titular, NumeroCuenta, TipoTransaccion, cuentadestino, cantidad, SaldoAnterior, SaldoActual) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    
    try (Connection connection = new Connexio().connecta();
         PreparedStatement statement = connection.prepareStatement(sql)) {
        
        statement.setInt(1, transaccion.getId_cliente());
        statement.setString(2, transaccion.getTitular());
        statement.setInt(3, transaccion.getNumeroCuenta());
        statement.setString(4, transaccion.getTipo().name());
        
        // Verificar si es una transferencia y establecer el valor de cuentadestino
        if (transaccion.getTipo() == TipoTransaccion.TRANSFERENCIA) {
            statement.setInt(5, transaccion.getCuentadestino());
        } else {
            statement.setNull(5, java.sql.Types.INTEGER); // Si no es transferencia, establecer como NULL
        }
        
        statement.setDouble(6, transaccion.getCantidad());
        statement.setDouble(7, transaccion.getSaldoAnterior());
        statement.setDouble(8, transaccion.getSaldoActual());
        
        int rowsInserted = statement.executeUpdate();
        return rowsInserted > 0;
        
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

  // Método para verificar la existencia
    public boolean verificarExistenciaCuenta(String numeroCuenta) {
    try {
        Connection connection = new Connexio().connecta(); // Obtener conexión a la base de datos
        String sql = "SELECT COUNT(*) FROM cuentas_bancarias WHERE NumeroCuenta = ? AND Estado = 'ACTIVA'";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, numeroCuenta);
        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
            int count = rs.getInt(1);
            return count > 0;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

    
}
