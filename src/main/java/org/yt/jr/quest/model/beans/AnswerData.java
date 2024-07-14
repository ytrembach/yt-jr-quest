package org.yt.jr.quest.model.beans;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.yt.jr.quest.model.Answer;

@Getter
@AllArgsConstructor
public class AnswerData {
    final private String answer;
    final private String next;
    final private String message;
    @Setter
    Answer answerBuilt;
}
