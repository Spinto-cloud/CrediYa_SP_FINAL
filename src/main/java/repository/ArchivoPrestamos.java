package repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Prestamos;
import model.Cuotas;
import model.Cliente;
import model.Empleado;

public class ArchivoPrestamos {

    private final String RUTA_PRESTAMOS = "data/prestamos.txt";
    private final String RUTA_CUOTAS = "data/cuotas.txt";

    public void guardar(List<Prestamos> prestamos) {

        try {
            Files.createDirectories(Paths.get("data"));

            try (BufferedWriter escritorPrestamos = Files.newBufferedWriter(
                    Paths.get(RUTA_PRESTAMOS), StandardCharsets.UTF_8);
                 BufferedWriter escritorCuotas = Files.newBufferedWriter(
                    Paths.get(RUTA_CUOTAS), StandardCharsets.UTF_8)) {

                for (Prestamos p : prestamos) {

                    escritorPrestamos.write(
                            p.getIdPrestamo() + "|" +
                            p.getCliente().getId() + "|" +
                            p.getEmpleado().getId() + "|" +
                            p.getMonto() + "|" +
                            p.getTasaInteres() + "|" +
                            p.getNumeroCuotas() + "|" +
                            p.getFechaInicio() + "|" +
                            p.getEstado()
                    );

                    escritorPrestamos.newLine();

                    for (Cuotas c : p.getListaCuotas()) {

                        escritorCuotas.write(
                                p.getIdPrestamo() + "|" +
                                c.getNumero() + "|" +
                                c.getValor() + "|" +
                                c.getInteres() + "|" +
                                c.getCapital() + "|" +
                                c.getMontoPagado() + "|" +
                                c.getFechaVencimiento()
                        );

                        escritorCuotas.newLine();
                    }
                }
            }

            System.out.println("Prestamos y cuotas guardados correctamente.");

        } catch (IOException e) {
            System.out.println("Error al guardar prestamos: " + e.getMessage());
        }
    }

    public ArrayList<Prestamos> cargar(
            List<Cliente> clientes,
            List<Empleado> empleados) {

        ArrayList<Prestamos> prestamos = new ArrayList<>();

        Path rutaPrestamos = Paths.get(RUTA_PRESTAMOS);
        Path rutaCuotas = Paths.get(RUTA_CUOTAS);

        if (!Files.exists(rutaPrestamos)) {
            return prestamos;
        }

        Map<Integer, Prestamos> prestamosPorId = new HashMap<>();

        try (BufferedReader lector = Files.newBufferedReader(
                rutaPrestamos, StandardCharsets.UTF_8)) {

            String linea;

            while ((linea = lector.readLine()) != null) {

                String[] datos = linea.split("\\|", -1);

                if (datos.length != 8) {
                    continue;
                }

                int idPrestamo = Integer.parseInt(datos[0]);
                int idCliente = Integer.parseInt(datos[1]);
                int idEmpleado = Integer.parseInt(datos[2]);

                Cliente cliente = clientes.stream()
                        .filter(c -> c.getId() == idCliente)
                        .findFirst()
                        .orElse(null);

                Empleado empleado = empleados.stream()
                        .filter(e -> e.getId() == idEmpleado)
                        .findFirst()
                        .orElse(null);

                if (cliente == null || empleado == null) {
                    System.out.println(
                            "No se pudo recuperar el prestamo " + idPrestamo
                    );
                    continue;
                }

                Prestamos prestamo = new Prestamos(
                        idPrestamo,
                        cliente,
                        empleado,
                        Double.parseDouble(datos[3]),
                        Double.parseDouble(datos[4]),
                        Integer.parseInt(datos[5]),
                        java.time.LocalDate.parse(datos[6])
                );

                prestamo.cargarEstado(datos[7]);

                prestamos.add(prestamo);
                prestamosPorId.put(idPrestamo, prestamo);
            }

        } catch (IOException | NumberFormatException e) {
            System.out.println("Error al cargar prestamos: " + e.getMessage());
        }

        // Recuperar las cuotas guardadas
        if (Files.exists(rutaCuotas)) {

            Map<Integer, ArrayList<Cuotas>> cuotasPorPrestamo =
                    new HashMap<>();

            try (BufferedReader lector = Files.newBufferedReader(
                    rutaCuotas, StandardCharsets.UTF_8)) {

                String linea;

                while ((linea = lector.readLine()) != null) {

                    String[] datos = linea.split("\\|", -1);

                    if (datos.length != 7) {
                        continue;
                    }

                    int idPrestamo = Integer.parseInt(datos[0]);

                    Cuotas cuota = new Cuotas(
                            Integer.parseInt(datos[1]),
                            Double.parseDouble(datos[2]),
                            Double.parseDouble(datos[3]),
                            Double.parseDouble(datos[4]),
                            java.time.LocalDate.parse(datos[6])
                    );

                    cuota.cargarMontoPagado(
                            Double.parseDouble(datos[5])
                    );

                    cuotasPorPrestamo
                            .computeIfAbsent(idPrestamo, k -> new ArrayList<>())
                            .add(cuota);
                }

                for (Map.Entry<Integer, ArrayList<Cuotas>> entrada
                        : cuotasPorPrestamo.entrySet()) {

                    Prestamos prestamo = prestamosPorId.get(entrada.getKey());

                    if (prestamo != null) {
                        prestamo.cargarCuotas(entrada.getValue());
                    }
                }

            } catch (IOException | NumberFormatException e) {
                System.out.println("Error al cargar cuotas: " + e.getMessage());
            }
        }

        return prestamos;
    }
}