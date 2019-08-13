package com.climate.fight;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.climate.fight.recycler.CommentAdapter;
import com.climate.fight.recycler.ItemComment;
import com.climate.fight.recycler.ItemHome;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.climate.fight.recycler.ItemHome.TIPO_BATIDA;
import static com.climate.fight.recycler.ItemHome.TIPO_MANIF;
import static com.climate.fight.recycler.ItemHome.TIPO_REUNION;
import static com.climate.fight.recycler.ItemHome.TIPO_TALLER;
import static com.climate.fight.recycler.ItemHome.TIPO_VOLUNTARIADO;

public class MoreInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private MapView map = null;
    private ItemHome event;
    private boolean fav = false, attend = false;
    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    List<ItemComment> commentList = new ArrayList<>();

    private TextView tvTitle, tvType, tvLoc, tvDate, tvDesc;
    private EditText editNewComment;
    private MaterialButton bLink, bAttend, bNewComment;
    private RecyclerView rvComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        event = new Gson().fromJson(getIntent().getStringExtra("object"), ItemHome.class);

        tvType = findViewById(R.id.infoev_type);
        tvTitle = findViewById(R.id.infoev_tit);
        tvLoc = findViewById(R.id.infoev_loc);
        tvDate = findViewById(R.id.infoev_date);
        tvDesc = findViewById(R.id.infoev_des);
        bAttend = findViewById(R.id.infoev_attending);
        bLink = findViewById(R.id.infoev_link);
        // TODO: cambiar programáticamente TextView del tipo de event
        final FloatingActionButton fab = findViewById(R.id.fab);

        @StringRes int type = R.string.event_info;
        switch (event.getType()) {
            case TIPO_BATIDA:
                type = R.string.type_rubcollect;
                break;
            case TIPO_MANIF:
                type = R.string.type_protest;
                break;
            case TIPO_REUNION:
                type = R.string.type_meeting_talk;
                break;
            case TIPO_TALLER:
                type = R.string.type_workshop;
                break;
            case TIPO_VOLUNTARIADO:
                type = R.string.type_volunteering;
        }
        tvType.setText(type);
        tvTitle.setText(event.getName());
        tvLoc.setText(event.getRefLoc());
        tvDesc.setText(event.getDesc());
        tvDate.setText(new HelperTiempos(MoreInfoActivity.this).tiempoRestante(event.getMillisStart()));
        if(event.getUrlInfo() != null && event.getUrlInfo().trim().length() > 3){
            bLink.setVisibility(View.VISIBLE);
        }

        DatabaseReference likeUser = db.child("likes/" + event.getId());
        DatabaseReference attUser = db.child("attending/" + event.getId());
        DatabaseReference comments = db.child("comments/" + event.getId());
        if(user != null){
            rvComments = findViewById(R.id.infoev_comments);
            bNewComment = findViewById(R.id.infoev_publishcomment);
            editNewComment = findViewById(R.id.infoev_newcomment);

            final String uid = user.getUid();
            fab.setVisibility(View.VISIBLE);
            likeUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Object value = dataSnapshot.child(uid).getValue();
                    if(value instanceof Boolean && (boolean)value){
                        fab.setImageDrawable(ContextCompat.getDrawable(MoreInfoActivity.this, R.drawable.ic_favorite_black));
                        fav = true;
                    }else{
                        fab.setImageDrawable(ContextCompat.getDrawable(MoreInfoActivity.this, R.drawable.ic_favorite_border_black));
                        fav = false;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    fab.setImageDrawable(ContextCompat.getDrawable(MoreInfoActivity.this, R.drawable.ic_favorite_border_black));
                    fav = false;
                }
            });
            fab.setOnClickListener(view -> {
                fav = !fav;
                likeUser.child(uid).setValue(fav);
            });

            bAttend.setVisibility(View.VISIBLE);
            attUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Object value = dataSnapshot.child(uid).getValue();
                    long count = 0;
                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        Object val = ds.getValue();
                        if(val instanceof Boolean && (boolean)val)count++;
                    }
                    if(value instanceof Boolean && (boolean)value){
                        String txt = String.format(getString(R.string.info_not_attending), count);
                        bAttend.setText(txt);
                        attend = true;
                    }else{
                        String txt = String.format(getString(R.string.info_attending), count);
                        bAttend.setText(txt);
                        attend = false;
                    }
                }
                @Override public void onCancelled(@NonNull DatabaseError databaseError) {
                    bAttend.setText(getString(R.string.info_attending));
                    attend = false;
                }
            });
            bAttend.setOnClickListener(view -> {
                attend = !attend;
                attUser.child(uid).setValue(attend);
            });

            CommentAdapter adapter = new CommentAdapter(commentList, MoreInfoActivity.this, comments, uid);
            rvComments.setLayoutManager(new LinearLayoutManager(MoreInfoActivity.this, RecyclerView.VERTICAL, false));
            rvComments.setAdapter(adapter);
            comments.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    commentList.clear();
                    for (DataSnapshot comment : dataSnapshot.getChildren()) {
                        String commentText = (String)comment.child("comment").getValue();
                        String commentUser = (String)comment.child("user").getValue();
                        long postedDate = (long)comment.child("posted").getValue();
                        String commentId = comment.getKey();
                        if(commentId.equals(uid)){
                            editNewComment.setHint(R.string.edit_comment);
                            editNewComment.setOnClickListener(view -> {
                                if(editNewComment.getText().toString().trim().length() < 1)
                                    editNewComment.setText(commentText);
                            });
                        }
                        long count = 0;
                        boolean currentLiked = false;
                        for(DataSnapshot like : comment.child("likes").getChildren()){
                            Object val = like.getValue();
                            if(val instanceof Boolean && (boolean)val){
                                count++;
                                try {
                                    if (like.getKey().equals(uid)) {
                                        currentLiked = true;
                                    }
                                }catch (Exception e){
                                    Crashlytics.logException(e);
                                    e.printStackTrace();
                                }
                            }
                        }
                        commentList.add(new ItemComment(commentId, commentUser, commentText, count, postedDate, currentLiked));
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
            bNewComment.setOnClickListener(view -> {
                String text = editNewComment.getText().toString().trim();
                if(text.length() < 2){
                    editNewComment.setError(getString(R.string.err_comment_short));
                }else {
                    HashMap<String, Object> comment = new HashMap<>();
                    comment.put("comment", text);
                    comment.put("user", user.getDisplayName());
                    comment.put("posted", System.currentTimeMillis());
                    comments.child(uid).updateChildren(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Snackbar.make(findViewById(R.id.evinfo_root), R.string.upl_success, BaseTransientBottomBar.LENGTH_SHORT).show();
                            editNewComment.getText().clear();
                        }
                    });
                }
            });
        }else{

        }

        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setOnTouchListener((v, event) -> true);
        map.setBuiltInZoomControls(false);
        map.setMultiTouchControls(false);

        IMapController mapController = map.getController();
        mapController.setZoom(18);
        GeoPoint centro = new GeoPoint(event.getLocation().getLatitude(), event.getLocation().getLongitude());
        mapController.setCenter(centro);
        Marker evMarker = new Marker(map);
        evMarker.setPosition(centro);
        evMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        evMarker.setIcon(ContextCompat.getDrawable(MoreInfoActivity.this, R.drawable.placeholder));
        map.getOverlays().add(evMarker);

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.infoev_calendar:
                intent = new Intent(Intent.ACTION_EDIT);
                intent.setType("vnd.android.cursor.item/event");
                intent.putExtra(CalendarContract.Events.TITLE, event.getName());
                intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, event.getMillisStart());
                intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, event.getMillisEnd());
                intent.putExtra(CalendarContract.Events.ALL_DAY, false);// periodicity
                intent.putExtra(CalendarContract.Events.DESCRIPTION, event.getDesc());
                startActivity(intent);
                break;
            case R.id.infoev_link:
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(event.getUrlInfo()));
                startActivity(intent);
                break;
        }
    }

    public void onResume(){
        super.onResume();
        map.onResume();
    }
    public void onPause(){
        super.onPause();
        map.onPause();
    }
}
