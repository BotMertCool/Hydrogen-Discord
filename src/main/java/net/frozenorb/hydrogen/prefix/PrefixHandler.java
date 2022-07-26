package net.frozenorb.hydrogen.prefix;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import net.frozenorb.hydrogen.Hydrogen;
import net.frozenorb.hydrogen.connection.RequestHandler;
import net.frozenorb.hydrogen.connection.RequestResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Getter @Setter
public class PrefixHandler {

    private List<Prefix> prefixes = Lists.newArrayList();
    private Map<String, Prefix> prefixCache;

    public Optional<Prefix> getPrefix(String parse) {
        return Optional.ofNullable(this.prefixCache.get(parse));
    }

    public List<Prefix> getPrefixes() {
        return new ArrayList<>(this.prefixes);
    }

}