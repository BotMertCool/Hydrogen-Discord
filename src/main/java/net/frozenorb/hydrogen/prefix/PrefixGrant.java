package net.frozenorb.hydrogen.prefix;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.frozenorb.hydrogen.connection.RequestHandler;
import net.frozenorb.hydrogen.connection.RequestResponse;
import org.json.JSONObject;

import java.util.Set;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class PrefixGrant {

    private String id;
    private UUID user;
    private String reason;
    private Set<String> scopes;
    private String prefix;
    private long expiresAt;
    private UUID addedBy;
    private long addedAt;
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

}