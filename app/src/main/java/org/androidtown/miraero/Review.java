package org.androidtown.miraero;

public class Review {
    private int grade;
    private String rcontent;
    private String writer;

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getGrade() {
        return grade;
    }

    public void setRcontent(String rcontent) {
        this.rcontent = rcontent;
    }

    public String getRcontent() {
        return rcontent;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getWriter() {
        return writer;
    }
}
