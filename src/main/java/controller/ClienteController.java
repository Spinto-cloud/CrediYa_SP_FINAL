package controller;

import java.util.ArrayList;
import java.util.List;
import model.Cliente;

public class ClienteController {
    
    private ArrayList<Cliente> clientes = new ArrayList();
    
    public void registrarCliente(String nombre, String documento,
                                 String correo, String telefono) {
        
        int id = clientes.size() + 1;
        
        Cliente cliente = new Cliente(
            id, nombre, documento, correo, telefono
        );
        
        clientes.add(cliente);
        
        System.out.println("Cliente registrado correctamente");
    }
    
    public List<Cliente> listarClientes() {
        return new ArrayList<>(clientes);
    }   
}
