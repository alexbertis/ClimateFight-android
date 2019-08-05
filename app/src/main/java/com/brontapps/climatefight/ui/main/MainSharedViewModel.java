package com.brontapps.climatefight.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.brontapps.climatefight.ItemHome;
import com.brontapps.climatefight.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class MainSharedViewModel extends ViewModel {
    public MutableLiveData<List<ItemHome>> itemList = new MutableLiveData<List<ItemHome>>();
    public AppCompatActivity activity = null;

    public Fragment active;

    // TODO: salvar los fragmentos al rotar la pantalla
    private boolean firstInit = true;
    public void initMain(FragmentManager fm, Fragment fragmentA, Fragment fragmentM, Fragment fragmentF) {
        if (firstInit) {
            fm.beginTransaction().add(R.id.main_container, fragmentA, "account").hide(fragmentA).commit();
            fm.beginTransaction().add(R.id.main_container, fragmentM, "map").hide(fragmentM).commit();
            fm.beginTransaction().add(R.id.main_container, fragmentF, "feed").commit();
            active = fragmentF;
            firstInit = false;
        }
    }

    GeoPoint startPoint = new GeoPoint(42.1401, -0.408897);

    public void updateFirestoreMain(FirebaseFirestore firestore){
        long millis = new GregorianCalendar().getTimeInMillis();
        firestore.collection("eventos").orderBy("inicio").startAt(millis).get().addOnSuccessListener(queryDocumentSnapshots -> {
            // TODO: código de muestra de conexión a la DB
            List<ItemHome> items = new ArrayList<>();
            for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                items.add(new ItemHome(ds.getString("nombre"), ds.getString("descripcion"),
                        (ds.getLong("tipo")).intValue(), ds.getLong("inicio"), ds.getLong("fin"),
                        ds.getBoolean("periodico"), ds.getGeoPoint("centro"), ds.getString("nlugar"), ds.getString("url")));
            }
            itemList.postValue(items);
        });
    }


}
