package com.example.lmslite;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
    private List<Course> courses;
    private boolean isEnrolled;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Course course, int position);
    }

    public CourseAdapter(List<Course> courses, boolean isEnrolled) {
        this.courses = courses;
        this.isEnrolled = isEnrolled;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_course, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courses.get(position);
        holder.bind(course);
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    class CourseViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivCourseImage;
        private TextView tvCourseTitle;
        private TextView tvCourseDescription;
        private TextView tvModuleCount;
        private TextView tvDuration;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCourseImage = itemView.findViewById(R.id.ivCourseImage);
            tvCourseTitle = itemView.findViewById(R.id.tvCourseTitle);
            tvCourseDescription = itemView.findViewById(R.id.tvCourseDescription);
            tvModuleCount = itemView.findViewById(R.id.tvModuleCount);
            tvDuration = itemView.findViewById(R.id.tvDuration);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(courses.get(position), position);
                }
            });
        }

        public void bind(Course course) {
            tvCourseTitle.setText(course.getTitle());
            tvCourseDescription.setText(course.getDescription());
            tvModuleCount.setText(course.getModules().size() + " Modules");
            tvDuration.setText(course.getDuration() + " hours");

            // Load course image using Glide
            if (course.getImageUrl() != null && !course.getImageUrl().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(course.getImageUrl())
                        .placeholder(R.drawable.placeholder_course)
                        .error(R.drawable.placeholder_course)
                        .into(ivCourseImage);
            } else {
                ivCourseImage.setImageResource(R.drawable.placeholder_course);
            }
        }
    }
} 