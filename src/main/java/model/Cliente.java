package model;

public class Cliente extends Persona{
    
    private String telefono;
    
    public Cliente(int id, String nombre, String documento,
            String correo, String telefono) {
        
        super(id, nombre, documento,correo);
        
        this.telefono = telefono;
    }

    public String getTelefono() {
        return telefono;
    }
    
    @Override
    public void mostrarInformacion() {
    
        System.out.println("ID: " + getId());
        System.out.println("Nombre: " + getNombre());
        System.out.println("Documento: " + getDocumento());
        System.out.println("Correo: " + getCorreo());
        System.out.println("Telefono: " + telefono);
    }
}
