package com.example.ungdungcoxuongkhop;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ViewHolder> {
    private List<Doctor> doctorList;
    private List<Doctor> filteredList;

    public DoctorAdapter(List<Doctor> doctorList) {
        this.doctorList = doctorList;
        this.filteredList = new ArrayList<>(doctorList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, specialty, phone, experience, address;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.txtDoctorName);
            specialty = view.findViewById(R.id.txtDoctorSpecialty);
            phone = view.findViewById(R.id.txtDoctorPhone);
            experience = view.findViewById(R.id.txtDoctorExperience);
            address = view.findViewById(R.id.txtDoctorAddress);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_doctor, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Doctor doctor = filteredList.get(position);

        holder.name.setText(doctor.getName());
        holder.specialty.setText("Chuyên khoa: " + doctor.getSpecialty());
        holder.phone.setText("📞 SĐT: " + doctor.getPhone());
        holder.experience.setText("Kinh nghiệm: " + doctor.getExperience() + " năm");
        holder.address.setText("Địa chỉ: " + doctor.getAddress());

        // Giới hạn số dòng để tránh chữ bị đè lên nhau
        holder.name.setMaxLines(1);
        holder.specialty.setMaxLines(1);
        holder.phone.setMaxLines(1);
        holder.experience.setMaxLines(1);
        holder.address.setMaxLines(1);

        // Cắt chữ nếu quá dài
        holder.name.setEllipsize(android.text.TextUtils.TruncateAt.END);
        holder.specialty.setEllipsize(android.text.TextUtils.TruncateAt.END);
        holder.phone.setEllipsize(android.text.TextUtils.TruncateAt.END);
        holder.experience.setEllipsize(android.text.TextUtils.TruncateAt.END);
        holder.address.setEllipsize(android.text.TextUtils.TruncateAt.END);
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public void filter(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(doctorList);
        } else {
            for (Doctor doctor : doctorList) {
                if (doctor.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(doctor);
                }
            }
        }
        notifyDataSetChanged();
    }
}