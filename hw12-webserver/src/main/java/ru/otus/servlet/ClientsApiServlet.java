package ru.otus.servlet;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.crm.model.Client;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.model.ClientDTO;
import ru.otus.services.TemplateProcessor;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

public class ClientsApiServlet extends HttpServlet {
    public static final String URL = "/api/clients";
    private final DBServiceClient dbServiceClient;
    private final Gson gson;

    public ClientsApiServlet(DBServiceClient dbServiceClient, Gson gson) {
        this.dbServiceClient = dbServiceClient;
        this.gson = gson;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String body = req.getReader().lines().collect(Collectors.joining());
        var client = gson.fromJson(body, Client.class);
        client = dbServiceClient.saveClient(client);
        resp.setContentType("application/json;charset=UTF-8");
        resp.getWriter().print(gson.toJson(ClientDTO.fromEntity(client)));
    }
}
