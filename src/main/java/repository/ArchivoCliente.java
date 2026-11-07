package repository;

import model.Cliente;

import java.io.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ArchivoCliente {
    private final String RUTA = "data/clientes.txt";
    
    public void guardar(List<Cliente> clientes) {
        
        try {
            Files.createDirectories(Paths.get("data"));
            
            BufferedWriter escritor = Files.newBufferedWriter(
                    Paths.get(RUTA), StandardCharsets.UTF_8);
            
            for (Cliente c : clientes) {
                escritor.write(
                        c.getId() + "|" + 
                        c.getNombre() + "|" + 
                        c.getDocumento() + "|" +
                        c.getCorreo() + "|" +
                        c.getTelefono()
                );
                
                escritor.newLine();
            }
            
            escritor.close();
            
            System.out.println("Clientes guardados correctamente");
         
        } catch (IOException e) {
            System.out.println("Error al guardar clientes: " + e.getMessage());
        }
    }
    
    public ArrayList<Cliente> cargar() {
        
        ArrayList<Cliente> clientes = new ArrayList();
        
        Path ruta = Paths.get(RUTA);
        
        if (!Files.exists(ruta)) {
            return clientes;
        }
        
        try (BufferedReader lector = Files.newBufferedReader(
                ruta, StandardCharsets.UTF_8)) {
            
            String linea;
            
            while ((linea = lector.readLine()) != null) {
                
                String[] datos = linea.split("\\|", -1);
                
                if (datos.length == 5) {
                
                    Cliente cliente = new Cliente(
                        Integer.parseInt(datos[0]),
                        datos[1],
                        datos[2],
                        datos[3],
                        datos[4]           
                    );
                
                    clientes.add(cliente);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error al cargar empleados: " + e.getMessage());
        }
        
        return clientes;
    }
}
