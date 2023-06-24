package com.example.quizapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionBank {
    private List<Question> questions;
    private int questionIndex;
    private int correctAnswerCount;
    private List<Boolean> userAnswers;
    private int numberOfQuestions;

    public QuestionBank(List<Question> questionList) {
        this.questions = new ArrayList<>(questionList);
        this.questionIndex = 0;
        this.correctAnswerCount = 0;
        this.userAnswers = new ArrayList<>();
        this.numberOfQuestions = questions.size();
    }

    public void addQuestion(Question question) {
        questions.add(question);
    }

    public Question getNextQuestion() {
        if (questionIndex < questions.size()) {
            Question question = questions.get(questionIndex);
            questionIndex++;
            return question;
        } else {
            return null;
        }
    }

    public Question getCurrentQuestion() {
        if (questionIndex >= 0 && questionIndex < questions.size()) {
            return questions.get(questionIndex);
        }
        return null;
    }

    public void shuffleQuestions() {
        Collections.shuffle(questions);
        questionIndex = 0;
    }

    public  int getQuestionCount() {
        return numberOfQuestions;
    }

    public  int getQuestionIndex() {
        return questionIndex;
    }

    public void incrementCorrectAnswerCount() {
        correctAnswerCount++;
    }

    public int getCorrectAnswerCount() {
        return correctAnswerCount;
    }
    public void addUserAnswer(boolean isCorrectAnswer) {
        userAnswers.add(isCorrectAnswer);
        if (isCorrectAnswer) {
            incrementCorrectAnswerCount();
        }
    }

    public boolean getUserAnswer(int questionIndex) {
        if (questionIndex >= 0 && questionIndex < userAnswers.size()) {
            return userAnswers.get(questionIndex);
        }
        return false;
    }

    public void resetUserAnswers() {
        questionIndex = 0;
        correctAnswerCount = 0;
        userAnswers.clear();
    }

    public void clearQuestions() {
        questions.clear();
        questionIndex = 0;
        correctAnswerCount = 0;
        userAnswers.clear();
    }
    public void setNumberOfQuestions(int numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }
}

