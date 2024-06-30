package org.yt.jr.quest.model.gson;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.yt.jr.quest.model.Node;

import java.util.List;

@Getter
@AllArgsConstructor
public class NodeData {
    final private String name;
    final private String banner;
    @SerializedName("isfinal")
    final private boolean isFinal;
    final private String question;
    final private List<AnswerData> answers;
    @Setter
    Node nodeBuilt;
}
