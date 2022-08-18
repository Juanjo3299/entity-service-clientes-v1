package com.jm.entityserviceclientesv1.controllers;

import com.jm.entityserviceclientesv1.models.entity.Cliente;
import com.jm.entityserviceclientesv1.utils.ReadJsonFile;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ClienteController {

    @PostConstruct
    public void init() {
        List<Cliente> clientes = null;
        try {
            clientes = ReadJsonFile.findAllClientes();
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @GetMapping("/clientes")
    public List<Cliente> getAllClientes() {
        List<Cliente> clientes = new ArrayList<>();
        try {
            clientes = ReadJsonFile.findAllClientes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return clientes;
    }

    @PostMapping("/clientes")
    public ResponseEntity<Map<String, Object>> createCliente(@RequestBody Cliente cliente) {
        List<Cliente> clientes = new ArrayList<>();
        Map<String, Object> response = new HashMap<>();
        try {
            clientes = ReadJsonFile.findAllClientes();
            cliente.setId(obtieneId(clientes));
            clientes.add(cliente);
            ReadJsonFile.actualizarJson(clientes);
        } catch (IOException e) {
            e.printStackTrace();
            response.put("mensaje", "Error al realizar el insert");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("mensaje", "El cliente ha sido creado con éxito!");
        response.put("cliente", cliente);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @GetMapping("/clientes/{name}")
    public ResponseEntity<?> buscarCliente(@PathVariable("name") String name) {
        List<Cliente> clientes = new ArrayList<>();
        List<Cliente> resultClientes = new ArrayList<>();
        Map<String, Object> response = new HashMap<>();
        try {
            clientes = ReadJsonFile.findAllClientes();
            for (Cliente cliente : clientes) {
                if (cliente.getNombre().equals(name)) {
                    resultClientes.add(cliente);
                }
            }
            ReadJsonFile.actualizarJson(clientes);
        } catch (IOException e) {
            e.printStackTrace();
            response.put("mensaje", "Error al realizar la busqueda");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("mensaje", "Se encontraron los siguients clientes");
        response.put("clientes", resultClientes);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @PutMapping("/clientes")
    public ResponseEntity<?> updateCliente(@RequestBody Cliente clienteToUpdate) {
        List<Cliente> clientes = new ArrayList<>();
        Map<String, Object> response = new HashMap<>();
        try {
            clientes = ReadJsonFile.findAllClientes();
            for (Cliente cliente : clientes) {
                if (cliente.getId() == clienteToUpdate.getId()) {
                    cliente.setNombre(clienteToUpdate.getNombre());
                    cliente.setCorreo(clienteToUpdate.getCorreo());
                    break;
                }
            }
            ReadJsonFile.actualizarJson(clientes);
        } catch (IOException e) {
            e.printStackTrace();
            response.put("mensaje", "Error al realizar el update");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("mensaje", "El cliente ha sido actualizado con éxito!");
        response.put("clientes", clientes);
        response.put("cliente", clienteToUpdate);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "clientes/{idCliente}")
    public ResponseEntity<?> deleteCliente(@PathVariable int idCliente) {
        List<Cliente> clientes = new ArrayList<>();
        Map<String, Object> response = new HashMap<>();
        boolean isFind = false;
        try {
            clientes = ReadJsonFile.findAllClientes();
            int index = 0;
            for (Cliente cliente : clientes) {
                if (cliente.getId() == idCliente) {
                    clientes.remove(index);
                    isFind = true;
                    break;
                }
                index++;
            }
            ReadJsonFile.actualizarJson(clientes);
        } catch (IOException e) {
            e.printStackTrace();
            response.put("mensaje", "Error al realizar el delete");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("mensaje", isFind ? "El cliente eliminado con éxito!" : "No se encontraron clientes con ese id");
        response.put("clientes", clientes);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    public int obtieneId(List<Cliente> listaClientes) {
        int id = 0;
        if (listaClientes.size() == 0)
            return id;
        List<Integer> idlista = new ArrayList<Integer>();
        for (Cliente cliente : listaClientes) {
            idlista.add(cliente.getId());
        }
        Collections.sort(idlista);
        int index = idlista.size() - 2;
        id = idlista.get(index + 1) + 1;

        return id;
    }
}
