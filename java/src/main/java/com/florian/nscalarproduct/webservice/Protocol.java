package com.florian.nscalarproduct.webservice;

import java.util.List;

public class Protocol {
    private List<ServerEndpoint> servers;
    private ServerEndpoint secretServer;
    private String id;

    public Protocol(List<ServerEndpoint> servers, ServerEndpoint secretServer, String id) {
        this.servers = servers;
        this.secretServer = secretServer;
        this.id = id;
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
