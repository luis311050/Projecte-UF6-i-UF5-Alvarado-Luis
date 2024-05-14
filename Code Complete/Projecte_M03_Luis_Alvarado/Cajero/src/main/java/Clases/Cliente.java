package Clases;

public class Cliente {
    private int idCliente;
    private String nombre;
    private String telefono;
    private String correo;
    private String pin;

    public Cliente(String nombre, String telefono, String correo, String pin) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
        this.pin = pin;
    }

    // Getters y setters
    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
