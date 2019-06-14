package com.springer.hack.exambuddy.external;

public class BaseRestServiceProperties {

    private String host;

    private Integer port;

    private String path;

    private Boolean ssl;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Boolean getSsl() {
        return ssl;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setSsl(Boolean ssl) {
        this.ssl = ssl;
    }
}
