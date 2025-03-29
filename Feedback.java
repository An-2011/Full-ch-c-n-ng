package com.example.ungdungcoxuongkhop;

public class Feedback {
    private int id;
    private int userId; // 🆕 Thêm userId
    private int doctorId;
    private int score;
    private String comment;
    private String date;

    public Feedback(int id, int userId, int doctorId, int score, String comment, String date) {
        this.id = id;
        this.userId = userId; // 🆕
        this.doctorId = doctorId;
        this.score = score;
        this.comment = comment;
        this.date = date;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; } // 🆕
    public int getDoctorId() { return doctorId; }
    public int getScore() { return score; }
    public String getComment() { return comment; }
    public String getDate() { return date; }
}
