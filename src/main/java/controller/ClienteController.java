package controller;

import java.util.ArrayList;
import java.util.List;

import model.Cliente;

import repository.ArchivoCliente;

public class ClienteController {
    
    private ArrayList<Cliente> clientes = new ArrayList();
    
    private ArchivoCliente archivoCliente = new ArchivoCliente();
    
    public ClienteController() {
        clientes = archivoCliente.cargar();

        System.out.println("Clientes cargados: " + clientes.size());
    }
    
    public void registrarCliente(String nombre, String documento,
                                 String correo, String telefono) {
        
        int id;

        if (clientes.isEmpty()) {
            id = 1;
        } else {
            id = clientes.stream()
                    .mapToInt(c -> c.getId())
                    .max()
                    .getAsInt() + 1;
        }

        Cliente cliente = new Cliente(
                id, nombre, documento, correo, telefono
        );
        
        clientes.add(cliente);
        
        archivoCliente.guardar(clientes);
        
        System.out.println("Cliente registrado correctamente");
        System.out.println("Total de clientes: " + clientes.size());
    }
    
    public List<Cliente> listarClientes() {
        return new ArrayList<>(clientes);
    }   
}
