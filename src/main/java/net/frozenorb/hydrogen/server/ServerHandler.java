package net.frozenorb.hydrogen.server;

import lombok.Getter;
import lombok.Setter;

import net.frozenorb.hydrogen.RefreshTask;

import java.util.*;
import java.util.concurrent.CompletableFuture;


@Getter @Setter
public class ServerHandler {
    
    private Set<ServerGroup> serverGroups = new HashSet<>();
    private Set<Server> servers = new HashSet<>();
    
    public ServerHandler() {
        CompletableFuture.runAsync(() -> new Timer().scheduleAtFixedRate(new RefreshTask(), 5000L, 3000L));
    }
    
    public Optional<ServerGroup> getServerGroup(String parse) {
        for (ServerGroup group : this.serverGroups) {
            if (group.getId().equalsIgnoreCase(parse))
                return Optional.of(group);
        }
        return Optional.empty();
    }
    
    public Optional<Server> getServer(String parse) {
        for (Server server : this.servers) {
            if (server.getId().equalsIgnoreCase(parse) || server.getDisplayName().equalsIgnoreCase(parse))
                return Optional.of(server);
        }
        return Optional.empty();
    }
    
}
