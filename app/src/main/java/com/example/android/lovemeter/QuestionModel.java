package com.example.android.lovemeter;

public class QuestionModel {
    private String answer;
    private String question;

    public QuestionModel() {
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public QuestionModel(String question, String answer) {
        this.answer = answer;
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
