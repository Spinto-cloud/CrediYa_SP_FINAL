package model;

import java.time.LocalDate;

public class Prestamos {
    
    private int idPrestamo;
    private Cliente cliente;
    private Empleado empleado;
    private double monto;
    private double tasaInteres;
    private int cuotas;
    private LocalDate fechaInicio;
    private String estado;
    
    public Prestamos(int idPrestamo, Cliente cliente,
                    Empleado empleado, double monto,
                    double tasaInteres, int cuotas,
                    LocalDate fechaInicio) {
        
        this.idPrestamo = idPrestamo;
        this.cliente = cliente;
        this.empleado = empleado;
        this.monto = monto;
        this.tasaInteres = tasaInteres;
        this.cuotas = cuotas;
        this.fechaInicio = fechaInicio;
        this.estado = "pendiente";
    }
    
    public double calcularTotal() {
        return monto + (monto * tasaInteres / 100);
    }
    
    public double calcularCuotaMensual() {
        return calcularTotal() / cuotas;
    }    
    
    public void mostrarInformacion () {
            System.out.println("ID Prestamo: " + idPrestamo);
            System.out.println("Cliente: " + cliente.getNombre());
            System.out.println("Empleado: " + empleado.getNombre());
            System.out.println("Monto: $" + monto);
            System.out.println("Interes: " + tasaInteres + "%");
            System.out.println("Cuotas: " + cuotas);
            System.out.println("Total a pagar: $" + calcularTotal());
            System.out.println("Cuota mensual: $" + calcularCuotaMensual());
            System.out.println("Fecha: " + fechaInicio);
            System.out.println("Estado: " + estado);
    }
    
    public int getIdPrestamo() {
        return idPrestamo;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public Cliente getCliente() {
        return cliente;
    }
    
    public double getTotalPagar() {
        return calcularTotal();
    }
}
