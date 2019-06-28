package com.brontapps.climatefight;

import android.app.Activity;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class MainSharedViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    public MutableLiveData<List<ItemHome>> itemList = new MutableLiveData<List<ItemHome>>();
    public Activity activity = null;
}
