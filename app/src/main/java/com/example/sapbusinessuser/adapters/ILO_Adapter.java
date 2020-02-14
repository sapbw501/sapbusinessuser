package com.example.sapbusinessuser.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sapbusinessuser.R;
import com.example.sapbusinessuser.model.ILO;

import java.util.ArrayList;

public class ILO_Adapter extends RecyclerView.Adapter<ILO_Adapter.ViewHolder> {
    private Context context;
    private ArrayList<ILO> ilos;
    private ProgressDialog progressDialog;
    public ILO_Adapter(Context context){
        progressDialog = new ProgressDialog(context);
        this.context = context;

    }
    public void SetData(ArrayList<ILO> ilos){
        this.ilos = ilos;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemview_ilo_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ILO ilo = ilos.get(position);
        holder.tv_ilo_name.setText(ilo.getName());


    }


    @Override
    public int getItemCount() {
        return ilos!=null?ilos.size():0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_ilo_name;
        public ImageView more;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_ilo_name= itemView.findViewById(R.id.tv_ilo_name);
            more = itemView.findViewById(R.id.more);
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

