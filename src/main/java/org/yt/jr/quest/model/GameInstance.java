package org.yt.jr.quest.model;

import lombok.Getter;

import java.time.LocalTime;
import java.util.UUID;

@Getter
public class GameInstance {

    final private UUID id = UUID.randomUUID();
    final private LocalTime timestamp = LocalTime.now();

    final String player;
    final private Game game;

    public GameInstance(String player, Game game) {
        this.player = player;
        this.game = game;
    }
}
