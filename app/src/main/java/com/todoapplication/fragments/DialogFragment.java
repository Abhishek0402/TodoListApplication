package com.todoapplication.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import com.todoapplication.R;
import com.todoapplication.activities.MainActivity;
import com.todoapplication.models.TaskBlock;
import com.todoapplication.utils.DatabaseHelper;

public class DialogFragment extends AppCompatDialogFragment {

    private EditText addNewTask;
    DatabaseHelper toDoDB;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_dialog, null);

        toDoDB = new DatabaseHelper(getActivity());
        addNewTask = view.findViewById(R.id.addNewTask);

        builder.setView(view).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        })
                .setPositiveButton("Add Task", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newTask = addNewTask.getText().toString();
                        TaskBlock taskBlock = new TaskBlock(newTask, false);
                        if(addNewTask.length()!= 0){
                            AddData(taskBlock);
                            addNewTask.setText("");
                        }else{
                            Toast.makeText(getActivity(), "You must put something in the text field!", Toast.LENGTH_LONG).show();
                        }
                    }
                });

        return builder.create();
    }

    public void AddData(TaskBlock taskBlock) {

        boolean insertData = toDoDB.addData(taskBlock);

        if(insertData==true){
            Toast.makeText(getActivity(), "Data Successfully Inserted!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else{
            Toast.makeText(getActivity(), "Something went wrong :(.", Toast.LENGTH_LONG).show();
        }
    }
}
