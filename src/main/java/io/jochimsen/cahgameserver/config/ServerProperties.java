package io.jochimsen.cahgameserver.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gameserver")
public class ServerProperties {
    private int port;
    private int bossCount;
    private int workerCount;
    private boolean keepAlive;
    private int backlog;
    private String webserviceUrl;

    public int getPort() {
        return port;
    }

    public void setPort(final int port) {
        this.port = port;
    }

    public int getBossCount() {
        return bossCount;
    }

    public void setBossCount(final int bossCount) {
        this.bossCount = bossCount;
    }

    public int getWorkerCount() {
        return workerCount;
    }

    public void setWorkerCount(final int workerCount) {
        this.workerCount = workerCount;
    }

    public boolean isKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(final boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    public int getBacklog() {
        return backlog;
    }

    public void setBacklog(final int backlog) {
        this.backlog = backlog;
    }

    public String getWebserviceUrl() {
        return webserviceUrl;
    }

    public void setWebserviceUrl(final String webserviceUrl) {
        this.webserviceUrl = webserviceUrl;
    }
}
