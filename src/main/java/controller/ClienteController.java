package controller;

import java.util.ArrayList;
import java.util.List;

import model.Cliente;

import repository.ClienteDAO;
import repository.ClienteRepository;
import repository.ArchivoCliente;

public class ClienteController {

    private ArrayList<Cliente> clientes = new ArrayList<>();

    // Dependemos de la interfaz
    private final ClienteDAO clienteRepository;

    private final ArchivoCliente archivoCliente =
            new ArchivoCliente();

    public ClienteController() {
        this(new ClienteRepository());
    }

    // Constructor con inyección de dependencias
    public ClienteController(ClienteDAO clienteRepository) {

        this.clienteRepository = clienteRepository;

        // Cargar clientes desde MySQL
        clientes = new ArrayList<>(
                clienteRepository.listar()
        );

        // Actualizar archivo TXT
        archivoCliente.guardar(clientes);

        System.out.println(
                "Clientes cargados desde MySQL: "
                + clientes.size()
        );
    }

    public void registrarCliente(
            String nombre,
            String documento,
            String correo,
            String telefono) {

        if (nombre == null || nombre.trim().isEmpty()
                || documento == null || documento.trim().isEmpty()
                || correo == null || correo.trim().isEmpty()
                || telefono == null || telefono.trim().isEmpty()) {

            System.out.println(
                    "Los datos del cliente son inválidos."
            );
            return;
        }

        // Validar documento duplicado
        boolean documentoExiste = clientes.stream()
                .anyMatch(c ->
                        c.getDocumento().equals(documento));

        if (documentoExiste) {
            System.out.println(
                    "Ya existe un cliente con ese documento."
            );
            return;
        }

        // Crear cliente provisional
        Cliente cliente = new Cliente(
                0,
                nombre,
                documento,
                correo,
                telefono
        );

        // Guardar en MySQL y recuperar ID generado
        int idGenerado = clienteRepository.guardar(cliente);

        if (idGenerado > 0) {

            // Crear objeto con el ID real de MySQL
            Cliente clienteGuardado = new Cliente(
                    idGenerado,
                    nombre,
                    documento,
                    correo,
                    telefono
            );

            clientes.add(clienteGuardado);

            // Actualizar archivo TXT
            archivoCliente.guardar(clientes);

            System.out.println(
                    "Cliente registrado correctamente."
            );

            System.out.println(
                    "ID del cliente: " + idGenerado
            );

        } else {

            System.out.println(
                    "No fue posible registrar el cliente."
            );
        }
    }

    public List<Cliente> listarClientes() {
        return new ArrayList<>(clientes);
    }
    
    public void actualizarCliente(
        int id,
        String nombre,
        String documento,
        String correo,
        String telefono) {

        if (nombre == null || nombre.trim().isEmpty()
                || documento == null || documento.trim().isEmpty()
                || correo == null || correo.trim().isEmpty()
                || telefono == null || telefono.trim().isEmpty()) {

            System.out.println("Los datos del cliente son inválidos.");
            return;
        }

        Cliente clienteActualizado = new Cliente(
                id,
                nombre,
                documento,
                correo,
                telefono
        );

        boolean actualizado = clienteRepository.actualizar(
                clienteActualizado
        );

        if (actualizado) {

            clientes = new ArrayList<>(clienteRepository.listar());

            archivoCliente.guardar(clientes);

            System.out.println("Cliente actualizado correctamente.");

        } else {

            System.out.println("No fue posible actualizar el cliente.");
        }
    }
    
    public void eliminarCliente(int id) {

        boolean eliminado = clienteRepository.eliminar(id);

        if (eliminado) {

            clientes = new ArrayList<>(clienteRepository.listar());

            archivoCliente.guardar(clientes);

            System.out.println("Cliente eliminado correctamente.");

        } else {

            System.out.println("No fue posible eliminar el cliente.");
        }
    }
}
