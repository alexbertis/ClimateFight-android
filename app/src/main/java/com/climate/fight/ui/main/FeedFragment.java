package com.climate.fight.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.climate.fight.MainActivity;
import com.climate.fight.R;
import com.climate.fight.recycler.ItemHome;
import com.climate.fight.recycler.MultiAdapter;

import java.util.ArrayList;
import java.util.List;

public class FeedFragment extends Fragment {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private List<ItemHome> itemList = new ArrayList<>();
    private MultiAdapter adapter;

    public static FeedFragment newInstance() {
        return new FeedFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.feed_fragment, container, false);
        recyclerView = v.findViewById(R.id.recyclerFeed);
        refreshLayout = v.findViewById(R.id.swipeRefresh);

        new Thread(() -> {
            while (((MainActivity)getActivity()).getItemsHome() == null || ((MainActivity)getActivity()).getItemsHome().size() == 0){ }
            itemList = ((MainActivity)getActivity()).getItemsHome();
            getActivity().runOnUiThread(() -> {
                adapter = new MultiAdapter(itemList, getContext());
                LinearLayoutManager ll = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(ll);
                recyclerView.setAdapter(adapter);
            });
        }).start();
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
        refreshLayout.setOnRefreshListener(() -> {
            ((MainActivity)getActivity()).updateFirestoreMain(adapter);
            refreshLayout.setRefreshing(false);
        });
    }


}
