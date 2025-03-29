package com.example.ungdungcoxuongkhop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class FunctionAdapter extends RecyclerView.Adapter<FunctionAdapter.ViewHolder> {

    private final Context context;
    private final List<FunctionItem> functionList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public FunctionAdapter(Context context, List<FunctionItem> functionList) {
        this.context = context;
        this.functionList = functionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.function_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FunctionItem function = functionList.get(position);
        holder.functionTitle.setText(function.getTitle());
        holder.functionIcon.setImageResource(function.getIconResId());

        // Hiệu ứng nhấn vào item
        holder.cardView.setOnClickListener(v -> {
            setFadeAnimation(holder.cardView);
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return functionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView functionIcon;
        TextView functionTitle;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            functionIcon = itemView.findViewById(R.id.function_icon);
            functionTitle = itemView.findViewById(R.id.function_title);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }

    // Hiệu ứng fade khi click vào item
    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.3f, 1.0f);
        anim.setDuration(300);
        view.startAnimation(anim);
    }
}
