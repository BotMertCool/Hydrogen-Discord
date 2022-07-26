package net.frozenorb.hydrogen.punishment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.frozenorb.hydrogen.connection.RequestHandler;
import net.frozenorb.hydrogen.connection.RequestResponse;
import org.json.JSONObject;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class Punishment {

    private String id;
    private String publicReason;
    private String privateReason;
    private PunishmentType type;
    private UUID addedBy;
    private long addedAt;
    private long expiresAt;
    private String actorName;
    private String actorType;
    private UUID removedBy;
    private long removedAt;
    private String removalReason;

    public boolean isActive() {
        return !this.isExpired() && !this.isRemoved();
    }

    public boolean isExpired() {
        return this.expiresAt != 0L && System.currentTimeMillis() > this.expiresAt;
    }

    public boolean isRemoved() {
        return this.removedAt > 0L;
    }
    
    
    public String resolveAddedBy() {
        if (this.addedBy == null)
            return null;
        
        RequestResponse response = RequestHandler.get("/users/" + this.addedBy);
        if (!response.wasSuccessful())
            return "Internal error";
        
        JSONObject json = response.asJSONObject();
        return json.getString("lastUsername");
    }

    @Getter
    @RequiredArgsConstructor
    public enum PunishmentType {
        BLACKLIST("Blacklist"),
        BAN("Ban"),
        MUTE("Mute"),
        WARN("Warning");

        private final String name;
    }
}
