package webservice;

import java.util.List;

public class Protocol {
    private List<Server> servers;
    private Server secretServer;
    private String id;

    public Protocol(List<Server> servers, Server secretServer, String id) {
        this.servers = servers;
        this.secretServer = secretServer;
        this.id = id;
    }

    public List<Server> getServers() {
        return servers;
    }

    public Server getSecretServer() {
        return secretServer;
    }

    public String getId() {
        return id;
    }
}
