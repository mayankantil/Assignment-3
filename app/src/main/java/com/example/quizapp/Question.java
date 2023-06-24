package com.example.quizapp;

import java.io.Serializable;

public class Question implements Serializable {
    private String questionText;
    private boolean answer;
    private int backgroundColor;

    public Question(String questionText, boolean answer, int backgroundColor) {
        this.questionText = questionText;
        this.answer = answer;
        this.backgroundColor = backgroundColor;
    }

    public String getQuestionText() {
        return questionText;
    }

    public boolean isCorrectAnswer() {
        return answer;
    }



    public boolean getAnswer() {
        return answer;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }
}
