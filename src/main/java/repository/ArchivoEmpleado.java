package repository;

import model.Empleado;

import java.io.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ArchivoEmpleado {
    
    private final String RUTA = "data/empleados.txt";
    
    public void guardar(List<Empleado> empleados) {
        
        try {
            Files.createDirectories(Paths.get("data"));
            
            BufferedWriter escritor = Files.newBufferedWriter(
                    Paths.get(RUTA), StandardCharsets.UTF_8);
            
            for (Empleado e : empleados) {
                escritor.write(
                        e.getId() + "|" + 
                        e.getNombre() + "|" + 
                        e.getDocumento() + "|" +
                        e.getCorreo() + "|" +
                        e.getRol() + "|" +
                        e.getSalario()
                );
                
                escritor.newLine();
            }
            
            escritor.close();
            
            System.out.println("Empleados guardados correctamente");
         
        } catch (IOException e) {
            System.out.println("Error al guardar empleados: " + e.getMessage());
        }
    }
    
    public ArrayList<Empleado> cargar() {
        
        ArrayList<Empleado> empleados = new ArrayList();
        
        Path ruta = Paths.get(RUTA);
        
        if (!Files.exists(ruta)) {
            return empleados;
        }
        
        try (BufferedReader lector = Files.newBufferedReader(
                ruta, StandardCharsets.UTF_8)) {
            
            String linea;
            
            while ((linea = lector.readLine()) != null) {
                
                String[] datos = linea.split("\\|", -1);
                
                if (datos.length == 6) {
                
                    Empleado empleado = new Empleado(
                        Integer.parseInt(datos[0]),
                        datos[1],
                        datos[2],
                        datos[3],
                        datos[4],
                        Double.parseDouble(datos[5])            
                    );
                
                    empleados.add(empleado);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error al cargar empleados: " + e.getMessage());
        }
        
        return empleados;
    }
}
