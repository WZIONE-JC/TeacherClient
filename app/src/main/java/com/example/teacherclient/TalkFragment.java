package com.example.teacherclient;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import org.litepal.LitePal;

import java.util.List;


public class TalkFragment extends Fragment {

    private RecyclerView talkArea;
    private ImageView writeNewCard;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_talk,null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        talkArea = (RecyclerView)getActivity().findViewById(R.id.view_talk_area);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        talkArea.setLayoutManager(manager);
        writeNewCard = (ImageView)getActivity().findViewById(R.id.write_card);

        writeNewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),WriteNewCard.class));
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        //根据时间戳按降序排列
        List<TalkCardTable> cardTables = LitePal.order("timeStamp desc").find(TalkCardTable.class);
        TalkAdapter adapter = new TalkAdapter(cardTables);
        talkArea.setAdapter(adapter);
    }
}
