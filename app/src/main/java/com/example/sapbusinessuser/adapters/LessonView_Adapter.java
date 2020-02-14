package com.example.sapbusinessuser.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sapbusinessuser.R;
import com.example.sapbusinessuser.model.LessonItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Locale;

public class LessonView_Adapter extends RecyclerView.Adapter<LessonView_Adapter.ViewHolder> {
    private Context context;
    private ArrayList<LessonItem> lessonItems;
    private ProgressDialog progressDialog;
    public static TextToSpeech t1;
    public LessonView_Adapter(Context context){
        this.context=context;


        t1=new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });
        progressDialog= new ProgressDialog(context);
        progressDialog.setCancelable(false);
    }



    public void SetData(ArrayList<LessonItem> lessonItems){
        this.lessonItems = lessonItems;
        notifyDataSetChanged();

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemview_lessonitem,parent,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final LessonItem lessonItem = lessonItems.get(position);
        if(lessonItem.getTitle() == null ){
            holder.tv_title.setVisibility(View.GONE);
        } else {
            if (lessonItem.getTitle().equals("")) {
                holder.tv_title.setVisibility(View.GONE);

            } else {
                holder.tv_title.setVisibility(View.VISIBLE);

            }
        }
        holder.iv_image.setVisibility(View.VISIBLE);
        holder.tv_text.setText(lessonItem.getText());
        holder.tv_title.setText(lessonItem.getTitle());
        if(lessonItem.getImage()!=null){
            Glide.with(context).load(FirebaseStorage.getInstance().getReference().child(lessonItem.getImage())).into(holder.iv_image);
        }else{
            holder.iv_image.setVisibility(View.GONE);

        }
    }


    private void showMessage(String title, String message){
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
        return lessonItems!=null?lessonItems.size():0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_text,tv_title;
        public ImageView iv_image,iv_more;
        public FloatingActionButton btn_speaker;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_text = itemView.findViewById(R.id.text);
            tv_title = itemView.findViewById(R.id.title);
            iv_more = itemView.findViewById(R.id.more);
            iv_image = itemView.findViewById(R.id.image);
            btn_speaker = itemView.findViewById(R.id.btn_speaker);

            btn_speaker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if(t1!=null){
                        if(t1.isSpeaking()){
                            t1.stop();
                        }else{
                            int pos = getAdapterPosition();
                            notifyDataSetChanged();
                            t1.speak(lessonItems.get(pos).getText(), TextToSpeech.QUEUE_FLUSH, null);

                        }
                    }
                }
            });

        }
    }
}