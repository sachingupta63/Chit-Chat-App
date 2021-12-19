package com.example.chitchatapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chitchatapp.MessageActivity;
import com.example.chitchatapp.Model.User;
import com.example.chitchatapp.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserRecyclerAdapter extends RecyclerView.Adapter<UserRecyclerAdapter.viewHolder> {
    private Context context;
    private ArrayList<User> users;
    public UserRecyclerAdapter(Context context,ArrayList<User> users){
        this.context=context;
        this.users=users;
    }


    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.custom_user,parent,false);
        return new UserRecyclerAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder( UserRecyclerAdapter.viewHolder holder, int position) {

        User user=users.get(position);
        holder.txtView.setText(user.getUsername());
        if (user.getImageURL().equals("default")){
            holder.imageView.setImageResource(R.mipmap.ic_launcher);
        }else {

            Glide.with(context).load(user.getImageURL()).into(holder.imageView);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, MessageActivity.class);
                intent.putExtra("userID",user.getid());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        TextView txtView;
        CircleImageView imageView;

        public viewHolder(View itemView) {
            super(itemView);
            txtView=itemView.findViewById(R.id.username_recycler);
            imageView=itemView.findViewById(R.id.profile_image_custom_recycler);
        }
    }
}
