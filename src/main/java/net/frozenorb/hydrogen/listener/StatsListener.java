package net.frozenorb.hydrogen.listener;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.frozenorb.hydrogen.Hydrogen;
import net.frozenorb.hydrogen.connection.RequestHandler;
import net.frozenorb.hydrogen.connection.RequestResponse;
import net.frozenorb.hydrogen.server.Server;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class StatsListener extends ListenerAdapter {
    
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        System.out.println("Hydrogen-Discord has started.");
    }
    
}
