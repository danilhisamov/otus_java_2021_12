package ru.otus.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.model.ClientDTO;
import ru.otus.services.TemplateProcessor;

import java.io.IOException;
import java.util.Map;

public class ClientsServlet extends HttpServlet {
    private static final String TEMPLATE = "clients.ftlh";
    public static final String URL = "/clients";
    private final DBServiceClient dbServiceClient;
    private final TemplateProcessor templateProcessor;

    public ClientsServlet(DBServiceClient dbServiceClient, TemplateProcessor templateProcessor) {
        this.dbServiceClient = dbServiceClient;
        this.templateProcessor = templateProcessor;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        var clients = dbServiceClient.findAll().stream().map(ClientDTO::fromEntity).toList();
        Map<String, Object> paramsMap = Map.of(
                "clients", clients
        );

        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(TEMPLATE, paramsMap));
    }
}
