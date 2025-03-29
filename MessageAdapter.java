package com.example.ungdungcoxuongkhop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private Context context;
    private List<Message> messageList;
    private int currentUserId;

    public MessageAdapter(Context context, List<Message> messageList, int currentUserId) {
        this.context = context;
        this.messageList = messageList;
        this.currentUserId = currentUserId;
    }

    @Override
    public int getItemViewType(int position) {
        return (messageList.get(position).getIdNguoiGui() == currentUserId) ? 1 : 0;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1) {
            view = LayoutInflater.from(context).inflate(R.layout.item_message_sent, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_message_received, parent, false);
        }
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messageList.get(position);

        if (holder.txtMessage != null) {
            holder.txtMessage.setText(message.getTinNhan());
        }

        if (holder.txtTime != null) {
            holder.txtTime.setText(message.getThoiGianGui());
        }

        if (holder.imgMessage != null) {
            if (message.getHinhAnh() != null && !message.getHinhAnh().isEmpty()) {
                holder.imgMessage.setVisibility(View.VISIBLE);
                Glide.with(context).load(message.getHinhAnh()).into(holder.imgMessage);
            } else {
                holder.imgMessage.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView txtMessage, txtTime;
        ImageView imgMessage;

        public MessageViewHolder(View itemView) {
            super(itemView);
            txtMessage = itemView.findViewById(R.id.txtMessage);
            txtTime = itemView.findViewById(R.id.txtTime);
            imgMessage = itemView.findViewById(R.id.imgMessage);
        }
    }
}

