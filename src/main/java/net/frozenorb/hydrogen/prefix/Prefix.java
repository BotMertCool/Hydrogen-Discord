package net.frozenorb.hydrogen.prefix;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Prefix {

    private String id;
    private String displayName;
    private String prefix;
    private boolean purchaseable;
    private String buttonName;
    private String buttonDescription;

    public String getFormattedPrefix() {
        return this.prefix.replace('§', '&');
    }

}