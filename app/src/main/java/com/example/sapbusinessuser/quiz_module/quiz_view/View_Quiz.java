package com.example.sapbusinessuser.quiz_module.quiz_view;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.sapbusinessuser.R;
import com.example.sapbusinessuser.model.AppPreference;
import com.example.sapbusinessuser.model.Question;
import com.example.sapbusinessuser.model.ScoreRealmManager;
import com.example.sapbusinessuser.model.Scores;
import com.example.sapbusinessuser.model.Topic;
import com.example.sapbusinessuser.quiz_module.quiz_result.Quiz_Result;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.Lists;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class View_Quiz extends AppCompatActivity {
    private Button btn_choiceA,btn_choiceB,btn_choiceC,btn_choiceD;
    private LinearLayout question_multiple,question_trueorfalse,question_identification;
    private Button btn_next,btn_true,btn_false;
    private EditText identification_answer;
    private Toolbar toolbar;
    private TextView tv_question;
    private ArrayList<Question> questions;

    public static Topic topic;
    @Override
    protected void onPause() {
        super.onPause();
        fb_quizRef.removeEventListener(valueEventListener);
    }
    private ProgressDialog progressDialog;


    private void initUI(){
        question_multiple = findViewById(R.id.question_multiple);
        question_trueorfalse = findViewById(R.id.question_trueorfalse);
        question_identification = findViewById(R.id.question_identification);
        tv_question = findViewById(R.id.mult_Question);
        btn_next= findViewById(R.id.btn_next);
        identification_answer = findViewById(R.id.identification_answer);
        btn_choiceA = findViewById(R.id.choiceA);
        btn_choiceB = findViewById(R.id.choiceB);
        btn_choiceC = findViewById(R.id.choiceC);
        btn_choiceD = findViewById(R.id.choiceD);
        btn_true = findViewById(R.id.btn_true);
        btn_false= findViewById(R.id.btn_false);
        toolbar = findViewById(R.id.toolbar);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Preparing quiz..");
        questions = new ArrayList<>();

    }
    private DatabaseReference fb_quizRef;
    private ValueEventListener valueEventListener;

    private void initFirebase(){
        fb_quizRef = FirebaseDatabase
                .getInstance()
                .getReference().child("Topics")
                .child(topic.getKey())
                .child("Quiz");
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_takequiz);
        initUI();
        initFirebase();

        if(getSupportActionBar()==null){
            setSupportActionBar(toolbar);
            toolbar.setTitleTextColor(Color.WHITE);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }

        question_multiple.setVisibility(View.GONE);
        question_trueorfalse.setVisibility(View.GONE);
        question_identification.setVisibility(View.GONE);

        tv_question.setVisibility(View.GONE);
        btn_choiceA.setVisibility(View.GONE);
        btn_choiceB.setVisibility(View.GONE);
        btn_choiceC.setVisibility(View.GONE);
        btn_choiceD.setVisibility(View.GONE);
        btn_next.setVisibility(View.GONE);

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


    @Override
    protected void onResume() {
        super.onResume();
        progressDialog.show();
        valueEventListener = fb_quizRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                questions.clear();
                for (DataSnapshot questionData : dataSnapshot.getChildren()) {
                    Question question = questionData.getValue(Question.class);
                    question.setKey(questionData.getKey());
                    questions.add(question);

                }
                if(questions.size() == 0) {
                    progressDialog.dismiss();
                    fb_quizRef.removeEventListener(valueEventListener);
                    showMessage("Failed", "No Question here");

                }else {
                    progressDialog.dismiss();
                    fb_quizRef.removeEventListener(valueEventListener);


                    tv_question.setVisibility(View.VISIBLE);
                    btn_choiceA.setVisibility(View.VISIBLE);
                    btn_choiceB.setVisibility(View.VISIBLE);
                    btn_choiceC.setVisibility(View.VISIBLE);
                    btn_choiceD.setVisibility(View.VISIBLE);
                    Collections.shuffle(questions);


                    SetQuestion();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                showMessage("Failed",databaseError.getMessage());

            }
        });

    }
    private void SaveScore(){
        progressDialog.setMessage("Checking Answers..");
        progressDialog.show();
        DatabaseReference progressRef = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Progress");
        String key = progressRef.push().getKey();
        DatabaseReference progRef = progressRef.child(key);
        progRef.child("student").setValue(new AppPreference(View_Quiz.this).getId());
        progRef.child("topic").setValue(topic.getKey());
        progRef .child("score").setValue(score)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            Quiz_Result.questioAnswers = questions;
                            startActivity(new Intent(View_Quiz.this, Quiz_Result.class));
                            finish();

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                new AlertDialog.Builder(View_Quiz.this)
                        .setTitle("Failed")
                        .setMessage("Retry again")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SaveScore();
                            }
                        }).show();
            }
        });

    }
    @SuppressLint("SimpleDateFormat")
    public void generateEventID() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        id = simpleDateFormat.format(calendar.getTime());
    }
    int questionNumber = 0;
    int score = 0;
    private String id;
    static int numTries;
    private Question quest;
    private void SetQuestion(){

        if(questionNumber>=10){
            btn_choiceA.setEnabled(false);
            btn_choiceB.setEnabled(false);
            btn_choiceC.setEnabled(false);
            btn_choiceD.setEnabled(false);

            //  SaveScore();
            generateEventID();
            ScoreRealmManager scoreRealmManager = new ScoreRealmManager(View_Quiz.this);
            List<Scores> scores = Lists.newArrayList(scoreRealmManager.realmResults());
            if(scores.isEmpty()){
                numTries = 1;

            }else {
                numTries = scores.size() + 1;
            }
            Scores scoreObject = new Scores();
            scoreObject.setId(id);
            scoreObject.setTopic_id(topic.getKey());
            scoreObject.setScore((int) score);
            scoreObject.setTrial(numTries);
            scoreRealmManager.RecordScore(scoreObject);
            Quiz_Result.questioAnswers = questions;
            startActivity(new Intent(View_Quiz.this, Quiz_Result.class));
            finish();


        }else {
            quest = questions.get(questionNumber);
            btn_choiceA.setEnabled(true);
            btn_choiceB.setEnabled(true);
            btn_choiceC.setEnabled(true);
            btn_choiceD.setEnabled(true);

            tv_question.setText(quest.getQuestion());
            switch (quest.getQuestionType()) {
                case "Multiple Choice":

                    question_multiple.setVisibility(View.VISIBLE);
                    question_trueorfalse.setVisibility(View.GONE);
                    question_identification.setVisibility(View.GONE);
                    break;
                case "True or False":

                    question_multiple.setVisibility(View.GONE);
                    question_trueorfalse.setVisibility(View.VISIBLE);
                    question_identification.setVisibility(View.GONE);
                    break;
                case "Identification":
                    btn_next.setVisibility(View.VISIBLE);
                    question_multiple.setVisibility(View.GONE);
                    question_trueorfalse.setVisibility(View.GONE);
                    question_identification.setVisibility(View.VISIBLE);
                    break;
            }
            btn_choiceA.setText("A.) " + quest.getChoiceA());
            btn_choiceB.setText("B.) " + quest.getChoiceB());
            btn_choiceC.setText("C.) " + quest.getChoiceC());
            btn_choiceD.setText("D.) " + quest.getChoiceD());
            btn_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtils.isEmpty(identification_answer.getText().toString())) {
                        showMessage("Failed", "Empty identification answer");

                    } else {
                        btn_next.setEnabled(false);
                        quest.setUser_answer(identification_answer.getText().toString());
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                btn_next.setEnabled(true);
                                questionNumber++;
                                if (quest.getAnswer().equals(quest.getUser_answer())) {
                                    identification_answer.setText("");
                                    score++;
                                }
                                SetQuestion();
                            }
                        }, 1);
                    }

                }
            });

            btn_true.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btn_true.setEnabled(true);
                    btn_false.setEnabled(true);
                    quest.setUser_answer("True");
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            questionNumber++;
                            if (quest.getAnswer().equals(quest.getUser_answer())) {
                                score++;
                            }
                            SetQuestion();

                        }
                    }, 1);

                }
            });
            btn_false.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btn_true.setEnabled(true);
                    btn_false.setEnabled(true);
                    quest.setUser_answer("False");
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            questionNumber++;
                            if (quest.getAnswer().equals(quest.getUser_answer())) {
                                score++;
                            }
                            SetQuestion();
                        }
                    }, 1);

                }
            });
            btn_choiceA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btn_choiceA.setEnabled(false);
                    btn_choiceB.setEnabled(false);
                    btn_choiceC.setEnabled(false);
                    btn_choiceD.setEnabled(false);
                    quest.setUser_answer(quest.getChoiceA());
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            questionNumber++;
                            if (quest.getAnswer().equals(quest.getUser_answer())) {
                                score++;
                            }
                            SetQuestion();

                        }
                    }, 1);

                }
            });
            btn_choiceB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btn_choiceA.setEnabled(false);
                    btn_choiceB.setEnabled(false);
                    btn_choiceC.setEnabled(false);
                    btn_choiceD.setEnabled(false);

                    quest.setUser_answer(quest.getChoiceB());
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            questionNumber++;
                            if (quest.getAnswer().equals(quest.getUser_answer())) {
                                score++;
                            }
                            SetQuestion();

                        }
                    }, 1);
                }
            });
            btn_choiceC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btn_choiceA.setEnabled(false);
                    btn_choiceB.setEnabled(false);
                    btn_choiceC.setEnabled(false);
                    btn_choiceD.setEnabled(false);

                    quest.setUser_answer(quest.getChoiceC());
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            questionNumber++;
                            if (quest.getAnswer().equals(quest.getUser_answer())) {
                                score++;
                            }
                            SetQuestion();

                        }
                    }, 1);
                }
            });
            btn_choiceD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btn_choiceA.setEnabled(false);
                    btn_choiceB.setEnabled(false);
                    btn_choiceC.setEnabled(false);
                    btn_choiceD.setEnabled(false);
                    quest.setUser_answer(quest.getChoiceD());
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            questionNumber++;
                            if (quest.getAnswer().equals(quest.getUser_answer())) {
                                score++;
                            }
                            SetQuestion();

                        }
                    }, 1);
                }
            });
        }

       /* if(questionNumber>=questions.size()){
            btn_choiceA.setEnabled(false);
            btn_choiceB.setEnabled(false);
            btn_choiceC.setEnabled(false);
            btn_choiceD.setEnabled(false);

            SaveScore();

        }else{
            final Question question = questions.get(questionNumber);
            int temp = questionNumber;
            tv_question.setText((temp+1)+".) "+question.getQuestion());
            btn_choiceA.setText("A.) "+question.getChoiceA());
            btn_choiceB.setText("B.) "+question.getChoiceB());
            btn_choiceC.setText("C.) "+question.getChoiceC());
            btn_choiceD.setText("D.) "+question.getChoiceD());

            btn_choiceA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btn_choiceA.setEnabled(false);
                    btn_choiceB.setEnabled(false);
                    btn_choiceC.setEnabled(false);
                    btn_choiceD.setEnabled(false);
                    question.setUser_answer(question.getChoiceA());
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            questionNumber++;
                            if (question.getAnswer().equals(question.getUser_answer())) {
                                score++;
                            }
                            SetQuestion();

                        }
                    }, 1000);

                }
            });
            btn_choiceB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btn_choiceA.setEnabled(false);
                    btn_choiceB.setEnabled(false);
                    btn_choiceC.setEnabled(false);
                    btn_choiceD.setEnabled(false);

                    question.setUser_answer(question.getChoiceB());
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            questionNumber++;
                            if (question.getAnswer().equals(question.getUser_answer())) {
                                score++;
                            }
                            SetQuestion();

                        }
                    }, 1000);
                }
            });
            btn_choiceC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btn_choiceA.setEnabled(false);
                    btn_choiceB.setEnabled(false);
                    btn_choiceC.setEnabled(false);
                    btn_choiceD.setEnabled(false);

                    question.setUser_answer(question.getChoiceC());
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            questionNumber++;
                            if (question.getAnswer().equals(question.getUser_answer())) {
                                score++;
                            }
                            SetQuestion();

                        }
                    }, 1000);
                }
            });
            btn_choiceD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btn_choiceA.setEnabled(false);
                    btn_choiceB.setEnabled(false);
                    btn_choiceC.setEnabled(false);
                    btn_choiceD.setEnabled(false);
                    question.setUser_answer(question.getChoiceD());
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            questionNumber++;
                            if (question.getAnswer().equals(question.getUser_answer())) {
                                score++;
                            }
                            SetQuestion();

                        }
                    }, 1000);
                }
            });

        }*/
    }
    private void showMessage(String title,String message){
        new AlertDialog.Builder(this).setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }


}
