package net.frozenorb.hydrogen.rank;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import net.frozenorb.hydrogen.RefreshTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.concurrent.CompletableFuture;

@Getter @Setter
public class RankHandler {

    private List<Rank> ranks = Lists.newArrayList();

    public RankHandler() {
        CompletableFuture.runAsync(() -> new Timer().scheduleAtFixedRate(new RefreshTask(), 5000L, 3000L));
    }

    public Optional<Rank> getRank(String parse) {
        for (Rank rank : this.ranks) {
            if (rank.getId().equalsIgnoreCase(parse))
                return Optional.of(rank);

            if (rank.getDisplayName().equalsIgnoreCase(parse))
                return Optional.of(rank);
        }

        return Optional.empty();
    }

    public List<Rank> getRanks() {
        return new ArrayList<>(this.ranks);
    }

}