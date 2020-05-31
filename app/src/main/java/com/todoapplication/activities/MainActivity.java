package com.todoapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.todoapplication.fragments.DialogFragment;
import com.todoapplication.models.TaskBlock;
import com.todoapplication.utils.DatabaseHelper;
import com.todoapplication.adapters.TaskAdapter;
import com.todoapplication.R;
import android.app.Activity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {

        DatabaseHelper toDoDB;
        ImageButton taskAddButton, checkButton, undoButton;
        ArrayList<TaskBlock> taskList = new ArrayList<>();
        private RecyclerView mRecyclerView;
        private TaskAdapter mAdapter;
        private RecyclerView.LayoutManager mLayoutManager;
        SearchView searchView;
        TextView title;

    public static void setStatusBarGradiant(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            Drawable background = activity.getResources().getDrawable(R.drawable.toolbar);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setNavigationBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }
    }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setStatusBarGradiant(this);
            setContentView(R.layout.activity_main);

            toDoDB = new DatabaseHelper(this);
            taskAddButton = (ImageButton) findViewById(R.id.taskAddButton);
            checkButton = (ImageButton) findViewById(R.id.checkButton);
            undoButton = (ImageButton) findViewById(R.id.undoButton);
            title = (TextView) findViewById(R.id.title);
            searchView = (SearchView) findViewById(R.id.search);
            searchView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    taskAddButton.setVisibility(View.GONE);
                    title.setVisibility(View.GONE);
                }
            });
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    mAdapter.getFilter().filter(newText);
                    return false;
                }
            });

            createTaskList();
            buildRecyclerView();

            taskAddButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDialog();
                }
            });
        }

    public void openDialog(){
            DialogFragment dialogFragment = new DialogFragment();
            dialogFragment.show(getSupportFragmentManager(), "custom dialog");
        }



    public void createTaskList() {
        Cursor data = toDoDB.getListContents();
        if(data.getCount() == 0){
            Toast.makeText(this, "There are no contents in this list!",Toast.LENGTH_LONG).show();
        }else{
            while(data.moveToNext()){
                String task = data.getString(1);
                boolean status = data.getInt(2)==1 ? true:false;
                TaskBlock taskBlock = new TaskBlock(task, status);
                taskList.add(taskBlock);
            }
        }
    }

    public void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new TaskAdapter(taskList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
        mAdapter.setOnItemClickListener(new TaskAdapter.OnItemClickListener() {
            @Override
            public void onStatusChange(int position) {
                boolean result = toDoDB.update(taskList.get(position), true);
                if(result==true){
                    taskList.get(position).setStatus(true);
                    mAdapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "updated",Toast.LENGTH_LONG).show();
                }
            }

            public void onUndo(int position){
                boolean result = toDoDB.update(taskList.get(position), false);
                if(result==true){
                    taskList.get(position).setStatus(false);
                    mAdapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "updated",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to Exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            boolean result = toDoDB.delete(taskList.get(viewHolder.getAdapterPosition()));
            if(result==true) {
                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Deleted")
                        .setConfirmText("Ok")
                        .setConfirmButtonBackgroundColor(MainActivity.this.getResources().getColor(R.color.colorPrimaryDark))
                        .show();
                taskList.remove(viewHolder.getAdapterPosition());
                mAdapter.notifyDataSetChanged();
            }
        }
    };
}
