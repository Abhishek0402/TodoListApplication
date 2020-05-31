package com.todoapplication.adapters;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.todoapplication.R;
import com.todoapplication.models.TaskBlock;
import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

        private ArrayList<TaskBlock> taskList;
        private ArrayList<TaskBlock> searchTaskList;
        private OnItemClickListener mListener;

        public interface OnItemClickListener {
            void onStatusChange(int position);
            void onUndo(int position);
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            mListener = listener;
        }

        public static class TaskViewHolder extends RecyclerView.ViewHolder {
            public TextView task;
            public ImageView status;
            public ImageView undo;
            public TaskViewHolder(View itemView, final OnItemClickListener listener) {
                super(itemView);
                task = itemView.findViewById(R.id.task);
                status = itemView.findViewById(R.id.checkButton);
                undo = itemView.findViewById(R.id.undoButton);

                status.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                listener.onStatusChange(position);
                            }
                        }
                    }
                });
                undo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION){
                                listener.onUndo(position);
                            }
                        }
                    }
                });
            }
        }

        public TaskAdapter(ArrayList<TaskBlock> taskList) {
            this.taskList = taskList;
            searchTaskList = new ArrayList<>(taskList);
        }

        @Override
        public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tasks_list, parent, false);
            TaskViewHolder taskViewHolder = new TaskViewHolder(view, mListener);
            return taskViewHolder;
        }

        @Override
        public void onBindViewHolder(TaskViewHolder holder, int position) {
            TaskBlock currentItem = taskList.get(position);
            holder.task.setText(currentItem.getTask());

            if(currentItem.getStatus()==true){
                holder.task.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                holder.undo.setVisibility(View.VISIBLE);
                holder.status.setVisibility(View.GONE);
            }else{
                holder.task.setPaintFlags(View.INVISIBLE);
                holder.status.setVisibility(View.VISIBLE);
                holder.undo.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return taskList.size();
        }

    public Filter getFilter(){
        return menuFilter;
    }
    private Filter menuFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<TaskBlock> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(searchTaskList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (TaskBlock item : searchTaskList) {
                    if (item.getTask().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            taskList.clear();
            taskList.addAll((ArrayList)results.values);
            notifyDataSetChanged();
        }
    };
}
