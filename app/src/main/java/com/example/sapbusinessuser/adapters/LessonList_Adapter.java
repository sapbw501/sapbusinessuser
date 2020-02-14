package com.example.sapbusinessuser.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sapbusinessuser.R;
import com.example.sapbusinessuser.lesson_module.lesson_view.LessonView;
import com.example.sapbusinessuser.model.Lesson;

import java.util.ArrayList;

public class LessonList_Adapter  extends RecyclerView.Adapter<LessonList_Adapter.ViewHolder> {
    private Context context;
    private ArrayList<Lesson> lessons;
    private ProgressDialog progressDialog;
    public LessonList_Adapter(Context context){
        progressDialog = new ProgressDialog(context);
        this.context = context;

    }
    public void SetData(ArrayList<Lesson> lessons){
        this.lessons = lessons;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemview_lessonlist,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Lesson lesson = lessons.get(position);
        holder.tv_lesson_description.setText(lesson.getDescription());
        holder.tv_lesson_name.setText(lesson.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LessonView.lesson = lesson;
                context.startActivity(new Intent(context, LessonView.class));

            }
        });
    }

    @Override
    public int getItemCount() {
        return lessons!=null?lessons.size():0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_lesson_description;
        public TextView tv_lesson_name;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_lesson_description = itemView.findViewById(R.id.tv_lesson_description);
            tv_lesson_name= itemView.findViewById(R.id.tv_lesson_name);
        }
    }
    private void showMessage(String title,String message){
        new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
}

