package org.kristiania.chatRoom;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.Persistence;
import jakarta.servlet.DispatcherType;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;
import org.flywaydb.core.Flyway;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Properties;

public class ChatRoomServer {

    private final Server server;
    private static final Logger logger = LoggerFactory.getLogger(ChatRoomServer.class);

    public ChatRoomServer(int port, DataSource dataSource) throws IOException, NamingException {
        this.server = new Server(port);
        server.setHandler(new HandlerList(
                createApiContext(dataSource),
                createWebAppContext()
        ));
    }

    private WebAppContext createWebAppContext() throws IOException {

        WebAppContext webAppContext = new WebAppContext();
        webAppContext.setContextPath("/");

        // This code is to use src/main/resources instead of target/class. don't need to restart server to update.
        Resource resource = Resource.newClassPathResource("/webApp");
        File sourceDirectory = getSourceDirectory(resource);
        if (sourceDirectory != null) {
            webAppContext.setBaseResource(Resource.newResource(sourceDirectory));
            webAppContext.setInitParameter(DefaultServlet.CONTEXT_INIT + "useFileMappedBuffer", "false");
        } else {
            webAppContext.setBaseResource(resource);
        }
        // end of restartfix.

        return webAppContext;
    }

    private File getSourceDirectory(Resource resource) throws IOException {
        if (resource.getFile() == null) {
            return null;
        }
        var sourceDirectory = new File(resource.getFile().getAbsolutePath()
                .replace('\\', '/')
                .replace("target/classes", "src/main/resources"));
        return sourceDirectory.exists() ? sourceDirectory : null;
    }


    private ServletContextHandler createApiContext(DataSource dataSource) throws NamingException {
        var context = new ServletContextHandler(server, "/api");
        new org.eclipse.jetty.plus.jndi.Resource("jdbc/dataSource", dataSource);
        var entityManagerFactory = Persistence.createEntityManagerFactory("ChatRoom");
        ChatRoomConfig chatRoomConfig = new ChatRoomConfig(entityManagerFactory);
        context.addServlet(new ServletHolder(
                new ServletContainer(chatRoomConfig)), "/*");

        context.addFilter(new FilterHolder(
                new EntityManagerFilter(chatRoomConfig)), "/*", EnumSet.of(DispatcherType.REQUEST));
        return context;
    }

    public void start() throws Exception {
        server.start();
    }

    public URL getURL() throws MalformedURLException {
        return server.getURI().toURL();
    }


    public static void main(String[] args) throws Exception {

        int port = Optional.ofNullable(System.getenv("HTTP_PLATFORM_PORT"))
                .map(Integer::parseInt)
                .orElse(9090);

        var properties = new Properties();
        try (var reader = new FileReader("application.properties")) {
            properties.load(reader);
        }

        var dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(properties.getProperty("database.url"));
        dataSource.setUsername(properties.getProperty("database.username"));
        dataSource.setPassword(properties.getProperty("database.password"));
        Flyway.configure().dataSource(dataSource).load().migrate();

        var server = new ChatRoomServer(port, dataSource);
        server.start();
        logger.info("Server starting at {} ", server.getURL());
    }
}
