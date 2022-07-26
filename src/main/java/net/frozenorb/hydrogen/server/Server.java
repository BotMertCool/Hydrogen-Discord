package net.frozenorb.hydrogen.server;

import net.frozenorb.hydrogen.Hydrogen;
import lombok.Getter;

@Getter
public class Server {
    
    private String id;
    private String displayName;
    private String serverGroup;
    private String serverIp;
    private long lastUpdatedAt;
    private double lastTps;
    
    
    public ServerGroup resolveGroup() {
        return Hydrogen.getInstance().getServerHandler()
                .getServerGroup(serverGroup)
                .orElse(null);
    }
    
}
