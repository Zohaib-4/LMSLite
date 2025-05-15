package com.example.lmslite;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ModuleAdapter extends RecyclerView.Adapter<ModuleAdapter.ModuleViewHolder> {

    private List<Module> modules;

    public ModuleAdapter(List<Module> modules) {
        this.modules = modules;
    }

    @NonNull
    @Override
    public ModuleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_module, parent, false);
        return new ModuleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ModuleViewHolder holder, int position) {
        Module module = modules.get(position);
        holder.tvModuleTitle.setText(module.getTitle());
        holder.tvModuleDescription.setText(module.getDescription());

        // Count videos and quizzes
        int videoCount = 0;
        int quizCount = 0;
        for (ModuleContent content : module.getContent()) {
            if (content.getType() == ModuleContent.TYPE_VIDEO) {
                videoCount++;
            } else if (content.getType() == ModuleContent.TYPE_QUIZ) {
                quizCount++;
            }
        }

        // Display content count
        StringBuilder contentText = new StringBuilder();
        if (videoCount > 0) {
            contentText.append(videoCount).append(" Video").append(videoCount > 1 ? "s" : "");
        }
        if (quizCount > 0) {
            if (contentText.length() > 0) {
                contentText.append(", ");
            }
            contentText.append(quizCount).append(" Quiz").append(quizCount > 1 ? "zes" : "");
        }
        holder.tvContentCount.setText(contentText.toString());
    }

    @Override
    public int getItemCount() {
        return modules.size();
    }

    static class ModuleViewHolder extends RecyclerView.ViewHolder {
        TextView tvModuleTitle, tvModuleDescription, tvContentCount;

        ModuleViewHolder(View itemView) {
            super(itemView);
            tvModuleTitle = itemView.findViewById(R.id.tvModuleTitle);
            tvModuleDescription = itemView.findViewById(R.id.tvModuleDescription);
            tvContentCount = itemView.findViewById(R.id.tvContentCount);
        }
    }
} 