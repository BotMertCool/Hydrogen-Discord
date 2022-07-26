package net.frozenorb.hydrogen.server;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;

@Getter
@RequiredArgsConstructor
public class ServerGroup {
    
    private final String id;
    private String image;
    private List<String> announcements;
    
}
