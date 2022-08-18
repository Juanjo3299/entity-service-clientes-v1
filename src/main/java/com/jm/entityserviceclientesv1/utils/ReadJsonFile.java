package com.jm.entityserviceclientesv1.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.jm.entityserviceclientesv1.models.entity.Cliente;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadJsonFile {

    public static List<Cliente> findAllClientes() throws IOException {
        ObjectMapper  mapper = new ObjectMapper();
        List<Cliente> clientes = new ArrayList<>();
        File jsonFile = new ClassPathResource("static/persistenciaClientes.json").getFile();
        if (jsonFile.length() == 0)
            return clientes;
        clientes = mapper.readValue(jsonFile, new TypeReference<List<Cliente>>() {});
        return clientes;
    }

    public static void actualizarJson(List<Cliente> clientes) throws IOException {
        String json = new Gson().toJson(clientes);
        File jsonFile = new ClassPathResource("static/persistenciaClientes.json").getFile();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(jsonFile, clientes);
//        FileWriter file = new FileWriter(jsonFile.getPath());
//        file.write(json);
//        file.close();
    }

}
