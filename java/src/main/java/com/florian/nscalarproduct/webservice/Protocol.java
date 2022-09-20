package com.florian.nscalarproduct.webservice;

import java.util.List;

public class Protocol {
    private List<ServerEndpoint> servers;
    private ServerEndpoint secretServer;
    private String id;
    private int precision;

    public Protocol(List<ServerEndpoint> servers, ServerEndpoint secretServer, String id, int precision) {
        this.servers = servers;
        this.secretServer = secretServer;
        this.id = id;
        this.precision = precision;
    }

    public int getPrecision() {
        return precision;
    }

    public List<ServerEndpoint> getServers() {
        return servers;
    }

    public ServerEndpoint getSecretServer() {
        return secretServer;
    }

    public String getId() {
        return id;
    }
}
