package net.frozenorb.hydrogen;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

@Getter @Setter
public class Settings {

    private final String botToken;
    private Guild botGuild;
    private TextChannel botPlayerStatsChannel;
    private final String apiHost;
    private final String apiKey;
    private final Config config;
    
    public Settings() {
        System.setProperty("config.file", System.getProperty("config.file", "application.conf"));
        config = ConfigFactory.load();
    
        this.botToken = config.getString("bot.token");
        
        this.apiHost = config.getString("api.host");
        this.apiKey = config.getString("api.key");
    }
    
}
