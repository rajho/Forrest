package com.example.android.forrest.ui.home.progress;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.forrest.R;
import com.example.android.forrest.data.model.Exercise;
import com.example.android.forrest.databinding.ListItemRecentActivitiesBinding;
import com.example.android.forrest.utils.TimeUtils;

import java.util.List;


public class ExercisesAdapter extends RecyclerView.Adapter<ExercisesAdapter.ViewHolder> {
  private List<Exercise> mExercises;

  public ExercisesAdapter(List<Exercise> exercises) {
    mExercises = exercises;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return ViewHolder.from(parent);
  }

  @Override
  public void onBindViewHolder(@NonNull ExercisesAdapter.ViewHolder holder, int position) {
    holder.bind(mExercises.get(position));
  }

  @Override
  public int getItemCount() {
    return mExercises.size();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    ListItemRecentActivitiesBinding mBinding;

    private ViewHolder(
        @NonNull ListItemRecentActivitiesBinding binding) {
      super(binding.getRoot());
      mBinding = binding;
    }

    public static ViewHolder from(ViewGroup parent) {
      ListItemRecentActivitiesBinding binding = ListItemRecentActivitiesBinding
          .inflate(LayoutInflater.from(parent.getContext()), parent, false);
      return new ViewHolder(binding);
    }

    public void bind(Exercise exercise) {
      mBinding.setExercise(exercise);

      String duration = TimeUtils.getFormattedTime(exercise.getDuration(), "%02d:%02d:%02d");
      mBinding.setDuration(duration);
    }
  }
}
