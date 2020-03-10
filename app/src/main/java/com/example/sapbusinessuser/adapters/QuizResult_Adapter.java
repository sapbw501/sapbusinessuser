package com.example.sapbusinessuser.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sapbusinessuser.R;
import com.example.sapbusinessuser.model.Question;

import java.util.ArrayList;

public class QuizResult_Adapter extends RecyclerView.Adapter<QuizResult_Adapter.ViewHolder> {
    private Context context;
    private ArrayList<Question> questions;
    public QuizResult_Adapter(Context context){
        this.context = context;

    }
    public void SetData(ArrayList<Question> questions){
        this.questions=questions;
        notifyDataSetChanged();

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.itemview_quizresult,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Question question = questions.get(position);
        int temp = position+1;
        if(!question.getAnswer().equals(question.getUser_answer())){
            holder.cardView.setBackgroundColor(Color.parseColor("#ed6868"));
            holder.explanation.setVisibility(View.VISIBLE);
            if(question.getExplanation()!=null){
                holder.explanation.setText("Tip :\t"+question.getExplanation());
            }
        }else{
            holder.explanation.setVisibility(View.GONE);
            holder.cardView.setBackgroundColor(Color.parseColor("#99CCFF"));

        }
        holder.question.setText(temp+" .) "+question.getQuestion());
        holder.answer.setText("Correct Answer : "+question.getAnswer());

    }

    @Override
    public int getItemCount() {
        return questions!=null?10:0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public CardView cardView;
        public TextView answer,question,explanation;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            answer = itemView.findViewById(R.id.answer);
            question = itemView.findViewById(R.id.mult_Question);
            cardView = itemView.findViewById(R.id.card);
            explanation = itemView.findViewById(R.id.explanation);
        }
    }
}
