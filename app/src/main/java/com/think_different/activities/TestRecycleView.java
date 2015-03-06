package com.think_different.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.think_different.R;


/**
 * Created by oceancx on 15/3/5.
 */
public class TestRecycleView extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycleview);
        RecyclerView cardList= (RecyclerView) findViewById(R.id.cardList);
        cardList.setHasFixedSize(true);
        LinearLayoutManager llm=new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        cardList.setLayoutManager(llm);
    }
}
