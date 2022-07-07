package com.example.eventplannerapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.ViewHolder> {

    Iterator next;
    HashMap<String, HashMap<String, String>> events;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView title;
        private final TextView desc;
        private final TextView date;

        public ViewHolder(View view){
            super(view);
            this.title = view.findViewById(R.id.Title);
            this.desc = view.findViewById(R.id.Desc);
            this.date = view.findViewById(R.id.Date);
        }
    }

    @NonNull
    @Override
    public ViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewAdapter.ViewHolder holder, int position) {
        Map.Entry<String, HashMap<String, String>> event = (Map.Entry<String, HashMap<String, String>>) next.next();
        holder.title.setText(event.getValue().get("title"));
        holder.desc.setText(event.getValue().get("description"));
        holder.date.setText(event.getValue().get("date"));
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}
