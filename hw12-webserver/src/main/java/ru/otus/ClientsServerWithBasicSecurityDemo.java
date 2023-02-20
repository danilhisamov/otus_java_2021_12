package ru.otus;

import com.google.gson.GsonBuilder;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.LoginService;
import org.hibernate.cfg.Configuration;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.crm.service.DbServiceClientImpl;
import ru.otus.helpers.FileSystemHelper;
import ru.otus.server.ClientsWebServer;
import ru.otus.server.ClientsWebServerWithBasicSecurity;
import ru.otus.services.TemplateProcessor;
import ru.otus.services.TemplateProcessorImpl;

import java.util.List;


public class ClientsServerWithBasicSecurityDemo {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";
    private static final String HASH_LOGIN_SERVICE_CONFIG_NAME = "realm.properties";
    private static final String REALM_NAME = "AnyRealm";
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) throws Exception {
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        var gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        String hashLoginServiceConfigPath = FileSystemHelper.localFileNameOrResourceNameToFullPath(HASH_LOGIN_SERVICE_CONFIG_NAME);
        LoginService loginService = new HashLoginService(REALM_NAME, hashLoginServiceConfigPath);

        var dbServiceClient = createDBServiceClient();

        ClientsWebServer clientsWebServer = new ClientsWebServerWithBasicSecurity(WEB_SERVER_PORT,
                gson, loginService, dbServiceClient, templateProcessor);

        initClients(dbServiceClient);

        clientsWebServer.start();
        clientsWebServer.join();
    }

    private static DBServiceClient createDBServiceClient() {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);
        var sessionFactory = HibernateUtils.buildSessionFactory(
                configuration,
                Client.class, Address.class, Phone.class
        );
        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
        return new DbServiceClientImpl(transactionManager, clientTemplate);
    }

    private static void initClients(DBServiceClient dbServiceClient) {
        dbServiceClient.saveClient(
                new Client(
                        null,
                        "Джон Уик",
                        new Address(null, "Отель Континенталь"),
                        List.of(new Phone(null, "+1 234"))
                )
        );

        dbServiceClient.saveClient(
                new Client(
                        null,
                        "Брюс Уейн",
                        new Address(null, "Бэт пещера"),
                        List.of(new Phone(null, "+1 234"))
                )
        );
    }
}
