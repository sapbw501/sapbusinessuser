package com.example.sapbusinessuser.quiz_module.quiz_result;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sapbusinessuser.R;
import com.example.sapbusinessuser.adapters.QuizResult_Adapter;
import com.example.sapbusinessuser.model.Question;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Quiz_Result extends AppCompatActivity {
    public static ArrayList<Question> questioAnswers;
    private QuizResult_Adapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar layout_loading;
    private Toolbar toolbar;
    private FloatingActionButton btn_add;
    private TextView tv_score;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        tv_score = findViewById(R.id.score);
        recyclerView = findViewById(R.id.recycleview);
        toolbar = findViewById(R.id.toolbar);
        adapter = new QuizResult_Adapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
        recyclerView.setAdapter(adapter);
        adapter.SetData(questioAnswers);
        if(getSupportActionBar()==null){
            setSupportActionBar(toolbar);
            toolbar.setTitleTextColor(Color.WHITE);
            getSupportActionBar().setTitle("Quiz Result");
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        int totalScore = 0;
        for(int i = 0 ; i < 15;i++){
            Question question  = questioAnswers.get(i);
            if(question.getAnswer().equals(question.getUser_answer())){
                totalScore++;
            }
        }
        tv_score.setText("Total Score : "+totalScore+"/"+15);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
