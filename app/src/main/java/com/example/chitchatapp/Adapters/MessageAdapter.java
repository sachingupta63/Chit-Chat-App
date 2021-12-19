package com.example.chitchatapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chitchatapp.Model.Chat;
import com.example.chitchatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.viewHolder> {

    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;
    private Context mcontext;
    private List<Chat> mChat;
    private String imageUrl;

    FirebaseUser firebaseUser;

    public MessageAdapter(Context mcontext,List<Chat> mChat,String imageUrl){
        this.mcontext=mcontext;
        this.mChat=mChat;
        this.imageUrl=imageUrl;

    }

    @Override
    public MessageAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType==MSG_TYPE_LEFT){
        View view= LayoutInflater.from(mcontext).inflate(R.layout.left_chat,parent,false);

        return new viewHolder(view);
        }else {
            View view= LayoutInflater.from(mcontext).inflate(R.layout.right_chat,parent,false);

            return new viewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(MessageAdapter.viewHolder holder, int position) {

        Chat chat=mChat.get(position);

        holder.txt.setText(chat.getMessage());
        if (imageUrl.equals("default")){
            holder.image.setImageResource(R.mipmap.ic_launcher);
        }else {
            Glide.with(mcontext).load(imageUrl).into(holder.image);
        }



    }



    @Override
    public int getItemCount() {
        return mChat.size();
    }
    public class viewHolder extends RecyclerView.ViewHolder{

        public TextView txt;
        public ImageView image;


        public viewHolder(View itemView) {
            super(itemView);
            txt=itemView.findViewById(R.id.show_text);
            image=itemView.findViewById(R.id.receiver_profile_image);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        }else {
            return MSG_TYPE_LEFT;
        }
    }
}
