package org.androidtown.miraero;

public class QnA {
    private String type;
    private String date;
    private String qwriter;
    private String question;
    private int what_open;
    private int what_reply;

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setQwriter(String qwriter) {
        this.qwriter = qwriter;
    }

    public String getQwriter() {
        return qwriter;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public void setWhat_open(int what_open) {
        this.what_open = what_open;
    }

    public int getWhat_open() {
        return what_open;
    }

    public void setWhat_reply(int what_reply) {
        this.what_reply = what_reply;
    }

    public int getWhat_reply() {
        return what_reply;
    }
}
