
package repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import model.Pagos;
import model.Prestamos;

public class ArchivoPagos {

    private static final String RUTA = "data/pagos.txt";

    // Guardar historial de pagos en archivo TXT.
    public void guardar(List<Pagos> pagos) {

        try {
            Files.createDirectories(Paths.get("data"));

            try (BufferedWriter escritor = Files.newBufferedWriter(
                    Paths.get(RUTA),
                    StandardCharsets.UTF_8)) {

                for (Pagos p : pagos) {

                    escritor.write(
                            p.getIdPago() + "|"
                            + p.getPrestamo().getIdPrestamo() + "|"
                            + p.getMonto() + "|"
                            + p.getFecha()
                    );

                    escritor.newLine();
                }
            }

            System.out.println(
                    "Historial de pagos guardado correctamente."
            );

        } catch (IOException e) {

            System.out.println(
                    "Error al guardar pagos: " + e.getMessage()
            );
        }
    }

    // Recuperar historial de pagos desde archivo TXT.
    public ArrayList<Pagos> cargar(List<Prestamos> prestamos) {

        ArrayList<Pagos> pagos = new ArrayList<>();

        if (!Files.exists(Paths.get(RUTA))) {
            return pagos;
        }

        try (BufferedReader lector = Files.newBufferedReader(
                Paths.get(RUTA),
                StandardCharsets.UTF_8)) {

            String linea;

            while ((linea = lector.readLine()) != null) {

                try {

                    String[] datos = linea.split("\\|", -1);

                    if (datos.length != 4) {
                        System.out.println(
                                "Registro de pago ignorado: formato incorrecto."
                        );
                        continue;
                    }

                    int idPago = Integer.parseInt(datos[0]);
                    int idPrestamo = Integer.parseInt(datos[1]);
                    double monto = Double.parseDouble(datos[2]);
                    LocalDate fecha = LocalDate.parse(datos[3]);

                    Prestamos prestamo = null;

                    for (Prestamos p : prestamos) {

                        if (p.getIdPrestamo() == idPrestamo) {
                            prestamo = p;
                            break;
                        }
                    }

                    if (prestamo == null) {
                        System.out.println(
                                "No se encontró el préstamo del pago "
                                + idPago
                        );
                        continue;
                    }

                    Pagos pago = new Pagos(
                            idPago,
                            prestamo,
                            monto,
                            fecha
                    );

                    pagos.add(pago);

                } catch (NumberFormatException
                        | DateTimeParseException e) {

                    System.out.println(
                            "Se ignoró un registro de pago inválido: "
                            + linea
                    );
                }
            }

        } catch (IOException e) {

            System.out.println(
                    "Error al cargar pagos: " + e.getMessage()
            );
        }

        return pagos;
    }
}
