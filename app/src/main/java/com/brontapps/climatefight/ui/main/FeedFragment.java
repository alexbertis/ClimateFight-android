package com.brontapps.climatefight.ui.main;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brontapps.climatefight.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class FeedFragment extends Fragment {

    private MainSharedViewModel mViewModel;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;

    public static FeedFragment newInstance() {
        return new FeedFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.feed_fragment, container, false);
        recyclerView = v.findViewById(R.id.recyclerFeed);
        refreshLayout = v.findViewById(R.id.swipeRefresh);

        //MultiAdapter adapter = new MultiAdapter();
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(MainSharedViewModel.class);
        // TODO: Use the ViewModel
        refreshLayout.setOnRefreshListener(() -> {
            mViewModel.updateFirestoreMain(FirebaseFirestore.getInstance());
            refreshLayout.setRefreshing(false);
        });
    }


}
