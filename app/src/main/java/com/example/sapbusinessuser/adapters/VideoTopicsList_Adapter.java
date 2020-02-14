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
import com.example.sapbusinessuser.model.Topic;
import com.example.sapbusinessuser.video_module.video_list.Video_List;

import java.util.ArrayList;

public class VideoTopicsList_Adapter extends RecyclerView.Adapter<VideoTopicsList_Adapter.ViewHolder> {
    private Context context;
    private ArrayList<Topic> topics;
    private ProgressDialog progressDialog;
    public VideoTopicsList_Adapter(Context context){
        progressDialog = new ProgressDialog(context);
        this.context = context;

    }
    public void SetData(ArrayList<Topic> topics){
        this.topics = topics;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemview_videotopiclist,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Topic topic = topics.get(position);
        holder.tv_topic_description.setText(topic.getDescription());
        holder.tv_topic_name.setText(topic.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Video_List.topic = topic;
                context.startActivity(new Intent(context, Video_List.class));

            }
        });

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
        return topics!=null?topics.size():0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_topic_description;
        public TextView tv_topic_name;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_topic_description = itemView.findViewById(R.id.tv_topic_description);
            tv_topic_name = itemView.findViewById(R.id.tv_topic_name);

        }
    }
}
