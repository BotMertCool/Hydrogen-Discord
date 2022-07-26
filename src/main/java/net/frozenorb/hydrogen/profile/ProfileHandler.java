package net.frozenorb.hydrogen.profile;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import lombok.Setter;
import net.frozenorb.hydrogen.Hydrogen;
import net.frozenorb.hydrogen.connection.RequestHandler;
import net.frozenorb.hydrogen.connection.RequestResponse;

import java.util.*;

@Getter @Setter
public class ProfileHandler {

    private Set<UUID> totpEnabled = new HashSet<>();
    private final Map<UUID, Profile> profiles = Maps.newConcurrentMap();

    public Optional<Profile> getProfile(UUID player) {
        return Optional.ofNullable(this.profiles.get(player));
    }
    

}