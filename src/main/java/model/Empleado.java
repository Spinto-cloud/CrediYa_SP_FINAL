package model;

public class Empleado extends Persona {
    
    private String rol;
    private double salario;
    
    public Empleado(int id, String nombre, String documento, 
                    String correo, String rol, double salario) {
        
        super(id, nombre, documento, correo);
        
        this.rol = rol;
        this.salario = salario;
    }

    public String getRol() {
        return rol;
    }

    public double getSalario() {
        return salario;
    }

    @Override
    public void mostrarInformacion() {
    
        System.out.println("ID: " + getId());
        System.out.println("Nombre: " + getNombre());
        System.out.println("Documento: " + getDocumento());
        System.out.println("Correo: " + getCorreo());
        System.out.println("Rol: " + rol);
        System.out.println("Salario: " + salario);
    }
}
