package com.example.ungdungcoxuongkhop;

import com.google.gson.annotations.SerializedName;

public class Doctor {
    @SerializedName("id")  // Thêm id của bác sĩ
    private int id;

    @SerializedName("ho_ten")
    private String name;

    @SerializedName("chuyen_khoa")
    private String specialty;

    @SerializedName("so_dien_thoai")
    private String phone;

    @SerializedName("kinh_nghiem")
    private String experience;

    @SerializedName("dia_chi_phong_kham")
    private String address;

    // Constructor
    public Doctor(int id, String name, String specialty, String phone, String experience, String address) {
        this.id = id;
        this.name = name;
        this.specialty = specialty;
        this.phone = phone;
        this.experience = experience;
        this.address = address;
    }

    // Getter Methods
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSpecialty() {
        return specialty;
    }

    public String getPhone() {
        return phone;
    }

    public String getExperience() {
        return experience;
    }

    public String getAddress() {
        return address;
    }

    // Setter Methods
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // toString Method (Chỉ hiển thị tên + chuyên khoa cho Spinner)
    @Override
    public String toString() {
        return name + " - " + specialty;
    }
}
