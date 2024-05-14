/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

/**
 *
 * @author luis
 */
//clase de User para validar el acceso de la app
public class Usuario {
    private String username;
    private String password;
    private int id_cliente;

    // Constructor
    public Usuario(String username, String password, int id_cliente) {
        this.username = username;
        this.password = password;
        this.id_cliente = id_cliente;
    }

       public Usuario(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters y setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
     public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }
}

