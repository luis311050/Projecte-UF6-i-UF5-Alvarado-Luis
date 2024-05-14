package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connexio {

    private final String URL = "jdbc:mysql://localhost:3306/bank?zeroDateTimeBehavior=CONVERT_TO_NULL"; // Cambia esto según tu configuración
    private final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private final String USER = "root"; // Cambia esto por el usuario de tu base de datos
    private final String PASSWD = "@Qwerty1"; // Cambia esto por la contraseña de tu base de datos

    public Connection connecta() {
        Connection connexio = null;
        try {
            // Cargar el driver
            Class.forName(DRIVER);
            // Establecer la conexión
            connexio = DriverManager.getConnection(URL, USER, PASSWD);
            System.out.println("¡Conexión exitosa!");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        }
        return connexio;
    }
}
