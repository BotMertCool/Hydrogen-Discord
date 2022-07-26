package net.frozenorb.hydrogen;

import com.google.common.collect.Maps;
import com.google.gson.reflect.TypeToken;
import net.frozenorb.hydrogen.Hydrogen;
import net.frozenorb.hydrogen.connection.RequestHandler;
import net.frozenorb.hydrogen.connection.RequestResponse;
import net.frozenorb.hydrogen.prefix.Prefix;
import net.frozenorb.hydrogen.rank.Rank;
import net.frozenorb.hydrogen.server.Server;
import net.frozenorb.hydrogen.server.ServerGroup;

import java.util.*;


public class RefreshTask extends TimerTask {
    int previousPlayerCount = 0;
    
    @Override
    public void run() {
        int playerCount = 0;
        
        RequestResponse response = RequestHandler.get("/serverGroups");
        if (response.wasSuccessful()) {
            Hydrogen.getInstance().getServerHandler().setServerGroups(Hydrogen.PLAIN_GSON.fromJson(response.getResponse(), new TypeToken<Set<ServerGroup>>() {}.getType()));
        } else System.out.println("ServerHandler - Could not get server groups from API: " + response.getErrorMessage());
    
        response = RequestHandler.get("/servers");
        if (response.wasSuccessful()) {
            Hydrogen.getInstance().getServerHandler().setServers(Hydrogen.PLAIN_GSON.fromJson(response.getResponse(), new TypeToken<Set<Server>>() {}.getType()));
        } else System.out.println("ServerHandler - Could not get servers from API: " + response.getErrorMessage());
    
        response = RequestHandler.get("/ranks");
        if (response.wasSuccessful()) {
            Hydrogen.PLAIN_GSON.fromJson(response.getResponse(), new TypeToken<List<Rank>>() {}.getType());
            Hydrogen.getInstance().getRankHandler().getRanks().sort(Rank.DISPLAY_WEIGHT_COMPARATOR);
        } else System.out.println("RankHandler - Could not retrieve ranks from API: " + response.getErrorMessage());
    
        response = RequestHandler.get("/dumps/totp");
        if (response.wasSuccessful()) {
            Hydrogen.getInstance().getProfileHandler().setTotpEnabled(Hydrogen.PLAIN_GSON.fromJson(response.getResponse(), new TypeToken<Set<UUID>>() {}.getType()));
        } else System.out.println("ProfileHandler - Could not get totp-enabled users from API: " + response.getErrorMessage());
    
        response = RequestHandler.get("/prefixes");
        if (response.wasSuccessful()) {
            Hydrogen.getInstance().getPrefixHandler().setPrefixes(Hydrogen.PLAIN_GSON.fromJson(response.getResponse(), new TypeToken<List<Prefix>>() {}.getType()));

            Map<String, Prefix> newPrefixCache = Maps.newHashMap();
            for (Prefix prefix : Hydrogen.getInstance().getPrefixHandler().getPrefixes())
                newPrefixCache.put(prefix.getId(), prefix);
    
            Hydrogen.getInstance().getPrefixHandler().setPrefixCache(newPrefixCache);
        } else System.out.println("PrefixHandler - Could not retrieve prefixes from API: " + response.getErrorMessage());
        
        for (Server servers : Hydrogen.getInstance().getServerHandler().getServers()) {
            response = RequestHandler.get("/servers/" + servers.getId() + "/playerCount");
        
            if (!response.wasSuccessful()) {
                System.out.println(response.getErrorMessage());
                return;
            }
        
            String serverPlayerCount = response.getResponse();
        
            //System.out.println("[ Server: " + servers.getId() + ", Players: " + serverPlayerCount + " ]");
            
            playerCount += Integer.parseInt(serverPlayerCount);
            
            if (playerCount != this.previousPlayerCount) {
                Hydrogen.getInstance().getSettings().getBotPlayerStatsChannel().getManager().setName("players-" + playerCount).queue();
                System.out.println("server player count display has been updated");
            }
            
            this.previousPlayerCount = playerCount;
        }
        
        
    }
}
