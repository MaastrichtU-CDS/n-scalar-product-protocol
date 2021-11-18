package com.florian.nscalarproduct.webservice.domain;

import java.util.List;

public class EndpointRequest {
    private List<String> servers;

    public EndpointRequest() {
    }

    public List<String> getServers() {
        return servers;
    }

    public void setServers(List<String> servers) {
        this.servers = servers;
    }
}
