package net.frozenorb.hydrogen.rank;

import com.google.common.primitives.Ints;
import lombok.Data;
import net.frozenorb.hydrogen.Hydrogen;

import java.util.Comparator;

@Data
public class Rank {

    private String id;
    private String inheritsFromId;
    private int generalWeight;
    private int displayWeight;
    private String displayName;
    private String gamePrefix;
    private String gameColor;
    private boolean staffRank;
    private boolean grantRequiresTotp;
    private String queueMessage;

    public static Comparator<Rank> GENERAL_WEIGHT_COMPARATOR = ((a, b) -> Ints.compare(b.getGeneralWeight(), a.getGeneralWeight()));
    public static Comparator<Rank> DISPLAY_WEIGHT_COMPARATOR = ((a, b) -> Ints.compare(b.getDisplayWeight(), a.getDisplayWeight()));

    @Override
    public String toString() {
        return Hydrogen.GSON.toJson(this);
    }
    
    public String getGameColor() {
        return this.gameColor.replace('§', '&');
    }
    
    public String getGamePrefix() {
        return this.gamePrefix.replace('§', '&');
    }
    
    public String getFormattedName() {
        return gameColor + displayName;
    }
    
    
}