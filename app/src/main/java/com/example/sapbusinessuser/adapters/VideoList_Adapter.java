package com.example.sapbusinessuser.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sapbusinessuser.R;
import com.example.sapbusinessuser.model.Video;

import java.util.ArrayList;

public class VideoList_Adapter extends RecyclerView.Adapter<VideoList_Adapter.ViewHolder> {
    private Context context;
    private ArrayList<Video> videos;
    private ProgressDialog progressDialog;
    public VideoList_Adapter(Context context){
        progressDialog = new ProgressDialog(context);
        this.context = context;

    }
    public void SetData(ArrayList<Video> videos){
        this.videos= videos;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemview_videolist,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Video video = videos.get(position);
        holder.tv_lesson_description.setText(video.getDescription());
        holder.tv_lesson_name.setText(video.getName());

        holder.btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(video.getUrl()));
                intent.putExtra("force_fullscreen",true);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return videos!=null?videos.size():0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_lesson_description;
        public TextView tv_lesson_name;
        public Button btn_play;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            btn_play = itemView.findViewById(R.id.btn_play);
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