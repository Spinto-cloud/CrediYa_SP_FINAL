package model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Cuotas {
    
    private int numero;
    private double valor;
    private double interes;
    private double capital;
    private double montoPagado;
    private LocalDate fechaVencimiento;
    
    public Cuotas(int numero, double valor, double interes,
           double capital, LocalDate fechaVencimiento) {
        
        this.numero = numero;
        this.valor = valor;
        this.interes = interes;
        this.capital = capital;
        this.fechaVencimiento = fechaVencimiento;
        this.montoPagado = 0;
    }
    
    public void registrarAbono(double monto) {
        montoPagado += monto;
    }
    
    public double getSaldo() {
        return Math.max(0, valor - montoPagado);
    }
    
    public String getEstado() {
        
        if (getSaldo() == 0) {
            return "Pagada";
        }
        
        if (LocalDate.now().isAfter(fechaVencimiento)) {
            return "Vencida";
        }
        
        return "Pendiente";
    }
    
    public long calcularDiasMora() {
        
        if (getSaldo() > 0
                && LocalDate.now().isAfter(fechaVencimiento)) {
            
            return ChronoUnit.DAYS.between(fechaVencimiento, LocalDate.now()
            );
        }
        
        return 0L;
    }

    public int getNumero() {
        return numero;
    }

    public double getValor() {
        return valor;
    }

    public double getInteres() {
        return interes;
    }

    public double getCapital() {
        return capital;
    }

    public double getMontoPagado() {
        return montoPagado;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }
}
