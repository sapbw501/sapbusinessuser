package com.example.sapbusinessuser.video_module.video_list;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.sapbusinessuser.R;
import com.example.sapbusinessuser.adapters.VideoList_Adapter;
import com.example.sapbusinessuser.model.Lesson;
import com.example.sapbusinessuser.model.Topic;
import com.example.sapbusinessuser.model.Video;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Video_List extends AppCompatActivity {
    /*firebase*/
    private DatabaseReference videoRef;
    private ValueEventListener valueEventListener;
    /*uis*/
    private SwipeRefreshLayout sr_swipeRefresh;
    private RecyclerView rv_recycleView;
    public static String activity;
    public static Lesson lesson;
    /*initialize*/
    private void initUI(){
        sr_swipeRefresh = findViewById(R.id.sr_swiperefresh);
        rv_recycleView = findViewById(R.id.rv_recycleview);
        rv_recycleView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
    }

    private void initFirebase(){
        videoRef = FirebaseDatabase.getInstance().getReference()
                .child("Topics")
                .child(topic.getKey())
                .child("Video");
    }
    private ArrayList<Video> videos;
    private VideoList_Adapter adapter;
    public static Topic topic;
    private Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lesson_list_activity);
        toolbar = findViewById(R.id.toolbar);
        if(getSupportActionBar()==null){
            setSupportActionBar(toolbar);
            toolbar.setTitleTextColor(Color.WHITE);

            getSupportActionBar().setTitle("List of Videos");
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }
        initUI();
        initFirebase();
        videos = new ArrayList<>();
        adapter  = new VideoList_Adapter(this);
        rv_recycleView.setAdapter(adapter);

        sr_swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllTopics();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        sr_swipeRefresh.setRefreshing(true);
        getAllTopics();
    }
    @Override
    protected void onPause() {
        super.onPause();
        videoRef.removeEventListener(valueEventListener);
    }
    private void getAllTopics(){

        valueEventListener = videoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                videos.clear();
                for(DataSnapshot dataSnap : dataSnapshot.getChildren()){
                    Video video= dataSnap.getValue(Video.class);
                    video.setKey(dataSnap.getKey());
                    videos.add(video);

                }
                sr_swipeRefresh.setRefreshing(false);
                if(videos.size()>0){
                    adapter.SetData(videos);
                }else{
                    adapter.SetData(new ArrayList<Video>());
                    showMessage("No Video","No Data Found");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showMessage("Error",databaseError.getMessage());
            }
        });
    }
    private void showMessage(String title,String message){
        new AlertDialog.Builder(this)
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}