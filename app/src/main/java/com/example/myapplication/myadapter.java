package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.fragment.BFragment;

import java.util.ArrayList;

public class myadapter extends RecyclerView.Adapter<myadapter.myviewholder>
{
    //--------------------Receiving data from dataholder----------------------------------------------------------------------------------
    ArrayList<datamodel> dataholder;
    Fragment fragment;
    //Context context;
    public myadapter( ArrayList<datamodel> dataholder, Fragment fragment) {
        this.dataholder = dataholder;
        this.fragment = fragment;

    }

    //---------------------THE THREE METHODS OF ADAPTER---------------------------------------------------------------------------
    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_design,parent,false);
        myviewholder holder = new myviewholder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    if(fragment instanceof  BFragment){
                        ((BFragment) fragment).showOptionsDialog(position);
                    }
                }
            }
        });


        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        holder.taskName.setText(dataholder.get(position).getTaskName());
        holder.dueDate.setText(dataholder.get(position).getDueDate());
    }

    @Override
    public int getItemCount() {
        return dataholder.size();
    }



    //--------------------myviewholder class----------------------------------------------------------------------------------------------
    class myviewholder extends RecyclerView.ViewHolder
    {
        TextView taskName,dueDate;
        public myviewholder(@NonNull View itemView)
        {
            super(itemView);
            //taking references
            taskName= itemView.findViewById(R.id.t1);
            dueDate = itemView.findViewById(R.id.t2);

        }
    }
}
