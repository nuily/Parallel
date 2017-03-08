package com.rooksoto.parallel.view.activityLogin;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rooksoto.parallel.R;
import com.rooksoto.parallel.network.objects.Events;
import com.rooksoto.parallel.viewwidgets.recyclerview.EventsAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentLoginWaitPageFavorites extends Fragment {
    private View mView;
    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_login_wait_event, container, false);
        setDemoDayEvent();
        return mView;
    }

    private void setDemoDayEvent(){
        List<Events> eventsList = new ArrayList<>();
        eventsList.add(new Events());
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.fragment_login_wait_event_current_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mView.getContext()));
        mRecyclerView.setAdapter(new EventsAdapter(eventsList));
    }
}
