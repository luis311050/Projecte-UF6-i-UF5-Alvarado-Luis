package Clases;

public class CuentaBancaria extends EntidadBancaria {
    private String tipoCuenta;
    private double saldo;
    private String fechaApertura;
    private EstadoCuenta estado;

    // Constructor
    public CuentaBancaria(String titular, int numeroCuenta, String tipoCuenta, double saldo, String fechaApertura, EstadoCuenta estado) {
      super(titular, numeroCuenta);
      this.tipoCuenta = tipoCuenta;
      this.saldo = saldo;
      this.fechaApertura = fechaApertura;
      this.estado = estado; // Asignar el estado proporcionado al estado de la cuenta
  }


    // Enum EstadoCuenta
    public enum EstadoCuenta {
        ACTIVA,
        INACTIVA,
        BLOQUEADA,
        CERRADA
    }

    // Getters y Setters
    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public String getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(String fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public EstadoCuenta getEstado() {
        return estado;
    }

    public void setEstado(EstadoCuenta estado) {
        this.estado = estado;
    }
}
