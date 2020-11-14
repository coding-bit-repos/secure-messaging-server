package com.codingbit.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.servlet.GuiceServletContextListener;

public class SMServer {

    private static final Logger log = LoggerFactory.getLogger(SMServer.class);

    private Integer port;
    private final Server server;

    public SMServer(Module[] guiceModules) {
        this.server = buildServer(guiceModules);
    }

    private Server buildServer(Module[] guiceModules) {
        // create the server
        Server newServer = new Server();

        // add the connector
        ServerConnector connector = new ServerConnector(newServer);
        connector.setPort(port != null ? port : 8080);
        newServer.addConnector(connector);

        // configure servlet
        ServletContextHandler servletContextHandler = new ServletContextHandler(newServer, "/",
                ServletContextHandler.NO_SESSIONS);

        servletContextHandler.setContextPath("/");
        servletContextHandler.addServlet(DefaultServlet.class, "/");

        // configure guice
        servletContextHandler.addEventListener(new GuiceServletContextListener() {
            @Override
            protected Injector getInjector() {
                try {
                    return Guice.createInjector(guiceModules);
                } catch (Exception e) {
                    throw new RuntimeException("Error creating Guice injector", e);
                }
            }
        });

        return newServer;
    }

    public void start() {
        new Thread(() -> {
            try {
                server.start();

                log.info(server.dump());

                server.join();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public void stop() throws Exception {
        server.stop();
    }
}
