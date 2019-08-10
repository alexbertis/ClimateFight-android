package com.climate.fight.recycler;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;

import com.climate.fight.MoreInfoActivity;
import com.climate.fight.R;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.climate.fight.recycler.ItemHome.*;

/**
 * Creadito con cariño por alexb el día 23/06/2019.
 */

public class MultiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ItemHome> items;
    private Context context;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM HH:mm");

    public MultiAdapter(List<ItemHome> items, Context context) {
        this.items = items;
        this.context = context;
    }


    /*public class ViewHolderBati extends RecyclerView.ViewHolder {
        TextView tit, lug, start;
        ViewHolderBati(View itemView) {
            super(itemView);
            tit = itemView.findViewById(R.id.batitit);
            start = itemView.findViewById(R.id.batitime);
            lug = itemView.findViewById(R.id.batiplace);
        }
    }
    public class ViewHolderMani extends RecyclerView.ViewHolder {
        TextView tit, lug, start;
        ViewHolderMani(View itemView) {
            super(itemView);
            tit = itemView.findViewById(R.id.manitit);
            start = itemView.findViewById(R.id.manitime);
            lug = itemView.findViewById(R.id.maniplace);
        }
    }*/
    public class ViewHolderGeneric extends RecyclerView.ViewHolder {
        TextView type, tit, pla, start;
        ViewHolderGeneric(View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.itemtype);
            tit = itemView.findViewById(R.id.itemtit);
            start = itemView.findViewById(R.id.itemtime);
            pla = itemView.findViewById(R.id.itemplace);
        }
    }

    /*@Override
    public int getItemViewType(int position) {
        ItemHome item = items.get(position);
        return item.getTipo();
    }*/

    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_adapter, viewGroup, false);
        return new ViewHolderGeneric(v);
        /*switch (viewType) {
            case TIPO_BATIDA:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_adapter, viewGroup, false);
                return new ViewHolderBati(v);
            case TIPO_MANIF:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_manifestacion, viewGroup, false);
                return new ViewHolderMani(v);
            case TIPO_REUNION:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_manifestacion, viewGroup, false);
                return new ViewHolderReunion(v);
        }*/
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        ViewHolderGeneric vh = (ViewHolderGeneric) holder;
        ItemHome item = items.get(position);
        @StringRes int type = R.string.event_info;
        switch (item.getTipo()) {
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
        vh.type.setText(type);
        Date date = new Date(item.getMillisStart());
        String name = item.getNombre(), lugar = item.getRefUbi();
        vh.itemView.setOnClickListener(view -> {
            Intent eventIntent = new Intent(context, MoreInfoActivity.class);
            Gson gson = new Gson();
            String json = gson.toJson(item);
            eventIntent.putExtra("object", json);
            context.startActivity(eventIntent);
        });
        vh.tit.setText(name);
        vh.start.setText(context.getString(R.string.ev_starts) + sdf.format(date));
        vh.pla.setText(context.getString(R.string.ev_at) + lugar);
        /*switch (holder.getItemViewType()) {
            case TIPO_MANIF:
                ViewHolderMani vhMani = (ViewHolderMani) holder;
                vhMani.tit.setText(nombre);
                vhMani.start.setText("Comienza: " + sdf.format(date));
                vhMani.lug.setText("En " + lugar);
                break;
            case TIPO_BATIDA:
                ViewHolderBati vhBati = (ViewHolderBati) holder;
                vhBati.tit.setText(nombre);
                vhBati.start.setText("Comienza: " + sdf.format(date));
                vhBati.lug.setText("En " + lugar);
                break;
        }*/
    }

}
