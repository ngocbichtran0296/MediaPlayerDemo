package com.ngocbich.mediaplayerdemo.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ngocbich.mediaplayerdemo.Interface.ItemClickListener;
import com.ngocbich.mediaplayerdemo.R;

/**
 * Created by Ngoc Bich on 5/3/2018.
 */

public class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView listsongname,listsongartist;

    private ItemClickListener itemClickListener;

    public ListViewHolder(View itemView) {
        super(itemView);

        listsongname=itemView.findViewById(R.id.listsongName);
        listsongartist=itemView.findViewById(R.id.listsongArtist);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }
}
