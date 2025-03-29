package com.example.ungdungcoxuongkhop;

public class Message {
    private int id;
    private int idNguoiGui;
    private int idNguoiNhan;
    private String tinNhan;
    private String hinhAnh;
    private String thoiGianGui; // Thêm biến này

    public Message(int id, int idNguoiGui, int idNguoiNhan, String tinNhan, String hinhAnh, String thoiGianGui) {
        this.id = id;
        this.idNguoiGui = idNguoiGui;
        this.idNguoiNhan = idNguoiNhan;
        this.tinNhan = tinNhan;
        this.hinhAnh = hinhAnh;
        this.thoiGianGui = thoiGianGui;
    }


    // Getter và Setter (nếu cần)
    public int getId() { return id; }
    public int getIdNguoiGui() { return idNguoiGui; }
    public int getIdNguoiNhan() { return idNguoiNhan; }
    public String getTinNhan() { return tinNhan; }
    public String getHinhAnh() { return hinhAnh; }
    public String getThoiGianGui() { return thoiGianGui; }
    public String getNoiDung() {
        return tinNhan;
    }


}


