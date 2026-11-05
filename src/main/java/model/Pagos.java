package model;

import java.time.LocalDate;

public class Pagos {
    
    private int idPago;
    private Prestamos prestamo;
    private double monto;
    private LocalDate fecha;
    
    public Pagos(int idPago, Prestamos prestamo,
            double monto, LocalDate fecha) {
        
        this.idPago = idPago;
        this.prestamo = prestamo;
        this.monto = monto;
        this.fecha = fecha;
    }

    public int getIdPago() {
        return idPago;
    }

    public Prestamos getPrestamo() {
        return prestamo;
    }

    public double getMonto() {
        return monto;
    }

    public LocalDate getFecha() {
        return fecha;
    }
    
    public void mostrarInformacion() {
        System.out.println("ID del pago: " + idPago);
        System.out.println("ID del prestamo: "
                + prestamo.getIdPrestamo());
        System.out.println("Cliente: "
                + prestamo.getCliente().getNombre());
        System.out.println("Monto pagado: $" + monto);
        System.out.println("Fecha: " + fecha);
    }
}
