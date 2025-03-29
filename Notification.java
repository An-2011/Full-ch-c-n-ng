package com.example.ungdungcoxuongkhop;

public class Notification {
    private int id;
    private String tieuDe;
    private String noiDung;
    private String ngayTao;
    private String trangThai;

    // 🔹 Constructor mới (chấp nhận 5 tham số)
    public Notification(int id, String tieuDe, String noiDung, String ngayTao, String trangThai) {
        this.id = id;
        this.tieuDe = tieuDe;
        this.noiDung = noiDung;
        this.ngayTao = ngayTao;
        this.trangThai = trangThai;
    }

    // Getter
    public int getId() {
        return id;
    }

    public String getTieuDe() {
        return tieuDe;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public String getNgayTao() {
        return ngayTao;
    }

    public String getTrangThai() {
        return trangThai;
    }
}
