package io.jochimsen.cahgameserver.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gameserver")
@Getter
@Setter
public class ServerProperties {
    private int port;
    private int bossCount;
    private int workerCount;
    private boolean keepAlive;
    private int backlog;
    private String webserviceUrl;
}
