package com.example.sapbusinessuser.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sapbusinessuser.R;
import com.example.sapbusinessuser.model.Question;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class QuizManage_Adapter extends RecyclerView.Adapter<QuizManage_Adapter.ViewHolder> {
    private Context context;
    private ArrayList<Question> questions;
    private DatabaseReference quizItemRef;
    private ProgressDialog progressDialog;
    public QuizManage_Adapter(Context context){
        this.context = context;
        progressDialog = new ProgressDialog(context);
        questions = new ArrayList<>();


    }
    public ArrayList<Question> getData(){

        return this.questions;
    }
    public void SetData(ArrayList<Question> questions){
        this.questions = questions;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.itemview_quizmanage,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Question question = questions.get(position);
        holder.tv_question.setText(position+1+".) "+question.getQuestion());
        holder.choiceA.setText("A.)\t"+question.getChoiceA());
        holder.choiceB.setText("B.)\t"+question.getChoiceB());
        holder.choiceC.setText("C.)\t"+question.getChoiceC());
        holder.choiceD.setText("D.)\t"+question.getChoiceD());

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


    @Override
    public int getItemCount() {
        return questions!=null?questions.size():0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView choiceA,choiceB,choiceC,choiceD;
        public TextView tv_question;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_question = itemView.findViewById(R.id.mult_Question);
            choiceA = itemView.findViewById(R.id.choiceA);
            choiceB = itemView.findViewById(R.id.choiceB);
            choiceC = itemView.findViewById(R.id.choiceC);
            choiceD = itemView.findViewById(R.id.choiceD);

        }
    }

}
