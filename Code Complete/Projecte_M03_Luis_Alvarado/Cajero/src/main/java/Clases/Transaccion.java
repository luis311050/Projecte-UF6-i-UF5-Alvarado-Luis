package Clases;


import java.util.Date;

public class Transaccion extends EntidadBancaria {
    private int id_cliente;
    private TipoTransaccion tipo;
    private int cuentadestino;
    private Date fechaHora;
    private double cantidad;
    private double saldoAnterior;
    private double saldoActual;

    // Constructor
    public Transaccion(int id_cliente, String titular, int numeroCuenta, TipoTransaccion tipo, double cantidad, double saldoAnterior, double saldoActual) {
        super(titular, numeroCuenta);
        this.id_cliente = id_cliente;
        this.tipo = tipo;
        this.fechaHora = new Date();
        this.cantidad = cantidad;
        this.saldoAnterior = saldoAnterior;
        this.saldoActual = saldoActual;
    }

    public Transaccion(int id_cliente, String titular, int numeroCuenta, TipoTransaccion tipo, int cuentadestino, double cantidad, double saldoAnterior, double saldoActual) {
        super(titular, numeroCuenta);
        this.id_cliente = id_cliente;
        this.tipo = tipo;
        this.cuentadestino = cuentadestino;
        this.fechaHora = new Date();
        this.cantidad = cantidad;
        this.saldoAnterior = saldoAnterior;
        this.saldoActual = saldoActual;
    }

    // Enum TipoTransaccion
    public enum TipoTransaccion {
        DEPOSITO,
        RETIRO,
        TRANSFERENCIA
    }

    // Getters y setters
    public int getId_cliente() {
        return id_cliente;
    }

    public TipoTransaccion getTipo() {
        return tipo;
    }

    public int getCuentadestino() {
        return cuentadestino;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public double getCantidad() {
        return cantidad;
    }

    public double getSaldoAnterior() {
        return saldoAnterior;
    }

    public double getSaldoActual() {
        return saldoActual;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public void setTipo(TipoTransaccion tipo) {
        this.tipo = tipo;
    }

    public void setCuentadestino(int cuentadestino) {
        this.cuentadestino = cuentadestino;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public void setSaldoAnterior(double saldoAnterior) {
        this.saldoAnterior = saldoAnterior;
    }

    public void setSaldoActual(double saldoActual) {
        this.saldoActual = saldoActual;
    }
}
