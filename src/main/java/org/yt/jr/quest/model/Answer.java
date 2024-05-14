package org.yt.jr.quest.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.yt.jr.quest.model.location.Location;

@Getter
@AllArgsConstructor
public class Answer {
    final private String answer;
    final private Location nextLocation;
}
