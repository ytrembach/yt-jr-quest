package org.yt.jr.quest.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class Question {
    final private String question;
    final private List<Answer> answers;
}
