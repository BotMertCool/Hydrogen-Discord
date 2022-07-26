package net.frozenorb.hydrogen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.frozenorb.hydrogen.commands.BotCommands;
import net.frozenorb.hydrogen.connection.RequestHandler;
import net.frozenorb.hydrogen.listener.StatsListener;
import net.frozenorb.hydrogen.prefix.PrefixHandler;
import net.frozenorb.hydrogen.profile.ProfileHandler;
import net.frozenorb.hydrogen.punishment.Punishment;
import net.frozenorb.hydrogen.punishment.PunishmentHandler;
import net.frozenorb.hydrogen.rank.RankHandler;
import net.frozenorb.hydrogen.server.ServerHandler;
import net.frozenorb.hydrogen.util.TimeUtils;
import okhttp3.OkHttpClient;

import javax.security.auth.login.LoginException;
import java.util.concurrent.TimeUnit;

@Getter
public class Hydrogen {
    
    @Getter private static Hydrogen instance;
    private OkHttpClient okHttpClient;
    private JDA client;
    private Settings settings;
    private ServerHandler serverHandler;
    private RankHandler rankHandler;
    private ProfileHandler profileHandler;
    private PrefixHandler prefixHandler;
    private PunishmentHandler punishmentHandler;

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
    public static final Gson PLAIN_GSON = new GsonBuilder().create();
    
    public Hydrogen() throws LoginException, InterruptedException {
        instance = this;
        this.settings = new Settings();
        
        System.out.println(settings.getApiHost() +", " + settings.getApiKey());
        
        this.okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(2L, TimeUnit.SECONDS)
                .writeTimeout(2L, TimeUnit.SECONDS)
                .readTimeout(2L, TimeUnit.SECONDS).build();
        
        this.serverHandler = new ServerHandler();
        this.rankHandler = new RankHandler();
        this.profileHandler = new ProfileHandler();
        this.prefixHandler = new PrefixHandler();
        this.punishmentHandler = new PunishmentHandler();
    
        this.client = JDABuilder
                .createDefault(this.settings.getBotToken())
                .addEventListeners(new StatsListener(), new BotCommands())
                .setStatus(OnlineStatus.ONLINE)
                .build().awaitReady();
    
        this.settings.setBotGuild(Hydrogen.getInstance().getClient().getGuildById(this.settings.getConfig().getString("bot.guild-id")));
    
        if (this.settings.getBotGuild() != null)
            this.settings.setBotPlayerStatsChannel(settings.getBotGuild().getTextChannelById(this.settings.getConfig().getString("bot.player-stats-id")));
        else
            this.settings.setBotPlayerStatsChannel(null);
        
        
        if (this.settings.getBotGuild() != null) {
            this.settings.getBotGuild().upsertCommand("api", "See API info").queue();
        }
        
    }
    
    public static void main(String[] args) throws Exception {
        new Hydrogen();
    }
    
    public static String status() {
        return  "Status: " + (RequestHandler.isApiDown() ? "Offline" : "Online") +
                "\nLast Request: " + ((RequestHandler.getLastAPIRequest() == 0L) ? "Never :3" : TimeUtils.formatIntoDetailedString((int) (System.currentTimeMillis() - RequestHandler.getLastAPIRequest()) / 1000) + " ago") +
                "\nLast Error: " + ((RequestHandler.getLastAPIError() == 0L) ? "Never :3" : TimeUtils.formatIntoDetailedString((int) (System.currentTimeMillis() - RequestHandler.getLastAPIError()) / 1000) + " ago") +
                "\nLast Latency: " + RequestHandler.getLastLatency() + "ms" +
                "\nAverage Latency: " + RequestHandler.getAverageLatency() + "ms";
    }
    
}
