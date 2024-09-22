package org.yt.jr.quest.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class Answer {
    final private String answer;

    final private String message;

    @Setter
    @ToString.Exclude
    private Node nextNode;

}