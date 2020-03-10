package com.example.sapbusinessuser.progress_module;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.sapbusinessuser.R;
import com.example.sapbusinessuser.model.AppPreference;
import com.example.sapbusinessuser.model.MyYAxisValueFormatter;
import com.example.sapbusinessuser.model.Progress;
import com.example.sapbusinessuser.model.ScoreRealmManager;
import com.example.sapbusinessuser.model.Scores;
import com.example.sapbusinessuser.model.Topic;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.common.collect.Lists;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProgressActivity extends AppCompatActivity {
    LineChart mChart;
    private DatabaseReference progressRef;
    private ValueEventListener valueEventListener;
    private ArrayList<Progress> progresses;
    private AppPreference appPreference;
    private ArrayList<Progress> progressData;
    public static Topic topic;
    private Toolbar toolbar;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Getting Progress..");
        progressDialog.setCancelable(false);
        appPreference = new AppPreference(this);
        progresses = new ArrayList<>();
        progressData = new ArrayList<>();
        progressRef = FirebaseDatabase.getInstance().getReference().child("Progress");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mChart = (LineChart) findViewById(R.id.chart);

        if(getSupportActionBar()==null){
            setSupportActionBar(toolbar);
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setSubtitleTextColor(Color.WHITE);
            getSupportActionBar().setTitle("Progress");
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);

        }


        ScoreRealmManager scoreRealmManager = new ScoreRealmManager(ProgressActivity.this);
        initGraph();


        ArrayList<Entry> values = new ArrayList<Entry>();
        List<Scores> scoreList = Lists.newArrayList(scoreRealmManager.realmResults(topic.getKey()));
        for(int i = 0 ; i < scoreList.size() ; i++){
            values.add(new Entry(scoreList.get(i).getTrial(),scoreList.get(i).getScore()));
        }
        if(scoreList.size()>0) {
            LineDataSet data = new LineDataSet(values, "Scores");

            // data.setValueFormatter(new ValueFormatter());
            data.setColor(Color.WHITE);
            data.setFillAlpha(110);
            data.setCircleColor(Color.GREEN);
            List<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(data);
            LineData data1 = new LineData(dataSets);
            mChart.setData(data1);
            mChart.notifyDataSetChanged();
            mChart.invalidate();
        }




        /*List<Scores> scoreList = Lists.newArrayList(scoreRealmManager.realmResults(topicID));
        for(int i = 0 ; i < scoreList.size() ; i++){
            values.add(new Entry(scoreList.get(i).getTrial(),scoreList.get(i).getScore()));
        }*/



    }
    public void initGraph(){
        // layout.setVisibility(View.INVISIBLE);
        mChart = findViewById(R.id.chart);
        mChart.setDragEnabled(false);
        mChart.setScaleEnabled(false);
        mChart.setClickable(false);
        mChart.getAxisRight().setDrawGridLines(false);
        mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mChart.getAxisRight().setDrawAxisLine(false);
        mChart.getAxisRight().setDrawLabels(false);
        mChart.getDescription().setText("Number Of Trials");
        YAxis yAxis = mChart.getAxisLeft();
        yAxis.setAxisMinimum(0f);
        yAxis.setDrawGridLines(false);
        yAxis.setGridColor(Color.BLACK);
        yAxis.setAxisMaximum(10f);
        yAxis.setGranularity(1f);
        yAxis.setLabelCount(10, true);
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setGridColor(Color.BLACK);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMinimum(1);
        xAxis.setValueFormatter(new MyYAxisValueFormatter());
        //   initGraphValues();
    }
/*

    @Override
    protected void onResume() {
        super.onResume();
        progressDialog.show();

        valueEventListener =
                progressRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                progresses.clear();
                for(DataSnapshot dataSnap : dataSnapshot.getChildren()){
                    Progress progress = dataSnap.getValue(Progress.class);
                    progress.setKey(dataSnap.getKey());
                    progresses.add(progress);
                }
                initGraph();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        progressRef.removeEventListener(valueEventListener);
    }

    public void initGraph(){

        mChart = (LineChart) findViewById(R.id.chart);
        mChart.setDragEnabled(false);
        mChart.setScaleEnabled(false);
        mChart.setClickable(false);
        mChart.getAxisRight().setDrawGridLines(false);
        mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mChart.getAxisRight().setDrawAxisLine(false);
        mChart.getAxisRight().setDrawLabels(false);
        mChart.getDescription().setText("Number Of Trials");
        YAxis yAxis = mChart.getAxisLeft();
        yAxis.setAxisMinimum(0f);
        yAxis.setDrawGridLines(false);
        yAxis.setGridColor(Color.BLACK);
        yAxis.setAxisMaximum(30f);
        yAxis.setGranularity(1f);
        yAxis.setLabelCount(15, true);
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setGridColor(Color.BLACK);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMinimum(1);
        xAxis.setValueFormatter(new MyYAxisValueFormatter());



        progressDialog.dismiss();

        if(progresses.size()>0){
            boolean isMatch = false;
            ArrayList<Entry> values = new ArrayList<Entry>();
            int count = 0;
            progressData.clear();
            for (int i = 0; i < progresses.size(); i++) {
                Progress progress = progresses.get(i);

                if(topic.getKey().equals(progress.getTopic())
                        && appPreference.getId().equals(progress.getStudent())){
                    progressData.add(progress);



                }
            }
            if(progressData.size()>0) {
                values = new ArrayList<Entry>();
                values.clear();
                for(int i = 0 ; i < progressData.size();i++){
                    int temp = i;
                    values.add(new Entry(temp+1,progressData.get(i).getScore()));


                }

                LineDataSet data = new LineDataSet(values, "Scores");
                data.setColor(Color.RED);
                data.setFillAlpha(110);
                data.setCircleColor(Color.RED);
                List<ILineDataSet> dataSets = new ArrayList<>();
                dataSets.add(data);
                LineData data1 = new LineData(dataSets);
                mChart.setData(data1);
                mChart.notifyDataSetChanged();
                mChart.invalidate();
            }
        }


    }

*/

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
    public void onBackPressed() {
        super.onBackPressed();



    }
}