package org.yt.jr.quest.model.gson;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GameData {
    final private String title;
    final private String intro;
    final private List<NodeData> nodes;
}
