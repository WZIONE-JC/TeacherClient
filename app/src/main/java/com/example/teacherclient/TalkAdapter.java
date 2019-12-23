package com.example.teacherclient;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import org.litepal.LitePal;

import java.util.List;

public class TalkAdapter extends RecyclerView.Adapter<TalkAdapter.ViewHolder> {

    private List<TalkCardTable> cardTableLists;

    public TalkAdapter(List<TalkCardTable> mList) {
        cardTableLists = mList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView avatar;
        TextView name;
        TextView title;
        TextView textContent;
        ImageView pic1;
        ImageView pic2;
        ImageView pic3;
        TextView reviews;
        TextView type;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = (ImageView)itemView.findViewById(R.id.avatar);
            name = (TextView)itemView.findViewById(R.id.talk_card_name);
            title = (TextView)itemView.findViewById(R.id.talk_card_title);
            textContent = (TextView)itemView.findViewById(R.id.talk_card_text);
            pic1 = (ImageView)itemView.findViewById(R.id.talk_card_pci_1);
            pic2 = (ImageView)itemView.findViewById(R.id.talk_card_pci_2);
            pic3 = (ImageView)itemView.findViewById(R.id.talk_card_pci_3);
            reviews = (TextView)itemView.findViewById(R.id.text_reviews);
            type = (TextView)itemView.findViewById(R.id.text_card_type);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.talk_area_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return  holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TalkCardTable talkCardItem = cardTableLists.get(position);
        holder.name.setText(talkCardItem.getWriter());
        holder.title.setText(talkCardItem.getTitle());
        holder.textContent.setText(talkCardItem.getContent());
        holder.reviews.setText(talkCardItem.getReviews());
        holder.type.setText(talkCardItem.getType());
        //图片设置


    }

    @Override
    public int getItemCount() {
        return cardTableLists.size();
    }


}
