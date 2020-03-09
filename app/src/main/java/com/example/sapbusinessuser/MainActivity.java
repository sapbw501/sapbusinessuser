package com.example.sapbusinessuser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sapbusinessuser.model.AppPreference;
import com.example.sapbusinessuser.quiz_module.QuizPeriodActivity;
import com.example.sapbusinessuser.simulation.Simulation;
import com.example.sapbusinessuser.video_module.VideosPeriodActivity;

public class MainActivity extends AppCompatActivity {
    private Button btn_topics;
    private Button btn_videos;
    private Button btn_quizzes;
    private Button btn_simulation;
    private Toolbar toolbar;
    private AppPreference appPreference;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        appPreference = new AppPreference(this);
        if(getSupportActionBar()==null){
            setSupportActionBar(toolbar);
            toolbar.setTitleTextColor(R.color.slategray);

        }
        btn_topics  = findViewById(R.id.topics);
        btn_videos = findViewById(R.id.videos);
        btn_quizzes = findViewById(R.id.quizzes);
        btn_simulation= findViewById(R.id.simulation);
        btn_topics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this,PeriodActivity.class));

            }
        });
        btn_videos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, VideosPeriodActivity.class));

            }
        });
        btn_quizzes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, QuizPeriodActivity.class));

            }
        });
        btn_simulation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Simulation.class));


            }
        });

    }

}
