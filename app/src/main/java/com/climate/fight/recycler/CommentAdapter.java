package com.climate.fight.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.climate.fight.HelperTiempos;
import com.climate.fight.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

/**
 * Creadito con cariño por alexb el día 23/06/2019.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {


    class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView user, text, date;
        MaterialButton like;
        CommentViewHolder(View itemView) {
            super(itemView);
            user = itemView.findViewById(R.id.comm_name);
            text = itemView.findViewById(R.id.comm_text);
            date = itemView.findViewById(R.id.comm_date);
            like = itemView.findViewById(R.id.comm_like);
        }

    }

    private List<ItemComment> items;
    private final Context context;
    private HelperTiempos tiempos;
    private DatabaseReference db;
    private String userid;
    public CommentAdapter(List<ItemComment> items, Context context, DatabaseReference db, String userid) {
        this.items = items;
        this.context = context;
        this.db = db;
        tiempos = new HelperTiempos(context);
        this.userid = userid;
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_comment, viewGroup, false);
        return new CommentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final CommentViewHolder vh, final int position) {
        ItemComment item = items.get(position);
        String text = item.getCommentText(), id = item.getCommentId(), user = item.getUser();
        vh.text.setText(text);
        vh.user.setText(user);
        vh.date.setText(tiempos.tiempoRestante(item.getPostedDate()));
        if(item.getNumLikes() == 0){
            vh.like.setText(context.getString(R.string.comment_like));
            vh.like.setIcon(ContextCompat.getDrawable(context, R.drawable.ic_favorite_border_black));
        }else{
            vh.like.setText(String.valueOf(item.getNumLikes()));
            vh.like.setIcon(ContextCompat.getDrawable(context, R.drawable.ic_favorite_black));
        }
        vh.like.setOnClickListener(view -> {
            db.child(id).child("likes").child(userid).setValue(!item.isLiked());
        });
    }

}
