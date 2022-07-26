package net.frozenorb.hydrogen.profile;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.frozenorb.hydrogen.Hydrogen;
import net.frozenorb.hydrogen.Settings;
import net.frozenorb.hydrogen.connection.RequestHandler;
import net.frozenorb.hydrogen.connection.RequestResponse;
import net.frozenorb.hydrogen.prefix.Prefix;
import net.frozenorb.hydrogen.prefix.PrefixHandler;
import net.frozenorb.hydrogen.punishment.Punishment;
import net.frozenorb.hydrogen.rank.Rank;
import net.frozenorb.hydrogen.rank.RankHandler;
import lombok.Getter;
import lombok.Setter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
    
@Getter
@Setter
public class Profile {
    
    private UUID player;
    private List<Rank> ranks;
    private Map<String, List<Rank>> scopeRanks;
    private boolean accessAllowed;
    private String accessDenialReason;
    private Map<String, Boolean> permissions;
    private boolean totpRequired;
    private boolean authenticated;
    private boolean ipWhitelisted;
    private Punishment mute;
    private Set<Prefix> authorizedPrefixes;
    private Prefix activePrefix;
    private Rank bestGeneralRank;
    private Rank bestDisplayRank;
    
    public void checkTotpLock(String ip) {
        ProfileHandler profileHandler = Hydrogen.getInstance().getProfileHandler();
        if (!profileHandler.getTotpEnabled().contains(this.player) && !this.getBestGeneralRank().isStaffRank())
            this.totpRequired = false;
        
        RequestResponse response = RequestHandler.get("/users/" + this.player.toString() + "/requiresTotp", ImmutableMap.of("userIp", ip));
        if (response.wasSuccessful()) {
            this.totpRequired = response.asJSONObject().getBoolean("required");
            this.ipWhitelisted = response.asJSONObject().getString("message").equals("NOT_REQUIRED_IP_PRE_AUTHORIZED");
        }
    }
    
    public void setActivePrefix(Prefix prefix) {
        this.activePrefix = prefix;
        
        Map<String, Object> meta = Maps.newHashMap();
        meta.put("prefix", prefix == null ? null : prefix.getId());
        
        RequestResponse response = RequestHandler.post("/users/" + net.frozenorb.hydrogen.profile.Profile.this.player + "/prefix", meta);
        if (!response.wasSuccessful())
            System.out.println(response.asJSONObject().toString());
        
    }
    
    public void authenticated() {
        this.authenticated = true;
    }
    
    public boolean isMuted() {
        return this.mute != null;
    }
}


