package com.example.ungdungcoxuongkhop;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import com.example.ungdungcoxuongkhop.model.FutureFeature;

public class FutureFeatureAdapter extends RecyclerView.Adapter<FutureFeatureAdapter.ViewHolder> {
    private List<FutureFeature> featureList;

    public FutureFeatureAdapter(List<FutureFeature> featureList) {
        this.featureList = featureList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_future_feature, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FutureFeature feature = featureList.get(position);
        holder.titleTextView.setText(feature.getTitle());
        holder.descriptionTextView.setText(feature.getDescription());
    }

    @Override
    public int getItemCount() {
        return featureList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView descriptionTextView;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardFeature);
            titleTextView = itemView.findViewById(R.id.textFeatureTitle);
            descriptionTextView = itemView.findViewById(R.id.textFeatureDescription);

            // Tăng kích thước chữ
            titleTextView.setTextSize(18);
            descriptionTextView.setTextSize(16);

            // Căn giữa nội dung
            titleTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            descriptionTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }
    }
}
