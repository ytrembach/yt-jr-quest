package org.yt.jr.quest.model.location;

import lombok.Getter;

import java.util.UUID;

@Getter
public class Location {
    final private UUID uuid;
    final private String name;
    final private String banner;

    public Location(String name, String banner) {
        uuid = UUID.randomUUID();
        this.name = name;
        this.banner = banner;
    }
}
