package model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Prestamos {
    
    private int idPrestamo;
    private Cliente cliente;
    private Empleado empleado;
    private double monto;
    private double tasaInteres;
    private int cuotas;
    private LocalDate fechaInicio;
    private String estado;
    
    private ArrayList<Cuotas> listaCuotas;
    
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
        
        this.listaCuotas = new ArrayList<>();
    
        generarCuotas();
    }
    
    private double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }

    private void generarCuotas() {

        double tasaMensual = tasaInteres / 100.0;
        double cuotaMensual;

        if (tasaMensual == 0) {
            cuotaMensual = monto / cuotas;
        } else {
            cuotaMensual = monto * tasaMensual
                    / (1 - Math.pow(1 + tasaMensual, -cuotas));
        }

        double saldo = monto;

        for (int i = 1; i <= cuotas; i++) {

            double interes = saldo * tasaMensual;
            double capital = cuotaMensual - interes;

            if (i == cuotas) {
                capital = saldo;
            }

            double valorCuota = capital + interes;

            LocalDate fechaVencimiento = fechaInicio.plusMonths(i);

            Cuotas nuevaCuota = new Cuotas(
                    i,
                    valorCuota,
                    interes,
                    capital,
                    fechaVencimiento
            );

            listaCuotas.add(nuevaCuota);

            saldo -= capital;
            saldo = Math.max(0, saldo);
        }
    }
    
    public double calcularTotal() {

        double total = 0;

        for (Cuotas cuota : listaCuotas) {
            total += cuota.getValor();
        }

        return total;
    }

    
    public double calcularCuotaMensual() {

        if (listaCuotas.isEmpty()) {
            return 0;
        }

        return listaCuotas.get(0).getValor();
    }

    public LocalDate calcularFechaVencimiento() {
        return fechaInicio.plusMonths(cuotas);
    }
    
    public long calcularDiasMora() {

        LocalDate vencimiento = calcularFechaVencimiento();

        if (LocalDate.now().isAfter(vencimiento)) {
            return ChronoUnit.DAYS.between(vencimiento, LocalDate.now());
        }

        return 0;
    }
    
    public double calcularSaldoCapital(int numeroCuota) {

        double saldo = monto;

        for (Cuotas cuota : listaCuotas) {

            if (cuota.getNumero() > numeroCuota) {
                break;
            }

            saldo -= cuota.getCapital();
        }

        saldo = Math.max(0, saldo);

        return Math.round(saldo * 100.0) / 100.0;
    }
    
    public void cargarCuotas(List<Cuotas> cuotasGuardadas) {
        this.listaCuotas.clear();
        this.listaCuotas.addAll(cuotasGuardadas);
    }
    
    //Recuperar el estado original del prestamo
    public void cargarEstado(String estado) {
        this.estado = estado;
    }
    
     public int getIdPrestamo() {
        return idPrestamo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public double getMonto() {
        return monto;
    }

    public double getTasaInteres() {
        return tasaInteres;
    }

    public int getNumeroCuotas() {
        return cuotas;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public String getEstado() {
        return estado;
    }

    public double getTotalPagar() {
        return calcularTotal();
    }

    public List<Cuotas> getListaCuotas() {
        return listaCuotas;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public void mostrarInformacion() {

    System.out.println("ID del préstamo: " + idPrestamo);
    System.out.println("Cliente: " + cliente.getNombre());
    System.out.println("Documento del cliente: " + cliente.getDocumento());
    System.out.println("Empleado responsable: " + empleado.getNombre());
    System.out.println("Monto solicitado: $" + monto);
    System.out.println("Tasa de interés: " + tasaInteres + "%");
    System.out.println("Número de cuotas: " + cuotas);
    System.out.println("Valor de cuota mensual: $" + calcularCuotaMensual());
    System.out.println("Total a pagar: $" + calcularTotal());
    System.out.println("Fecha de inicio: " + fechaInicio);
    System.out.println("Fecha de vencimiento: " + calcularFechaVencimiento());
    System.out.println("Estado: " + estado);
    }
}
