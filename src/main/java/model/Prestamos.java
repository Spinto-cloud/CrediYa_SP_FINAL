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
    
    private ArrayList<Cuotas> listaCuotas = new ArrayList<>();
    
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
    
        generarCuotas();
    }
    
    private double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }

    private void generarCuotas() {

        double tasa = tasaInteres / 100.0;

        double valorCuota;

        if (tasa == 0) {
            valorCuota = monto / cuotas;
        } else {
            valorCuota = monto * tasa
                    / (1 - Math.pow(1 + tasa, -cuotas));
        }

        valorCuota = redondear(valorCuota);

        double saldoCapital = monto;

        for (int i = 1; i <= cuotas; i++) {

            double interes = redondear(saldoCapital * tasa);

            double capital = valorCuota - interes;

            double valor;

            if (i == cuotas) {

                capital = saldoCapital;
                valor = redondear(capital + interes);

            } else {

                capital = redondear(capital);
                valor = redondear(interes + capital);
            }

            Cuotas nuevaCuota = new Cuotas(
                    i,
                    valor,
                    interes,
                    capital,
                    fechaInicio.plusMonths(i)
            );

            listaCuotas.add(nuevaCuota);

            saldoCapital = redondear(saldoCapital - capital);
        }
    }
    
    public double calcularTotal() {

        double total = 0;

        for (Cuotas cuota : listaCuotas) {
            total += cuota.getValor();
        }

        return redondear(total);
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
    
    public Long calcularDiasMora() {
        
        LocalDate vencimiento = calcularFechaVencimiento();
        
        if(LocalDate.now().isAfter(vencimiento)) {
            return ChronoUnit.DAYS.between(
                    vencimiento, LocalDate.now()
            );
        }
        
        return 0L;
    }
    
     public List<Cuotas> getListaCuotas() {
        return listaCuotas;
    }
     
    public void mostrarInformacion() {

        System.out.println("ID Prestamo: " + idPrestamo);
        System.out.println("Cliente: " + cliente.getNombre());
        System.out.println("Empleado: " + empleado.getNombre());
        System.out.println("Monto: $" + monto);
        System.out.println("Interes mensual: " + tasaInteres + "%");
        System.out.println("Cuotas: " + cuotas);
        System.out.println("Total a pagar: $" + calcularTotal());
        System.out.println("Cuota mensual aproximada: $"
                + calcularCuotaMensual());
        System.out.println("Fecha de inicio: " + fechaInicio);
        System.out.println("Fecha de vencimiento: "
                + calcularFechaVencimiento());
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
