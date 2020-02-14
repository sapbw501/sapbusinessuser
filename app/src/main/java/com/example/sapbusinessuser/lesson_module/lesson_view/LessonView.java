package com.example.sapbusinessuser.lesson_module.lesson_view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.sapbusinessuser.R;
import com.example.sapbusinessuser.adapters.ILO_Adapter;
import com.example.sapbusinessuser.adapters.LessonView_Adapter;
import com.example.sapbusinessuser.lesson_module.lesson_list.Lesson_List;
import com.example.sapbusinessuser.model.ILO;
import com.example.sapbusinessuser.model.Lesson;
import com.example.sapbusinessuser.model.LessonItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LessonView extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Toolbar toolbar;
    private LessonView_Adapter adapter;
    private ArrayList<LessonItem> lessonItems;
    public static Lesson lesson;
    private void initUI(){
        ilo_adapter = new ILO_Adapter(this);
        lessonItems = new ArrayList<>();
        adapter = new LessonView_Adapter(this);
        toolbar = findViewById(R.id.toolbar);
        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        recyclerView = findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        recyclerView.setAdapter(adapter);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        if(getSupportActionBar()==null){
            setSupportActionBar(toolbar);
            toolbar.setTitleTextColor(Color.WHITE);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(lesson.getName());
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                getLessonItem();
            }
        });
    }

    private DatabaseReference fb_lessonItemRef;
    private ValueEventListener valueEventListener;
    private void initFirebase(){
        fb_lessonItemRef = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Topics")
                .child(Lesson_List.topic.getKey())
                .child("Lesson")
                .child(lesson.getKey())
                .child("LessonItem");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lessonitem_menu, menu);
        return true;
    }


    private Dialog d;
    private ArrayList<ILO> ilos;
    private ILO_Adapter ilo_adapter;
    private void getAllILO(){
        //showProgress();

        View view = LayoutInflater.from(LessonView.this).inflate(R.layout.rv_manage_ilo, null, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycleview);
        recyclerView.setAdapter(ilo_adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LessonView.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        AlertDialog.Builder adb = new AlertDialog.Builder(LessonView.this);

        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        d = adb.setView(view).create();

        ilos = new ArrayList<>();
        valueEventListener = fb_iloRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ilos.clear();
                for(DataSnapshot dataSnap : dataSnapshot.getChildren()){
                    ILO ilo = dataSnap.getValue(ILO.class);
                    ilo.setKey(dataSnap.getKey());
                    ilos.add(ilo);
                }

                ilo_adapter.SetData(ilos);

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(d.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                d.show();
                d.getWindow().setAttributes(lp);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //  stopProgress();
                showMessage("Error",databaseError.getMessage());
            }
        });

    }


    private DatabaseReference fb_iloRef;
    private void initFirebaseILO(){
        fb_iloRef = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Topics")
                .child(Lesson_List.topic.getKey())
                .child("Lesson")
                .child(lesson.getKey())
                .child("ILO");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessonitem);
        initUI();
        initFirebase();
        initFirebaseILO();
    }

    @Override
    protected void onResume() {
        super.onResume();
        swipeRefreshLayout.setRefreshing(true);
        getAllILO();
        getLessonItem();

    }
    private void getLessonItem(){
        valueEventListener = fb_lessonItemRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lessonItems.clear();
                for(DataSnapshot dataSnap : dataSnapshot.getChildren()){
                    LessonItem lessonItem = dataSnap.getValue(LessonItem.class);
                    lessonItem.setKey(dataSnap.getKey());
                    lessonItems.add(lessonItem );

                }
                swipeRefreshLayout.setRefreshing(false);
                if(lessonItems.size()==0){
                    adapter.SetData(new ArrayList<LessonItem>());
                    showMessage("No Data","No Lessons Available");
                }else{
                    adapter.SetData(lessonItems);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showMessage("Failed",databaseError.getMessage());

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        fb_lessonItemRef.removeEventListener(valueEventListener);
        if(adapter.t1!=null){
            adapter.t1.stop();
            adapter.t1.shutdown();
        }
        adapter.t1.stop();


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
        int id =  item.getItemId();
        switch (id){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.info:
                d.show();
                break;
        }
        return true;
    }


    @Override
    protected void onStop() {
        super.onStop();
        if(adapter.t1!=null){
            adapter.t1.stop();
            adapter.t1.shutdown();
        }
        adapter.t1.stop();
    }
}
