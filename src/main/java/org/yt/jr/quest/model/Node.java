package org.yt.jr.quest.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString
public class Node {
    final private UUID id;

    final private String name;

    final private String banner;

    final private boolean isFinal;

    @Setter
    private Question question;

    public Node(String name, String banner, boolean isFinal) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.banner = banner;
        this.isFinal = isFinal;
    }
}
